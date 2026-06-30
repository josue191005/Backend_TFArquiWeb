## **1.1.**                   **User Stories.**

 

| US01: Registrarme en la App  | Épica: EP01  |
| :---- | :---- |
| **Como**  | adolescente usuario o psicólogo  |
| **Quiero**  | registrarme dentro de la app de manera rápida y sencilla  |
| **Para**  | no sentirme abrumado y acceder a mi cuenta de StayCool  |
| **Pantalla(s)**  | Pantalla 1, Pantalla 2, Pantalla 3  |
| **Validaciones y Datos**  | **Request:** {"nombre": "string", "correo": "string", "contrasena": "string", "nombreEmpresa": "string (opcional)", "rol": "USER|PSYCHOLOGIST"}    **Response:** {"status": 201, "message": "Usuario creado exitosamente", "token": "string"}  |
| **Criterios de Aceptación**  | • El usuario debe poder ingresar sus datos personales y rol para crear una cuenta.    • En el caso de que ya este registrado, el sistema mostrará un mensaje de error indicando "Este correo ya está en uso" si el email ingresado ya existe en los registros.  |
| **Endpoint(s)**  | POST /api/v1/auth/register  |
| **Evidencia**  | Evidencia\_Swagger\_US01  |

 

| US02: Recibir notificaciones de pausa digital  | Épica: EP02  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | recibir notificaciones de pausa digital con mensajes de ánimo  |
| **Para**  | poder descansar sin estrés ni culpabilidad  |
| **Pantalla(s)**  | Pantalla 4, Pantalla 5  |
| **Validaciones y Datos**  | **Request:** {"userId": "UUID"}    **Response:** {"status": 200, "alertId": "UUID", "message": "string", "type": "PAUSE"}  |
| **Criterios de Aceptación**  | • El sistema enviará una notificación con un mensaje de descanso al usuario cuando supere el tiempo máximo de pantalla configurado.  • Si el usuario ha desactivado las notificaciones en sus ajustes, el sistema respetará la configuración y no enviará ninguna alerta.  |
| **Endpoint(s)**  | GET /api/v1/alertas  |
| **Evidencia**  | Evidencia\_Swagger\_US02  |

 

| US03: Recibir Alertas de indicadores críticos  | Épica: EP02  |
| :---- | :---- |
| **Como**  | psicólogo  |
| **Quiero**  | recibir alertas de indicadores críticos de uso excesivo  |
| **Para**  | dar seguimiento y notificar a mis pacientes  |
| **Pantalla(s)**  | Pantalla 6, pantalla 7  |
| **Validaciones y Datos**  | **Request:** {"psychologistId": "UUID"}  **Response:** \[{"patientId": "UUID", "alertLevel": "CRITICAL", "excessMinutes": 120}\]  |
| **Criterios de Aceptación**  | • El psicólogo visualizará una alerta en su panel cuando uno de sus pacientes exceda el límite de uso configurado de forma crítica.    • El sistema asegurará que el psicólogo solo reciba notificaciones de los adolescentes que tenga asignados como pacientes.  |
| **Endpoint(s)**  | GET /api/v1/psychologist/alerts/critical  |
| **Evidencia**  | Evidencia\_Swagger\_US03  |

 

