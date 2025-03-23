// Coded by Sanjana S - 23011101130

import java.io.*;
import java.util.*;

public class History {
    private static final String FILE_NAME = "calculation_history.txt";

    public void addCalculation(String calculation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(calculation);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to history file: " + e.getMessage());
        }
    }

    public List<String> getHistory() {
        List<String> history = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading history file: " + e.getMessage());
        }
        return history;
    }
}