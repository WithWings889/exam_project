package com.example.exam_progect;

public class Cryptographer {
    private static String shift(String text, int k){
        String newText = text.substring(text.length() - k, text.length());
        newText = newText.concat(text.substring(0, text.length() - k));
        return newText;
    }

    private static String deleteDublicates(String word){
        String newWord = "" + word.charAt(0);
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i - 1) == word.charAt(i) && i != 0) {
                continue;
            }
            newWord = newWord + word.charAt(i);
        }
        return newWord;
    }

    private static String shift(String text, String code){
        return code + text;
    }

    private static String findAlfabet(String language){
        String currentLanguage;

        final String English = "abcdefghijklmnopqrstuvwxyz";
        final String Russian = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        final String Ukrainian = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя";

        if (language.equals("English"))
            currentLanguage = English;
        else
        if(language.equals("Русский"))
            currentLanguage = Russian;
        else
        if(language.equals("Українська"))
            currentLanguage = Ukrainian;
        else
            currentLanguage = English;
        return currentLanguage;
    }

    private static String doCode(String currentAlfabet, String modernAlfabet, String text){
        String newText = "";

        for(int i = 0; i < text.length(); ++i){
            if (currentAlfabet.contains(String.valueOf(text.charAt(i))))
                newText = newText.concat(Character.toString(modernAlfabet.charAt(currentAlfabet.indexOf(text.charAt(i)))));
            else
                newText = newText.concat(Character.toString(text.charAt(i)));
        }
        return newText;
    }

    public static String caesarСipher(String text, String language, int k){
        String currentAlfabet = findAlfabet(language);
        k = k % currentAlfabet.length();
        String modernAlfabet = shift(currentAlfabet, k);
        return doCode(currentAlfabet, modernAlfabet, text.toLowerCase());
    }

    public static String caesarCipherDecode(String text, String language, int k){
        String currentAlfabet = findAlfabet(language);
        k = k % currentAlfabet.length();
        String modernAlfabet = shift(currentAlfabet, k);
        return doCode(modernAlfabet, currentAlfabet, text.toLowerCase());
    }

    public static String wordCode(String text, String language, String code){
        String currentAlfabet = findAlfabet(language);
        String modernAlfabet = shift(currentAlfabet, deleteDublicates(code));
        return doCode(currentAlfabet, modernAlfabet, text.toLowerCase());
    }
    public static String wordCodeDecode(String text, String language, String code){
        String currentAlfabet = findAlfabet(language);
        String modernAlfabet = shift(currentAlfabet, deleteDublicates(code));
        return doCode(modernAlfabet, currentAlfabet, text.toLowerCase());
    }

    public static String encryption(String word, String colKeys, String rowKeys) {

        char[][] firstTable = new char[5][5];
        char[][] secondTable = new char[5][5];

        // fill with spaces, if length is less than 25
        if (word.length() < 25) {
            for (int i = word.length() + 1; i <= 25; i++)
                word += " ";
        }

        // fill matrix with symbols
        for (int i = 0; i < firstTable.length; i++) {
            for (int j = 0; j < firstTable.length; j++) {
                firstTable[i][j] = word.charAt(firstTable.length * i + j);
            }
        }

        // apply column key
        for (int i = 0; i < firstTable.length; i++) {
            int a = colKeys.charAt(i) - '0' - 1; // current number of column key
            for (int j = 0; j < firstTable.length; j++) {
                secondTable[j][a] = firstTable[j][i];
            }
        }

        // apply row key
        for (int i = 0; i < firstTable.length; i++) {
            int a = rowKeys.charAt(i) - '0' - 1; // current number of row key
            for (int j = 0; j < firstTable.length; j++)
                firstTable[a][j] = secondTable[i][j];
        }

        // write to string characters from matrix, row by row
        String result = "";
        for (int i = 0; i < firstTable.length; i++) {
            for (int j = 0; j < firstTable.length; j++) {
                result += firstTable[i][j];
            }
        }
        System.out.println(result);
        return result;
    }

    public static String decryption(String word, String colKeys, String rowKeys) {

        char[][] firstTable = new char[5][5];
        char[][] secondTable = new char[5][5];

        // fill matrix with symbols
        for (int i = 0; i < firstTable.length; i++) {
            for (int j = 0; j < firstTable.length; j++) {
                firstTable[i][j] = word.charAt(firstTable.length * i + j);
            }
        }

        // apply row key
        for (int i = 0; i < firstTable.length; i++) {
            int a = rowKeys.charAt(i) - '0' - 1; // current number of row key
            for (int j = 0; j < firstTable.length; j++) {
                secondTable[i][j] = firstTable[a][j];
            }
        }

        // apply column key
        for (int i = 0; i < firstTable.length; i++) {
            int a = colKeys.charAt(i) - '0' - 1; // current column key number
            for (int j = 0; j < firstTable.length; j++) {
                firstTable[j][i] = secondTable[j][a];
            }
        }

        // write to string characters from matrix, row by row
        String result = "";
        for (int i = 0; i < firstTable.length; i++) {
            for (int j = 0; j < firstTable.length; j++) {
                result += firstTable[i][j];
            }
        }
        System.out.println(result);
        return result;
    }
}
