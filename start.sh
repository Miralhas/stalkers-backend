#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# Check if .env file exists
if [ ! -f .env ]; then
  echo "Error: .env file not found."
  exit 1
fi

echo "Starting Docker containers..."

docker compose -f compose.yaml up -d --build --force-recreate

echo "Script completed."