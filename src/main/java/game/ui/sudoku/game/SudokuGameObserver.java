package game.ui.sudoku.game;

public interface SudokuGameObserver {
    void subscribe(SudokuGame game);
    void unSubscribe(SudokuGame game);
    void notifyFullUpdate();
    void notifySolution();
}
