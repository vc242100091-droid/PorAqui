---
name: ponytail-review
description: >
  Revisión de código estilo senior: evalúa si la lógica es correcta, segura, resistente bajo carga, testeada, rápida y sobria (sin sobreingeniería). Usa para revisar diffs, commits o cambios.
---

Revisa los cambios de código como un desarrollador senior. El orden de prioridad es: **correcto > seguro > soporta carga > testeado > rápido > sobrio (lean)**.

## Proceso

1. Examina los cambios (diffs, funciones llamadas, tests, archivos afectados).
2. Evalúa:
   - **Errores:** Resultados incorrectos, casos límite no manejados, comportamiento inesperado.
   - **Riesgos:** Vulnerabilidades de seguridad, fuga o pérdida de datos.
   - **Escala/Carga:** Condiciones de carrera, fugas de memoria, algoritmos ineficientes.
   - **Pruebas faltantes:** Lógica compleja sin prueba unitaria que falle si se rompe.
   - **Velocidad:** Cuellos de botella significativos.
   - **Código sobrio (Lean):** Eliminación de código muerto, abstracciones innecesarias (YAGNI), o reemplazo por funciones estándar/existentes.

## Salida esperada

1. **Resumen de cambios:** En 2-3 frases.
2. **Hallazgos:** Clasificados en *Must fix* (Obligatorio), *Should fix* (Recomendado), y *Nice to have* (Opcional).
3. **Veredicto:** `Verdict: Ship` o `Verdict: arreglar X e Y primero`.
