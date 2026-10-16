package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Consumer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Debe tener exactamente la misma configuración del Producer
            channel.queueDeclare(
                    QUEUE_NAME,
                    true,   // durable
                    false,  // exclusive
                    false,  // autoDelete
                    null
            );

            System.out.println(" [*] Waiting for messages. To exit press Ctrl+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String message = new String(
                        delivery.getBody(),
                        StandardCharsets.UTF_8
                );

                System.out.println(" [x] Received '" + message + "'");
            };

            channel.basicConsume(
                    QUEUE_NAME,
                    true,   // autoAck
                    deliverCallback,
                    consumerTag -> {
                    }
            );

        } catch (Exception e) {
            System.err.println("Error consumiendo mensajes:");
            e.printStackTrace();
        }
    }
}