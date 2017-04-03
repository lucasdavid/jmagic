package magic.core.cards;

import magic.core.cards.magics.AreaBurn;
import magic.core.cards.magics.Burn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 *
 * @author ldavid
 */
public class MagicTest {

    public MagicTest() {
    }

    @Test
    public void testDefaultLackeys() {
        assertNotNull(Burn.SAMPLES);
        assertFalse(AreaBurn.SAMPLES.isEmpty());
    }

    @Test
    public void testToString() {
        System.out.println(Burn.SAMPLES.stream()
                .findFirst()
                .orElse(null));
    }
}
