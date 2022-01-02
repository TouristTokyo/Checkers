package ru.vsu.сs.shemenev.console;
import ru.vsu.сs.shemenev.Figure;
import ru.vsu.сs.shemenev.SimpleFigure;

public class ConsolePainter{
    public static void drawBoardWithFigures(Figure[][] figures) {
        System.out.println("--------------------------------");
        char ch = 'A';
        for (int i = -1; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == -1) {
                    System.out.print(" " + (ch++) + " ");
                    continue;
                }
                if (figures[i][j] == null) {
                    System.out.print(" - ");
                } else {
                    if (figures[i][j] instanceof SimpleFigure) {
                        System.out.print(" " + (figures[i][j].isBlack() ? "*" : "o") + " ");
                    } else {
                        System.out.print(" " + (figures[i][j].isBlack() ? "0" : "@") + " ");
                    }
                }
                if (j == 7) {
                    System.out.print(" " + (i + 1));
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }
}
