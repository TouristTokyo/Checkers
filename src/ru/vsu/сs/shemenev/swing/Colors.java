package ru.vsu.сs.shemenev.swing;

import java.awt.*;

public enum Colors {

    DARK_FIGURE(new Color(117, 21, 21)),
    WHITE_FIGURE(new Color(255, 199, 199)),
    BLACK_CELL(new Color(210, 140, 69)),
    ORANGE_LIGHT_BACKGROUND(new Color(242, 204, 128)),
    GREEN_BUTTON(new Color(17, 189, 28)),
    WHITE_CELL(new Color(255, 206, 158)),
    GOLD_CROWN(new Color(163, 142, 21)),
    LIGHT_GREEN_SELECTED(new Color(179, 242, 172));

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
