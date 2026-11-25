# Laser Calculator

Aplicación de escritorio para **calcular cuánto cuesta un trabajo de grabado láser** y decidir a qué precio venderlo.

Nació para resolver un problema real: una gráfica no sabía con exactitud cómo calcular el costo de sus trabajos con láser. Laser Calculator junta en un solo lugar todo lo que influye en ese costo (la máquina, el material, el tiempo de trabajo y los gastos del taller) y hace las cuentas por vos.

## ¿Qué podés hacer con la aplicación?

- **Calcular presupuestos**: indicás la máquina, el material, el tiempo y la ganancia que querés, y la aplicación te muestra el desglose completo del costo y el precio final.
- **Guardar presupuestos en el historial** para consultarlos después.
- **Administrar tus impresoras láser**, con sus datos de compra, repuestos, vida útil y consumo.
- **Administrar tus materiales**, con su costo, su merma y su stock.
- **Configurar los gastos del taller** (mano de obra, luz, alquiler, etc.) que se reparten en cada trabajo.

## Instalación

1. Descargá el instalador: [Laser Calculator-1.0.0.exe](Laser%20Calculator-1.0.0.exe)
2. Abrilo y seguí los pasos del asistente. Podés elegir en qué carpeta instalarlo.
3. Al terminar, vas a encontrar la aplicación en el menú de inicio y como acceso directo en el escritorio.

**Requisitos:** Windows 10. No necesitás instalar nada más: el instalador ya incluye todo lo necesario.

Si desinstalás o reinstalás la aplicación, **tus datos no se pierden**: se guardan aparte, en `Documentos\LaserCalculator\`.

## Cómo empezar a usarla

Al abrir la aplicación por primera vez, la sección **Ajustes** ya viene con valores precargados. En cambio, las **impresoras** y los **materiales** los tenés que cargar vos a mano. El orden recomendado es:

1. **Ajustes**: revisá que los valores (mano de obra, luz y demás gastos) coincidan con los de tu taller. Son la base de todas las cuentas.
2. **Impresoras**: agregá tus máquinas láser.
3. **Materiales**: agregá lo que usás para trabajar.
4. **Calculadora**: ya podés armar tu primer presupuesto.
5. **Historial**: ahí quedan guardados los presupuestos que confirmes.

## Qué hace cada sección

### Calculadora
Completás el nombre del trabajo, la impresora, el tiempo de grabado, las horas de mano de obra, el material, la cantidad y el porcentaje de ganancia. La aplicación calcula:

- **Mano de obra**: lo que cuestan las horas de trabajo.
- **Materiales**: el costo del material por la cantidad de piezas.
- **Costo de máquina**: el desgaste de la impresora y la electricidad que consume mientras graba.
- **Gastos fijos**: la parte de los gastos del taller que corresponde a ese trabajo.
- **Costo total**: la suma de todo lo anterior.
- **Precio final**: el costo total más tu ganancia.
- **Costo y precio por unidad**.

La ganancia se calcula sobre los materiales, la máquina y los gastos fijos. **No se aplica sobre la mano de obra**, que se suma tal cual.

También podés elegir "Ninguno" como material si el trabajo no consume material. Cuando el resultado te convence, tocás **Confirmar presupuesto** para guardarlo en el historial.

### Impresoras
Catálogo de tus máquinas con marca, modelo, tipo (por ejemplo Diodo o CO2), vida útil en horas, consumo en watts, precio de compra y precio de repuestos. La aplicación calcula sola cuánto cuesta cada hora de uso de la máquina. También podés crear y eliminar tipos nuevos. Un tipo que esté en uso no se puede eliminar.

### Materiales
Tabla con buscador donde cargás cada material con su precio de compra, su **merma** (el porcentaje que se pierde al trabajarlo), sus dimensiones y su stock. El precio de costo se calcula solo, sumando la merma. El stock se marca con colores: rojo cuando queda poco, amarillo cuando está bajando y verde cuando hay de sobra.

### Ajustes
Lista de gastos con su valor y la frecuencia con que se pagan (por hora, diario, semanal, mensual o anual). La aplicación los convierte a "costo por hora de trabajo" para repartirlos en cada presupuesto. Podés agregar gastos nuevos y modificar los existentes.

Dos ajustes, **Mano de obra** y **kWh** (el precio de la electricidad), están protegidos con un candado: no se pueden eliminar porque la calculadora los necesita para funcionar.

### Historial
Todos los presupuestos confirmados, con el título, la fecha, el precio final y los datos principales. Podés buscar por título o filtrar por rango de fechas.

Cada registro guarda una **"fotografía"** de la impresora y el material tal como estaban en ese momento. Así, si más adelante cambiás un precio o eliminás un material, los presupuestos viejos siguen mostrando los datos con los que fueron calculados.

## Dónde se guardan tus datos

Todo lo que cargues se guarda en tu computadora, en este archivo:

`Documentos\LaserCalculator\app.db`

La carpeta y el archivo se crean solos la primera vez que abrís la aplicación.

- **No necesitás internet** para usarla.
- **Para hacer un respaldo**, copiá ese archivo a un pendrive o a la nube.
- **Para pasar tus datos a otra computadora**, copiá ese archivo a la misma ubicación en la otra.

## Información para desarrolladores

Esta sección es solo para quien quiera modificar o compilar el proyecto.

### Tecnologías

- Java 21
- JavaFX 21 (interfaz gráfica)
- SQLite (base de datos local)
- HikariCP (manejo de conexiones a la base)
- Maven (compilación y dependencias)

### Estructura del código

El proyecto está organizado en capas, cada una con una responsabilidad:

- `view`: lo que se ve en pantalla.
- `controller`: recibe las acciones del usuario y las ejecuta en segundo plano para que la ventana no se congele.
- `service`: la lógica y los cálculos.
- `repository`: lee y guarda datos en la base.
- `model`: las entidades y los objetos de transferencia (DTO).
- `database`: configuración de la base y migraciones.

Las migraciones (`MigrationManager`) actualizan la base automáticamente cuando la aplicación cambia de versión, sin perder los datos existentes.

### Ejecutar en modo desarrollo

```
mvn javafx:run
```

### Generar el instalador

El instalador se crea con `jpackage`, una herramienta que viene incluida con el JDK.

**Requisitos previos** (solo en la computadora donde se genera el instalador):

- Windows 10
- JDK 21
- Maven
- [WiX Toolset](https://wixtoolset.org/) **versión 3.x** (probado con 3.14.1). `jpackage` lo necesita para crear archivos `.exe` en Windows. Con JDK 21 no sirven las versiones 4 o superiores.

**Pasos.** Ejecutá estos comandos en PowerShell, desde la carpeta del proyecto:

```powershell
# 1. Compilar y generar el JAR
mvn clean package

