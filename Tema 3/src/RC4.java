
import java.util.ArrayList;
import java.util.List;

public class RC4 {
    private final String plainText;
    private final String key;
    private final int iterations;
    private List<String> plainTextInBits = new ArrayList<>();

    public RC4(String plainText, String key, int iterations) {
        System.out.println("------------------------ RC4 Generator ------------------------\n");
        this.plainText = plainText;
        this.key = key;
        this.iterations = iterations;
        convertToByte();
        // keySchedulingAlgorithm();
        // prga();
    }

    private void convertToByte(){
        for(int i = 0; i < plainText.length(); i++){
            plainTextInBits.add(Integer.toBinaryString(plainText.charAt(i)));
            System.out.println(plainText.charAt(i) + " -> " + plainTextInBits.get(i));
        }
    }

    private void keySchedulingAlgorithm(){
        int j = 0;
        for(int i = 0; i < iterations - 1; i++){
            j = (j + (int) plainText.charAt(i) + (int) key.charAt(i % plainText.length())) % iterations;
            swap(plainText.charAt(i), plainText.charAt(j));
        }
        System.out.println(plainText.toString());
    }

    private void prga(){
        int i = 0, j = 0, m = -1;
        while(m != 0){
            i = (i + 1) % iterations;
            j = (j + (int) plainText.charAt(i)) % iterations;
            swap(plainText.charAt(i), plainText.charAt(j));
            m = plainText.charAt((plainText.charAt(i) + plainText.charAt(j)) % iterations);
        }
    }


    private void swap(char word1, char word2){
        char aux = word1;
        word1 = word2;
        word2 = aux;
    }
}
