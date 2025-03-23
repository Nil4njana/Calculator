// Coded by R. Sai Sreya - 23011101105

import javax.swing.*;
public class Main {
    private static class GuiRunner implements Runnable {
        public void run() {
            new CalculatorGUI();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GuiRunner());
    }
}