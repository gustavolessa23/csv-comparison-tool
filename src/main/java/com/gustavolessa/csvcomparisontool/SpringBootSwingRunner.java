package com.gustavolessa.csvcomparisontool;

import com.gustavolessa.csvcomparisontool.ui.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * This CommandLineRunner fires off at runtime and boots up our GUI.
 */
@Component
public class SpringBootSwingRunner implements CommandLineRunner {

    private final MainController controller;

    @Autowired
    public SpringBootSwingRunner(MainController controller) {
        //System.out.println("RUNNER FIRED!");
        this.controller = controller;

    }


    @Override
    public void run(String... args) {
        //This boots up the GUI.
        EventQueue.invokeLater(() -> {
            controller.setLocationRelativeTo(null);
            controller.setVisible(true);
        });
    }
}

