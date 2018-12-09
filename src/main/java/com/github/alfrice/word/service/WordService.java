package com.github.alfrice.word.service;

import com.github.alfrice.word.error.WordException;
import com.github.alfrice.word.model.NumberCharacterizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.alfrice.word.util.WordNumberConstants.enties;
import static com.github.alfrice.word.util.WordNumberConstants.illions;
import static com.github.alfrice.word.util.WordNumberConstants.ones;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 11/16/18
 * Time: 12:19 PM
 * Description: Service to convert words to numbers
 */
@Component
@Slf4j
public class WordService {

    final Map<String, String> wordMap = new HashMap<>();

    static HashMap<String, String> cache;

    private final String HUNDRED = "Hundred and ";

    @Autowired
    public WordService(HashMap<String, String> lruCache) {
        if (cache == null) {
            cache = lruCache;
        }
    }

    public Map<String, String> convert(String[] items) {
        return Arrays.stream(items).collect(Collectors.toMap(t -> t, this::convertSingle));
    }

    /**
     * Preloads three number values... numbers 0-999
     */
    @PostConstruct
    public void init() {
        assert (cache != null);

        wordMap.put("00", "");
        wordMap.put("000", "");
        for (int i = 0; i < 999; i++) {
            String str = Integer.toString(i);
            NumberCharacterizer c = new NumberCharacterizer(str);
            if (i < 20) {
                wordMap.put(str, ones[i]);
            } else {
                wordMap.put(str, toWords(str));
            }
        }
        wordMap.put("0", "Zero ");

        assert (wordMap.size() > 1000);
    }

    /**
     * Validates and converts a single numberstring
     *
     * @param numString the number string, like '100,000'
     * @return The string phrase for that number or the error message.
     */
    public String convertSingle(String numString) {
        //try 0(1)
        if (wordMap.containsKey(numString)) {
            return wordMap.get(numString);
        } else if (cache.containsKey(numString)) {
            return cache.get(numString);
        }
        //try to parse
        NumberCharacterizer characterized = new NumberCharacterizer(numString);
        String[] chunks = characterized.getChunks();

        //check if too big
        if (characterized.isOutOfBounds() ) {
            return characterized.getErrorMessage();
        } else if (characterized.isParseable()) {

            StringBuilder resultBuilder = new StringBuilder();

            if (chunks.length > 0 || characterized.isFloatingPoint()) {

                List<String> phrases = Arrays.stream(chunks).map(t -> toWords(t)).collect(Collectors.toList());

                compilePhrases(phrases, resultBuilder);

                handleNumberTypes(characterized, resultBuilder);

                cleanUp(resultBuilder);

                String result = resultBuilder.toString();

                if (characterized.isParseable() &&
                        (chunks.length > 1 || characterized.isFloatingPoint()) && !cache.containsKey(numString)) {
                    cache.put(numString, result);

                }

                return result;

            }
        }

        throw new WordException("Not resolvable: " + characterized.getErrorMessage(), null);

    }

    /**
     * Cleans up any trailing commas etc.
     *
     * @param resultBuilder Stringbuilder of number word phrase
     */
    private void cleanUp(StringBuilder resultBuilder) {

        int len = resultBuilder.length();

        if (len > 0 && resultBuilder.lastIndexOf(", ") >= len - 2) {
            resultBuilder.delete(len - 2, len);
        }
    }

    /**
     * Adds qualifying words for negartives and floating point, plus the floating point values
     *
     * @param characterized The {@link NumberCharacterizer} to be processed
     * @param resultBuilder The StringBuilder to use for processing
     */
    private void handleNumberTypes(NumberCharacterizer characterized, StringBuilder resultBuilder) {
        if (characterized.isNegative()) {
            resultBuilder.insert(0, "Negative ");
        }
        if (characterized.isFloatingPoint()) {
            resultBuilder.append("Point ").append(getDigits(characterized.getDigits().toCharArray()));
        }
    }

    /**
     * Iterates over the processed phrases (up to three letters) and applies quanitifers,<br/>
     * such a <i>Million</i>, <i>thousands</i>, etc
     *
     * @param phrases A list of phrases that make up a complete number. <br/>
     * 25,456 is ["Twenty Five", "Four Hundred Fifty Six"]
     * @param builder The builder to process strings upon.
     */
    private void compilePhrases(List<String> phrases, StringBuilder builder) {
        int cur = phrases.size() == 1 ? phrases.size() : phrases.size() + 1;

        for (int i = 0; i < phrases.size(); i++) {
            String phrase = phrases.get(i);
            String suffix = illions[--cur];

            if (StringUtils.isNotEmpty(phrase)) {
                builder.append(phrases.get(i)).append(suffix);
                if (i != phrases.size() - 1) {
                    builder.append(", ");
                }
            }
        }
    }

    /**
     * Retrieves an existing epression for three letter number strings or creates them.
     *
     * @param chunk A three letter number string such as <i>124</i>
     * @returnb The word expression of the three letter number such as <i>One Hundred Twenty Three</i>
     */
    private String toWords(String chunk) {
        if (wordMap.containsKey(chunk)) {
            return wordMap.get(chunk);
        } else {
            String inWords = chunkToWords(chunk);
            wordMap.put(chunk, inWords);
            return inWords;

        }
    }

    /**
     * Returns word values for floating point values past '.'
     *
     * @param digits The {@link Character[]} of numbers that make up the digits
     * @return digits.length expression in words
     */
    private StringBuilder getDigits(char[] digits) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digits.length; i++) {
            int num = (digits[i] - '0');
            if (num == 0) {
                builder.append("Zero ");
            } else {
                builder.append(ones[num]);
            }
        }
        return builder;
    }


    /**
     * Creates a single expression of a three letter number
     *
     * @param chunk A three letter number string
     * @return the word expression of <i>chunk</i>
     */
    String chunkToWords(String chunk) {
        int[] numbers = getNumbers(chunk.toCharArray());

        StringBuilder builder = new StringBuilder();
        switch (numbers.length) {
            case 3:
                getHundred(builder, numbers[0], chunk.substring(1, chunk.length()));
                break;
            case 2:
                getEnties(builder, numbers);
                break;
            case 1:
                builder.append(ones[numbers[0]]);
                break;
        }

        return builder.toString();
    }

    /**
     * For three letter numbers, adds a single digit epression plus {@link WordService#HUNDRED}
     *
     * @param builder The builder with which to process
     * @param first The first letter of the three letter number
     * @param end The last two letters of the three letter number
     */
    void getHundred(StringBuilder builder, int first, String end) {
        if (first > 0) {
            builder.append(ones[first]).append(HUNDRED);
        }

        if (wordMap.containsKey(end)) {
            builder.append(wordMap.get(end));
        } else {
            getEnties(builder, getNumbers(end.toCharArray()));
        }
    }

    void getEnties(StringBuilder builder, int[] numbers) {

        builder.append(enties[numbers[0]]).append(ones[numbers[1]]);

    }


    int[] getNumbers(char[] chars) {
        int[] numbs = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            numbs[i] = chars[i] - '0';
        }
        return numbs;
    }


}
