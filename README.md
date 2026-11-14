# Taller: Antipatrones de Diseño - La Oficina de Visas del Caos

Implementación y resolución del taller de antipatrones de diseño de software en Java.

---

## 1. Parte 1: Cacería de Antipatrones

| Fragmento | Antipatrón identificado | Síntoma delator | Consecuencia en el consulado |
|---|---|---|---|
| **Fragmento 1: La ventanilla de Ramiro** | **God Object** | La clase `OficinaDeTodo` concentra múltiples responsabilidades en un único método `procesar()`: valida pasaporte, procesa cobro, guarda expediente y envía correo. | Si Ramiro se enferma o falla cualquier tarea, todo el consulado se detiene. El código es rígido y no cumple SRP. |
| **Fragmento 2: La tarifa clonada** | **Copy-Paste Programming** | La fórmula de cálculo base `(50 * meses) + 10` está duplicada textualmente en los tres métodos de visado (`visaTurista`, `visaEstudiante`, `visaTrabajo`). | Al modificar la tarifa base, hay que editar 3 métodos manualmente. Si uno se olvida, quedan tarifas desactualizadas e inconsistentes. |
| **Fragmento 3: Los números misteriosos** | **Magic Numbers** | Presencia de constantes numéricas y multiplicadores sin contexto semántico (`1`, `2`, `3`, `17`, `15`, `31`, `66`, `0.5`, `1.5`). | Dificulta la lectura del código, y si entra un tipo inválido (ej. tipo 9), se rechaza en silencio sin explicar la causa. |
| **Fragmento 4: La cancelación en cadena** | **Acoplamiento Fuerte** | `SolicitudAcoplada` instancia directamente dependencias concretas (`new CorreoServicio()`, etc.) y las llama de forma manual y secuencial. | Si se olvida invocar un servicio (como ocurrió con la caja de pagos), el flujo falla silenciosamente y el usuario pierde su reembolso. |

---

## 2. Parte 2: Arquitectura del Rescate

Estructura de paquetes en `src/`:

```text
src/
├── Main.java                          # Ejecución de los 5 casos de prueba comparativos
├── legacy/                            # Fragmentos heredados originales
│   ├── CalculadoraDeTarifas.java
│   ├── CitasServicio.java
│   ├── CorreoServicio.java
│   ├── EvaluadorMagico.java
│   ├── OficinaDeTodo.java
│   ├── PagosServicio.java
│   └── SolicitudAcoplada.java
└── rescued/                           # Código refactorizado y libre de antipatrones
    ├── enums/                         # Solución Fragmento 3: Enums y validación clara
    │   ├── EvaluadorRequisitos.java
    │   └── TipoVisa.java
    ├── observer/                      # Solución Fragmento 4: Patrón Observer
    │   ├── AgendaCitas.java
    │   ├── CajaReembolsos.java
    │   ├── CorreoNotificador.java
    │   ├── ObservadorCancelacion.java
    │   └── SolicitudDesacoplada.java
    ├── srp/                           # Solución Fragmento 1: Responsabilidad Única
    │   ├── CajaCobro.java
    │   ├── NotificadorCorreo.java
    │   ├── RepositorioExpedientes.java
    │   ├── TramiteVisaServicio.java
    │   └── ValidadorDocumento.java
    └── strategy/                      # Solución Fragmento 2: Patrón Strategy
        ├── CalculadoraTarifasConsulado.java
        ├── EstrategiaTarifa.java
        ├── TarifaEstudiante.java
        ├── TarifaTrabajo.java
        └── TarifaTurista.java
```

### Soluciones aplicadas
1. **Responsabilidad Única (SRP):** Descomposición de `OficinaDeTodo` en componentes especializados (`ValidadorDocumento`, `CajaCobro`, `RepositorioExpedientes`, `NotificadorCorreo`), orquestados por `TramiteVisaServicio`.
2. **Patrón Strategy:** Cálculo desacoplado con la interfaz `EstrategiaTarifa` e implementaciones (`TarifaTurista`, `TarifaEstudiante`, `TarifaTrabajo`). `CalculadoraTarifasConsulado` sirve como **único punto de cambio** de la tarifa base.
3. **Enums y Constantes con Nombre:** `TipoVisa` modela los visados y sus rangos de edad. `EvaluadorRequisitos` devuelve mensajes comprensibles para códigos inválidos.
4. **Patrón Observer:** `SolicitudDesacoplada` notifica eventos a `ObservadorCancelacion` (`CorreoNotificador`, `AgendaCitas`, `CajaReembolsos`) sin acoplamiento rígido.

