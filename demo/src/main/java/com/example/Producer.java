package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;

public class Producer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {

            // Cola durable
            channel.queueDeclare(
                    QUEUE_NAME,
                    true,   // durable
                    false,  // exclusive
                    false,  // autoDelete
                    null
            );

            String message = "Hello World desde Java 21!";

            channel.basicPublish(
                    "",
                    QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8)
            );

            System.out.println(" [x] Sent '" + message + "'");

        } catch (Exception e) {
            System.err.println("Error enviando mensaje:");
            e.printStackTrace();
        }
    }
}