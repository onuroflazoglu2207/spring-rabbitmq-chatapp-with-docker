package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class RabbitMQWindowClient {
    private final AmqpTemplate rabbitTemplate;
    private final RabbitMQConfig config;
    private final RabbitMQWindowServer server;
    private final String name;
    private final RabbitMQWindowAbs window;

    public RabbitMQWindowClient(AmqpTemplate rabbitTemplate, RabbitMQConfig config, RabbitMQWindowServer server, String name) {
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
        this.server = server;
        this.name = name;
        Runnable run = new Runnable() {
            public void run() {
                String mess = window.getField().getText();
                if (!mess.isEmpty()) {
                    RabbitMQModel model = RabbitMQModel.builder().from(name).message(mess).build();
                    rabbitTemplate.convertAndSend(config.getExchange(), config.getRoutingkey(), model);
                    window.getField().setText("");
                }
            }
        };
        WindowAdapter adapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                window.getFrame().dispose();
                super.windowClosing(e);
            }
        };
        window = new RabbitMQWindowAbs(run, adapter, "Chat Client", "", "Send");
    }

    public void listen(RabbitMQModel model) {
        String mess = "[" + model.getFrom() + "] : " + model.getMessage();
        window.getArea().setText(window.getArea().getText() + "\n" + mess);
    }

    private void dispose() {
        server.clientDispose(this);
    }

    public String getName() {
        return name;
    }
}
