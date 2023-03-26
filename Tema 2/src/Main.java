import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        blumBlumShub();
        jacobiGenerator();
    }

    private static void blumBlumShub() throws IOException {
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
        int bitSequenceLength = 1000000;

        BigInteger x = seed.pow(2).mod(n);
        int numarulDe1 = 0, numarulDe0 = 0;

        File file = new File("D:\\Semestrul 2\\Criptografie\\Tema 2\\src\\BBS.zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        ZipEntry entry = new ZipEntry("outputBits.txt");
        out.putNextEntry(entry);

        byte[] dataInBits;

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

        out.closeEntry();
        out.close();

        System.out.println("\n" + "Numarul de 0 este egal cu -> " + numarulDe0 + "\n" + "Numarul de 1 este egal cu -> " + numarulDe1);
    }

    private static void jacobiGenerator() throws IOException {
        Random rand = new Random();
        int numberOfRepetions = 5000, numberOf0 = 0, numberOf1 = 0;
        ArrayList<Integer> bitSequence = new ArrayList<>();

        while (numberOfRepetions != 0) {
            numberOfRepetions -= 1;

            BigInteger a = new BigInteger(1024, rand);
            BigInteger n = new BigInteger(1024, rand);

            while (n.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
                n = new BigInteger(1024, rand);
            }

            // System.out.println("a -> " + a + "\n" + "n -> " + n);

            BigInteger remainder;
            int result;

            if (!n.equals(BigInteger.valueOf(0)) && n.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(1))) {
                a = a.mod(n);
                result = 1;
                while (!a.equals(BigInteger.valueOf(0))) {
                    while (a.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
                        a = a.divide(BigInteger.valueOf(2));
                        remainder = n.mod(BigInteger.valueOf(8));

                        if (remainder.equals(BigInteger.valueOf(3)) || remainder.equals(BigInteger.valueOf(5))) {
                            result = -result;
                        }
                    }
                    BigInteger aux;
                    aux = n;
                    n = a;
                    a = aux;
                    if (a.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) && n.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                        result = -result;
                    }
                    a = a.mod(n);
                }

                if (result == 1) {
                    bitSequence.add(1);
                    numberOf1 += 1;
                } else {
                    bitSequence.add(0);
                    numberOf0 += 1;
                }
            }
        }

        File file = new File("D:\\Semestrul 2\\Criptografie\\Tema 2\\src\\Jacobi.zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        ZipEntry entry = new ZipEntry("outputBits.txt");
        out.putNextEntry(entry);

        byte[] dataInBits;

        for (int i = 0; i < bitSequence.size(); i++) {
            dataInBits = bitSequence.get(i).toString().getBytes();
            System.out.print(bitSequence.get(i));
            out.write(dataInBits, 0, dataInBits.length);
        }

        out.closeEntry();
        out.close();
        System.out.println("\nNumarul de 0 -> " + numberOf0 + "\nNumarul de 1 -> " + numberOf1);
    }
}
