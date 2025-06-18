# 📦 InventaryApp

**InventaryApp** es una aplicación Android desarrollada con **Jetpack Compose**, diseñada para llevar el control de productos, movimientos (ventas y gastos), y generar reportes visuales. Está pensada para negocios pequeños que manejan inventario con productos simples o paquetes.

---

## 🧩 Características principales

### 🛍 Registro de productos
- ID autogenerado
- Nombre, precio, costo
- Tipo de cantidad (unidad/peso/etc.)
- Imagen (emoji, letra, o imagen del sistema)
- Detalles opcionales
- Asociación a una categoría
- Composición como paquete de otros productos

### 📈 Registro de movimientos
- Tipos de movimiento: venta o gasto
- Calculadora para sumar montos sin producto
- Selección de productos desde un `BottomSheet`
- Visualización de ítems agregados al movimiento

### 📊 Reportes (en desarrollo)
- Intervalos por fechas
- Productos más vendidos
- Totales por etiqueta, categoría o producto

### 🧭 Navegación
- Estilo tipo WhatsApp: navegación por pestañas (`TabRow`)
- Tabs: Productos, Movimientos y Reportes

---

## 🏗 Arquitectura

La app sigue una arquitectura limpia basada en **Clean Architecture** y **MVI**:

```
|-- core/
|   |-- base/
|   |-- extensions/
|   |-- utils/
|	
|-- data/
|   |-- local/ (Room)
|   |-- repository/ (implementación)
|
|-- domain/
|   |-- model/
|   |-- usecase/
|   |-- repository/
|
|-- di/ (modules)
|
|-- ui/ 
|   |-- activity/
|   |-- viewmodel/
|   |-- screens/
```

### Tecnologías y librerías

- 🛠 Jetpack Compose
- 🗃 Room (con `Flow`)
- 🧠 ViewModel + StateFlow + Channel (intents)
- 🔀 Clean Architecture + UseCases
- 🗂 BottomSheets y navegación por pestañas
- 🧪 Listo para pruebas unitarias y de UI

---

## ⚙️ Cómo clonar y correr la app

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/InventaryApp.git
   ```

2. Ábrelo en Android Studio `Giraffe+`

3. Ejecuta en un emulador o dispositivo Android (API 27+)

---

## 🚧 Roadmap

- [x] CRUD de productos
- [x] CRUD de categorías
- [x] Registro de movimientos con productos o montos libres
- [x] Soporte para productos tipo paquete
- [ ] Estadísticas por producto y categoría
- [ ] Soporte multiusuario / sincronización
- [ ] Exportar datos a Excel o CSV

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Puedes abrir un `issue` o enviar un `pull request`.

---

## ✨ Desarrollador

Creado por **Felipe Méndez**  
[GitHub](https://github.com/FelipeMz-dev) • [LinkedIn](https://www.linkedin.com/in/juan-felipe-mendez-carmona-3ab104242/)
