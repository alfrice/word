package com.github.alfrice.word;

import com.github.alfrice.word.controller.WordController;
import com.github.alfrice.word.error.WordException;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/8/18
 * Time: 1:52 PM
 * Description: com.github.alfrice.word
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WordApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
@Slf4j
public class WordControllerIntegrationTest {

    @Autowired
    public WordController controller;

    @Test
    public void testGoodWord() {

        String value = controller.convertNumberString("246777");
        assertTrue(value.equals("Two Hundred and Forty Six Thousand, Seven Hundred and Seventy Seven "));

    }

    @Test(expected = WordException.class)
    public void testBadWord() {

        controller.convertNumberString("24677,,-87");

    }

    @Test
    public void testGoodWords() {

        String[] goodValues = new String[]{"246777", "1.5", ".25", "0.25", "456,789,999,999", ".25e12", "-42,999555", "0"};

        final Map<String, String> value = controller.convertNumberStrings(goodValues);
        Arrays.stream(goodValues).forEach(t -> {
            assertTrue(value.containsKey(t));
        });
        log.info("{}", goodValues);

    }

    @Test(expected = WordException.class)
    public void testBadWords() {

        String[] goodValues = new String[]{"Poo!", "f*&^@!", ".2-5", "=.25", "456,789,999.999", ".dam!", "sheesh", "Zero"};

        final Map<String, String> value = controller.convertNumberStrings(goodValues);
        Arrays.stream(goodValues).forEach(t -> {
            assertTrue(value.containsKey(t));
        });
        log.info("{}", goodValues);

    }


}
