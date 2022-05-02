# Prueba Mercado Libre

## Estructura del ejercicio:

### El Problema

	Se necesita coordinar acciones de respuesta ante fraudes, para ello seria de utilidad contar con información del lugar de origen de una compra, búsqueda o pago.
	
	La solución debe tener en cuenta que puede recibir fluctuaciones agresivas de tráfico (Entre
	100 y 1 millón de peticiones por segundo).
	
	La aplicación puede ser en línea de comandos o web. En el primer caso se espera
	que el IP sea un parámetro, y en el segundo que exista un form donde escribir la
	dirección.
	
	La aplicación deberá hacer un uso racional de las Apis, evitando hacer llamadas
	innecesarias.
	
	Además de funcionamiento, prestar atención al estilo y calidad del código fuente.
	La aplicación deberá poder correr ser construida y ejecutada dentro de un
	contenedor Docker (incluir un Dockerfile e instrucciones para ejecutarlo).


### Solución Planteada


	Como parte de la solución se plantea la creación de una herramienta que permita obtener información de un país dada su dirección ip. 
	La herramienta debe retornar los siguientes datos:
	
	● El nombre y código ISO del país
	● Los idiomas oficiales del país
	● Hora(s) actual(es) en el país (si el país cubre más de una zona horaria, mostrar
	  todas)
	● Distancia estimada entre Buenos Aires y el país, en km.
	● Moneda local, y su cotización actual en dólares (si está disponible)
	  También debe poder guardar y retornar las siguientes estadísticas:
	● Distancia más lejana a Buenos Aires desde la cual se haya consultado el servicio
	● Distancia más cercana a Buenos Aires desde la cual se haya consultado el servicio
	● Distancia promedio de todas las ejecuciones que se hayan hecho del servicio.
	
	Para evitar problemas de fluctuaciones agresivas de tráfico por altos volúmenes de peticiones, se plantea el uso de Redis, un rápido almacén de datos clave-valor en memoria de código abierto que permite un acceso a datos de baja latencia y alto rendimiento
	Para obtener la información necesaria se utilizaron las siguientes Apis publicas:

		- Geolocalización por Ip:  http://api.ipapi.com/ 
		- Información de país por Ip:  http://usercountry.agum.com/v1.0/json/ 
		- Información sobre monedas:  http://data.fixer.io
		
	Se creará una pagina web usando Angular versión 13 para crear un formulario que permita digitar la dirección Ip, esta se conecte a un servicio web que utilice los servicios de consulta previamente mencionados y finalmente muestre los datos solicitados en el navegador. 
	Para hacer uso racional de las Apis y evitar llamadas innecesarias, se usarán dos estrategias.
	
	1)	Usar Redis con almacenamiento en cache para el servicio de monedas que no necesita ser consultado constantemente.
	2)	Usar Redis con webSocket para almacenamiento de mensajería y así poder informar de las modificaciones de estadística de distancia solicitadas
	
	Para prestar atención al estilo y calidad del código fuente se incorporará al IDE de desarrollo el plugin de sonarLint que permitirá hacer revisiones previas al código antes de ejecutar el reporte en el Sobarque.
	Se incorporarán a la solución pruebas Unitarias y de mutación(pitest) en conjunto con jacoco como plugin de cobertura de código para ejecutar en conjunto los reportes en Sonarqube.
	Se crearán instrucciones para que la aplicación sea construida y ejecutada sobre Docker.


### Patrones de diseño:


	Los principales patrones comunes de diseño que se implementarán en el desarrollo del software serán los siguientes:
	Singleton:  es un patrón de diseño creacional común en aplicaciones de Spring ya que por medio de anotaciones (@autowired, @bean) y su configuración es permitido invocar el método estático que retorna un único constructor.
	Factory Method pattern: es un patrón de diseño creacional común en java que se basa en el uso de interfaces abstractas, se busca separar el código que define con el código que implementa. Ejemplo creación de interfaces de servicio que posteriormente serán implementados según se requiera, en el proyecto se usa para los llamados a los servicios externos. 
	Proxy pattern: es un patrón de diseño estructural común en java que indica que debe existir barreras o controles para acceder a ciertas porciones de código, la aplicación a desarrollar se construirá por diferentes capas, tales como presentación, negocio, acceso a datos.
	Template Method: es un patrón de diseño de comportamiento común basado en la herencia, permite brindar medios sencillos para extender/sobre escribir funcionalidades estándar. Ejemplo de uso practico, cuando se extienden funcionalidades de frameworks en las clases de configuración de un proyecto.


### Tecnologias:


	A continuación, se mencionarán las tecnologías/algoritmos trabajados
	-	Redis como Bd estática y de cache.
	-	Swagger como solución a la documentación del servicio
	-	spring-boot-starter-websocket como solución para abrir sesiones de comunicación
	-	Harvesine fórmula como solución para el cálculo de distancias
	-	Spring-web-client como solución para consumir servicios externos de forma individual o simultanea
	-	spring-boot-starter-cache Como solución para cachear data del lado del servidor 


### Despliegue:

 
	Paso a paso para realizar el despliegue
	
		1.	clonar el repositorio actual

		2.	compilar e instalar el proyecto java en la ubicación /RestRastreoIp

			- mvn clean install -DskipTests

		3.	ejecutar el archivo docker-compose con el comando
		
			- docker compose up
			
		4. si la aplicacion java no sube, ejemplo en windows docker esta presentando un problema con la dependencia (redis.clients.jedis), ubicación en /RestRastreoIp ejecutar el comando
		
			- mvn spring-boot:run
			
		5.	actualizar sonar default user/pass cuando se ejecute el comando anterior: admin/admin el nuevo password admin23

		6.	Url reporte estado del servicio actuator, acceder a:
		
			- http://localhost:8080/actuator

		7.	Url swagger-ui para la documentacion del servicio, acceder a:
		
			- http://localhost:8080/swagger-ui.html
			
		8. ubicado en /RestRastreoIp  generar reporte de pruebas, mutacion en sonar
		
			- mvn clean verify org.pitest:pitest-maven:mutationCoverage sonar:sonar
			
		9. url sonar para ver reportes y reportes de mutacion, acceder a:
		
			- http://localhost:9000/
			
			- /RestRastreoIp/target/pit-reports
			
		10. Aplicación front con el formulario, acceder a :
		
			- http://localhost:4201/traceo

#### Tener encuenta: 

	Posiblemente el docker arroje error de memoria en el despliegue de sonarqube, para esto seria necesario configurar la variable: vm.max_map_count
		* ejemplo: 
			wsl -d docker-desktop
			sysctl -w vm.max_map_count=262144
		
		https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html#docker-cli-run-prod-mode


