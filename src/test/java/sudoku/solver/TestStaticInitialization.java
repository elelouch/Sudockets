package sudoku.solver;


import game.connection.UpdateListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStaticInitialization {

    @Test
    public void testInitialization() {
        Assertions.assertNotNull(UpdateListener.byteToOptions);
        Assertions.assertEquals(UpdateListener.byteToOptions.size(), 4);
    }
}
