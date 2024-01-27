package game.ui.sudoku.game;

public interface SudokuGameObserver {
    void suscribe(SudokuGame game);
    void desuscribe(SudokuGame game);
}
