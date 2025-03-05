package graphicalUserInterface;

import businessLogic.Operations;
import dataModels.Polynomial;

import javax.swing.*;
import java.awt.*;

public class DerivativeIntegralDialog extends JDialog {
    private JTextField polynomialField, resultField;
    private JLabel resultLabel;
    private JButton derivativeButton, integralButton;

    public DerivativeIntegralDialog(JFrame parentFrame) {
        super(parentFrame, "Derivative & Integral", true);
        setLayout(new GridBagLayout());
        initComponents();
        setSize(450, 250);
        setLocationRelativeTo(parentFrame);
    }

    private void initComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        polynomialField = new JTextField(20);
        polynomialField.setToolTipText("Enter your polynomial here. For example: 3x^2 + 2x + 1");
        polynomialField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update(javax.swing.event.DocumentEvent e) {
                resultField.setText("");
            }
        });

        resultField = new JTextField(20);
        resultField.setEditable(false);

        derivativeButton = new JButton("Derivative");
        integralButton = new JButton("Integral");

        derivativeButton.addActionListener(e -> performDerivativeOrIntegral(true));
        integralButton.addActionListener(e -> performDerivativeOrIntegral(false));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(new JLabel("Polynomial:"), constraints);

        constraints.gridy++;
        add(polynomialField, constraints);

        constraints.gridy++;
        constraints.gridwidth = 1;
        add(derivativeButton, constraints);

        constraints.gridx = 1;
        add(integralButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        resultLabel = new JLabel("Result:");
        add(resultLabel, constraints);

        constraints.gridy++;
        add(resultField, constraints);

        // Set the background color
        getContentPane().setBackground(new Color(255, 205, 130));
        polynomialField.setBackground(Color.WHITE);
        resultField.setBackground(Color.WHITE);
    }

    private void performDerivativeOrIntegral(boolean isDerivative) {
        String polynomial = polynomialField.getText().trim();
        if (polynomial.isEmpty()) {
            resultField.setText("Please enter a valid polynomial.");
            return;
        }

        try {
            Polynomial poly = new Polynomial(polynomial);
            String resultStr;
            if (isDerivative) {
                Polynomial result = Operations.derivative(poly);
                resultStr = result.toString();
            } else {
                Polynomial integralResult = Operations.integral(poly);
                resultStr = Operations.integralToString(integralResult);
            }
            resultField.setText(resultStr);
        } catch (Exception e) {
            resultField.setText("Error: " + e.getMessage());
        }
    }

    @FunctionalInterface
    interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
        void update(javax.swing.event.DocumentEvent e);

        default void insertUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        default void removeUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        default void changedUpdate(javax.swing.event.DocumentEvent e) { update(e); }
    }
}
