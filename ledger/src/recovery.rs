use std::fs;

/// Restores the database from a backup.
///
/// # Arguments
///
/// * `backup_path` - The path to the backup file.
/// * `db_path` - The path to the database file.
///
/// # Returns
///
/// Returns `Ok(())` if the database was restored successfully, or an error if the restoration failed.
pub fn restore_db(backup_path: &str, db_path: &str) -> std::io::Result<()> {
    fs::copy(backup_path, db_path)?;
    Ok(())
}
