package com.example.ledger.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeyManager(context: Context) {

    private val appContext = context.applicationContext
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val KEY_ALIAS = "ledger_master_encryption_key"
    private val SHARED_PREFS_NAME = "ledger_security_prefs"
    private val ENCRYPTED_DB_KEY_PREF = "encrypted_db_key"

    fun getOrCreateDatabaseKey(): ByteArray {
        // Ensure the master encryption key exists in the Android Keystore
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            generateMasterEncryptionKey()
        }

        val prefs = appContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val encryptedDbKeyString = prefs.getString(ENCRYPTED_DB_KEY_PREF, null)

        return if (encryptedDbKeyString == null) {
            // First run: generate a new database key, encrypt it, and save it
            val newDbKey = ByteArray(32).apply {
                // In a real app, use a secure random source
                (0 until 32).forEach { this[it] = it.toByte() }
            }
            val encryptedDbKey = encrypt(newDbKey)
            prefs.edit().putString(ENCRYPTED_DB_KEY_PREF, Base64.encodeToString(encryptedDbKey, Base64.NO_WRAP)).apply()
            newDbKey
        } else {
            // Subsequent runs: decrypt the saved database key
            val encryptedDbKey = Base64.decode(encryptedDbKeyString, Base64.NO_WRAP)
            decrypt(encryptedDbKey)
        }
    }

    private fun generateMasterEncryptionKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getMasterEncryptionKey(): SecretKey {
        val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }

    private fun encrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getMasterEncryptionKey())
        // The IV is prepended to the ciphertext
        return cipher.iv + cipher.doFinal(data)
    }

    private fun decrypt(encryptedData: ByteArray): ByteArray {
        val ivSize = 12 // GCM IV size is 12 bytes
        val iv = encryptedData.copyOfRange(0, ivSize)
        val ciphertext = encryptedData.copyOfRange(ivSize, encryptedData.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getMasterEncryptionKey(), spec)
        return cipher.doFinal(ciphertext)
    }
}