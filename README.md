# 📦 InventaryApp

**InventaryApp** es una aplicación Android desarrollada con **Jetpack Compose**, pensada para pequeños negocios que necesitan llevar el control de su inventario de manera simple y ordenada.
Con ella podrás **registrar productos, registrar ventas y gastos, y generar reportes visuales** para entender mejor el rendimiento de tu negocio en distintos periodos de tiempo.

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

### 📱 Diseño

| **Inventario** | **Movimientos** | **Reportes** |
|----------------|-----------------|--------------|
|![WhatsApp Image 2025-09-26 at 3 40 16 PM](https://github.com/user-attachments/assets/bfebd1fe-b901-4381-babb-bb8644c0d3fb)|![WhatsApp Image 2025-09-26 at 3 40 16 PM (1)](https://github.com/user-attachments/assets/e0cd9361-fda4-475c-8765-555a62602f03)|![WhatsApp Image 2025-09-26 at 3 40 16 PM (2)](https://github.com/user-attachments/assets/8b6aa0f5-c686-461c-b35a-4674abbf66fe)|
|![WhatsApp Image 2025-09-26 at 3 40 16 PM (4)](https://github.com/user-attachments/assets/91dbeb81-0ed2-43e8-87a7-405076d1d352)|![WhatsApp Image 2025-09-26 at 3 40 16 PM (3)](https://github.com/user-attachments/assets/da6b6e9f-0382-411f-9cce-ec8b9c70290d)|![WhatsApp Image 2025-09-26 at 3 40 17 PM](https://github.com/user-attachments/assets/7255273b-6f4e-4902-a9d9-0ef0539e9fca)



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
- [ ] Estadísticas por fechas, montos y productos
- [ ] Soporte multiusuario / sincronización en la nube
- [ ] Exportar datos a Excel o CSV

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Puedes abrir un `issue` o enviar un `pull request`.

---

## ✨ Desarrollador

Creado por **Felipe Méndez**  
[GitHub](https://github.com/FelipeMz-dev) • [LinkedIn](https://www.linkedin.com/in/juan-felipe-mendez-carmona-3ab104242/)
