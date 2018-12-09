package com.github.alfrice.word.service;

import com.github.alfrice.word.cache.LRUCache;
import com.github.alfrice.word.error.WordException;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/2/18
 * Time: 2:57 PM
 * Description: Exercises the word service
 */
@SuppressWarnings("unchecked")
@Slf4j
public class WordServiceTest {

    private final LRUCache cache = new LRUCache(100);
    private final WordService wordService = new WordService(cache);

    @Before
    public void init() {
        wordService.init();
        WordService.cache=cache;
    }

    @Test
    public void testConvertScientific() {
        String str = "10.6e25";
        Map<String, String> map =  wordService.convert(new String[]{str});
        assertEquals("One Hundred and Six Septillion", map.get(str));
        String[] strings = new String[]{"427234567800", ".427234567800e12"};

        map = wordService.convert(strings);

        assertEquals(map.get(strings[0]), map.get(strings[1]));
    }

    @Test(expected = WordException.class)
    public void testMoney() {
        String str = "$23,043.77";
        Map<String, String> map =  wordService.convert(new String[]{str});
        //todo implement

        // assertEquals("Twenty Three Thousand, Forty Three Dollars and Seventy Seven Cents", map.get(0));
    }


    @Test
    public void testConvertHundred() {
        String str = "427";
        Map<String, String> map =  wordService.convert(new String[]{str});
        assertEquals("Four Hundred and Twenty Seven ", map.get(str));

        str+=",234,567,800";
        map =  wordService.convert(new String[]{str});
        assertEquals("Four Hundred and Twenty Seven billion, Two Hundred and Thirty Four Million, Five Hundred and Sixty Seven Thousand, Eight Hundred ", map.get(str));
    }

    @Test(expected = WordException.class)
    public void testCrap() {

        wordService.convertSingle("$53-x1");


    }


    @Test
    public void testGetNothings() {
        Map<String, String> map =  wordService.convert(new String[]{"0", "00", "000"});
        assertEquals("Zero ", map.get("0"));
        assertEquals("", map.get("00"));
        assertEquals("", map.get("000"));

    }


    @Test
    public void testTooBig() {
        String str = "2.4e99";
        Map<String, String> map =  wordService.convert(new String[]{str});
        assertTrue(map.get(str).contains("I cant count that high. Try something smaller."));
    }


    @Test
    public void testGiantFloatingPoint() {
        String[] list = new String[]{"2,147,483,647.44556677"};
        Map<String, String> map =  wordService.convert(new String[]{"2,147,483,647.44556677"});
        Arrays.stream(list).forEach(t->{
            assertTrue(map.containsKey(t));
            assertTrue(map.get(t).length()>100);
        });

    }

    @Test
    public void testConvertBigNegativeFloating() {
        String result =  wordService.convertSingle("-23,043.7775");
        assertEquals("Negative Twenty Three Thousand, Forty Three Point Seven Seven Seven Five ", result);
    }

    @Test
    public void testConvertRealBig() {
        String result =  wordService.convertSingle("1233300002045487878787878787878787800000000");
        assertEquals("One Tredecillion, Two Hundred and Thirty Three Duodecillion, Three Hundred Undecillion, Two Decillion, Forty Five Nonillion, Four Hundred and Eighty Seven Octillion, Eight Hundred and Seventy Eight Septillion, Seven Hundred and Eighty Seven Sextillion, Eight Hundred and Seventy Eight Quintillion, Seven Hundred and Eighty Seven Quadrillion, Eight Hundred and Seventy Eight Trillion, Seven Hundred and Eighty Seven billion, Eight Hundred Million", result);
    }

    @Test
    public void testFloatingPoint() {
        Map<String, String> map =  wordService.convert(new String[]{"0.64"});
        assertEquals("Zero Point Six Four ", map.get("0.64"));

        map =  wordService.convert(new String[]{".64"});
        assertEquals("Point Six Four ", map.get(".64"));

        map =  wordService.convert(new String[]{"7.64"});
        assertEquals("Seven Point Six Four ", map.get("7.64"));

    }


    @Test
    public void testGet100() {
        String[] strings = new String[1000];
        for (int i = 0; i < 1000; i++) {
            String str = String.valueOf(i);
            strings[i] = str;

        }
        Map<String, String> result = wordService.convert(strings);
        assertTrue(result.size() == 1000);

        Arrays.stream(strings).forEach(t -> assertTrue(wordService.wordMap.containsKey(t)));

    }

    @Test
    public void testCache() {
        String testNumber = "456,789,999,999";
        String t2 = "-4.35";
        for (int i = 1001; i < 20000; i++) {
            if (i ==19950) {
                wordService.convertSingle(testNumber);
            }
            if (i==19925){
                wordService.convertSingle(t2);
            }
            wordService.convertSingle(String.valueOf(i));
        }

        assertTrue(cache.containsKey(testNumber));
        assertTrue(cache.containsKey(t2));
        assertEquals(cache.size(), 99);

    }


}

