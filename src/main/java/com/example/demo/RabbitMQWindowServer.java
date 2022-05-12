package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.awt.event.*;
import java.util.ArrayList;

public class RabbitMQWindowServer {
    private final AmqpTemplate rabbitTemplate;
    private final RabbitMQConfig config;
    private final RabbitMQWindowAbs window;
    private final ArrayList<RabbitMQWindowClient> clients;

    public static final int NAME_MIN = 2;
    public static final int NAME_MAX = 8;

    public RabbitMQWindowServer(AmqpTemplate rabbitTemplate, RabbitMQConfig config) {
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
        Runnable run = new Runnable() {
            public void run() {
                String name = window.getField().getText();
                int len = name.length();
                if (len < NAME_MIN || len > NAME_MAX)
                    setTextInArea("[WARNING] : Name length must be " + NAME_MIN + " - " + NAME_MAX);
                else if (isHaveName(name)) setTextInArea("[WARNING] : This name is have!");
                else createClient(name);
            }
        };
        WindowAdapter adapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        window = new RabbitMQWindowAbs(run, adapter, "Chat Server", "Name", "Create Client");
        clients = new ArrayList<>();
    }

    private void createClient(String name) {
        RabbitMQWindowClient client = new RabbitMQWindowClient(rabbitTemplate, config, this, name);
        clients.add(client);
        setTextInArea("[CREATE] : " + client.getName() + " is created!");
        window.getField().setText("Name");
    }

    public void clientDispose(RabbitMQWindowClient client) {
        clients.remove(client);
        setTextInArea("[DISPOSE] : " + client.getName() + " is disposed!");
    }

    private boolean isHaveName(String name) {
        for (RabbitMQWindowClient client : clients)
            if (name.equals(client.getName())) return true;
        return false;
    }

    private void setTextInArea(String mess) {
        window.getArea().setText(window.getArea().getText() + "\n" + mess);
    }

    @RabbitListener(queues = "rabbit.queue")
    private void listen(RabbitMQModel model) {
        for (RabbitMQWindowClient client : clients)
            client.listen(model);
    }
}
