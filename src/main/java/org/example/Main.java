package org.example;

import graphicalUserInterface.Calculator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
        System.out.println("Starting Calculator");
    }
}
