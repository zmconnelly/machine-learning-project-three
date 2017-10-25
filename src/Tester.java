
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Tester class provides a means to initialize and train neural networks
 * Additionally a five by two cross fold validation function is provided to test and compare networks
 */
public class Tester {

    public static void main(String[] args) {
        INeuralNetwork MLP = NetworkFactory.buildNewNetwork(NetworkType.MultiLayerPerceptron);
        INeuralNetworkTrainer trainer = NetworkFactory.buildNetworkTrainer(NetworkTrainerType.BPNetworkTrainer);

        assert MLP != null;
        assert trainer != null;

        trainer.train(MLP, null);

        crossValidate();
    }

    // Execute a 5x2 cross validation for both networks computing the mean and standard deviation of their errors
    public static void crossValidate() {
//        IFunctionApproximator FFN;
//
//        List<Double> ffnErrors = new ArrayList<>();
//        List<Double> rbfErrors = new ArrayList<>();
//
//        for (int k = 0; k < 5; k++) {
//            Collections.shuffle(dataSet);
//            List<Sample> set1 = dataSet.subList(0, (dataSet.size() / 2));
//            List<Sample> set2 = dataSet.subList((dataSet.size() / 2), dataSet.size());
//
//            FFN = buildNewNetwork(NetworkType.FeedForwardNetwork);
//       //     RBN = buildNewNetwork(NetworkType.RadialBasisNetwork);
//
//            ffnErrors.addAll(computeFold(set1, set2, FFN));
//     //       rbfErrors.addAll(computeFold(set1, set2, RBN));
//
//            FFN = buildNewNetwork(NetworkType.FeedForwardNetwork);
//         //   RBN = buildNewNetwork(NetworkType.RadialBasisNetwork);
//
//            ffnErrors.addAll(computeFold(set2, set1, FFN));
//         //   rbfErrors.addAll(computeFold(set2, set1, RBN));
//        }
//
//        double mean = calcMean(ffnErrors);
//        double SD = calcStandardDeviation(mean, ffnErrors);
//        printStats(mean, SD, "Feed Forward");
//
//        mean = calcMean(rbfErrors);
//        SD = calcStandardDeviation(mean, rbfErrors);
//        printStats(mean, SD, "Radial Basis");
    }

    private static List<Double> computeFold(List<Sample> trainSet, List<Sample> testSet, IFunctionApproximator network) {
        network.train(trainSet);
        return getApproximationErrors(testSet, network);
    }

    // Iterates through testing set and calculates the approximated values and the error of the samples of the supplied network
    private static List<Double> getApproximationErrors(List<Sample> testSet, IFunctionApproximator network) {
        List<Double> totalError = new ArrayList<>(testSet.size());
        for (Sample sample : testSet) {
            // Get the network's approximation
            double[] networkOutput = network.approximate(sample.inputs);

            // Add the error for each sample to the total error
            totalError.add(IntStream
                    .range(0, networkOutput.length)
                    .mapToDouble(j -> Math.abs(networkOutput[j] - sample.outputs[j]))
                    .sum());
        }
        return totalError;
    }


    // Calculates the mean of all the samples errors
    private static double calcMean(List<Double> totalError) {
        return totalError
                .stream()
                .reduce(0.0, Double::sum) / totalError.size();
    }

    // Calculates the standard deviation of the provided errors
    private static double calcStandardDeviation(double average, List<Double> totalError) {
        return Math.sqrt(totalError
                .stream()
                .mapToDouble(aDouble -> Math.pow((aDouble - average), 2) / totalError.size())
                .sum());
    }

    // Writes the mean ans standard deviation to std out
    private static void printStats(double mean, double standardDeviation, String networkType) {
        System.out.println("Network type: " + networkType);
        System.out.println("-------------------------------");
        System.out.println("Mean error:         " + mean);
        System.out.println("Standard Deviation: " + standardDeviation);
    }

    private static void writeFile(String outputPath, List<String> lines) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            writer.write(lines.stream()
                    .reduce((sum, currLine) -> sum + "\n" + currLine)
                    .orElse(""));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

