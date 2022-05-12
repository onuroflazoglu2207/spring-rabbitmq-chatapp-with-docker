package com.example.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;

public class RabbitMQWindowAbs {
    private final Runnable run;
    private final JFrame frame;
    private final JPanel panel;
    private final JTextArea area;
    private final JScrollPane scroll;
    private final JTextField field;
    private final JButton button;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    public RabbitMQWindowAbs(Runnable run, WindowAdapter adapter, String title, String defFieldText, String buttonText) {
        this.run = run;
        frame = new JFrame();
        panel = new JPanel();
        area = new JTextArea();
        scroll = new JScrollPane();
        field = new JTextField();
        button = new JButton();
        frame.setTitle(title);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.addWindowListener(adapter);
        panel.setLayout(new GridBagLayout());
        Insets insets = new Insets(0, 0, 0, 0);
        area.setEnabled(false);
        area.setFont(new Font("Arial", Font.PLAIN, 15));
        area.setBackground(Color.gray);
        area.setForeground(Color.white);
        scroll.setViewportView(area);
        field.setText(defFieldText);
        field.setFont(new Font("Arial", Font.PLAIN, 17));
        field.setBackground(Color.lightGray);
        field.setForeground(Color.black);
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) run.run();
            }
        });
        button.setText(buttonText);
        button.addActionListener((e) -> run.run());
        panel.add(scroll, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.95, 10, 1, insets, 0, 0));
        panel.add(field, new GridBagConstraints(0, 1, 1, 1, 0.9, 0.05, 10, 1, insets, 0, 0));
        panel.add(button, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.05, 10, 1, insets, 0, 0));
        frame.add(panel);
        frame.setVisible(true);
    }

    public JTextArea getArea() {
        return area;
    }

    public JTextField getField() {
        return field;
    }

    public JFrame getFrame() {
        return frame;
    }
}
