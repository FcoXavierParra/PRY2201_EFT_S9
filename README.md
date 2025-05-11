# 🎭 Sistema de Gestión de Entradas - Teatro Moro

Este proyecto implementa una aplicación de consola en Java para la gestión de entradas en el Teatro Moro, incluyendo la compra, reserva, visualización de promociones y generación de boletas.

## 📌 Objetivo

Simular un sistema completo de ventas de entradas que permita:

- Comprar y reservar entradas.
- Aplicar promociones por tipo de cliente.
- Confirmar reservas temporales.
- Visualizar ingresos y ventas por zonas.
- Modificar entradas compradas.
- Emitir boletas personalizadas por cliente.

## 🚀 Funcionalidades Principales

- **Zonas disponibles:** VIP, Palco, Platea Baja, Platea Alta y Galería.
- **Descuentos aplicables:**
  - 10% para niños (< 12 años)
  - 20% para mujeres
  - 15% para estudiantes
  - 25% para tercera edad (≥ 60 años)
- **Reserva con tiempo limitado:** 2 minutos antes de expirar automáticamente.
- **Visualización de resumen de ventas por zona.**
- **Modificación de entradas compradas.**
- **Generación de boletas detalladas.**

## 🧠 Lógica del Sistema

El sistema funciona a través de un menú interactivo por consola, y se apoya en las siguientes estructuras:

- Arrays 2D para representar los asientos de cada zona.
- Clases `Cliente` y `Entrada` para manejar la lógica de negocio.
- Temporizador (`TimerTask`) para gestionar la expiración de reservas.
- Listas dinámicas (`ArrayList`) para almacenar las entradas compradas y reservadas.

## 🏗️ Estructura del Código

- `EFT_S9_Francisco_Parra.java` – Clase principal con lógica del sistema.
- `Cliente` – Contiene los datos personales y de elegibilidad para descuentos.
- `Entrada` – Contiene los datos de la ubicación y precios de una entrada.

## 📈 Estadísticas Generadas

- Cantidad de entradas por zona.
- Ingresos por zona.
- Total de descuentos aplicados por tipo.
- Ingreso neto final.

## 📚 Autor

- Desarrollado por: **Francisco J. Parra A.**
- Curso: Fundamentos de Programación - Duoc UC

