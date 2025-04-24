Set-Location $PSScriptRoot

Write-Host "Starting RabbitMQ servers..."
docker compose up rabbitmq redis -d --build --force-recreate

