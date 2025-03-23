// Coded by Nilanjana T - 23011101088

interface Operations {
    double performOperation(double... operands) throws IllegalArgumentException;
}

class Addition implements Operations {
    public double performOperation(double... operands) {
        return operands[0] + operands[1];
    }
}

class Subtraction implements Operations {
    public double performOperation(double... operands) {
        return operands[0] - operands[1];
    }
}

class Multiplication implements Operations {
    public double performOperation(double... operands) {
        return operands[0] * operands[1];
    }
}

class Division implements Operations {
    public double performOperation(double... operands) {
        if (operands[1] == 0) throw new IllegalArgumentException("Division by zero");
        return operands[0] / operands[1];
    }
}

class Factorial implements Operations {
    public double performOperation(double... operands) {
        int n = (int) operands[0];
        if (n < 0) throw new IllegalArgumentException("Factorial not defined for negative numbers");
        double result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}

class Power implements Operations {
    public double performOperation(double... operands) {
        return Math.pow(operands[0], operands[1]);
    }
}

class Square implements Operations {
    public double performOperation(double... operands) {
        return Math.pow(operands[0], 2);
    }
}

class SquareRoot implements Operations {
    public double performOperation(double... operands) {
        return Math.sqrt(operands[0]);
    }
}