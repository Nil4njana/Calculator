// Coded by Sanjana S - 23011101130

import java.util.Stack;

public class ExpressionEvaluator {
    public double evaluate(String expression) throws Exception {
        expression = expression.replace("root", "√");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isWhitespace(ch)) continue;

            if (ch == '(') {
                ops.push(ch);
            } 
            else if (Character.isDigit(ch) || (ch == '-' && (i == 0 || isOperator(expression.charAt(i - 1)) || expression.charAt(i - 1) == '('))) {
                StringBuilder sb = new StringBuilder();
                if (ch == '-') {  
                    sb.append(ch);
                    i++;
                }
                while (i < expression.length() && 
                       (Character.isDigit(expression.charAt(i)) || 
                        expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--; 
                numbers.push(Double.parseDouble(sb.toString()));
            } 
            else if (ch == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
                }
                ops.pop(); 
            } 
            else if (ch == '√') { 
                i++; 
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--; 
                double value = sb.length() > 0 ? Double.parseDouble(sb.toString()) : 0;
                numbers.push(Math.sqrt(value)); 
            }
            else if (isOperator(ch)) {
                while (!ops.isEmpty() && hasPrecedence(ch, ops.peek())) {
                    numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
                }
                ops.push(ch);
            }
        }

        while (!ops.isEmpty()) {
            numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean isOperator(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/' || op == '^';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if (op1 == '^' && op2 != '^') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+' -> { return a + b; }
            case '-' -> { return a - b; }
            case '*' -> { return a * b; }
            case '/' -> {
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            }
            case '^' -> { return Math.pow(a, b); }
            default -> { return 0; }
        }
    }
}