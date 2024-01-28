package sudoku.solver;


import game.connection.updaters.SudokuUpdateListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStaticInitialization {

    @Test
    public void testInitialization() {
        Assertions.assertNotNull(SudokuUpdateListener.byteToOptions);
        Assertions.assertEquals(SudokuUpdateListener.byteToOptions.size(), 4);
    }
}
