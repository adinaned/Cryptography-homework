package com.company;

import java.math.BigInteger;
import java.sql.SQLOutput;
import java.util.*;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger d;
    private BigInteger phi;
    private BigInteger e = BigInteger.valueOf(-1);
    private BigInteger m = BigInteger.valueOf(0);
    private BigInteger encryption;
    private final List<BigInteger> continuousFraction = new ArrayList<>();
    private List<BigInteger> qList = new ArrayList<>();
    private boolean found = false;

    RSA(String plaintext){
        for(int i = 0; i < plaintext.length(); i++){
            if(plaintext.charAt(i) < 100) {
                m = m.multiply(BigInteger.valueOf(100)).add(BigInteger.valueOf(plaintext.charAt(i)));
            } else {
                m = m.multiply(BigInteger.valueOf(1000)).add(BigInteger.valueOf(plaintext.charAt(i)));
            }
        }
        System.out.println("The plaintext is: " + plaintext + " => " + m);
        run();
    }

    private void run(){
        generatePrimeNumbers();
        generateKeys();
        System.out.println("\n---------------------- Encryption ----------------------");
        encryption();
        System.out.println("\n---------------------- Decryption ----------------------");
        decryption();
        System.out.println("\n---------------------- Decryption using CRT ----------------------");
        CRT();
        System.out.println("\n---------------------- Wiener Attack ----------------------");
        wienerAttack();
    }

    private void generatePrimeNumbers(){
        // generate two random prime numbers
        Random rand = new Random();

        p = BigInteger.probablePrime(1024, rand);
        q = BigInteger.probablePrime(1024, rand);

        System.out.println("p = " + p + "\n" + "q = " + q);
    }

    private void generateKeys(){
        // first part of public key
        n = p.multiply(q);
        System.out.println("n = " + n);

        // phi(n)
        phi = (p.subtract(BigInteger.valueOf(1))).multiply(q.subtract(BigInteger.valueOf(1)));
        System.out.println("phi = " + phi);

        // find e
        BigInteger primeCounter = BigInteger.valueOf(2);

        while(Objects.equals(e, BigInteger.valueOf(-1))){
            if(phi.mod(primeCounter).equals(BigInteger.valueOf(0))){
                primeCounter = primeCounter.add(BigInteger.valueOf(1));
            } else {
                e = primeCounter;
            }
        }
        System.out.println("e = " + e);

        // private key
        d = e.modInverse(phi);
        System.out.println("d = " + d);
    }

    private void encryption(){
        encryption = m.modPow(e, n);
        System.out.println("The encrypted message is: " + encryption);
    }

    private void decryption(){
        long startTime = System.nanoTime();

        BigInteger decryption = encryption.modPow(d, n);
        System.out.println("The decrypted text is: " + decryption);

        long endTime = System.nanoTime();

        System.out.println("The execution time has  " + ((endTime - startTime) / Math.pow(10, 9)) + " seconds\n");
    }

    private void CRT(){
        long startTime = System.nanoTime();
        BigInteger dp = d.mod(p.subtract(BigInteger.valueOf(1)));
        BigInteger dq = d.mod(q.subtract(BigInteger.valueOf(1)));
        BigInteger qinv = q.modInverse(p);

        BigInteger m1 = new BigInteger(String.valueOf(encryption)).modPow(dp, p);
        BigInteger m2 = new BigInteger(String.valueOf(encryption)).modPow(dq, q);
        BigInteger h = qinv.multiply(m1.subtract(m2)).mod(p);
        BigInteger decryption = m2.add(h.multiply(q)).mod(p.multiply(q));

        long endTime = System.nanoTime();

        System.out.println("The decrypted text using CRT is: " + decryption);
        System.out.println("The execution time has  " + ((endTime - startTime) / Math.pow(10, 9)) + " seconds\n");
    }

    private void wienerAttack(){
        qList.addAll(continuousFraction(e, n));
        List<BigInteger> alpha = new ArrayList<>(
                Arrays.asList(qList.get(0), qList.get(0).multiply(qList.get(1)).add(BigInteger.valueOf(1))));
        List<BigInteger> beta = new ArrayList<>(
                Arrays.asList(BigInteger.valueOf(1), qList.get(1)));
        BigInteger l = null;

        int i = 0;

        while(!found){
            if(i > 1){
                alpha.add(qList.get(i).multiply(alpha.get(i - 1)).add(alpha.get(i - 2)));
                beta.add(qList.get(i).multiply(beta.get(i - 1)).add(beta.get(i - 2)));
            }
            l = alpha.get(i);
            System.out.println(l);
            d = beta.get(i);
            checkCriteria(l, d, e, n);
            i += 1;
        }
        System.out.println(l + "\n" + d + "\n" + e + "\n" + n);
    }

    private List<BigInteger> continuousFraction(BigInteger e, BigInteger n) {
        BigInteger quotient = e.divide(n);
        continuousFraction.add(quotient);
        while (!n.multiply(quotient).equals(e)) {
            BigInteger aux = e;
            e = n;
            n = aux.mod(n);

            quotient = e.divide(n);
            continuousFraction.add(quotient);
        }
        System.out.println(continuousFraction);
        return continuousFraction;
    }

    private void checkCriteria(BigInteger l, BigInteger d, BigInteger e, BigInteger n){
        if(l.equals(BigInteger.valueOf(0)) || d.equals(BigInteger.valueOf(0))){
            found = false;
        }

        if(d.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))){
            found = false;
        }

        BigInteger intermediary = e.multiply(d).subtract(BigInteger.valueOf(1));
        if(!intermediary.mod(l).equals(BigInteger.valueOf(0))){
            found = false;
        }

        phi = intermediary.divide(l);
        BigInteger a = BigInteger.valueOf(1);
        BigInteger b = BigInteger.valueOf(-1).multiply(n.subtract(phi).add(BigInteger.valueOf(1)));
        BigInteger delta = b.modPow(BigInteger.valueOf(2), BigInteger.valueOf(1)).subtract(BigInteger.valueOf(4).multiply(a).multiply(n));

        if(delta.compareTo(BigInteger.valueOf(0)) < 0){
            found = false;
        }

        BigInteger sqrtDelta = delta.sqrt();
        if(sqrtDelta.multiply(sqrtDelta).equals(delta)){
            q = sqrtDelta.subtract(b);
            p = b.negate().subtract(sqrtDelta);

            if(p.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0)) && q.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0)) && p.compareTo(BigInteger.valueOf(0)) > 0 && q.compareTo(BigInteger.valueOf(0)) > 0){
                p = p.divide(BigInteger.valueOf(2));
                q = q.divide(BigInteger.valueOf(2));
                found = true;
            }
        }
    }
}
