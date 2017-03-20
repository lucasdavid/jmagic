package hearthstone.core.cards;

import java.util.Arrays;
import java.util.Collection;
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
public class LackeyTest {

    public LackeyTest() {
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

    @Test
    public void testDefaultLackeys() {
        assertNotNull(Lackey.DEFAULT_CARDS);
        assertFalse(Lackey.DEFAULT_CARDS.isEmpty());
    }

    @Test
    public void testToString() {
        System.out.println(Lackey.DEFAULT_CARDS.stream()
                .findFirst()
                .orElse(null));
    }

    @Test
    public void testBuffsAreImmutable() {
        Lackey lackey = new Lackey("Joe", 10, 4, 5, Arrays.asList(Buff.HASTE));

        Collection<Buff> buffs = lackey.getBuffs();
        buffs.add(Buff.PROVOKE);
        buffs.add(Buff.WIND_FURY);

        assertEquals(lackey.getBuffs().size(), 1);
        assertTrue(lackey.getBuffs().contains(Buff.HASTE));
    }

}
