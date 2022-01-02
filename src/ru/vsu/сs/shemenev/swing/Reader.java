package ru.vsu.сs.shemenev.swing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
    public static List<Move> getStrategy(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        List<Move> strategies = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] positions = line.split("(--)|(:)");
            boolean isFirstPlayer = positions[2].equals("P1");
            strategies.add(new Move(positions[0].toUpperCase(), positions[1].toUpperCase(), isFirstPlayer));
        }
        scanner.close();
        return strategies;
    }

//    public static List<String> getStrategy2() throws IOException {
//        Scanner scanner = new Scanner(new File(), StandardCharsets.UTF_8);
//        List<String> strategies = new ArrayList<>();
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            String[] positions = line.split("(--)|(:)");
//            strategies.add(positions[0].toUpperCase()+" " +positions[1].toUpperCase());
//        }
//        scanner.close();
//        return strategies;
//    }
}
