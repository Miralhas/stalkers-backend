Set-Location $PSScriptRoot

Write-Host "Starting Docker containers..."
# $env:MYSQL_PASSWORD = "admin"
docker compose -f compose.yaml up -d --build --force-recreate