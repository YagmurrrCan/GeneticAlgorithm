import java.io.IOException;
import java.util.Arrays;

public class Chromosome {

    private int[] genes;
    private boolean isFitnessChanged=true;
    private double fitness=0;
    private int genesLength = 75;


    public Chromosome(){

        genes = new int[genesLength];
    }

    public int[] getGenes() {
        isFitnessChanged = true;
        return genes;
    }

    public double getFitness() throws IOException {
        if(isFitnessChanged){
            fitness = recalculateFitness(); // get ingredients and user
            isFitnessChanged = false;
        }
        return fitness;
    }

    public Chromosome initializeChromosome(){
        // System.out.println("INSIDE INITIALIZE CHROMOSOME\n");

        for(int i=0;i<genes.length;i++){
            if(Math.random()>0.8) genes[i]=1;
            else genes[i]=0;
        }
        //   System.out.println("CHROMOSOME INITIALIZED\n");

        return this;

    }



    public String toString(){
        return Arrays.toString(this.genes);
    }


    public double recalculateFitness() throws IOException {
        //  System.out.println("INSIDE CALCULATE FITNESS\n");


        //    System.out.println("INSIDE FITNESS CALCULATION LOOP\n");
        for(int i=0;i<genes.length;i++){

            if (genes[i] == 1) {
                //     System.out.println("INSIDE IFFFFF CALCULATION LOOP\n");



                //   System.out.println("IF GENE IS 1\n");
            }

        }

    }
}