package com.matrix_maeny.stressgame.gameboard;

public class RowColumns {

  private int selectedRow;
  private int selectedColumn;
  private boolean fromUser;
  private int score;

    public RowColumns() {
        selectedColumn = selectedRow = -1;
        fromUser = false;
        score = 0;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isFromUser() {
        return fromUser;
    }

    public void setFromUser(boolean fromUser) {
        this.fromUser = fromUser;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
}
