import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new LFSR(8, 1, 5, 6);
        // new LFSR(16, 9, 5, 10); // the period isn't maximum
        // new LFSR(8, 1, 2, 3); // the period isn't maximum
        // new LFSR(16, 2, 3, 5);

//        new BlumBlumShub(255);
//        new Jacobi(255);
//        new RC4("57206702760", "1234", 256);
    }
}
