use hmac::{Hmac, Mac};
use sha2::Sha256;
use std::fs;

type HmacSha256 = Hmac<Sha256>;

pub fn calculate_checksum(data: &[u8], key: &[u8]) -> Vec<u8> {
    let mut mac = HmacSha256::new_from_slice(key).expect("HMAC can take key of any size");
    mac.update(data);
    mac.finalize().into_bytes().to_vec()
}

// This is a simplified backup implementation that just copies the database file.
// In a real application, a more robust solution would be needed to ensure atomicity.
pub fn backup_db(db_path: &str, backup_path: &str) -> std::io::Result<()> {
    fs::copy(db_path, backup_path)?;
    Ok(())
}