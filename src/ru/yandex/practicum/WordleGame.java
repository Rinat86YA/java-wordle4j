package ru.yandex.practicum;

import java.io.PrintWriter;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private final PrintWriter printWriter;
    private final int maxStepCount = 6;
    private final int wordsLength = 5;

    public WordleGame(WordleDictionary dictionary, PrintWriter printWriter) {
        this.dictionary = dictionary;
        this.steps = maxStepCount;
        this.printWriter = printWriter;
        answer = dictionary.getRandomWord(false);
    }

    public String makeSuggestion(String suggestionWord) throws WordleException {
        try {
            if (suggestionWord == null || suggestionWord.trim().isEmpty()) {
                throw new EmptyWordException();
            }

            if (suggestionWord.length() != wordsLength) {
                throw new InvalidWordLengthException(wordsLength);
            }

            if (!dictionary.isWordInDictionary(suggestionWord)) {
                throw new WordNotInDictionaryException();
            }

            steps--;
            String clue = WordleDictionary.getClue(answer, suggestionWord);
            dictionary.updateDependingOnClues(suggestionWord, clue);
            return clue;
        } catch (WordleException e) {
            printWriter.println(e.getMessage());
            throw e;
        } catch (Exception e) {
            printWriter.println(e.getMessage());
        }
        return "";
    }

    public boolean isStepsCountEnough() {
        return steps != 0;
    }

    public int getStepsLeft() {
        return steps;
    }

    public String getRandomWord() {
        return dictionary.getRandomWord(true);
    }

    public String getAnswer() {
        return answer;
    }
}