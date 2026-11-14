# Taller para la Casa – Antipatrones de Diseño: La Oficina de Visas del Caos

**Universidad Distrital Francisco José de Caldas**  
**Facultad de Ingeniería – Ingeniería de Sistemas**  
**Asignatura:** Modelos de Programación  
**Docente:** Bocanegra  
**Fecha:** Noviembre de 2026  

### Integrantes
* Eddie Santiago Rondón Capera – 20251020108
* Luna Alejandra Sandoval Rodríguez – 20241020053

---

## 1. Parte 1: Cacería de Antipatrones

A continuación se presenta el análisis del sistema heredado del consulado caótico:

| Fragmento | Antipatrón identificado | Síntoma delator | Consecuencia en el consulado |
|---|---|---|---|
| **Fragmento 1: La ventanilla de Ramiro** | **God Object (Objeto Todopoderoso)** | La clase `OficinaDeTodo` concentra múltiples responsabilidades en un único método `procesar()`: valida la estructura del pasaporte, procesa el cobro económico, guarda el expediente y envía notificaciones por correo. | Violación flagrante del principio de responsabilidad única (SRP). Si cambia cualquiera de estas reglas o falla el componente (o "se enferma Ramiro"), todo el consulado se paraliza. El código es rígido, no testeable y difícil de mantener. |
| **Fragmento 2: La tarifa clonada** | **Copy-Paste Programming (Código Duplicado)** | La fórmula base `(50 * meses) + 10` se encuentra copiada textualmente en los tres métodos (`visaTurista`, `visaEstudiante`, `visaTrabajo`). Cada método repite el cálculo en lugar de reutilizarlo o parametrizarlo. | Si cambia la tarifa base consular, se debe recordar editar cada método individualmente. Un error humano o descuido deja tarifas desactualizadas e inconsistentes (como ocurrió en el consulado al olvidar actualizar la de trabajo). |
| **Fragmento 3: Los números misteriosos** | **Magic Numbers (Números Mágicos)** | Presencia de constantes numéricas sin contexto semántico: tipos `1`, `2`, `3` y rangos de edades `17`, `15`, `31`, `66`, además de factores `0.5` o `1.5` sin nombre explicativo. | El código es ilegible e indescifrable para nuevos desarrolladores. Además, valores no contemplados (como un tipo 9) son rechazados silenciosamente sin dar retroalimentación ni mensajes claros del error. |
| **Fragmento 4: La cancelación en cadena** | **Acoplamiento Fuerte (Tight Coupling)** | `SolicitudAcoplada` instancia directamente (`new CorreoServicio()`, etc.) y encadena llamadas explícitas a otros subsistemas. | Flujo frágil y dependencias rígidas. Al agregar o modificar avisos, hay que modificar directamente la clase de solicitud. En el caso real, se les olvidó invocar a la caja de pagos y el usuario perdió su dinero de reembolso. |

---

## 2. Parte 2: Arquitectura del Rescate

La solución se organizó de manera modular bajo el directorio `src/`, dividida en paquetes según el problema atacado:

```text
src/
├── Main.java                          # Ejecución comparativa de los 5 casos de prueba
├── legacy/                            # Código heredado original (para contraste)
│   ├── CalculadoraDeTarifas.java
│   ├── CitasServicio.java
│   ├── CorreoServicio.java
│   ├── EvaluadorMagico.java
│   ├── OficinaDeTodo.java
│   ├── PagosServicio.java
│   └── SolicitudAcoplada.java
└── rescued/                           # Versión refactorizada y libre de antipatrones
    ├── enums/                         # Solución Fragmento 3 (Enums y constantes)
    │   ├── EvaluadorRequisitos.java
    │   └── TipoVisa.java
    ├── observer/                      # Solución Fragmento 4 (Patrón Observer)
    │   ├── AgendaCitas.java
    │   ├── CajaReembolsos.java
    │   ├── CorreoNotificador.java
    │   ├── ObservadorCancelacion.java
    │   └── SolicitudDesacoplada.java
    ├── srp/                           # Solución Fragmento 1 (Responsabilidad Única)
    │   ├── CajaCobro.java
    │   ├── NotificadorCorreo.java
    │   ├── RepositorioExpedientes.java
    │   ├── TramiteVisaServicio.java
    │   └── ValidadorDocumento.java
    └── strategy/                      # Solución Fragmento 2 (Patrón Strategy)
        ├── CalculadoraTarifasConsulado.java
        ├── EstrategiaTarifa.java
        ├── TarifaEstudiante.java
        ├── TarifaTrabajo.java
        └── TarifaTurista.java
```

