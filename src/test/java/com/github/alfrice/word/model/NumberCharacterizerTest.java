package com.github.alfrice.word.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.*;

import static com.github.alfrice.word.model.NumberCharacterizer.NUM_FORMAT_ERR_MSG;
import static org.junit.Assert.*;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/4/18
 * Time: 4:16 AM
 * Description: com.github.alfrice.word.model
 */
@Slf4j
public class NumberCharacterizerTest {

    @Test
    public void testCleanGood() {

        NumberCharacterizer characterizer = new NumberCharacterizer("");
        System.out.println(characterizer.chunks);

        String cleaned = characterizer.clean("423,333");
        assertEquals(cleaned, "423333");

        cleaned = characterizer.clean("423,333444,777.993456");
        assertEquals(cleaned, "423333444777.993456");

        cleaned = characterizer.clean("324.224");
        assertEquals(cleaned, "324.224");

        cleaned = characterizer.clean("0.24");
        assertEquals(cleaned, "0.24");

        cleaned = characterizer.clean("-423,333");
        assertEquals(cleaned, "423333");

        cleaned = characterizer.clean("-423,333,444,999,777,888,444,666,777.993456");
        assertEquals(cleaned, "423333444999777888444666777.993456");

        cleaned = characterizer.clean("4.232e25");
        assertEquals(cleaned, "42320000000000000000000000");

        cleaned = characterizer.clean("-4.232e25");
        assertEquals(cleaned, "42320000000000000000000000");

        cleaned = characterizer.clean("-324.224");
        assertEquals(cleaned, "324.224");

        cleaned = characterizer.clean("-0.24");
        assertEquals(cleaned, "0.24");

    }


    @Test
    public void testCleanError3() {

        NumberCharacterizer characterizer = new NumberCharacterizer("");

        characterizer.clean("22%");

        assertTrue(characterizer.getErrorMessage().contains(NUM_FORMAT_ERR_MSG));
        assertTrue(characterizer.getErrorMessage().contains("'%'"));


    }

    @Test
    public void testCleanError() {

        NumberCharacterizer characterizer = new NumberCharacterizer("");

        characterizer.clean("423$%xy333");

        assertTrue(characterizer.getErrorMessage().contains(NUM_FORMAT_ERR_MSG));

        characterizer.clean("hey there YOU");

        assertTrue(characterizer.getErrorMessage().contains(NUM_FORMAT_ERR_MSG));


    }

    @Test()
    public void testCleanError2() {

        NumberCharacterizer characterizer = new NumberCharacterizer("");

        characterizer.clean("$12.00");

        assertTrue(characterizer.getErrorMessage().contains(NUM_FORMAT_ERR_MSG));
        assertTrue(characterizer.getErrorMessage().contains("'$.'"));

    }

    @Test
    public void testToString(){
        NumberCharacterizer characterizer = new NumberCharacterizer("4.278545e81");

        assertEquals("NumberCharacterizer(chunks=[4, 278, 545, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000], digits=, negative=false, floatingPoint=false, errorMessage=null, parseable=true, outOfBounds=false)", characterizer.toString());
    }
}

