# dsm-desafio3

Aplicación móvil desarrollada en Android utilizando Kotlin, enfocada en la gestión de autenticación de usuarios y consumo de APIs mediante Retrofit. El proyecto implementa una arquitectura organizada y manejo de sesiones para brindar una experiencia de usuario fluida.

---

## 🚀 Características

- 🔐 Inicio de sesión de usuarios
- 👤 Registro de nuevos usuarios
- 📡 Consumo de API REST con Retrofit
- 💾 Manejo de sesiones
- 🎨 Interfaz moderna en Android
- 📱 Navegación entre pantallas
- ⚡ Desarrollo en Kotlin

---

## 🛠️ Tecnologías utilizadas

- **Kotlin**
- **Android Studio**
- **Retrofit**
- **XML Layouts**
- **Gradle**
- **REST API**

---

## 📂 Estructura del proyecto

```bash
dsm-desafio3/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/rodriguezcortez/desafio/
│   │   │   │   ├── model/
│   │   │   │   ├── network/
│   │   │   │   ├── util/
│   │   │   │   └── view/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │
│   └── build.gradle
│
├── gradle/
├── build.gradle
└── README.md
```

---

## 📦 Instalación

1. Clona el repositorio:

```bash
git clone https://github.com/RoqueLovelace/dsm-desafio3.git
```

2. Abre el proyecto en Android Studio.

3. Sincroniza las dependencias de Gradle.

4. Ejecuta la aplicación en un emulador o dispositivo físico.

---

## ▶️ Ejecución

Desde Android Studio:

- Selecciona un dispositivo o emulador
- Presiona el botón **Run ▶**

---

## 🔧 Configuración

Asegúrate de tener instalado:

- Android Studio Hedgehog o superior
- JDK 21+
- SDK de Android actualizado

---

## 📡 API

El proyecto utiliza Retrofit para consumir servicios REST.

Ejemplo de inicialización:

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://tu-api.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

---

## 👨‍💻 Autor

Desarrollado por [RoqueLovelace](https://github.com/RoqueLovelace) y [DiegoMajano](https://github.com/DiegoMajano)

---

## 📄 Licencia

Este proyecto es de uso académico y educativo.

---
