package hearthstone.core.actions;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ldavid
 */
public class ActionTest {

    public ActionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
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
