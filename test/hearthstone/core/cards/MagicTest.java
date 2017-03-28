package hearthstone.core.cards;

import hearthstone.core.cards.magics.AreaBurn;
import hearthstone.core.cards.magics.Burn;
import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author ldavid
 */
public class MagicTest {

    public MagicTest() {
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
        assertNotNull(Burn.DEFAULT_CARDS);
        assertFalse(AreaBurn.DEFAULT_CARDS.isEmpty());
    }

    @Test
    public void testToString() {
        System.out.println(Burn.DEFAULT_CARDS.stream()
                .findFirst()
                .orElse(null));
    }
}
