package game.ui.sudoku.game;

public interface SudokuGameObserver {
    void attach(SudokuGame game);
    void dettach(SudokuGame game);
    void notifyUndo();
    void notifyFill();
}
