### speckit.analyze

  we faced a lot of issues when trying to integrate the android frontend with our rust backend. the main root of all issues was
  sqlcipher-android library, which caused gradle builds to fail when trying to build and compile the sqlcipher-android with the
  help of NDK, SDK etc. we also tried building our own openssl but no luck.

  later upon doing some research i found that the orignl sqlcipher lib for android (sqlcipher/android-database-sqlcipher) has been
   deprecated in the favour of the new one which is (sqlcipher/sqlcipher-android).

  i know that we had cloned the latest in use library, but not sure if we used the instructions of the latest build or the
  deprecated build. because we had exhausted all debugging options but no luck with compilation.

  i believe the issue was isolated to the android frontend integration. and our core rust backend is still working as expected.

  i have compiled a list of documents for you to read and digest which will help you understand our situation better.
  @docs/new_info_to_digest/README-of-new.md
  @docs/new_info_to_digest/README-of-old-lib.md
  @docs/new_info_to_digest/SQLCipherCommunityEdition-OpenSourceInformation_Zetetic.pdf
  @docs/new_info_to_digest/SQLCipherforAndroidMigration-HowtoConvertApplicationsfromandroid-database-sqlciphertosqlcipher-android_
  Zetetic.pdf
  @docs/new_info_to_digest/SQLCipherforAndroid-FullDatabaseEncryptionforSQLiteonAndroid_Zetetic.pdf
  @docs/new_info_to_digest/SQLCipherforAndroid_16KBPageSizeSupport_Zetetic.pdf

  analyze the information and try to understand what must have been the exact root cause of the errors we faced. analyze if the
  errors were just isolated to our devlopment enviroment or would it persist upon deployment to android phones aswell?

  also analyze if it will be worth our time trying to fix this error, or is there another approach we can implement to integrate
  the backend to the android frontend and build it as an app without compromising our security and adhereing to our spec docs

/home/kaz/Dev/rust/_current/2teeLedger/target/aarch64-linux-android/release/libledger_lib.so
