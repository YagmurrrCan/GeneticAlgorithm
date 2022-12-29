import model.Solution;

import java.io.IOException;
import java.util.*;

//Individuals
public class Chromosome {

    Main main = new Main();

    private int[] genes;
    private double fitness = -1;  //0
    boolean isFitnessChanged = true;
    private int geneLength = 75;
    private int numbOfConflicts = 0;

    public Chromosome() {

        //Initialization
        // geneLength belirtmezsem => this.geneLength = geneLength;
        this.genes = new int[geneLength];

        Random rn = new Random();

        //Set genes randomly for each individual
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rn.nextInt() % 2);
        }

        fitness = 0;
    }

    public int[] getGenes() {
        isFitnessChanged = true;
        return genes;
    }

    public double getFitness() throws IOException {
        if(isFitnessChanged){
            fitness = calculateFitness();
            isFitnessChanged = false;
        }
        return fitness;
    }


    public int getNumbOfConflicts() {
        return numbOfConflicts;
    }

    public void setNumbOfConflicts(int numbOfConflicts) {
        this.numbOfConflicts = numbOfConflicts;
    }

    public Chromosome initializeChromosome(){

        for(int i = 0; i < genes.length; i++) {
            if( Math.random() > 0.5 ) genes[i]=1;
            else genes[i]=0;
        }
        return this;
    }

    public String toString(){
        return Arrays.toString(this.genes);
    }

    //Calculate fitness
    public double calculateFitness() throws IOException {
        // Implement the logic to calculate the fitness value for this chromosome.
        // The higher the fitness value, the more suitable the chromosome is for
        // the problem at hand.
        double totalFitness = 0;

        for (int i = 0; i < genes.length; i++) {
           label:
            if(genes[i] == 1) {
                if( totalFitness + Main.checkHardConstraints().get(i)){
                Random rand = new Random();

                // nextInt as provided by Random is exclusive of the top value so you need to add 1

                int randomPosition = rand.nextInt(geneLength);
                genes[randomPosition]=0;
                totalFitness=0;
                i=0;
                break label;
            }
                double totalFitness = hardConstraintsFitness + softConstraintsFitness;
            }
        }
        return totalFitness;
    }

    /* Bu crossover metodu, rastgele bir kesişim (crossover) noktası seçer ve iki Chromosome nesnesinin genlerini bu noktada kesiştirir. */
    public Chromosome crossover(Chromosome other) {
        // Perform crossover with another chromosome to produce new offspring.
        // This might involve randomly selecting genes from each parent
        // chromosome to create the genes for the new offspring.
        int geneLength = this.geneLength;

        Chromosome offspring = new Chromosome();
        int[] offspringGenes = new int[geneLength];

        Random random = new Random();
        int crossoverPoint = random.nextInt(geneLength);

        for (int i = 0; i < geneLength; i++) {
            if (i < crossoverPoint) {
                offspringGenes[i] = this.genes[i];
            } else {
                offspringGenes[i] = other.genes[i];
            }
        }

        offspring.setGenes(offspringGenes);

        return offspring;
    }

    public void mutate() {
        // Perform mutation on this chromosome to introduce new genetic
        // diversity. This might involve randomly altering one or more of the
        // genes in the chromosome.
        Random random = new Random();

        for (int i = 0; i < geneLength; i++) {
            if (random.nextDouble() <= 0.1) {
                genes[i] = random.nextInt(geneLength);
            }
        }
    }

    private void setGenes(int[] offspringGenes) {
    }

    public int getGeneLength() {
        return geneLength;
    }

    public void setGeneLength(int geneLength) {
        this.geneLength = geneLength;
    }
}