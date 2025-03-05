package graphicalUserInterface;

import businessLogic.Operations;
import dataModels.Polynomial;

import javax.swing.*;
import java.awt.*;

import static businessLogic.Operations.isDivisionResultInteger;

public class Calculator extends JFrame {
    private JTextField poly1Field, poly2Field, resultField;

    public Calculator() {
        initUI();
    }

    private void initUI() {
        setTitle("Polynomial Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Polynomial Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        poly1Field = new JTextField(20);
        poly2Field = new JTextField(20);
        resultField = new JTextField(20);
        resultField.setEditable(false);

        JButton addButton = new JButton("Add");
        JButton subtractButton = new JButton("Subtract");
        JButton multiplyButton = new JButton("Multiply");
        JButton divideButton = new JButton("Divide");
        JButton derivativeIntegralButton = new JButton("D&I");

        addButton.addActionListener(e -> performOperation(Operation.ADD));
        subtractButton.addActionListener(e -> performOperation(Operation.SUBTRACT));
        multiplyButton.addActionListener(e -> performOperation(Operation.MULTIPLY));
        divideButton.addActionListener(e -> performOperation(Operation.DIVIDE));
        derivativeIntegralButton.addActionListener(e -> openDerivativeIntegralWindow());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        inputPanel.add(new JLabel("Polynomial 1:"), gbc);
        gbc.gridy++;
        inputPanel.add(poly1Field, gbc);

        gbc.gridy++;
        inputPanel.add(new JLabel("Polynomial 2:"), gbc);
        gbc.gridy++;
        inputPanel.add(poly2Field, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(addButton, gbc);
        gbc.gridy++;
        buttonPanel.add(subtractButton, gbc);
        gbc.gridy++;
        buttonPanel.add(multiplyButton, gbc);
        gbc.gridy++;
        buttonPanel.add(divideButton, gbc);
        gbc.gridy++;
        buttonPanel.add(derivativeIntegralButton, gbc);

        JPanel resultPanel = new JPanel(new FlowLayout());
        resultPanel.add(new JLabel("Result:"));
        resultPanel.add(resultField);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        // Set a bright background color
        Color brightOrange = new Color(255, 205, 130);
        mainPanel.setBackground(brightOrange);
        inputPanel.setBackground(brightOrange);
        buttonPanel.setBackground(brightOrange);
        resultPanel.setBackground(brightOrange);

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openDerivativeIntegralWindow() {
        DerivativeIntegralDialog derivativeAndIntegralDialog = new DerivativeIntegralDialog(this);
        derivativeAndIntegralDialog.setVisible(true);
    }

    private enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    private void performOperation(Operation operation) {
        String poly1 = poly1Field.getText().trim();
        String poly2 = poly2Field.getText().trim();

        if (poly1.isEmpty() || poly2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter valid polynomials.");
            return;
        }

        try {
            Polynomial polynomial1 = new Polynomial(poly1);
            Polynomial polynomial2 = new Polynomial(poly2);

            switch (operation) {
                case DIVIDE:
                    if (polynomial2.isZero()) {
                        throw new ArithmeticException("Cannot divide by a zero polynomial.");
                    }

                    if (!isDivisionResultInteger(polynomial1, polynomial2)) {
                        resultField.setText("Result is not an integer polynomial.");
                    } else {
                        Polynomial[] divisionResult = Operations.divide(polynomial1, polynomial2);
                        resultField.setText(divisionResult[0].toString() + ", Remainder: " + divisionResult[1].toString());
                    }
                    break;
                default:
                    Polynomial result = calculatePolynomialOperation(polynomial1, polynomial2, operation);
                    resultField.setText(result.toString());
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error performing operation: " + e.getMessage());
        }
    }

    private Polynomial calculatePolynomialOperation(Polynomial poly1, Polynomial poly2, Operation operation) {
        switch (operation) {
            case ADD:
                return Operations.add(poly1, poly2);
            case SUBTRACT:
                return Operations.subtract(poly1, poly2);
            case MULTIPLY:
                return Operations.multiply(poly1, poly2);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }
}