### Soluciones aplicadas
1. **Responsabilidad Única (SRP):** Se dividió `OficinaDeTodo` en componentes especializados (`ValidadorDocumento`, `CajaCobro`, `RepositorioExpedientes`, `NotificadorCorreo`), orquestados por `TramiteVisaServicio`.
2. **Patrón Strategy:** Se desacopló el cálculo mediante la interfaz `EstrategiaTarifa` implementada por `TarifaTurista`, `TarifaEstudiante` y `TarifaTrabajo`. La clase `CalculadoraTarifasConsulado` concentra la tarifa base como **único punto de cambio**.
3. **Enum con constantes con nombre:** El enum `TipoVisa` modela los tipos (`TURISTA`, `ESTUDIANTE`, `TRABAJO`), encapsulando sus códigos y límites de edad. Se proporciona validación explícita con mensajes comprensibles si se ingresan tipos no existentes.
4. **Patrón Observer:** La clase `SolicitudDesacoplada` implementa un sujeto observable que emite eventos a objetos que implementan `ObservadorCancelacion`. De esta forma se notifican `CorreoNotificador`, `AgendaCitas` y `CajaReembolsos` sin acoplamiento directo.

---

## 3. Parte 3: Casos de Prueba Ejecutados

### Salida directa en consola de `Main.java`

```text
===============================================================================
        UNIVERSIDAD DISTRITAL FRANCISCO JOSE DE CALDAS
            Facultad de Ingenieria - Ingenieria de Sistemas
                   Modelos de Programacion
            Taller para la Casa: Antipatrones de Diseno
                 La Oficina de Visas del Caos (Java)
===============================================================================
Estudiantes: Eddie Santiago Rondon Capera (20251020108)
             Luna Alejandra Sandoval Rodriguez (20241020053)
Docente:     Bocanegra

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

Para evitar que el sistema vuelva a deteriorarse en parches sucesivos, la arquitectura implementada se basa en el principio de separación de responsabilidades y en el principio Abierto/Cerrado (OCP). En lugar de centralizar tareas en una sola clase o duplicar código por rapidez, se establecieron contratos claros mediante interfaces (`EstrategiaTarifa`, `ObservadorCancelacion`) y modelos fuertemente tipados (`TipoVisa`). Si en el futuro surge un nuevo tipo de visa, basta con agregar una nueva constante en el enumerador o una nueva implementación de la estrategia de cobro sin tener que alterar el código existente ni reescribir validaciones. De igual manera, si se requiere un nuevo servicio durante la cancelación (por ejemplo, auditoría o emisión de constancias), solo se suscribe un nuevo observador sin tocar la clase `SolicitudDesacoplada`, previniendo efectos secundarios indeseados y garantizando un mantenimiento sostenible en el tiempo.

---

## 5. Instrucciones de Compilación y Ejecución

Desde una terminal en la raíz del proyecto:

```bash
# Compilar los archivos Java en la carpeta bin:
javac -d bin src/*.java src/legacy/*.java src/rescued/srp/*.java src/rescued/strategy/*.java src/rescued/enums/*.java src/rescued/observer/*.java

# Ejecutar el programa principal:
java -cp bin Main
```
