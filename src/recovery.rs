use std::fs;

pub fn restore_db(backup_path: &str, db_path: &str) -> std::io::Result<()> {
    fs::copy(backup_path, db_path)?;
    Ok(())
}
