package game.ui.sudoku.game;

public interface SudokuGameObserver extends SudokuGame{
    void subscribe(SudokuGame game);
    void unSubscribe(SudokuGame game);
    void notifyUpdate(int i, int j, int num);
    void notifyUndo(int i, int j);
    void notifyFullUpdate();
    void notifySolution();
}