# 2. Preparar una carpeta limpia que contenga solo el JAR
Remove-Item -Recurse -Force target\input -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path target\input | Out-Null
Copy-Item target\abgraphicdesing-1.0-SNAPSHOT.jar target\input\

# 3. Crear el instalador
jpackage `
  --type exe `
  --name "Laser Calculator" `
  --app-version 1.0.0 `
  --vendor "Mateo Bazan" `
  --input target\input `
  --main-jar abgraphicdesing-1.0-SNAPSHOT.jar `
  --main-class com.abgraphicdesing.Launcher `
  --icon src\resource\assets\iconoAbGD.ico `
  --dest target `
  --win-menu `
  --win-shortcut `
  --win-dir-chooser
```

El instalador queda en `target\Laser Calculator-1.0.0.exe`.

**Qué hace cada opción de `jpackage`:**

| Opción | Función |
|---|---|
| `--type exe` | Genera un instalador `.exe` |
| `--name` | Nombre que ve el usuario en el menú de inicio |
| `--app-version` | Versión del instalador (solo números) |
| `--main-class` | Debe ser `Launcher`, no `App` (ver la nota más abajo) |
| `--icon` | Ícono de la aplicación (formato `.ico`) |
| `--win-menu` | Agrega la aplicación al menú de inicio |
| `--win-shortcut` | Crea un acceso directo en el escritorio |
| `--win-dir-chooser` | Permite elegir la carpeta de instalación |

**Si cambiás la versión del proyecto:** actualizá el nombre del JAR en los comandos (viene del `pom.xml`) y el valor de `--app-version`.

El instalador incluye su propio Java, así que quien lo use no necesita instalarlo.

> **Nota sobre `Launcher.java`:** esta clase es necesaria. Sin ella, el JAR empaquetado con JavaFX no arranca. No la elimines ni la fusiones con `App`.

## Autor y licencia

**Autor:** Mateo Bazan
**Contacto:** · [GitHub] (https://github.com/mateo-bazan)

**Licencia:** © 2026 Mateo Bazan. Todos los derechos reservados. No se permite copiar, modificar ni distribuir este software o su código sin autorización del autor.
