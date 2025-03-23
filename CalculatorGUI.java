// Coded by R. Sai Sreya - 23011101105

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class CalculatorGUI extends JFrame {
    private JTextArea displayArea;
    private History history;
    private final Map<String, Operations> operationsMap;

    public CalculatorGUI() {
        history = new History();
        operationsMap = initializeOperations();
        initializeGUI();
    }

    private Map<String, Operations> initializeOperations() {
        Map<String, Operations> map = new HashMap<>();
        map.put("+", new Addition());
        map.put("-", new Subtraction());
        map.put("*", new Multiplication());
        map.put("/", new Division());
        map.put("!", new Factorial());
        map.put("^", new Power());
        map.put("√", new SquareRoot());
        return map;
    }

    private void initializeGUI() {
        setTitle("Scientific Calculator");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        displayPanel.setBackground(Color.DARK_GRAY);

        displayArea = new JTextArea(4, 20);
        displayArea.setEditable(false);
        displayArea.setBackground(Color.DARK_GRAY);
        displayArea.setForeground(Color.WHITE);
        displayArea.setFont(new Font("Cambria Math", Font.PLAIN, 24));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        displayPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(displayPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 4, 10, 10));  // Added an extra row
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        addButtons(buttonPanel);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void addButtons(JPanel panel) {
        String[] buttonLabels = {
            "History", "!", "C", "←",
            "√", "x²", "π", "e",
            "(", ")", "^", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setBackground(new Color(173, 216, 230));
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Cambria Math", Font.PLAIN, label.equals("History") ? 16 : 20));
            
            if (label.equals("=")) {
                button.setBackground(Color.CYAN);
            }

            button.addActionListener(new ButtonClickListener(label));
            panel.add(button);
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final String label;

        public ButtonClickListener(String label) {
            this.label = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String currentText = displayArea.getText();
            
            switch (label) {
                case "=" -> evaluateExpression();
                case "C" -> displayArea.setText("");
                case "←" -> {
                    if (!currentText.isEmpty()) {
                        displayArea.setText(currentText.substring(0, currentText.length() - 1));
                    }
                }
                case "History" -> showHistory();
                case "+/-" -> {
                    try {
                        double number = Double.parseDouble(currentText);
                        displayArea.setText(String.valueOf(-number));
                    } catch (NumberFormatException ex) {
                        // Ignore if not a valid number
                    }
                }
                case "π" -> displayArea.append(String.valueOf(Math.PI));
                case "e" -> displayArea.append(String.valueOf(Math.E));
                case "!" -> displayArea.append("!");
                case "x²" -> displayArea.append("^2");  // Changed to use caret notation
                case "√" -> displayArea.append("√");
                case "^" -> displayArea.append("^");
                default -> {
                    if (isOperator(label)) {
                        displayArea.append(" " + label + " ");
                    } else {
                        displayArea.append(label);
                    }
                }
            }
        }

        private boolean isOperator(String label) {
            return label.matches("[+\\-*/%!()^]|log");
        }

        private void evaluateExpression() {
            String input = displayArea.getText().trim();
            try {
                // Handle factorial operations first
                while (input.contains("!")) {
                    int factorialIndex = input.lastIndexOf("!");
                    // Find the number before the factorial symbol
                    int startIndex = factorialIndex - 1;
                    while (startIndex >= 0 && Character.isDigit(input.charAt(startIndex))) {
                        startIndex--;
                    }
                    startIndex++;
                    
                    String numberStr = input.substring(startIndex, factorialIndex);
                    double number = Double.parseDouble(numberStr);

                    // Calculate factorial
                    double factorialResult = new Factorial().performOperation(number);
                    input = input.substring(0, startIndex) + factorialResult + 
                            (factorialIndex + 1 < input.length() ? input.substring(factorialIndex + 1) : "");
                }

                // Handle square root operations first
                while (input.contains("√")) {
                    int rootIndex = input.lastIndexOf("√");
                    // Find the end of the number after the root symbol
                    int endIndex = rootIndex + 1;
                    while (endIndex < input.length() && 
                           (Character.isDigit(input.charAt(endIndex)) || 
                            input.charAt(endIndex) == '.' || 
                            input.charAt(endIndex) == ')')) {
                        endIndex++;
                    }
                    
                    String numberStr = input.substring(rootIndex + 1, endIndex);
                    double number;
                    if (numberStr.startsWith("(") && numberStr.endsWith(")")) {
                        // Evaluate expression inside parentheses
                        number = new ExpressionEvaluator().evaluate(numberStr.substring(1, numberStr.length() - 1));
                    } else {
                        number = Double.parseDouble(numberStr);
                    }
                    
                    double sqrtResult = Math.sqrt(number);
                    input = input.substring(0, rootIndex) + sqrtResult + 
                           (endIndex < input.length() ? input.substring(endIndex) : "");
                }

                // Handle power operations (both ^ and ²)
                input = input.replace("²", "^2");  // Convert ² to ^2 for uniform handling
                
                double result = new ExpressionEvaluator().evaluate(input);
                history.addCalculation(displayArea.getText() + " = " + result);
                displayArea.setText(String.valueOf(result));
            } catch (Exception ex) {
                displayArea.setText("Error: " + ex.getMessage());
            }
        }

        private void showHistory() {
            List<String> historyList = history.getHistory();
            JDialog historyDialog = new JDialog(CalculatorGUI.this, "Calculation History", true);
            historyDialog.setSize(300, 400);
            historyDialog.setLayout(new BorderLayout());

            JTextArea historyArea = new JTextArea();
            historyArea.setEditable(false);
            historyArea.setFont(new Font("Cambria Math", Font.PLAIN, 14));
            for (String calculation : historyList) {
                historyArea.append(calculation + "\n");
            }
            historyArea.setLineWrap(true);
            historyArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(historyArea);
            historyDialog.add(scrollPane, BorderLayout.CENTER);

            historyDialog.setVisible(true);
        }
    }
}