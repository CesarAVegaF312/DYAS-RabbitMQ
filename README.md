### Taller sobre RabbitMQ

#### Objetivo
El objetivo de este taller es aprender a utilizar RabbitMQ para la mensajería asincrónica en aplicaciones. Implementaremos ejemplos prácticos para entender cómo enviar y recibir mensajes utilizando RabbitMQ.

#### Pre-requisitos
- Conocimientos básicos de Java o Python.
- Familiaridad con conceptos de mensajería y cola de mensajes.
- Tener RabbitMQ instalado y en ejecución en tu máquina.

### Instalación de RabbitMQ

1. **Instalar RabbitMQ:**
   - Descarga e instala RabbitMQ desde [aquí](https://www.rabbitmq.com/download.html).

2. **Verificar que RabbitMQ esté en Ejecución:**
   - Asegúrate de que RabbitMQ esté en ejecución. Puedes verificarlo abriendo el panel de control de RabbitMQ en tu navegador web:
     - Por defecto, el panel de control de RabbitMQ está disponible en [http://localhost:15672](http://localhost:15672).
   - Inicia RabbitMQ desde la línea de comandos:
     ```sh
     rabbitmq-server
     ```

### Paso 1: Configuración del Proyecto

#### Configuración del `pom.xml` para Java

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>rabbitmq-example</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!-- RabbitMQ Client -->
        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <version>5.13.1</version>
        </dependency>

        <!-- Dependencias estándar -->
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Configuración del entorno para Python

1. **Instalar la Biblioteca `pika`:**
   ```sh
   pip install pika
   ```

### Paso 2: Ejemplos de Productor y Consumidor en Java

#### Productor en Java

```java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello RabbitMQ!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
```

#### Consumidor en Java

```java
import com.rabbitmq.client.*;

public class Consumer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        }
    }
}
```

### Paso 3: Ejemplos de Productor y Consumidor en Python

#### Productor en Python

```python
import pika

def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='hello')

    channel.basic_publish(exchange='',
                          routing_key='hello',
                          body='Hello RabbitMQ!')
    print(" [x] Sent 'Hello RabbitMQ!'")
    connection.close()

if __name__ == '__main__':
    main()
```

#### Consumidor en Python

```python
import pika

def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='hello')

    def callback(ch, method, properties, body):
        print(f" [x] Received {body}")

    channel.basic_consume(queue='hello', on_message_callback=callback, auto_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

if __name__ == '__main__':
    main()
```

### Paso 4: Crear un Ejemplo Propio

**Instrucciones para los Estudiantes:**

1. **Definir un Caso de Uso:**
   - Piensa en un escenario donde la mensajería asincrónica pueda ser útil. Por ejemplo, procesamiento de órdenes, notificaciones en tiempo real, etc.
2. **Implementar un Productor y un Consumidor:**
   - Implementa un productor y un consumidor en Java o Python según tu caso de uso.
   - Asegúrate de comentar tu código para explicar qué hace cada parte.
3. **Probar y Depurar:**
   - Ejecuta tu código y verifica que funcione como esperas. Asegúrate de manejar cualquier posible error.
4. **Compartir y Discutir:**
   - Comparte tu código con los demás estudiantes del taller. Discutan sobre las diferentes implementaciones y enfoques utilizados.

**Ejemplo de Código para los Estudiantes:**

Supongamos que queremos implementar un sistema de notificaciones donde el productor envía mensajes de notificación y el consumidor los procesa.

#### Productor de Notificaciones en Python

```python
import pika

def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='notifications')

    for i in range(10):
        message = f"Notification {i}"
        channel.basic_publish(exchange='',
                              routing_key='notifications',
                              body=message)
        print(f" [x] Sent '{message}'")
    connection.close()

if __name__ == '__main__':
    main()
```

#### Consumidor de Notificaciones en Python

```python
import pika

def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='notifications')

    def callback(ch, method, properties, body):
        print(f" [x] Received {body}")

    channel.basic_consume(queue='notifications', on_message_callback=callback, auto_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

if __name__ == '__main__':
    main()
```

### Ejemplos avanzados incluidos en el repositorio

Además de los ejemplos anteriores (carpetas `demo/` y `demoPy/`), este repositorio incluye dos ejemplos adicionales, más cercanos a escenarios reales:

#### `demo-Exchange-Consumidor2/` — Exchange tipo `fanout`

Un productor publica un mismo mensaje a un *exchange* `fanout` (`ARQ_SOFT_EXCHANGE`), que lo reparte a dos colas (`ARQ_SOFT_1`, `ARQ_SOFT_2`), cada una con su propio consumidor — así ambos consumidores reciben una copia del mismo mensaje, a diferencia del ejemplo básico donde solo un consumidor se queda con cada mensaje.

```bash
cd demo-Exchange-Consumidor2
mvn compile
# En dos terminales distintas:
mvn exec:java -Dexec.mainClass=edu.unisabana.Consumer
mvn exec:java -Dexec.mainClass=edu.unisabana.Consumer2
# En una tercera terminal:
mvn exec:java -Dexec.mainClass=edu.unisabana.Producer
```

Deberías ver el mismo mensaje recibido en ambas terminales de consumidores.

#### `demo-API-Worker/` — Procesamiento asíncrono con API + Worker (Python, Docker Compose)

Un ejemplo "antes/después": un servicio FastAPI expone `/sync` (procesa la petición y espera) y `/async` (encola el trabajo en RabbitMQ y responde de inmediato), mientras uno o más *workers* independientes consumen la cola `jobs` y hacen el procesamiento pesado en segundo plano. Levanta todo con Docker Compose (incluye RabbitMQ, la API y 3 réplicas del worker):

```bash
cd demo-API-Worker
docker compose up --build
```

Prueba la diferencia entre ambos enfoques:

```bash
curl "http://localhost:8000/sync?ms=200"   # tarda ~200ms en responder
curl "http://localhost:8000/async?ms=200"  # responde casi de inmediato ("queued")
```

El panel de RabbitMQ (`http://localhost:15672`, usuario/clave `guest`/`guest`) te permite ver la cola `jobs` y confirmar que los workers están consumiendo los mensajes.

### Paso 5: Resumen y Conclusión

#### Resumen del Taller
- Aprendimos a utilizar RabbitMQ para la mensajería asincrónica.
- Implementamos ejemplos de productores y consumidores en Java y Python.
- Los estudiantes crearon sus propios ejemplos para aplicar lo aprendido.

#### Conclusión
RabbitMQ es una herramienta poderosa para la mensajería asincrónica y la integración de sistemas. Los conceptos y técnicas que hemos explorado en este taller son fundamentales para construir aplicaciones distribuidas y escalables. Continuar practicando con estos conceptos y explorando otros aspectos de RabbitMQ te permitirá construir aplicaciones más robustas y eficientes.

### Ejecución del Taller
1. Configura tu entorno de desarrollo con las dependencias necesarias.
2. Implementa y ejecuta cada ejemplo de productor y consumidor en Java o Python.
3. Crea y comparte tu propio ejemplo utilizando los conceptos aprendidos.
4. Experimenta con otros casos de uso para profundizar en tu comprensión de RabbitMQ.

Este taller proporciona una base sólida para comenzar a trabajar con RabbitMQ y fomenta la creación de ejemplos propios para reforzar el aprendizaje.