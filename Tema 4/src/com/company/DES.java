package com.company;
import java.util.*;
import java.lang.Math;

public class DES {
    private final List<Integer> initialKeyOfBits = new ArrayList<>();
    private final List<Integer> keyOfBits = new ArrayList<>();
    private final String plainText;
    private int plainTextLength = 0;
    private final List<Integer> rightSideKey = new ArrayList<>();
    private final List<Integer> leftSideKey = new ArrayList<>();
    private final List<String> plainTextInBits = new ArrayList<>();
    private final List<List<String>> bitsInitialPermutation = new ArrayList<>();
    private final List<List<String>> dividedPlainText = new ArrayList<>();
    private final List<Integer> keyOf56Bits = new ArrayList<>();
    private final List<Integer> keyOf48Bits = new ArrayList<>();
    private List <String> applyingXor;
    private StringBuilder LPT, RPT;
    private final List<String> cryptoText = new ArrayList<>();
    private final List<Integer> initialPermutation = new ArrayList<>(
            Arrays.asList(58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7));
    private final List<Integer> shiftingPosition = new ArrayList<>(
            Arrays.asList(1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1));
    private final List<Integer> finalPermutation = new ArrayList<>(
            Arrays.asList(40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25));
    private final List<Integer> compressionPermutation = new ArrayList<>(
            Arrays.asList(14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32));
    private final List<Integer> pBoxPermutation = new ArrayList<>(
            Arrays.asList(16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 ));
    private final List<List<Integer>> sBoxSubstitution = new ArrayList<>(Arrays.asList(
            Arrays.asList(14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
                            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
                            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
                            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13),
            Arrays.asList(15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
                            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9),
            Arrays.asList(10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
                            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12),
            Arrays.asList(7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
                            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14),
            Arrays.asList(2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
                            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15 , 10, 3, 9, 8, 6,
                            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3),
            Arrays.asList(12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
                            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13),
            Arrays.asList(4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
                            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12),
            Arrays.asList(13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
                            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11)
            ));

    public DES(String plainText){
        this.plainText = plainText;
        run();
    }

    private void run(){
        generateKey();
        convertPlainTextInBits();
        dividePlainText();
        for(int i = 0; i < 16; i++){
            keyPermutations(i);
            for(int rowIndex = 0; rowIndex < dividedPlainText.size(); rowIndex++) {
                System.out.println("\n------------------------ Block number " + (rowIndex + 1) + " ------------------------\n");
                if(i == 0){
                    generateLPTandRPT(rowIndex);
                }
                expandRPT(rowIndex);
                if(i == 15){
                    lastPermutation();
                }
            }
        }
    }

    private void generateKey(){
        Random random = new Random();

        System.out.println("The 64-bit key is: ");
        for(int i = 0; i < 64; i++){
            int randomBit = random.nextInt(2);
            initialKeyOfBits.add(randomBit);
            keyOfBits.add(randomBit);

            System.out.print(initialKeyOfBits.get(i));
            if((i + 1) % 8 == 0){
                System.out.print(" ");
            }
        }

        System.out.println("\nThe 56-bit key is: ");
        for(int i = 0; i < 56; i++){
            System.out.print(keyOfBits.get(i));
            if((i + 1) % 7 == 0) {
                keyOfBits.remove(i + 1);
                System.out.print(" ");
            }
        }
    }

    private void convertPlainTextInBits(){
        System.out.println("\n\nThe plaintext is: " + plainText);
        System.out.print("The plaintext converted in bits is: ");
        for(int i = 0; i < plainText.length(); i++){
            if(plainText.charAt(i) >= 65  && plainText.charAt(i) <= 122){
                plainTextInBits.add("0" + Integer.toString((byte) plainText.charAt(i), 2));
                plainTextLength += 1;
                System.out.print("0" + Integer.toString((byte) plainText.charAt(i), 2) + " ");
            }
        }
        System.out.print("\n\n");
    }

    private void dividePlainText(){
        System.out.println("\n------------------------ Dividing in 64-bit blocks ------------------------\n");
        int i = 0, j = 0, length;

        if(plainTextLength <= 8){
            length = 1;
        } else if(plainTextLength % 8 != 0){
            length = plainTextLength / 8 + 1;
        } else {
            length = plainTextLength / 8;
        }

        while(i < length){
            dividedPlainText.add(new ArrayList<>());

            while(dividedPlainText.get(i).size() < 8 && j < plainTextInBits.size()){
                dividedPlainText.get(i).add(plainTextInBits.get(j));
                j += 1;
            }
            i += 1;
        }

        while(dividedPlainText.get(i - 1).size() < 8){
            dividedPlainText.get(i - 1).add("0" + Integer.toString(32, 2) + "0");
        }

        System.out.println("The matrix is: ");
        for (List<String> strings : dividedPlainText) {
            System.out.println(strings);
        }
    }

