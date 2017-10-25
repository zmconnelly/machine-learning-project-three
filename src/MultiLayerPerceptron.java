import java.util.ArrayList;
import java.util.List;

public class MultiLayerPerceptron implements INeuralNetwork {

    private List<Layer> layers;
    private IActivationFunction activationFunction;

    MultiLayerPerceptron(IActivationFunction activationFunction, int[] networkDimensions) {
        this.activationFunction = activationFunction;
        this.initializeNetwork(networkDimensions);
    }

    public double[] execute(double[] inputs) {
        if (inputs.length != this.layers.get(0).size) {
            throw new IllegalArgumentException("Input/Network size mismatch!");
        }

        for (Layer layer : this.layers) {
            inputs = layer.execute(inputs, true);
        }

        return inputs;
    }

    public Layer getLayer(int index) {
        return this.layers.get(index);
    }

    public int getSize() {
        return this.layers.size();
    }

    public double computeActivation(double input) {
        return this.activationFunction.compute(input);
    }

    public double computeActivationDerivative(double input) {
        return this.activationFunction.computeDerivative(input);
    }

    private void initializeNetwork(int[] networkDimensions) {
        if (networkDimensions == null || networkDimensions.length < 2) {
            throw new IllegalArgumentException("Invalid network configuration!");
        }

        this.layers = new ArrayList<>(networkDimensions.length);
        for (int i = 1; i < networkDimensions.length; i++) {
            this.layers.add(new Layer(networkDimensions[i], networkDimensions[i - 1], this.activationFunction));
        }
    }
}