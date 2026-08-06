package edu.unisabana;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;


public class Consumer {

    public static void main(String[] args) {
        // crear una facbrica de conexiones
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        // Crear una conexion y un canal para rabbitMQ
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // declarar una cola
            channel.queueDeclare("ARQ_SOFT_1", false, false, false, null);

            // Crear un callback para recibir mensajes
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };

            channel.basicConsume("ARQ_SOFT_1", true, deliverCallback, consumerTag -> {});

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
