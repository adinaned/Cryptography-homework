package com.company;

public class Main {

    public static void main(String[] args) {
        SHA256 firstWord = new SHA256("abc");
        firstWord.hammingDistance(new SHA256("abd"));
        // firstWord.birthdayAttack();
    }
}
