package ru.vsu.сs.shemenev;

public class KingFigure extends Figure {

    public KingFigure(boolean black, int rowIndex, int colIndex) {
        super(black, rowIndex, colIndex);
    }

    @Override
    public boolean checkKill(Figure[][] figures, Figure figure, String position) {
        int indexRow = figure.getRowIndex();
        int indexCol = figure.getColIndex();
        boolean color = figure.isBlack();
        int currDistanceRow = 1;
        int currDistanceCol = 1;
        boolean upLeftKill = true, downLeftKill = true, upRightKill = true, downRightKill = true;
        if (position == null) {
            while (currDistanceRow < 7 && currDistanceCol < 7) {
                if (upLeftKill && indexRow - currDistanceRow - 1 > -1 && indexCol - currDistanceCol - 1 > -1 && //UpLeft
                        figures[indexRow - currDistanceRow][indexCol - currDistanceCol] != null &&
                        figures[indexRow - currDistanceRow - 1][indexCol - currDistanceCol - 1] == null &&
                        figures[indexRow - currDistanceRow][indexCol - currDistanceCol].isBlack() != color) {
                    return true;
                } else if (indexRow - currDistanceRow > -1 && indexCol - currDistanceCol > -1 &&
                        figures[indexRow - currDistanceRow][indexCol - currDistanceCol] != null &&
                        figures[indexRow - currDistanceRow][indexCol - currDistanceCol].isBlack() == color) {
                    upLeftKill = false;
                }
                if (downRightKill && indexRow + currDistanceRow + 1 < 8 && indexCol + currDistanceCol + 1 < 8 && //DownRight
                        figures[indexRow + currDistanceRow][indexCol + currDistanceCol] != null &&
                        figures[indexRow + currDistanceRow + 1][indexCol + currDistanceCol + 1] == null &&
                        figures[indexRow + currDistanceRow][indexCol + currDistanceCol].isBlack() != color) {
                    return true;
                } else if (indexRow + currDistanceRow < 8 && indexCol + currDistanceCol < 8 &&
                        figures[indexRow + currDistanceRow][indexCol + currDistanceCol] != null &&
                        figures[indexRow + currDistanceRow][indexCol + currDistanceCol].isBlack() == color) {
                    downRightKill = false;
                }
                if (upRightKill && indexRow - currDistanceRow - 1 > -1 && indexCol + currDistanceCol + 1 < 8 && //UpRight
                        figures[indexRow - currDistanceRow][indexCol + currDistanceCol] != null &&
                        figures[indexRow - currDistanceRow - 1][indexCol + currDistanceCol + 1] == null &&
                        figures[indexRow - currDistanceRow][indexCol + currDistanceCol].isBlack() != color) {
                    return true;
                } else if (indexRow - currDistanceRow > -1 && indexCol + currDistanceCol < 8 &&
                        figures[indexRow - currDistanceRow][indexCol + currDistanceCol] != null &&
                        figures[indexRow - currDistanceRow][indexCol + currDistanceCol].isBlack() == color) {
                    upRightKill = false;
                }
                if (downLeftKill && indexRow + currDistanceRow + 1 < 8 && indexCol - currDistanceCol - 1 > -1 && //DownLeft
                        figures[indexRow + currDistanceRow][indexCol - currDistanceCol] != null &&
                        figures[indexRow + currDistanceRow + 1][indexCol - currDistanceCol - 1] == null &&
                        figures[indexRow + currDistanceRow][indexCol - currDistanceCol].isBlack() != color) {
                    return true;
                } else if (indexRow + currDistanceRow < 8 && indexCol - currDistanceCol > -1 &&
                        figures[indexRow + currDistanceRow][indexCol - currDistanceCol] != null &&
                        figures[indexRow + currDistanceRow][indexCol - currDistanceCol].isBlack() == color) {
                    downLeftKill = false;
                }
                currDistanceRow++;
                currDistanceCol++;
            }
            return false;
        } else {
            int indexCol2 = position.charAt(0) - 'A';
            int indexRow2 = Integer.parseInt(position.substring(1, 2)) - 1;
            boolean findEnemyFigure = false;
            if (indexRow2 > indexRow) { //down
                indexRow++;
                if (indexCol2 > indexCol) { //right
                    indexCol++;
                    while (indexRow + 1 <= indexRow2 && indexCol + 1 <= indexCol2) {
                        if (!findEnemyFigure && figures[indexRow][indexCol] != null) {
                            findEnemyFigure = true;
                        }
                        if (figures[indexRow][indexCol] != null && figures[indexRow + 1][indexCol + 1] != null) {
                            return false;
                        }
                        indexRow++;
                        indexCol++;
                    }
                } else { // left
                    indexCol--;
                    while (indexRow + 1 <= indexRow2 && indexCol - 1 >= indexCol2) {
                        if (!findEnemyFigure && figures[indexRow][indexCol] != null) {
                            findEnemyFigure = true;
                        }
                        if (figures[indexRow][indexCol] != null && figures[indexRow + 1][indexCol - 1] != null) {
                            return false;
                        }
                        indexRow++;
                        indexCol--;
                    }
                }
            } else { //up
                indexRow--;
                if (indexCol2 > indexCol) { //right
                    indexCol++;
                    while (indexRow - 1 >= indexRow2 && indexCol + 1 <= indexCol2) {
                        if (!findEnemyFigure && figures[indexRow][indexCol] != null) {
                            findEnemyFigure = true;
                        }
                        if (figures[indexRow][indexCol] != null && figures[indexRow - 1][indexCol + 1] != null) {
                            return false;
                        }
                        indexRow--;
                        indexCol++;
                    }
                } else { // left
                    indexCol--;
                    while (indexRow - 1 >= indexRow2 && indexCol - 1 >= indexCol2) {
                        if (!findEnemyFigure && figures[indexRow][indexCol] != null) {
                            findEnemyFigure = true;
                        }
                        if (figures[indexRow][indexCol] != null && figures[indexRow - 1][indexCol - 1] != null) {
                            return false;
                        }
                        indexRow--;
                        indexCol--;
                    }
                }
            }
            return indexRow == indexRow2 && indexCol == indexCol2 && findEnemyFigure;
        }
    }