    private void initialPermutation(List<String> dividedPlainTextList, int rowIndex){
        int i = 0, j = 0;
        while(i < 64){
            int currentBitIndex = (initialPermutation.get(i) - 1) % 8;
            String currentBit = String.valueOf(dividedPlainTextList.get(7 - j).charAt(currentBitIndex));
            bitsInitialPermutation.get(rowIndex).add(currentBit);
            i += 1;
            j += 1;
            if(j == 8) {
                j = 0;
            }
        }
        // System.out.println("The initial permutation for row " + (rowIndex + 1) + " is: " + bitsInitialPermutation.get(rowIndex));
    }

    private void generateLPTandRPT(int i){

            bitsInitialPermutation.add(new ArrayList<>());

            System.out.println("\nRow " + (i + 1) + " has the following values: " + dividedPlainText.get(i));
            List<String> dividedPlainTextList = dividedPlainText.get(i);
            initialPermutation(dividedPlainTextList, i);
            int j = 0;
            StringBuilder RPT = new StringBuilder();
            StringBuilder LPT = new StringBuilder();
            while(j < 64) {
                if (j < 32) {
                    LPT.append(bitsInitialPermutation.get(i).get(j));
                } else {
                    RPT.append(bitsInitialPermutation.get(i).get(j));
                }
                j += 1;
            }
            this.LPT = LPT;
            this.RPT = RPT;
    }

    private void expandRPT(int index){
        System.out.println("LPT for row " + (index + 1) + " is: " + LPT);
        System.out.println("RPT for row " + (index + 1) + " is: " + RPT);

        List<String> newRPT = new ArrayList<>();
        applyingXor = new ArrayList<>();

        // adding first bit
        newRPT.add(String.valueOf(this.RPT.charAt(this.RPT.length() - 1)));

        // expanding to 48 bits
        int j = 0, count = 0;
        for(int i = 0; i < 47; i++){
            if((i + 1) % 6 == 0 && i != 46){
                newRPT.add(String.valueOf(RPT.charAt(j)));
            } else if((i + 1 + count) % 7 == 0){
                newRPT.add(String.valueOf(RPT.charAt(j - 1)));
                count += 1;
            } else if(i != 0 && j != 32){
                newRPT.add(String.valueOf(RPT.charAt(j)));
                j += 1;
            }
        }

        // adding last bit
        newRPT.add(String.valueOf(RPT.charAt(0)));

        System.out.println("\nThe 48-bit RPT is: " + newRPT + "    size: " + newRPT.size());

        // applying XOR to RPT and the 48-bit key
        System.out.println("The 48-bit key is: " + keyOf48Bits);

        for(int i = 0; i < 48; i++){
            if(newRPT.get(i).equals("1")){
                applyingXor.add(String.valueOf((1 + keyOf48Bits.get(i)) % 2));
            } else {
                applyingXor.add(String.valueOf((keyOf48Bits.get(i)) % 2));
            }
        }

        System.out.println("The result after applying XOR is: " + applyingXor);
        substitution();
    }

    private void substitution(){
        // dividing in 6 bits blocks and getting the row / column numbers
        List<String> compressedTo32Bits = new ArrayList<>();
        int sBoxCount = 0;
        for(int i = 0; i < applyingXor.size(); i = i + 6){
            List<String> colNumber = new ArrayList<>();
            List<String> rowNumber = new ArrayList<>();
            int convertToIntColNumber = 0, convertToIntRowNumber = 0;

            for(int j = i; j < i + 6; j++){
                if(j == i || j == ((i + 6) - 1)){
                    rowNumber.add(applyingXor.get(j));
                } else {
                    colNumber.add(applyingXor.get(j));
                }
            }
            // finding the index in S-box
            for(int k = 0; k < 4; k++){
                if(k < 2){
                    if(rowNumber.get(k).equals("1")){
                        convertToIntRowNumber = (int) (convertToIntRowNumber + Math.pow(2, 1 - k));
                    }
                }
                if(colNumber.get(k).equals("1")){
                    convertToIntColNumber = (int) (convertToIntColNumber + Math.pow(2, 3 - k));
                }
            }

            int compressed = sBoxSubstitution.get(sBoxCount).get(convertToIntRowNumber * 16 + convertToIntColNumber);
            sBoxCount += 1;
            // System.out.println("The S-box index is: " + sBoxCount + " The number is: " + compressed);

            List<String> temp = new ArrayList<>();
            while(compressed > 0){
                temp.add(String.valueOf(compressed % 2));
                compressed = compressed / 2;
            }

            while(temp.size() != 4){
                temp.add("0");
            }
            Collections.reverse(temp);
            for(int k = 0; k < 4; k++){
                compressedTo32Bits.add(String.valueOf(temp.get(k)));
            }
        }
        System.out.println("After the S-box substitution: " + compressedTo32Bits + "    size: " + compressedTo32Bits.size());

        permutation(compressedTo32Bits);
    }

