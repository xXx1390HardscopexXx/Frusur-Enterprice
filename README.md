# Sistema de Gestión de Compra e Inventario - Frusur

## 1. Descripción General
**Frusur** es una aplicación de escritorio desarrollada en **Java** bajo el patrón de arquitectura **MVC (Modelo-Vista-Controlador)**. El sistema está diseñado para digitalizar y gestionar la trazabilidad de la fruta (berries) desde su compra a productores externos hasta su procesamiento y almacenamiento en cámaras de frío.

El software permite coordinar las operaciones entre dos roles fundamentales:
* **Agrónomo:** Encargado de la gestión de proveedores y recepción de materia prima.
* **Estadístico:** Encargado del control de producción, tarjas y rendimiento de líneas de proceso.

## 2. Requisitos Técnicos
Para ejecutar y editar este proyecto correctamente, se requiere:

* **Java Development Kit (JDK):** Versión 11 o superior.
* **IDE Recomendado:** **IntelliJ IDEA**.
    * *Nota Importante:* El proyecto utiliza el diseñador de interfaces gráficas de IntelliJ (`.form` files). Se recomienda encarecidamente utilizar este entorno para asegurar que las interfaces carguen correctamente.

## 3. Instalación y Ejecución
1.  Clonar el repositorio o descargar el código fuente.
2.  Abrir el proyecto en IntelliJ IDEA.
3.  Esperar a que el IDE indexe las dependencias y reconozca los archivos `.form`.
4.  Ejecutar la clase principal que inicia la interfaz de bienvenida: `Main`.

## 4. Manual de Usuario

### 4.1. Inicio y Autenticación
Al iniciar el sistema, se presenta el menú principal con las opciones de acceso.
* **Crear Cuenta:** Permite registrar nuevos usuarios con roles de *Agrónomo* o *Estadístico*.
* **Iniciar Sesión:** Credenciales separadas por rol para mantener la integridad de las funciones.
* **Persistencia:** El sistema carga automáticamente los datos al iniciar (`cargar()`). Se recomienda usar el botón **"Guardar"** en el menú principal antes de cerrar la aplicación para persistir los cambios en el archivo `CFObjetos.obj`.

### 4.2. Módulo de Agronomía (Compras)
Este módulo gestiona la entrada de Materia Prima al sistema.
1.  **Gestión de Productores:** Permite registrar productores con su RUT, contacto y estimación de cosecha.
2.  **Ciclo de Vida del Productor:** El agrónomo debe avanzar el estado del productor a través del botón **"Editar Estado"**:
    * `PENDIENTE` → `EN_PROCESO` (Firma de Contrato).
    * `EN_PROCESO` → `HABILITADO` (Charla Técnica).
    * `HABILITADO` → `RECIBIDO` (Ingreso de fruta).
3.  **Recepción:** Al cambiar el estado a `RECIBIDO`, los kilos estimados se suman automáticamente al **Stock de Materia Prima** disponible para producción.

### 4.3. Módulo de Estadística (Producción)
Este módulo descuenta materia prima y genera producto terminado.
1.  **Iniciar Planilla:** Se debe definir una línea de proceso para el turno actual.
2.  **Registro de Tarjas:** El operador ingresa:
    * Tipo de Berry (Arándano, Frutilla, etc.).
    * Clasificación (IQF o Subproducto).
    * Kilos procesados.
    * *Validación:* El sistema verifica que exista stock suficiente de materia prima antes de permitir el registro.
3.  **Cierre de Planilla:** Al finalizar, se genera un **Resumen de Producción** y el stock resultante queda disponible como producto terminado.

### 4.4. Módulo de Inventario Físico (Bodega)
*Nota: Este módulo gestiona el producto final.*
* Visualización del stock IQF y Subproductos.
* **Paletizado:** Permite agrupar cajas en Palets (estándar de 80 cajas) generando identificadores únicos.
* **Despacho:** Simulación de escaneo de códigos de barra para dar salida a la mercadería (cambio de estado a `DESPACHADO`).

## 5. Estructura del Proyecto (Packages)
* `Modelo`: Contiene la lógica de negocio, entidades (Productor, Caja, Palet) y la gestión del Inventario.
* `Vista`: Contiene las interfaces gráficas (JDialog, JPanel) y formularios `.form`.
* `Controlador`: Clase `ControladorFrusur` (Singleton) que orquesta la comunicación entre la Vista y el Modelo.
* `Persistencia`: Manejo de serialización de objetos (`IOCF`).
* `Utilidades`: Clases auxiliares como validación de RUT y generación de Códigos.
* `Excepciones`: Manejo de errores personalizados (`CFException`).

---