    @Override
    public boolean checkMove(Figure[][] figures, Figure figure, String position) {
        int indexEndCol = position.charAt(0) - 'A';
        int indexEndRow = Integer.parseInt(position.substring(1, 2)) - 1;
        if (indexEndCol < 0 || indexEndCol > 7 || indexEndRow < 0 || indexEndRow > 7 || figures[indexEndRow][indexEndCol] != null) {
            return false;
        }
        if (Math.abs(indexEndRow - figure.getRowIndex()) != Math.abs(indexEndCol - figure.getColIndex())) {
            return false;
        }
        //boolean color = figure.isBlack();
        int indexStartRow = figure.getRowIndex();
        int indexStartCol = figure.getColIndex();
        if (indexEndRow > indexStartRow) {//Down
            indexStartRow++;
            if (indexEndCol > indexStartCol) {//Right
                indexStartCol++;
                while (indexStartRow < indexEndRow && indexStartCol < indexEndCol) {
                    if (figures[indexStartRow][indexStartCol] != null) {
                        return false;
                    }
                    indexStartCol++;
                    indexStartRow++;
                }
            } else { //Left
                indexStartCol--;
                while (indexStartRow < indexEndRow && indexStartCol > indexEndCol) {
                    if (figures[indexStartRow][indexStartCol] != null) {
                        return false;
                    }
                    indexStartCol--;
                    indexStartRow++;
                }
            }
        } else { //Up
            indexStartRow--;
            if (indexEndCol > indexStartCol) {//Right
                indexStartCol++;
                while (indexStartRow > indexEndRow && indexStartCol < indexEndCol) {
                    if (figures[indexStartRow][indexStartCol] != null) {
                        return false;
                    }
                    indexStartCol++;
                    indexStartRow--;
                }
            } else { //Left
                indexStartCol--;
                while (indexStartRow > indexEndRow && indexStartCol > indexEndCol) {
                    if (figures[indexStartRow][indexStartCol] != null) {
                        return false;
                    }
                    indexStartCol--;
                    indexStartRow--;
                }
            }
        }
        return true;
    }

    @Override
    public void makeKill(Figure[][] figures, int indexStartRow, int indexStartCol, int indexEndRow, int indexEndCol, Player player) {
        int currIndexStartRow = indexStartRow;
        int currIndexStartCol = indexStartCol;
        if (indexEndRow > currIndexStartRow) { //down
            currIndexStartRow++;
            if (indexEndCol > currIndexStartCol) { //right
                currIndexStartCol++;
                while (currIndexStartRow < indexEndRow && currIndexStartCol < indexEndCol) {
                    if (figures[currIndexStartRow][currIndexStartCol] != null) {
                        figures[currIndexStartRow][currIndexStartCol] = null;
                        player.setCountFigures(player.getCountFigures() - 1);
                    }
                    currIndexStartRow++;
                    currIndexStartCol++;
                }
            } else { // left
                currIndexStartCol--;
                while (currIndexStartRow < indexEndRow && currIndexStartCol > indexEndCol) {
                    if (figures[currIndexStartRow][currIndexStartCol] != null) {
                        figures[currIndexStartRow][currIndexStartCol] = null;
                        player.setCountFigures(player.getCountFigures() - 1);
                    }
                    currIndexStartRow++;
                    currIndexStartCol--;
                }
            }
        } else { //up
            currIndexStartRow--;
            if (indexEndCol > currIndexStartCol) { //right
                currIndexStartCol++;
                while (currIndexStartRow > indexEndRow && currIndexStartCol < indexEndCol) {
                    if (figures[currIndexStartRow][currIndexStartCol] != null) {
                        figures[currIndexStartRow][currIndexStartCol] = null;
                        player.setCountFigures(player.getCountFigures() - 1);
                    }
                    currIndexStartRow--;
                    currIndexStartCol++;
                }
            } else { // left
                currIndexStartCol--;
                while (currIndexStartRow > indexEndRow && currIndexStartCol > indexEndCol) {
                    if (figures[currIndexStartRow][currIndexStartCol] != null) {
                        figures[currIndexStartRow][currIndexStartCol] = null;
                        player.setCountFigures(player.getCountFigures() - 1);
                    }
                    currIndexStartRow--;
                    currIndexStartCol--;
                }
            }
        }
        figures[indexEndRow][indexEndCol] = figures[indexStartRow][indexStartCol];
        figures[indexStartRow][indexStartCol] = null;
        figures[indexEndRow][indexEndCol].setRowIndex(indexEndRow);
        figures[indexEndRow][indexEndCol].setColIndex(indexEndCol);
    }

}