| US04: Recibir estadísticas de tiempo en pantalla  | Épica: EP02  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | ver un gráfico estadístico de mi tiempo de pantalla y descansos  |
| **Para**  | orientarme sobre las mejoras para mi estilo de vida  |
| **Pantalla(s)**  | Pantalla 8   |
| **Validaciones y Datos**  | **Request:** {"userId": "UUID", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD"}  **Response:** {"dailyUsage": \[{"date": "YYYY-MM-DD", "activeMinutes": 200, "restMinutes": 45}\]}  |
| **Criterios de Aceptación**  | • El usuario podrá visualizar un gráfico claro con el tiempo diario que ha pasado en la aplicación y sus tiempos de descanso.  • El sistema mostrará un mensaje de advertencia si el usuario selecciona un rango de fechas inválido o ilógico.  |
| **Endpoint(s)**  | GET /api/v1/statistics/usage  |
| **Evidencia**  | Evidencia\_Swagger\_US04  |

 

| US05: Chatear con un experto  | Épica: EP03  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | tener acceso a un experto o especialista psicológico  |
| **Para**  | brindar consultas detalladas de forma privada  |
| **Pantalla(s)**  | Pantalla 9   |
| **Validaciones y Datos**  | **Payload WS:** {"chatId": "UUID", "senderId": "UUID", "content": "string", "timestamp": "ISO8601"}  |
| **Criterios de Aceptación**  | • El usuario podrá enviar y recibir mensajes de texto en tiempo real dentro de la sala de chat con el psicólogo.  • Si se pierde la conexión a internet, el chat intentará reconectarse automáticamente para no interrumpir la sesión.  |
| **Endpoint(s)**  | POST /api/v1/chat/messages  |
| **Evidencia**  | Evidencia\_Swagger\_US05  |

 

| US06: Buscar información para problema específico  | Épica: EP03  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | encontrar información filtrada sobre temas específicos  |
| **Para**  | tratar problemas puntuales sobre el uso de TICs  |
| **Pantalla(s)**  | Pantalla 10  |
| **Validaciones y Datos**  | **Request:** ?query=ansiedad\&category=tips  **Response:** {"results": \[{"id": "UUID", "title": "string", "url": "string"}\]}  |
| **Criterios de Aceptación**  | • El usuario podrá ingresar palabras clave y aplicar categorías para encontrar artículos o videos específicos.  • Si la búsqueda no coincide con ningún recurso, la pantalla mostrará un mensaje indicando "No existen resultados".  |
| **Endpoint(s)**  | GET /api/v1/resources/search  |
| **Evidencia**  | Evidencia\_Swagger\_US06  |

 

| US07: Realizar ejercicios de relajación guiado  | Épica: EP04  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | realizar ejercicios de relajación o meditación  |
| **Para**  | sentirme aliviado de síntomas de estrés  |
| **Pantalla(s)**  | Pantalla 11  |
| **Validaciones y Datos**  | **Request:** ?type=MEDITATION|MUSIC  **Response:** \[{"resourceId": "UUID", "mediaUrl": "string", "duration": "INT"}\]  |
| **Criterios de Aceptación**  | • El sistema presentará una lista de recursos (música o guías de meditación) basada en la opción que seleccione el usuario.  • El usuario podrá reproducir el contenido multimedia directamente desde esta sección sin salir de la app.  |
| **Endpoint(s)**  | GET /api/v1/resources/relaxation  |
| **Evidencia**  | Evidencia\_Swagger\_US07  |

 

| US08: Monitoreo de Pacientes (Psicólogo)  | Épica: EP05  |
| :---- | :---- |
| **Como**  | psicólogo  |
| **Quiero**  | ver y filtrar registros de actividades de mis pacientes  |
| **Para**  | priorizar intervenciones urgentes  |
| **Pantalla(s)**  | Pantalla 6   |
| **Validaciones y Datos**  | **Request:** ?sortBy=alertLevel\&order=DESC  **Response:** {"patients": \[{"id": "UUID", "name": "string", "status": "CRITICAL|STABLE"}\]}  |
| **Criterios de Aceptación**  | • El psicólogo verá una lista de sus pacientes, la cual se ordenará automáticamente colocando primero aquellos con alertas críticas.  • La lista se mostrará paginada o con scroll continuo para facilitar la navegación cuando existan muchos registros.  |
| **Endpoint(s)**  | GET /api/v1/psychologist/patients/monitoring  |
| **Evidencia**  | Evidencia\_Swagger\_US08  |

 

| US09: Subir materiales de asesoramiento  | Épica: EP03  |
| :---- | :---- |
| **Como**  | psicólogo  |
| **Quiero**  | subir materiales o videos informativos  |
| **Para**  | apoyar a mis pacientes a tener un estilo de vida responsable  |
| **Pantalla(s)**  | Pantalla 12  |
| **Validaciones y Datos**  | **Request:** Multipart/form-data (file: Blob, title: string)  **Response:** {"status": 201, "mediaUrl": "string"}  |
| **Criterios de Aceptación**  | • El psicólogo podrá adjuntar archivos como PDFs o videos cortos para compartirlos en la plataforma.  • El sistema notificará con un error si el archivo seleccionado es demasiado pesado o tiene un formato no válido.  |
| **Endpoint(s)**  | POST /api/v1/psychologist/materials/upload  |
| **Evidencia**  | Evidencia\_Postman\_US09  |

 

| US10: Exportar información de pacientes  | Épica: EP05  |
| :---- | :---- |
| **Como**  | psicólogo  |
| **Quiero**  | exportar datos anónimos agregados en PDF  |
| **Para**  | evaluar tendencias colectivas en organizaciones  |
| **Pantalla(s)**  | Pantalla 13, pantalla 23  |
| **Validaciones y Datos**  | **Request:** {"anonymize": true, "dateRange": "YYYY-MM"}  **Response:** application/pdf (Binary Stream)  |
| **Criterios de Aceptación**  | • El psicólogo podrá generar y descargar un documento PDF con las estadísticas generales de los adolescentes.    • El reporte exportado ocultará los nombres y datos personales para proteger el anonimato de los pacientes.  |
| **Endpoint(s)**  | POST /api/v1/psychologist/patients/export/pdf  |
| **Evidencia**  | Evidencia\_Swagger\_US10  |

 

| US11: Configuración de Perfil  | Épica: EP06  |
| :---- | :---- |
| **Como**  | usuario (adolescente o psicólogo)  |
| **Quiero**  | editar mi información personal y preferencias  |
| **Para**  | mantener mis datos actualizados en el sistema  |
| **Pantalla(s)**  | Pantalla 14  |
| **Validaciones y Datos**  | **Request:** {"nombre": "string", "preferencias": {"notificaciones": boolean}}    **Response:** {"status": 200, "message": "Perfil actualizado"}  |
| **Criterios de Aceptación**  | • El usuario podrá ingresar a los ajustes para modificar su nombre y activar/desactivar sus preferencias.    • Al presionar "Guardar cambios", la plataforma confirmará visualmente que la nueva información ha sido actualizada.  |
| **Endpoint(s)**  | PUT /api/v1/users/profile  |
| **Evidencia**  | Evidencia\_Swagger\_US11  |

 

| US12: Revisar calendario de actividades  | Épica: EP08  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | revisar mi calendario dentro de la app  |
| **Para**  | visualizar todas mis actividades y citas programadas  |
| **Pantalla(s)**  | Pantalla 15  |
| **Validaciones y Datos**  | **Request:** ?month=05\&year=2026    **Response:** {"events": \[{"eventId": "UUID", "title": "string", "datetime": "ISO8601"}\]}  |
| **Criterios de Aceptación**  | • El usuario podrá visualizar un calendario mensual o semanal con marcadores en los días que tenga eventos agendados.    • Si un mes no tiene actividades programadas, el calendario simplemente se mostrará en blanco sin lanzar errores.  |
| **Endpoint(s)**  | GET /api/v1/calendar/events  |
| **Evidencia**  | Evidencia\_Swagger\_US12  |

 

| US13: Iniciar sesión  | Épica: EP01  |
| :---- | :---- |
| **Como**  | adolescente usuario o psicólogo  |
| **Quiero**  | iniciar sesión en la app  |
| **Para**  | tener acceso rápido a mi cuenta  |
| **Pantalla(s)**  | Pantalla 16  |
| **Validaciones y Datos**  | **Request:** {"correo": "string", "contrasena": "string"}    **Response:** {"status": 200, "token": "JWT\_TOKEN", "rol": "USER"}  |
| **Criterios de Aceptación**  | • El usuario podrá acceder a su interfaz principal si proporciona el correo electrónico y la contraseña correctos.    • El sistema mostrará un mensaje de alerta si las credenciales ingresadas son incorrectas, impidiendo el acceso.  |
| **Endpoint(s)**  | POST /api/v1/auth/login  |
| **Evidencia**  | Evidencia\_Postman\_US13  |

 

| US14: Cerrar sesión  | Épica: EP08  |
| :---- | :---- |
| **Como**  | usuario  |
| **Quiero**  | cerrar mi sesión en la aplicación  |
| **Para**  | proteger la privacidad de mi cuenta al salir  |
| **Pantalla(s)**  | Pantalla 17  |
| **Validaciones y Datos**  | **Request:** Header: Authorization Bearer \<Token\>    **Response:** {"status": 200, "message": "Sesión cerrada"}  |
| **Criterios de Aceptación**  | • El usuario podrá utilizar la opción "Cerrar sesión" desde el menú principal para desconectarse de forma segura.    • Una vez cerrada la sesión, la aplicación redirigirá inmediatamente a la pantalla inicial de inicio de sesión.  |
| **Endpoint(s)**  | POST /api/v1/auth/logout  |
| **Evidencia**  | Evidencia\_Postman\_US14  |

   
 

| US15: Revisar historial de chats  | Épica: EP08  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | ver mis conversaciones anteriores con los psicólogos  |
| **Para**  | consultar respuestas y registros previos  |
| **Pantalla(s)**  | Pantalla 18  |
| **Validaciones y Datos**  | **Request:** Header: Authorization Bearer \<Token\>    **Response:** {"chatRooms": \[{"roomId": "UUID", "psychologistName": "string", "lastMessage": "string", "date": "ISO8601"}\]}  |
| **Criterios de Aceptación**  | • El usuario verá una lista de sus conversaciones ordenadas por fecha, desde la más reciente hasta la más antigua.  • Al tocar un chat específico, el usuario podrá leer todo el historial de mensajes anteriores que haya tenido con ese experto.  |
| **Endpoint(s)**  | GET /api/v1/chat/history  |
| **Evidencia**  | Evidencia\_Swagger\_US15  |

   
 

| US16: Panel de indicadores clínicos  | Épica: EP05  |
| :---- | :---- |
| **Como**  | psicólogo  |
| **Quiero**  | un dashboard con gráficos de indicadores críticos agregados  |
| **Para**  | detectar patrones de riesgo de manera rápida en mis pacientes  |
| **Pantalla(s)**  | Pantalla 7  |
| **Validaciones y Datos**  | **Request:** ?filterGroup=UUID\&startDate=YYYY-MM-DD    **Response:** {"avgHours": 4.5, "criticalAlerts": 12, "trend": "+5%"}  |
| **Criterios de Aceptación**  | • El psicólogo visualizará un resumen gráfico que muestre el promedio general de uso y la cantidad de alertas de sus pacientes.    • El panel permitirá aplicar filtros rápidos (por fechas o grupos) para actualizar las gráficas en pantalla.  |
| **Endpoint(s)**  | GET /api/v1/psychologist/dashboard/metrics  |
| **Evidencia**  | Evidencia\_Swagger\_US16  |

 

| US17: Establecer metas Diarias  | Épica: EP02  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | definir un objetivo de tiempo máximo diario de uso  |
| **Para**  | medir mi progreso y autorregular mi conducta  |
| **Pantalla(s)**  | Pantalla 19  |
| **Validaciones y Datos**  | **Request:** {"targetMinutes": 120}    **Response:** {"status": 201, "message": "Meta guardada"}  |
| **Criterios de Aceptación**  | • El usuario podrá seleccionar una cantidad de horas y minutos como su límite objetivo para el día.    • Al presionar "Guardar", la meta se registrará exitosamente y será visible en la pantalla principal.  |
| **Endpoint(s)**  | POST /api/v1/goals/daily  |
| **Evidencia**  | Evidencia\_Swagger\_US17  |

 

| US18: Sistema de recompensas  | Épica: EP08  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | ganar insignias o puntos al cumplir mis metas  |
| **Para**  | sentirme motivado y reconocido por la app  |
| **Pantalla(s)**  | Pantalla 20  |
| **Validaciones y Datos**  | **Request:** Interno    **Response:** {"pointsAwarded": 50, "newBadge": "UUID"}  |
| **Criterios de Aceptación**  | • El sistema sumará puntos automáticamente a la cuenta del usuario si este logra cumplir su meta diaria de tiempo.    • Si el usuario alcanza un puntaje clave, se mostrará una notificación de felicitación y se desbloqueará una nueva insignia visual.  |
| **Endpoint(s)**  | GET /api/v1/gamification/status  |
| **Evidencia**  | Evidencia\_Swagger\_US18  |

 

| US19: Recordatorios personalizados  | Épica: EP04  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | programar recordatorios a horas específicas  |
| **Para**  | desconectarme según mi rutina diaria  |
| **Pantalla(s)**  | Pantalla 5    |
| **Validaciones y Datos**  | **Request:** {"time": "22:00", "message": "string", "isRecurring": true}          **Response:** {"status": 201, "reminderId": "UUID"}  |
| **Criterios de Aceptación**  | • El usuario podrá escribir un mensaje propio y configurar una hora exacta para recibir un recordatorio en su dispositivo.    • Habrá una opción para repetir el mismo recordatorio de forma diaria o semanal sin tener que crearlo de nuevo.  |
| **Endpoint(s)**  | POST /api/v1/reminders  |
| **Evidencia**  | Evidencia\_Swagger\_US19  |

 

| US20: Notificaciones de urgencia (Psicólogo)  | Épica: EP02  |
| :---- | :---- |
| **Como**  | psicólogo  |
| **Quiero**  | recibir una alerta inmediata si un paciente supera un umbral crítico  |
| **Para**  | poder intervenir a tiempo  |
| **Pantalla(s)**  | Pantalla 6, pantalla 7   |
| **Validaciones y Datos**  | **Request:** Webhook Interno    **Response:** {"patientId": "UUID", "alertType": "URGENT", "timestamp": "ISO8601"}  |
| **Criterios de Aceptación**  | • El psicólogo recibirá un correo o notificación al instante si uno de sus pacientes sobrepasa el uso marcado como urgente.  • La notificación incluirá el nombre del paciente y un botón para contactarlo o revisar su ficha rápidamente.  |
| **Endpoint(s)**  | POST /api/v1/alerts/urgent/trigger  |
| **Evidencia**  | Evidencia\_Swagger\_US20  |

 

| US21: Chat de emergencia  | Épica: EP08  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | un botón de "Pedir ayuda" que conecte inmediatamente con un psicólogo  |
| **Para**  | ser atendido en casos de crisis o ansiedad  |
| **Pantalla(s)**  | Pantalla 21  |
| **Validaciones y Datos**  | **Request:** {"urgencyLevel": "HIGH", "message": {"URGENCY": string}}  **Response:** {"status": 200, "assignedPsychologistId": "UUID", "roomId": "UUID"}  |
| **Criterios de Aceptación**  | • Al oprimir el botón, el sistema conectará al usuario rápidamente con el primer profesional de salud que se encuentre en línea.  • Si no hay psicólogos disponibles en ese momento, la app mostrará un listado de contactos telefónicos alternativos de emergencia.  |
| **Endpoint(s)**  | POST /api/v1/chat/emergency/{patientId}  |
| **Evidencia**  | Evidencia\_Swagger\_US21  |

 

| US22: Integración con calendario externo  | Épica: EP08  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | sincronizar mi calendario de StayCool con Google Calendar  |
| **Para**  | recibir alertas también fuera de la app  |
| **Pantalla(s)**  | Pantalla 15  |
| **Validaciones y Datos**  | **Request:** {"authCode": "OAuth2\_Code"}    **Response:** {"status": 200, "message": "Sincronización exitosa"}  |
| **Criterios de Aceptación**  | • El usuario podrá vincular su cuenta seleccionando la opción "Sincronizar con Google" y otorgando los permisos en pantalla.    • Una vez vinculados, los eventos agendados en la app aparecerán automáticamente en el calendario personal del usuario.  |
| **Endpoint(s)**  | POST /api/v1/calendar/event  |
| **Evidencia**  | Evidencia\_Swagger\_US22  |

 

| US23: Ver logros y progreso personal  | Épica: EP08  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | consultar mis avances y logros acumulados  |
| **Para**  | sentirme motivado a seguir cumpliendo límites  |
| **Pantalla(s)**  | Pantalla 20  |
| **Validaciones y Datos**  | **Request:** Header: Authorization Bearer    **Response:** {"achievements": \[{"name": "Primer Logro", "unlocked": true}\], "pending": \[...\]}  |
| **Criterios de Aceptación**  | • El usuario podrá ingresar a una sección visual donde se enlistarán las medallas e insignias que ya ha desbloqueado.  • La misma sección le mostrará las insignias grises o pendientes para motivarlo a cumplir los siguientes objetivos.  |
| **Endpoint(s)**  | GET /api/v1/users/{userId}/achievements  |
| **Evidencia**  | Evidencia\_Swagger\_US23  |

 

| US24: Registrar emociones diarias  | Épica: EP08  |
| :---- | :---- |
| **Como**  | adolescente usuario  |
| **Quiero**  | registrar cómo me siento día a día  |
| **Para**  | identificar patrones de ánimo con mi uso de TICs  |
| **Pantalla(s)**  | Pantalla 22  |
| **Validaciones y Datos**  | **Request:** {"emotionType": "HAPPY|SAD|ANXIOUS", "notes": "string"}    **Response:** {"status": 201, "message": "Emoción registrada"}  |
| **Criterios de Aceptación**  | • El usuario podrá escoger un estado de ánimo mediante iconos y guardar el registro correspondiente al día actual.    • La plataforma guardará automáticamente la fecha y la hora exacta en la que se realizó la selección de la emoción.  |
| **Endpoint(s)**  | POST /api/v1/emotions/log  |
| **Evidencia**  | Evidencia\_Swagger\_US24  |

 

| US25: Agendar evento en el calendario  | Épica: EP08  |
| :---- | :---- |
| **Como**  | usuario  |
| **Quiero**  | agendar una cita o fecha pendiente en mi calendario  |
| **Para**  | organizarme y recibir recordatorios  |
| **Pantalla(s)**  | Pantalla 15  |
| **Validaciones y Datos**  | **Request:** {"title": "string", "datetime": "ISO8601", "description": "string"}    **Response:** {"status": 201, "eventId": "UUID"}  |
| **Criterios de Aceptación**  | • El usuario podrá presionar un botón de "Agregar" para seleccionar una fecha en el calendario e ingresar el motivo de la cita.    • El sistema no permitirá guardar un evento si la fecha seleccionada corresponde a un día que ya transcurrió en el pasado.  |
| **Endpoint(s)**  | POST /api/v1/calendar/events  |
| **Evidencia**  | Evidencia\_Postman\_US25  |

