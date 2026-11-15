param(
  [ValidateSet("exe", "msi")]
  [string]$Type = "exe",
  [string]$Version = "1.0.0"
)

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
Set-Location $root

# 1. Genera el jar ejecutable (shade).
mvn -q -DskipTests package
if ($LASTEXITCODE -ne 0) {
  throw "Fallo el empaquetado con Maven."
}

$jar = Get-ChildItem (Join-Path $root "target") -Filter "*.jar" |
  Where-Object { $_.Name -notlike "original-*" } |
  Sort-Object LastWriteTime -Descending |
  Select-Object -First 1
if (-not $jar) {
  throw "No se encontro el jar en target."
}

# 2. Deja solo el jar en una carpeta de entrada limpia.
$inputDir = Join-Path $root "target\instalador-input"
Remove-Item -Recurse -Force $inputDir -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path $inputDir | Out-Null
Copy-Item $jar.FullName $inputDir

# 3. Genera el instalador con el icono del navbar.
$icon = Join-Path $root "src\resource\assets\iconoAbGD.ico"
if (-not (Test-Path $icon)) {
  throw "No se encontro el icono: $icon"
}

$dest = Join-Path $root "target\instalador"
Remove-Item -Recurse -Force $dest -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path $dest | Out-Null

& jpackage `
  --type $Type `
  --name "AB Graphic Design" `
  --app-version $Version `
  --input $inputDir `
  --main-jar $jar.Name `
  --main-class com.abgraphicdesing.Launcher `
  --icon $icon `
  --dest $dest `
  --vendor "AB Graphic Design" `
  --description "Calculadora de presupuestos de impresion" `
  --win-shortcut `
  --win-menu `
  --win-menu-group "AB Graphic Design"

if ($LASTEXITCODE -ne 0) {
  throw "Fallo la creacion del instalador con jpackage."
}

Write-Output "Instalador generado en: $dest"
