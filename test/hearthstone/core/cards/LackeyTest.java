package hearthstone.core.cards;

import hearthstone.core.cards.magics.Attachment;
import org.junit.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

/**
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
        Lackey lackey = new Lackey("Joe", 10, 4, 5, Arrays.asList(LackeyAttributes.HASTE),
                Collections.emptyList());

        Collection<LackeyAttributes> buffs = lackey.getAttributes();
        buffs.add(LackeyAttributes.PROVOKE);
        buffs.add(LackeyAttributes.WIND_FURY);

        assertEquals(lackey.getAttributes().size(), 1);
        assertTrue(lackey.getAttributes().contains(LackeyAttributes.HASTE));
    }

    @Test
    public void testLackeyWithAttachments() {
        Attachment a = Attachment.DEFAULT_CARDS.stream()
                .findFirst()
                .orElse(null);

        assertEquals(2, a.getDamageIncrease());
        assertEquals(0, a.getLifeIncrease());

        Lackey l = new Lackey("Joe", 53, 31, 1, Collections.emptyList(), Arrays.asList(a));

        assertEquals(53 + 2, l.getDamage());
        assertEquals(31 + 0, l.getLife());
        assertEquals(31 + 0, l.getLife());
    }
}
