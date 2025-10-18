use std::env;

fn main() {
    let target = env::var("TARGET").unwrap();
    let sqlcipher_lib_dir = match target.as_str() {
        "aarch64-linux-android" => "/home/kaz/Dev/rust/_current/2teeLedger/sqlcipher-extracted/jni/arm64-v8a",
        "armv7-linux-androideabi" => "/home/kaz/Dev/rust/_current/2teeLedger/sqlcipher-extracted/jni/armeabi-v7a",
        "i686-linux-android" => "/home/kaz/Dev/rust/_current/2teeLedger/sqlcipher-extracted/jni/x86",
        "x86_64-linux-android" => "/home/kaz/Dev/rust/_current/2teeLedger/sqlcipher-extracted/jni/x86_64",
        _ => panic!("Unsupported target: {}", target),
    };

    println!("cargo:rustc-link-search=native={}", sqlcipher_lib_dir);
    println!("cargo:rustc-link-lib=dylib=sqlcipher");
    env::set_var("SQLCIPHER", "1");
}
