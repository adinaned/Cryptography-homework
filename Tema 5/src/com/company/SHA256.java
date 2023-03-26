package com.company;

import java.util.*;

public class SHA256 {
    private String message;
    private final StringBuilder initialMessage = new StringBuilder();
    private int index;
    private int size = 0;
    private final List<Integer> digestList = new ArrayList<>();
    private boolean firstLoop = true;
    private final List<Integer> messageInBits = new ArrayList<>();
    private final List<List<Integer>> dividingInBlocks = new ArrayList<>();
    private final List<String> hexadecimalRepresentation = new ArrayList<>();
    private final List<List<String>> hexadecimalBlocks = new ArrayList<>();
    StringBuilder h0, h1, h2, h3, h4, h5, h6, h7;
    List<Integer> a = new ArrayList<>();
    List<Integer> b = new ArrayList<>();
    List<Integer> c = new ArrayList<>();
    List<Integer> d = new ArrayList<>();
    List<Integer> e = new ArrayList<>();
    List<Integer> f = new ArrayList<>();
    List<Integer> g = new ArrayList<>();
    List<Integer> h = new ArrayList<>();
    StringBuilder digest = new StringBuilder();
    private final List<String> hexadecimalValues = new ArrayList<>
            (Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"));
    private final List<String> buffers = new ArrayList<>
            (Arrays.asList("0x6a09e667", "0xbb67ae85", "0x3c6ef372", "0xa54ff53a", "0x510e527f", "0x9b05688c",
                    "0x1f83d9ab", "0x5be0cd19"));
    private final List<String> kt = new ArrayList<>(Arrays.asList(
            "0x428a2f98", "0x71374491", "0xb5c0fbcf", "0xe9b5dba5", "0x3956c25b", "0x59f111f1", "0x923f82a4", "0xab1c5ed5",
            "0xd807aa98", "0x12835b01", "0x243185be", "0x550c7dc3", "0x72be5d74", "0x80deb1fe", "0x9bdc06a7", "0xc19bf174",
            "0xe49b69c1", "0xefbe4786", "0x0fc19dc6", "0x240ca1cc", "0x2de92c6f", "0x4a7484aa", "0x5cb0a9dc", "0x76f988da",
            "0x983e5152", "0xa831c66d", "0xb00327c8", "0xbf597fc7", "0xc6e00bf3", "0xd5a79147", "0x06ca6351", "0x14292967",
            "0x27b70a85", "0x2e1b2138", "0x4d2c6dfc", "0x53380d13", "0x650a7354", "0x766a0abb", "0x81c2c92e", "0x92722c85",
            "0xa2bfe8a1", "0xa81a664b", "0xc24b8b70", "0xc76c51a3", "0xd192e819", "0xd6990624", "0xf40e3585", "0x106aa070",
            "0x19a4c116", "0x1e376c08", "0x2748774c", "0x34b0bcb5", "0x391c0cb3", "0x4ed8aa4a", "0x5b9cca4f", "0x682e6ff3",
            "0x748f82ee", "0x78a5636f", "0x84c87814", "0x8cc70208", "0x90befffa", "0xa4506ceb", "0xbef9a3f7", "0xc67178f2"));

    SHA256(String message){
        this.message = message;
        run();
    }

    private void run(){
        index = -1;
        for(int i = 0; i < message.length(); i++){
            if(message.charAt(i) > 64 && message.charAt(i) < 123){
                initialMessage.append(message.charAt(i));
                size += 8;
            }
        }
        message = String.valueOf(initialMessage);
        int numberOfBlocks;
        if(message.length() * 8 % 448 == 0){
            numberOfBlocks = message.length() * 8 % 448 + 2;
        } else {
            numberOfBlocks = message.length() * 8 / 448 + 1;
        }
        while(numberOfBlocks != 0){
            preprocessing();
            messageProcessing();
            compressionFunction();
            firstLoop = false;
            numberOfBlocks -= 1;
        }
    }

    private void preprocessing() {
        // converting plaintext in bits
        int i;
        messageInBits.clear();
        dividingInBlocks.clear();
        hexadecimalBlocks.clear();

        for (i = index + 1; i < message.length(); i++) {
            List<Integer> temp = new ArrayList<>();
            int asciiCode = message.charAt(i);
            while (asciiCode > 0) {
                temp.add(asciiCode % 2);
                asciiCode /= 2;
            }
            temp.add(0);

            Collections.reverse(temp);
            messageInBits.addAll(temp);
            if (i * 8 == 512 - 64 && message.length() * 8 >= 448) {
                break;
            }
        }
        int copyOfSize = size;
        index = i;

        // appending 10000...0
        if(firstLoop){
            messageInBits.add(1);
        }
        if (i * 8 != 512 - 64) {
            while (messageInBits.size() != 512 - 64) {
                messageInBits.add(0);
            }
        }

        // appending 64-bit representation of length
        if(size != 448 || !firstLoop){
            List<Integer> lengthInBits = new ArrayList<>();
            while (size > 0) {
                lengthInBits.add(size % 2);
                size /= 2;
            }
            size = copyOfSize;
            while (lengthInBits.size() != 64) {
                lengthInBits.add(0);
            }
            Collections.reverse(lengthInBits);

            messageInBits.addAll(lengthInBits);
        } else {
            while(messageInBits.size() != 512){
                messageInBits.add(0);
            }
        }

        // dividing in 32-bit blocks
        i = 0;
        int j = 0;
        while (i < messageInBits.size() / 32) {
            dividingInBlocks.add(new ArrayList<>());
            while (dividingInBlocks.get(i).size() < 32 && j < messageInBits.size()) {
                dividingInBlocks.get(i).add(messageInBits.get(j));
                j += 1;
            }
            i += 1;
        }

        // converting in hexadecimal
        for (i = 0; i < dividingInBlocks.size(); i++) {
            hexadecimalBlocks.add(new ArrayList<>());
            for (j = 0; j < dividingInBlocks.get(i).size(); j = j + 4) {
                int temp = 0;
                for (int k = 0; k < 4; k++) {
                    temp = (int) (dividingInBlocks.get(i).get(k + j) * Math.pow(2, 3 - k) + temp);
                }
                for (int l = 0; l < hexadecimalValues.size(); l++) {
                    if (temp == l) {
                        hexadecimalBlocks.get(i).add(hexadecimalValues.get(l));
                    }
                }
            }
        }

        System.out.println("\nThe hexadecimal values are: \n");
        for (i = 0; i < hexadecimalBlocks.size(); i++) {
            System.out.println("W[" + i + "] = " + hexadecimalBlocks.get(i));
        }
        System.out.print("\n");
    }

    private void messageProcessing(){
        for(int i = 16; i < 64; i++){
            dividingInBlocks.add(new ArrayList<>());
            List<Integer> s0 = new ArrayList<>(XOR(shifting(dividingInBlocks.get(i - 15), 3),
                    XOR(rightRotation(dividingInBlocks.get(i - 15), 7), rightRotation(dividingInBlocks.get(i - 15), 18))));
            List<Integer> s1 = new ArrayList<>(XOR(shifting(dividingInBlocks.get(i - 2), 10),
                    XOR(rightRotation(dividingInBlocks.get(i - 2), 17), rightRotation(dividingInBlocks.get(i - 2), 19))));
            dividingInBlocks.get(i).addAll(ADD(ADD(ADD(s0, s1), dividingInBlocks.get(i - 7)), dividingInBlocks.get(i - 16)));
            // System.out.println("W[" + i + "] = " + binaryToHex(dividingInBlocks.get(i)));
        }
    }

    private void compressionFunction(){
        if(firstLoop) {
            h0 = new StringBuilder(buffers.get(0));
            h1 = new StringBuilder(buffers.get(1));
            h2 = new StringBuilder(buffers.get(2));
            h3 = new StringBuilder(buffers.get(3));
            h4 = new StringBuilder(buffers.get(4));
            h5 = new StringBuilder(buffers.get(5));
            h6 = new StringBuilder(buffers.get(6));
            h7 = new StringBuilder(buffers.get(7));
        }
        a = hexToBinary(String.valueOf(h0));
        b = hexToBinary(String.valueOf(h1));
        c = hexToBinary(String.valueOf(h2));
        d = hexToBinary(String.valueOf(h3));
        e = hexToBinary(String.valueOf(h4));
        f = hexToBinary(String.valueOf(h5));
        g = hexToBinary(String.valueOf(h6));
        h = hexToBinary(String.valueOf(h7));
        System.out.println(binaryToHex(a));
        for(int i = 0; i < 64; i++){
            List<Integer> ki = hexToBinary(kt.get(i));
            List<Integer> S1 = new ArrayList<>(XOR(rightRotation(e, 25),
                    XOR(rightRotation(e, 6), rightRotation(e, 11))));
            List<Integer> ch = new ArrayList<>(XOR(AND(e, f), AND(NOT(e), g)));
            List<Integer> temp1 = new ArrayList<>(ADD(ADD(ADD(ADD(h, S1), ch), dividingInBlocks.get(i)), ki));
            List<Integer> S0 = new ArrayList<>(XOR(rightRotation(a, 22),
                    XOR(rightRotation(a, 2), rightRotation(a, 13))));
            List<Integer> maj = new ArrayList<>(XOR(XOR(AND(a, b), AND(b, c)), AND(a, c)));
            List<Integer> temp2 = new ArrayList<>(ADD(S0, maj));

            h.clear(); h.addAll(g);
            g.clear(); g.addAll(f);
            f.clear(); f.addAll(e);
            e.clear(); e.addAll(ADD(d, temp1));
            d.clear(); d.addAll(c);
            c.clear(); c.addAll(b);
            b.clear(); b.addAll(a);
            a.clear(); a.addAll(ADD(temp1, temp2));

            System.out.println("\n----------------- W" + i + " -----------------\n");
            System.out.println("A: " + binaryToHex(a));
            System.out.println("B: " + binaryToHex(b));
            System.out.println("C: " + binaryToHex(c));
            System.out.println("D: " + binaryToHex(d));
            System.out.println("E: " + binaryToHex(e));
            System.out.println("F: " + binaryToHex(f));
            System.out.println("G: " + binaryToHex(g));
            System.out.println("H: " + binaryToHex(h));
        }

        // add to the current hash value
        digest = new StringBuilder();
        StringBuilder h0Copy = h0, h1Copy = h1, h2Copy = h2, h3Copy = h3, h4Copy = h4, h5Copy = h5, h6Copy = h6, h7Copy = h7;
        h0 = new StringBuilder();
        h1 = new StringBuilder();
        h2 = new StringBuilder();
        h3 = new StringBuilder();
        h4 = new StringBuilder();
        h5 = new StringBuilder();
        h6 = new StringBuilder();
        h7 = new StringBuilder();
            h0.append(ADD(hexToBinary(String.valueOf(h0Copy)), a));
            h1.append(ADD(hexToBinary(String.valueOf(h1Copy)), b));
            h2.append(ADD(hexToBinary(String.valueOf(h2Copy)), c));
            h3.append(ADD(hexToBinary(String.valueOf(h3Copy)), d));
            h4.append(ADD(hexToBinary(String.valueOf(h4Copy)), e));
            h5.append(ADD(hexToBinary(String.valueOf(h5Copy)), f));
            h6.append(ADD(hexToBinary(String.valueOf(h6Copy)), g));
            h7.append(ADD(hexToBinary(String.valueOf(h7Copy)), h));
        digest.append(h0);
        digest.append(h1);
        digest.append(h2);
        digest.append(h3);
        digest.append(h4);
        digest.append(h5);
        digest.append(h6);
        digest.append(h7);

        digestList.clear();
        for(int i = 0; i < digest.length(); i++){
            if(digest.charAt(i) == '1'){
                digestList.add(1);
            } else if(digest.charAt(i) == '0'){
                digestList.add(0);
            }
        }
        binaryToHex(digestList);
        hexadecimalRepresentation.clear();
        System.out.print("\nThe digest in hexadecimal is: ");
        hexadecimalRepresentation.addAll(binaryToHex(digestList));
        for (String s : hexadecimalRepresentation) {
            System.out.print(s);
        }
    }

    private List<Integer> rightRotation(List<Integer> list, int shifts){
        List<Integer> copyOfList = new ArrayList<>(list);
        while (shifts != 0) {
            int lastElement = copyOfList.get(copyOfList.size() - 1);
            for(int j = copyOfList.size() - 1; j >= 1; j--){
                copyOfList.set(j, copyOfList.get(j - 1));
            }
            copyOfList.set(0, lastElement);
            shifts -= 1;
        }
        return copyOfList;
    }

    private List<Integer> shifting(List<Integer> list, int shifts){
        List<Integer> copyOfList = new ArrayList<>(list);

        while (shifts != 0) {
            for(int j = copyOfList.size() - 1; j >= 1; j--){
                copyOfList.set(j, copyOfList.get(j - 1));
            }
            copyOfList.set(0, 0);
            shifts -= 1;
        }
        return copyOfList;
    }

    private List<Integer> hexToBinary(String hexNumber){
        List<Integer> binaryRepresentation = new ArrayList<>();

        for(int i = 2; i < hexNumber.length(); i++){
            for (int j = 0; j < hexadecimalValues.size(); j++) {
                List<Integer> binaryTemp = new ArrayList<>();
                if (String.valueOf(hexNumber.charAt(i)).equals(hexadecimalValues.get(j))) {
                    int index = j;
                    while (index > 0) {
                        binaryTemp.add(index % 2);
                        index /= 2;
                    }
                    while(binaryTemp.size() < 4){
                        binaryTemp.add(0);
                    }
                    Collections.reverse(binaryTemp);
                    binaryRepresentation.addAll(binaryTemp);
                    break;
                }
            }
        }
        return binaryRepresentation;
    }

    private List<Integer> XOR(List<Integer> list1, List<Integer> list2){
        List<Integer> firstList = new ArrayList<>(list1);
        List<Integer> secondList = new ArrayList<>(list2);

        List<Integer> result = new ArrayList<>();

        for(int i = 0; i < 32; i++){
            int applyingXOR = (firstList.get(i) + secondList.get(i)) % 2;
            result.add(applyingXOR);
        }

        return result;
    }

    private List<Integer> ADD(List<Integer> list1, List<Integer> list2){
        List<Integer> firstList = new ArrayList<>(list1);
        List<Integer> secondList = new ArrayList<>(list2);
        List<Integer> result = new ArrayList<>();
        Collections.reverse(firstList);
        Collections.reverse(secondList);
        int carry = 0, sum;

        for(int i = 0; i < 32; i++){
            sum = firstList.get(i) + secondList.get(i) + carry;
            carry = 0;
            if(sum > 1){
                result.add(sum % 2);
                if(firstList.get(i) != 0 || secondList.get(i) != 0){
                    carry = 1;
                }
            } else {
                result.add(sum);
            }
        }
        Collections.reverse(result);
        return result;
    }

    private List<Integer> AND(List<Integer> list1, List<Integer> list2){
        List<Integer> firstList = new ArrayList<>(list1);
        List<Integer> secondList = new ArrayList<>(list2);
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < 32; i++){
            result.add(firstList.get(i) * secondList.get(i));
        }
        return result;
    }

