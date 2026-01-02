package org.example;

import org.example.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // Main класът има само една отговорност: да стартира приложението.
        ConsoleUI app = new ConsoleUI();
        app.start();
    }
}