import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Chromosome {

    Main main = new Main();

    private int[] genes;
    boolean isFitnessChanged = true;
    private double fitness = -1;
    private int genesLength = 75;

    public Chromosome() {
        genes = new int[genesLength];
    }

    public int getGenesLength() {
        return genesLength;
    }

    public int[] getGenes() {
        isFitnessChanged = true;
        return genes;
    }

    public double getFitness() throws IOException {
        if(isFitnessChanged) {
            fitness = calculateFitness();
            isFitnessChanged = false;
        }
        return fitness;
    }

    public Chromosome initializeChromosome() {;

        for(int i=0; i < genes.length; i++){
            if(Math.random() > 0.5) {
                genes[i]=1;
            }
            else {
                genes[i] = 0;
            }
        }

        return this;
    }


    public String toString(){
        return Arrays.toString(this.genes);
    }


    public double calculateFitness() throws IOException {

         return 0;
    }



}