    private List<Integer> NOT(List<Integer> myList){
        List<Integer> list = new ArrayList<>(myList);
        List<Integer> result = new ArrayList<>();
        for (Integer integer : list) {
            if (integer == 0) {
                result.add(1);
            } else {
                result.add(0);
            }
        }
        return result;
    }

    private List<String> binaryToHex(List<Integer> hexNumber){
        List<Integer> number = new ArrayList<>(hexNumber);
        List<String> result = new ArrayList<>();

        for(int j = 0; j < number.size(); j = j + 4){
            int temp = 0;
            for(int k = 0; k < 4; k++){
                if(number.get(k + j) == 0 || number.get(k + j) == 1){
                    temp = (int) (number.get(k + j) * Math.pow(2, 3 - k) + temp);
                }
            }
            for(int l = 0; l < hexadecimalValues.size(); l++){
                if(temp == l){
                    result.add(String.valueOf(hexadecimalValues.get(l)));
                }
            }
        }

        return result;
    }

    private StringBuilder binaryToHex2(StringBuilder hexNumber){
            StringBuilder number = new StringBuilder(hexNumber);
            StringBuilder result = new StringBuilder();

            for(int j = 0; j < number.length(); j = j + 4){
                int temp = 0;
                for(int k = 0; k < 4; k++){
                    if(number.charAt(k + j) == 0 || number.charAt(k + j) == 1){
                        temp = (int) (number.charAt(k + j) * Math.pow(2, 3 - k) + temp);
                    }
                }
                for(int l = 0; l < hexadecimalValues.size(); l++){
                    if(temp == l){
                        result.append(hexadecimalValues.get(l));
                    }
                }
            }
            return result;
    }

    public void hammingDistance(SHA256 object1) {
        int distance = 0;

        for (int i = 0; i < object1.digest.length(); i++) {
            if (object1.digest.charAt(i) != this.digest.charAt(i)) {
                distance += 1;
            }
        }
        System.out.println("\nThe Hamming distance is: " + distance);
    }

    public void birthdayAttack(){
        List<String> copyOfDigest = new ArrayList<>();

        for(int i = 0; i < digest.length(); i++){
            if(String.valueOf(digest.charAt(i)).equals("1") || String.valueOf(digest.charAt(i)).equals("0")){
                copyOfDigest.add(String.valueOf(digest.charAt(i)));
            }
            if(copyOfDigest.size() == 32){
                break;
            }
        }
        System.out.println("\nThe digest in 32 bits is: " + copyOfDigest + " size: " + copyOfDigest.size());
    }
}