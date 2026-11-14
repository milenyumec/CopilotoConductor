# Uber Copiloto — MVP inicial

Aplicación Android nativa para estimar la rentabilidad de ofertas de viajes.
Este es un esqueleto de arranque; no contiene integración automática con Uber.

## Requisitos
- Android Studio reciente
- JDK 17
- Android SDK 35
- Kotlin + Jetpack Compose

## Primer arranque
1. Abre Android Studio.
2. Selecciona **Open** y elige esta carpeta.
3. Espera la sincronización de Gradle.
4. Ejecuta en un dispositivo Android o emulador.

## Alcance de esta primera entrega
- Pantalla inicial de Uber Copiloto.
- Calculadora de rentabilidad editable.
- Parámetros iniciales: gasolina US$3/galón, consumo 9 km/l y reserva de mantenimiento US$0,10/km.
- Resultado estimado separado de los datos introducidos.
- No acepta ni rechaza viajes automáticamente.

## Integración futura
- Notificaciones: solo se podrán leer los campos que la app de origen realmente publique en su notificación.
- Captura/OCR: se implementará con MediaProjection, consentimiento visible del usuario y procesamiento local cuando sea posible.
- API Uber Driver: acceso limitado y sujeto a aprobación de Uber. No asumir que entrega solicitudes entrantes en tiempo real.
- No usar AccessibilityService para automatizar decisiones o pulsar aceptar/rechazar.

## Próximos hitos
1. Validar cálculos con viajes reales.
2. Añadir historial local Room/SQLite.
3. Añadir flujo de captura/OCR consentida.
4. Añadir análisis de notificaciones compatibles.
5. Backend PHP/MySQL, cuentas y sincronización.
6. Preparar privacidad, términos y publicación comercial.
