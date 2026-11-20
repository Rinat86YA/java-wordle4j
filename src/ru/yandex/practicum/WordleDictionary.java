package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private List<String> originalWords;
    private List<String> words;
    private PrintWriter printWriter;

    public WordleDictionary(List<String> newWords, PrintWriter printWriter) {
        this.printWriter = printWriter;
        originalWords = newWords;
        words = new ArrayList<>();
        words.addAll(newWords);
    }

    public String getRandomWord(boolean flag) {
        String result = null;
        try {
            Random random = new Random();
            if (words.isEmpty()) {
                throw new DictionaryException("Слов в словаре не осталось!");
            } else {
                result = words.get(random.nextInt(words.size()));
            }
        } catch (DictionaryException e) {
            printWriter.println(e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            printWriter.println(e.getMessage());
        }
        if (flag && result != null) {
            words.remove(result);
        }
        return result;
    }

    public static String getClue(String answer, String suggestedWord) {
        char[] clue = new char[answer.length()];
        Map<Character, Integer> counts = new HashMap<>();

        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == suggestedWord.charAt(i)) {
                clue[i] = '+';
            } else {
                char c = answer.charAt(i);
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }
        }

        for (int i = 0; i < answer.length(); i++) {
            if (clue[i] == '+') {
                continue;
            }
            char c = suggestedWord.charAt(i);
            int count = counts.getOrDefault(c, 0);
            if (count > 0) {
                clue[i] = '^';
                counts.put(c, count - 1);
            } else {
                clue[i] = '-';
            }
        }

        return new String(clue);
    }

    public void updateDependingOnClues(String suggestedWord, String clue) {
        try {
            if (clue.length() != 5) {
                throw new InvalidClueFormatException();
            }
            for (int i = 0; i < clue.length(); i++) {
                char c = clue.charAt(i);
                if (c != '+' && c != '^' && c != '-') {
                    throw new InvalidClueFormatException();
                }
            }

            Set<String> toDelete = new HashSet<>();
            for (String word : words) {
                if (!getClue(word, suggestedWord).equals(clue)) {
                    toDelete.add(word);
                }
            }
            words.removeAll(toDelete);
        } catch (Exception e) {
            printWriter.println(e.getMessage());
        }
    }

    public boolean isWordInDictionary(String word) {
        boolean result = false;
        try {
            result = originalWords.contains(word);
        } catch (Exception e) {
            printWriter.println(e.getMessage());
        }
        return result;
    }
}