Set-Location $PSScriptRoot

Write-Host "Starting RabbitMQ servers..."
docker compose up rabbitmq -d --build --force-recreate