    private void permutation(List<String> compressedTo32Bits){
        List<String> textAfterPBox = new ArrayList<>();

        for (Integer integer : pBoxPermutation) {
            textAfterPBox.add(compressedTo32Bits.get(integer - 1));
        }
        System.out.println("After P-Box: " + textAfterPBox + "    size: " + textAfterPBox.size());

        XORandSWAP(textAfterPBox);
    }

    private void XORandSWAP(List<String> textAfterPBox){
        StringBuilder newRPT = new StringBuilder();

        for(int i = 0; i < 32; i++){
            newRPT.append((Integer.parseInt(textAfterPBox.get(i)) + (int) LPT.charAt(i)) % 2);
        }

        System.out.println("LPT is : " + LPT);
        LPT = RPT;
        RPT = newRPT;
        System.out.println("The new RPT is: " + newRPT);
        System.out.println("The new LPT is: " + LPT);
    }

    private void lastPermutation(){
        List<String> beforeFinalPermutation = new ArrayList<>();
        System.out.println("\n------------------------ Cryptotext ------------------------\n");
        for(int i = 0; i < 64; i++){
            if(i < 32){
                beforeFinalPermutation.add(String.valueOf(LPT.charAt(i)));
            } else {
                beforeFinalPermutation.add(String.valueOf(RPT.charAt((i + 1) % 32)));
            }
        }

        System.out.println("The cryptotext before final permutation is: " + beforeFinalPermutation);
        for(int i = 0; i < 64; i++){
            cryptoText.add(beforeFinalPermutation.get(finalPermutation.get(i) - 1));
        }

        System.out.println("The cryptotext is: " + cryptoText);
    }

    private void keyPermutations(int shiftingRound){
        System.out.println("\n------------------------ Shifting round number " + (shiftingRound + 1) + " ------------------------\n");

        if(shiftingRound == 0){
            for(int i = 0; i < keyOfBits.size(); i++){
                if(i < keyOfBits.size() / 2){
                    leftSideKey.add(keyOfBits.get(i));
                } else {
                    rightSideKey.add(keyOfBits.get(i));
                }
            }
        }

        System.out.println("The left side of the key is: " + leftSideKey);
        System.out.println("The right side of the key is: " + rightSideKey);

        int shifts = shiftingPosition.get(shiftingRound);

        while (shifts != 0) {
            int lastLeftBit = leftSideKey.get(keyOfBits.size() / 2 - 1);
            int lastRightBit = rightSideKey.get(keyOfBits.size() / 2 - 1);

            // shifting
            for(int j = rightSideKey.size() - 1; j >= 1; j--){
                leftSideKey.set(j, leftSideKey.get(j - 1));
                rightSideKey.set(j, rightSideKey.get(j - 1));
            }

            leftSideKey.set(0, lastLeftBit);
            rightSideKey.set(0, lastRightBit);

            shifts -= 1;
        }

        System.out.println("The left side of the key after shifting is: " + leftSideKey);
        System.out.println("The right side of the key after shifting is: " + rightSideKey);

        keyOf56Bits.clear();
        for(int i = 0; i < keyOfBits.size() / 2; i++){
            keyOf56Bits.add(leftSideKey.get(i));
        }

        for(int i = 0; i < keyOfBits.size() / 2; i++){
            keyOf56Bits.add(rightSideKey.get(i));
        }

        System.out.println("The new 56-bit key is: " + keyOf56Bits);

        keyOf48Bits.clear();
        for (Integer integer : compressionPermutation) {
            keyOf48Bits.add(keyOf56Bits.get(integer - 1));
        }
        System.out.println("The 48-bit key is: " + keyOf48Bits);
    }
}