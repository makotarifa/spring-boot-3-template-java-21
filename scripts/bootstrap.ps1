$ErrorActionPreference = "Stop"
if (Test-Path ".env") {
  Get-Content ".env" | ForEach-Object {
    if ($_ -match "^(\w+)=(.*)$") {
      $name = $Matches[1]
      $value = $Matches[2]
      [Environment]::SetEnvironmentVariable($name, $value, "Process")
    }
  }
}
Push-Location "docker/postgres"
docker compose up --build -d
Pop-Location
