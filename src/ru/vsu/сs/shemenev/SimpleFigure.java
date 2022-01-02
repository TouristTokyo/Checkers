package ru.vsu.сs.shemenev;

public class SimpleFigure extends Figure {
    public SimpleFigure(boolean black, int rowIndex, int colIndex) {
        super(black, rowIndex, colIndex);
    }

    @Override
    public boolean checkKill(Figure[][] figures, Figure figure, String position) {
        if (position != null) {
            int indCol = position.charAt(0) - 'A';
            int indRow = Integer.parseInt(position.substring(1, 2)) - 1;
            if (indCol < 0 || indCol > 7 || indRow < 0 || indRow > 7 || figures[indRow][indCol] != null) {
                return false;
            }
            if (Math.abs(figure.getRowIndex() - indRow) != 2 || Math.abs(figure.getColIndex() - indCol) != 2) {
                return false;
            }
        }
        int indexRow = figure.getRowIndex();
        int indexCol = figure.getColIndex();
        boolean color = figure.isBlack();
        if (indexCol + 2 < 8 && indexRow + 2 < 8 && figures[indexRow + 1][indexCol + 1] != null && figures[indexRow + 2][indexCol + 2] == null
                && figures[indexRow + 1][indexCol + 1].isBlack() != color) {
            return true;
        }
        if (indexCol + 2 < 8 && indexRow - 2 > -1 && figures[indexRow - 1][indexCol + 1] != null && figures[indexRow - 2][indexCol + 2] == null
                && figures[indexRow - 1][indexCol + 1].isBlack() != color) {
            return true;
        }
        if (indexCol - 2 > -1 && indexRow - 2 > -1 && figures[indexRow - 1][indexCol - 1] != null && figures[indexRow - 2][indexCol - 2] == null
                && figures[indexRow - 1][indexCol - 1].isBlack() != color) {
            return true;
        }
        if (indexCol - 2 > -1 && indexRow + 2 < 8 && figures[indexRow + 1][indexCol - 1] != null && figures[indexRow + 2][indexCol - 2] == null
                && figures[indexRow + 1][indexCol - 1].isBlack() != color) {
            return true;
        }
        return false;
    }

    @Override
    public void makeKill(Figure[][] figures, int indexStartRow, int indexStartCol, int indexEndRow, int indexEndCol, Player player) {
        int delta = indexEndCol > indexStartCol ? -1 : 1;
        int delta2 = indexEndRow > indexStartRow ? -1 : 1;
        figures[indexEndRow][indexEndCol] = figures[indexStartRow][indexStartCol];
        figures[indexStartRow][indexStartCol] = null;
        figures[indexStartRow - delta2][indexStartCol - delta] = null;
        figures[indexEndRow][indexEndCol].setRowIndex(indexEndRow);
        figures[indexEndRow][indexEndCol].setColIndex(indexEndCol);
        player.setCountFigures(player.getCountFigures() - 1);
        if (!(figures[indexEndRow][indexEndCol] instanceof KingFigure) && ((indexEndRow == 0 && !figures[indexEndRow][indexEndCol].isBlack())
                || (indexEndRow == 7 && figures[indexEndRow][indexEndCol].isBlack()))) {
            figures[indexEndRow][indexEndCol] = new KingFigure(figures[indexEndRow][indexEndCol].isBlack(), indexEndRow, indexEndCol);
        }
    }

    @Override
    public boolean checkMove(Figure[][] figures, Figure figure, String position) {
        int indexCol = position.charAt(0) - 'A';
        int indexRow = Integer.parseInt(position.substring(1, 2)) - 1;
        if (indexCol < 0 || indexCol > 7 || indexRow < 0 || indexRow > 7 || figures[indexRow][indexCol] != null) {
            return false;
        }
        if ((figure.isBlack() && indexRow != figure.getRowIndex() + 1) ||
                (!figure.isBlack() && indexRow != figure.getRowIndex() - 1)
                || Math.abs(indexCol - figure.getColIndex()) != 1) {
            return false;
        }
        return true;
    }
}
