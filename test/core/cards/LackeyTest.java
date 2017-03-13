/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.cards;

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

}
