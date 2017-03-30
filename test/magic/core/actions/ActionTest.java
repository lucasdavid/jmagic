package magic.core.actions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author ldavid
 */
public class ActionTest {

    public ActionTest() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of equals method, of class Action.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");

        Action i;

        i = new DrawAction();

        assertFalse(i.equals(null));
        assertTrue(i.equals(new DrawAction()));
        assertFalse(i.equals(new PassAction()));

        i = new PassAction();

        assertFalse(i.equals(null));
        assertFalse(i.equals(new DrawAction()));
        assertTrue(i.equals(new PassAction()));
    }
}
