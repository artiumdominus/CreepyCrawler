package dev.artiumdominus.creepycrawler.util;

import java.util.Random;

public class IdGenerator {

    private static final int ID_SIZE = 8;
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = LOWERCASE_LETTERS.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String VALID_CHARS = LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS;

    private final Random randomGenerator;

    public IdGenerator() {
        this.randomGenerator = new Random();
    }

    public String generate() {
        var randomString = new StringBuilder();
        while (randomString.length() < ID_SIZE) {
            randomString.append(randomChar());
        }
        return randomString.toString();
    }

    private char randomChar() {
        var randomIndex = randomGenerator.nextInt(VALID_CHARS.length());
        return VALID_CHARS.charAt(randomIndex);
    }
}
