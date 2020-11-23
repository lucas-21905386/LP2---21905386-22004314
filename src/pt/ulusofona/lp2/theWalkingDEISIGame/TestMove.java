package pt.ulusofona.lp2.theWalkingDEISIGame;

import org.junit.Test;
import java.io.File;
import static org.junit.Assert.assertEquals;

public class TestMove {

    TWDGameManager teste = new TWDGameManager();

    @Test
    public void testMove1() {
        teste.startGame(new File("test-files/teste.txt"));

        boolean atual = new TWDGameManager().move(3, 3, 2, 3);

        boolean esperado = true;
        assertEquals("Expected result is " + esperado + "but was " + atual, esperado, atual);
    }

    @Test
    public void testMove2() {
        teste.startGame(new File("test-files/teste.txt"));

        boolean atual = new TWDGameManager().move(3, 3, 3, 1);

        boolean esperado = false;
        assertEquals("Expected result is " + esperado + "but was " + atual, esperado, atual);
    }

    @Test
    public void testMove3() {
        teste.startGame(new File("test-files/teste.txt"));

        boolean atual = new TWDGameManager().move(4, 4, 4, 3);

        boolean esperado = false;
        assertEquals("Expected result is " + esperado + "but was " + atual, esperado, atual);
    }

    @Test
    public void testMove4() {
        teste.startGame(new File("test-files/teste.txt"));

        boolean atual = new TWDGameManager().move(2, 3, 1, 2);

        boolean esperado = false;
        assertEquals("Expected result is " + esperado + "but was " + atual, esperado, atual);
    }
}
