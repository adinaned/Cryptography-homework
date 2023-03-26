
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Jacobi {
    private int numberOfRepetitions;

    public Jacobi(int numberOfRepetitions) throws IOException {
        System.out.println("------------------------ Jacobi Generator ------------------------\n");
        this.numberOfRepetitions = numberOfRepetitions;
        this.jacobiGenerator();
    }

    private void jacobiGenerator() throws IOException {
        Random rand = new Random();
        int numberOf0 = 0, numberOf1 = 0;
        ArrayList<Integer> bitSequence = new ArrayList<>();

        long startTime = System.nanoTime();
        while (numberOfRepetitions != 0) {
            numberOfRepetitions -= 1;

            BigInteger a = new BigInteger(1024, rand);
            BigInteger n = new BigInteger(1024, rand);

            while (n.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
                n = new BigInteger(1024, rand);
            }

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
        long endTime = System.nanoTime();

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
        System.out.println("The execution time has  " + ((endTime - startTime) / Math.pow(10, 9)) + " seconds\n");
    }
}
