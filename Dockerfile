# Stage 1: Builder
FROM rust:1.75-slim-bookworm AS builder

WORKDIR /app

# Install build dependencies
RUN apt-get update && apt-get install -y --no-install-recommends libsqlcipher-dev libssl-dev build-essential pkg-config && rm -rf /var/lib/apt/lists/*

# Copy Cargo.toml and Cargo.lock for caching dependencies
COPY Cargo.toml Cargo.lock ./
RUN sed -i 's/version = 4/version = 3/' Cargo.lock
COPY ledger ./ledger
COPY cli ./cli

RUN cargo build --release --workspace

# Build the release binary for the CLI
RUN cargo build --release --package cli

# Stage 2: Runtime
FROM debian:bookworm-slim

# Install any runtime dependencies if necessary (e.g., openssl for some crates)
# For this project, it seems like sqlite is used, which might need some system libraries.
# Let's add libsqlite3-0 for now, as it's a common dependency for sqlite-based Rust apps.
RUN apt-get update && apt-get install -y --no-install-recommends libsqlcipher-dev libssl-dev build-essential pkg-config && rm -rf /var/lib/apt/lists/*
ENV PKG_CONFIG_PATH=/usr/lib/x86_64-linux-gnu/pkgconfig:/usr/local/lib/pkgconfig
ENV LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:/usr/local/lib

WORKDIR /app

# Copy the compiled binary from the builder stage
COPY --from=builder /app/target/release/cli ./cli

# Set the entry point to your CLI application
ENTRYPOINT ["./cli"]
