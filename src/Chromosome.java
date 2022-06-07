import java.util.Arrays;

public class Chromosome {
    private int fitness;
    private boolean isFitnessChanged=true;
    private int[] genes;
    private GeneticAlgorithm geneticAlgorithm;

    public Chromosome(GeneticAlgorithm geneticAlgorithm){

        //  genes = new int[geneticAlgorithm.getTargetChromosomeGenes().length];
        this.geneticAlgorithm = geneticAlgorithm;
    }

    public Chromosome initializeChromosome(){
        // DEMO (CHANGE LATER)
        for(int i=0;i<genes.length;i++){
            if(Math.random()>0.5) genes[i]=1;
            else genes[i]=0;
        }
        return this;
    }

    public int[] getGenes(){
        isFitnessChanged = true;
        return genes;
    }

    public int getFitness(){
        if(isFitnessChanged){
            fitness = recalculateFitness();
            isFitnessChanged = false;
        }
        return fitness;
    }

    public String toString(){
        return Arrays.toString(this.genes);
    }

    public int recalculateFitness(){
        int chromosomeFitness = 0;

        for(int i=0;i<genes.length;i++){
            // if(genes[i] == geneticAlgorithm.getTargetChromosomeGenes()[i]) chromosomeFitness++;
        }

        return chromosomeFitness;
    }


}