---

## 3. Parte 3: Casos de Prueba Ejecutados

Salida por consola al ejecutar `Main`:

```text
===============================================================================
            Taller: Antipatrones de Diseno de Software
                 La Oficina de Visas del Caos (Java)
===============================================================================

-------------------------------------------------------------------------------
CASO 1: God Object (OficinaDeTodo vs Clases con Responsabilidad Unica)
-------------------------------------------------------------------------------
[VERSION HEREDADA - Ramiro lo hace todo]:
Cobrando $150.0 a Carlos Mendoza
Guardando expediente de Carlos Mendoza
Correo para Carlos Mendoza: su visa va en camino

[VERSION RESCATADA - Responsabilidad Unica por Componentes]:
[ValidadorDocumento] Pasaporte verificado con exito: AB123456
[CajaCobro] Cobro registrado por $150,00 a nombre de Carlos Mendoza.
[RepositorioExpedientes] Expediente archivado digitalmente para Carlos Mendoza (Doc: AB123456).
[NotificadorCorreo] Correo enviado a Carlos Mendoza: su visa va en camino

>> CONCLUSION CASO 1: En la heredada Ramiro asume validacion, cobro, archivo
   y notificaciones. En la rescatada cada clase tiene una unica razon para cambiar.

-------------------------------------------------------------------------------
CASO 2: Copy-Paste Programming (CalculadoraDeTarifas vs Patron Strategy)
-------------------------------------------------------------------------------
[VERSION HEREDADA - Tarifas clonadas]:
Calculando tarifas originales (Base 50):
 - Turista:   $310.0
 - Estudiante:$155.0
 - Trabajo:   $465.0
Subiendo tarifa base de 50 a 60 en la version clonada...
Lugares que tuvieron que editarse en el codigo heredado: 3
Resultados actualizados: Turista=$370.0, Estudiante=$185.0, Trabajo=$555.0

[VERSION RESCATADA - Patron Strategy con Tarifa Base Centralizada]:
Calculando tarifas originales (Base 50):
 - Turista:   $310.0
 - Estudiante:$155.0
 - Trabajo:   $465.0
Subiendo tarifa base de 50 a 60 en la version Strategy...
Lugares editados en la version Strategy: 1
Resultados actualizados: Turista=$370.0, Estudiante=$185.0, Trabajo=$555.0

Comparacion de precios finales:
 - Turista:    Heredada=370.0 | Rescatada=370.0 (Coinciden: true)
 - Estudiante: Heredada=185.0 | Rescatada=185.0 (Coinciden: true)
 - Trabajo:    Heredada=555.0 | Rescatada=555.0 (Coinciden: true)

>> CONCLUSION CASO 2: En la version clonada se requirieron 3 ediciones propensas
   a error. En la version Strategy solo se modifico 1 unico punto y los precios coinciden.

-------------------------------------------------------------------------------
CASO 3: Magic Numbers (EvaluadorMagico vs Enums con Constantes)
-------------------------------------------------------------------------------
[PRUEBA A: Tipo 2 y Edad 20]
 - Version Heredada (tipo == 2, edad 20):        APROBADA
 - Version Rescatada (TipoVisa.ESTUDIANTE, 20): APROBADA
 -> Ambos resultados coinciden: true

[PRUEBA B: Tipo 9 Invalido / Inexistente]
 - Version Heredada (tipo == 9): RECHAZADA (Rechazo silencioso, sin explicacion)
 - Version Rescatada (tipo == 9): RECHAZADA: Codigo de visa 9 no existe en el sistema consular.

>> CONCLUSION CASO 3: Se reemplazaron numeros magicos (1, 2, 3, 17, 31, 66) por
   tipos expresivos y claros con retroalimentacion detallada ante valores invalidos.

-------------------------------------------------------------------------------
CASO 4: Acoplamiento Fuerte (SolicitudAcoplada vs Patron Observer)
-------------------------------------------------------------------------------
[VERSION HEREDADA - Cancelacion con dependencias fijas y olvido de caja]:
Solicitud cambiada a: CANCELADA
[CorreoServicio] Notificacion enviada: Su solicitud ha sido cancelada.
[CitasServicio] Cupo de cita liberado en la agenda.
--> EVIDENCIA DE FALLA: No se invoco PagosServicio. El reembolso quedo en el limbo!

[VERSION RESCATADA - Solicitud Desacoplada con Patron Observer]:
[Solicitud #SOL-2026-981] Estado actualizado a: CANCELADA
[CorreoNotificador] Notificacion remitida a Elena Rios confirmando la cancelacion del tramite SOL-2026-981.
[AgendaCitas] Cupo de cita consular presencial liberado para solicitud SOL-2026-981.
[CajaReembolsos] Orden de devolucion y reembolso generada para el titular Elena Rios (Solicitud: SOL-2026-981).

>> CONCLUSION CASO 4: La version con Observer notifica a correo, citas y caja
   de pagos de manera desacoplada y automatica, garantizando que nadie pierda su reembolso.

-------------------------------------------------------------------------------
CASO 5: Integracion de Punta a Punta con la Version Rescatada
-------------------------------------------------------------------------------
Simulando viaje completo de solicitud, evaluacion, cobro y eventual cancelacion...

Paso 1: Evaluacion de requisitos para Mateo Salazar (Visa de Estudiante, 24 anos):
        Resultado: APROBADA

Paso 2: Calculo de la tarifa oficial usando Strategy:
        Tarifa calculada: $365,00 (Base $60,00, 12 meses, descuento estudiante)

Paso 3: Tramite de ventanilla aplicando SRP:
[ValidadorDocumento] Pasaporte verificado con exito: CO789654
[CajaCobro] Cobro registrado por $365,00 a nombre de Mateo Salazar.
[RepositorioExpedientes] Expediente archivado digitalmente para Mateo Salazar (Doc: CO789654).
[NotificadorCorreo] Correo enviado a Mateo Salazar: su visa va en camino

Paso 4: El viajero cancela su solicitud; Observer orquesta los servicios:
[Solicitud #SOL-2026-999] Estado actualizado a: CANCELADA
[CorreoNotificador] Notificacion remitida a Mateo Salazar confirmando la cancelacion del tramite SOL-2026-999.
[AgendaCitas] Cupo de cita consular presencial liberado para solicitud SOL-2026-999.
[CajaReembolsos] Orden de devolucion y reembolso generada para el titular Mateo Salazar (Solicitud: SOL-2026-999).

>> CONCLUSION CASO 5: Todo el flujo consular funciono de forma limpia,
   modular, mantenible y sin antipatrones.

=========================================================================================================
                                RESUMEN FINAL: CACERIA DE ANTIPATRONES
=========================================================================================================
FRAGMENTO          | ANTIPATRON       | SINTOMA DELATOR                     | CONSECUENCIA EN EL CONSULADO
-------------------+------------------+-------------------------------------+-----------------------------
Frag 1: Ventanilla | God Object       | OficinaDeTodo valida, cobra, archiva | Si Ramiro se enferma, todo el
de Ramiro          |                  | y notifica en un solo metodo        | consulado se paraliza       
-------------------+------------------+-------------------------------------+-----------------------------
Frag 2: Tarifa     | Copy-Paste       | Formula duplicada en 3 metodos con  | Tarifas inconsistentes al   
clonada            | Programming      | valores fijos (50 * meses + 10)     | actualizar una y olvidar otra
-------------------+------------------+-------------------------------------+-----------------------------
Frag 3: Numeros    | Magic Numbers    | Literales 1, 2, 3, 17, 31, 66 y     | Imposible de mantener; codigos
misteriosos        |                  | multiplicadores sin nombre semantico | desconocidos fallan en silencio
-------------------+------------------+-------------------------------------+-----------------------------
Frag 4: Cancelacion | Acoplamiento     | Solicitud crea y llama manualmente  | Si olvidan llamar a un servicio
en cadena          | Fuerte           | servicios; olvido de caja de pagos  | (como pagos), se pierde plata
=========================================================================================================
```

---

## 4. Reflexión: Cómo evitar caer de nuevo en los antipatrones

Para evitar que el código caiga nuevamente en estos antipatrones, se aplicó el principio de responsabilidad única (SRP) y contratos mediante interfaces (`EstrategiaTarifa`, `ObservadorCancelacion`). Esto permite extender el sistema sin modificar clases existentes (Open/Closed Principle): si en el futuro se incorpora un nuevo visado, se añade una estrategia de tarifa o un valor al enum sin alterar el código que ya funciona. De igual manera, desacoplar las notificaciones con el patrón Observer previene que dependencias rígidas o llamadas manuales olvidadas dejen procesos inconsistentes.

---

## 5. Instrucciones de Compilación y Ejecución

```bash
# Compilar
javac -d bin src/*.java src/legacy/*.java src/rescued/srp/*.java src/rescued/strategy/*.java src/rescued/enums/*.java src/rescued/observer/*.java

# Ejecutar
java -cp bin Main
```
