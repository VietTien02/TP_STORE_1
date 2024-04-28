package com.pts.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
       // printAsciiArt();
    }
    public void printAsciiArt() {
        System.out.println("  ______     ______     __    __     ______   __     __   __     ______    ");
        System.out.println(" /      \\   /      \\   |  \\  |  \\   /      \\ |  \\   |  \\ |  \\   /      \\   ");
        System.out.println("|  $$$$$$\\ |  $$$$$$\\  | $$\\ | $$  |  $$$$$$\\| $$   | $$ | $$  |  $$$$$$\\  ");
        System.out.println("| $$___\\$$ | $$  | $$  | $$$\\| $$  | $$___\\$$| $$   | $$ | $$  | $$  | $$  ");
        System.out.println(" \\$$    \\  | $$  | $$  | $$$$\\ $$   \\$$    \\ | $$   | $$ | $$  | $$  | $$  ");
        System.out.println(" _\\$$$$$$\\ | $$  | $$  | $$\\$$ $$  _\\$$$$$$\\| $$   | $$ | $$  | $$  | $$  ");
        System.out.println("|  \\__| $$ | $$__/ $$  | $$ \\$$$$ |  \\__| $$| $$__/ $$ | $$  | $$__/ $$  ");
        System.out.println(" \\$$    $$  \\$$    $$  | $$  \\$$$  \\$$    $$ \\$$    $$  \\$$   \\$$    $$  ");
        System.out.println("  \\$$$$$$    \\$$$$$$    \\$$   \\$$   \\$$$$$$   \\$$$$$$    $$    \\$$$$$$   ");
    }
}
