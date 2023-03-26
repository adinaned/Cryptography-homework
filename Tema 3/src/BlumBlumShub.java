
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BlumBlumShub {
    private final int bitSequenceLength;

    public BlumBlumShub(int bitSequenceLength) throws IOException {
        System.out.println("------------------------ Blum Blum Shub Generator ------------------------\n");
        this.bitSequenceLength = bitSequenceLength;
        this.blumBlumShub();
    }

    private void blumBlumShub() throws IOException {
        Random rand = new Random();

        BigInteger p = BigInteger.probablePrime(1024, rand);
        BigInteger q = BigInteger.probablePrime(1024, rand);

        int pRemainder = p.mod(BigInteger.valueOf(4)).compareTo(BigInteger.valueOf(3));
        int qRemainder = p.mod(BigInteger.valueOf(4)).compareTo(BigInteger.valueOf(3));

        while (pRemainder != 0 || qRemainder != 0) {
            if (pRemainder != 0) {
                p = BigInteger.probablePrime(1024, rand);
                pRemainder = p.mod(BigInteger.valueOf(4)).compareTo(BigInteger.valueOf(3));
            }
            if (qRemainder != 0) {
                q = BigInteger.probablePrime(1024, rand);
                qRemainder = q.mod(BigInteger.valueOf(4)).compareTo(BigInteger.valueOf(3));
            }
        }
        System.out.println("p -> " + p + "\n" + "q -> " + q);

        BigInteger n = p.multiply(q);
        BigInteger seed = new BigInteger("0");

        while (!seed.gcd(n).equals(BigInteger.valueOf(1))) {
            seed = BigInteger.probablePrime(1024, rand);
        }

        System.out.println("Seed -> " + seed);

        ArrayList<Integer> bitSequence = new ArrayList<>();

        BigInteger x = seed.pow(2).mod(n);
        int numarulDe1 = 0, numarulDe0 = 0;

        File file = new File("D:\\Semestrul 2\\Criptografie\\Tema 2\\src\\BBS.zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        ZipEntry entry = new ZipEntry("outputBits.txt");
        out.putNextEntry(entry);

        byte[] dataInBits;
        long startTime = System.nanoTime();

        for (int i = 0; i < bitSequenceLength; i++) {
            x = x.multiply(x).mod(n);
            int verify = x.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(0));
            if (verify == 0) {
                bitSequence.add(0);
                numarulDe0 += 1;
                dataInBits = bitSequence.get(i).toString().getBytes();
                System.out.print(bitSequence.get(i));
                out.write(dataInBits, 0, dataInBits.length);
            } else {
                bitSequence.add(1);
                numarulDe1 += 1;
                dataInBits = bitSequence.get(i).toString().getBytes();
                System.out.print(bitSequence.get(i));
                out.write(dataInBits, 0, dataInBits.length);
            }
        }
        long endTime = System.nanoTime();
        out.closeEntry();
        out.close();

        System.out.println("\n" + "Numarul de 0 este egal cu -> " + numarulDe0 + "\n" + "Numarul de 1 este egal cu -> " + numarulDe1);
        System.out.println("The execution time has  " + ((endTime - startTime) / Math.pow(10, 9)) + " seconds\n");
    }
}
