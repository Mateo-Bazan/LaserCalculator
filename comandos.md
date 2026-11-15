Seeders:
mvn compile exec:java "-Dexec.mainClass=com.abgraphicdesing.scripts.SeedMateriales"

mvn compile exec:java "-Dexec.mainClass=com.abgraphicdesing.scripts.SeedAjustes"

mvn compile exec:java "-Dexec.mainClass=com.abgraphicdesing.scripts.SeedImpresoras"

Instalador (usa el icono del navbar):
powershell -ExecutionPolicy Bypass -File .\instalador.ps1

Instalador MSI:
powershell -ExecutionPolicy Bypass -File .\instalador.ps1 -Type msi