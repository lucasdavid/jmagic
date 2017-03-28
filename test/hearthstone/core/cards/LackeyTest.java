package hearthstone.core.cards;

import org.junit.*;

import java.util.Arrays;
import java.util.Collection;

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
        Lackey lackey = new Lackey("Joe", 10, 4, 5, Arrays.asList(LackeyAttributes.HASTE));

        Collection<LackeyAttributes> buffs = lackey.getBuffs();
        buffs.add(LackeyAttributes.PROVOKE);
        buffs.add(LackeyAttributes.WIND_FURY);

        assertEquals(lackey.getBuffs().size(), 1);
        assertTrue(lackey.getBuffs().contains(LackeyAttributes.HASTE));
    }

}
