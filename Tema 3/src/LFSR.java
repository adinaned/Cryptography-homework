
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LFSR {
    private final List<Integer> initialState = new ArrayList<>();
    private List<Integer> intermediateState = new ArrayList<>();
    private List<Integer> feedbackList = new ArrayList<>();
    private int numberOfBits;
    private int maxPeriod;
    private int numberOf0 = 0, numberOf1 = 0;

    public LFSR(int numberOfBits, int first, int second, int third) {
        this.numberOfBits = numberOfBits;
        this.feedbackList.add(first);
        this.feedbackList.add(second);
        this.feedbackList.add(third);
        this.run();
    }

    private void run(){
        System.out.println("------------------------ LFSR Generator ------------------------\n");
        generateInitialState();
        long startTime = System.nanoTime();
        iterations();
        long endTime = System.nanoTime();
        System.out.println("The execution time has  " + ((endTime - startTime) / Math.pow(10, 9)) + " seconds\n");
    }

    private void generateInitialState(){
        List<Integer> bits = Arrays.asList(0, 1);

        Random rand = new Random();
        maxPeriod = (int) (Math.pow(2, numberOfBits));

        System.out.println("The initial sequence has " + numberOfBits + " bits.");

        Generate:
        while(true) {
            while (numberOfBits != 0) {
                int bitGenerated = bits.get(rand.nextInt(bits.size()));
                initialState.add(bitGenerated);
                intermediateState.add(bitGenerated);
                numberOfBits -= 1;
            }
            numberOfBits = initialState.size();

            for(int i = 0; i < numberOfBits; i++){
                if(initialState.get(i) != 0){
                    break Generate;
                }
            }
        }

        System.out.println(Arrays.toString(initialState.toArray()));
        System.out.println("The maximum period is " + (maxPeriod - 1));
    }

    private void iterations(){
        int firstElement;
        int iterations = maxPeriod - 1;
        List<Integer> bitSequence = new ArrayList<>();

        while(iterations != 0){
            firstElement = (intermediateState.get(feedbackList.get(0) - 1) +
                    intermediateState.get(feedbackList.get(1) - 1) +
                    intermediateState.get(feedbackList.get(2) - 1) +
                    intermediateState.get(numberOfBits - 1)) % 2;

            if(firstElement == 0){
                numberOf0 += 1;
            } else {
                numberOf1 += 1;
            }
            bitSequence.add(firstElement);

            for(int j = numberOfBits - 1; j >= 1; j--){
                intermediateState.set(j, intermediateState.get(j - 1));
            }
            intermediateState.set(0, firstElement);

            System.out.println(Arrays.toString(intermediateState.toArray()));
            iterations -= 1;

            if(verify((ArrayList<Integer>) intermediateState) && iterations != 0){
                System.out.println("The initial state doesn't have a maximum period. The period is " + (maxPeriod - iterations - 1));
                return;
            }
        }
        System.out.println("The maximum period has been reached => the initial state has a maximum period.");
        System.out.println("The maximum period is " + (maxPeriod - iterations - 1));
        System.out.println("The bit sequence is " + Arrays.toString(bitSequence.toArray()));
        System.out.println("The number of 0 is " + numberOf0);
        System.out.println("The number of 1 is " + numberOf1);
    }

    private boolean verify(ArrayList<Integer> intermediateState){
        return initialState.equals(intermediateState);
    }
}

