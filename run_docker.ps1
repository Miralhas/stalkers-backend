Set-Location $PSScriptRoot

Write-Host "Starting Docker containers..."
docker compose -f compose.yaml up -d --build --force-recreate