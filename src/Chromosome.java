import model.Solution;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

//Individuals
public class Chromosome {

    Main main = new Main();

    private int[] genes;
    private double fitness = -1;  //0 olabilir
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
        double fitness = 0;

        /*
        assignRooms metodu, öğrenci grubunun sınav salonlarına atama işlemini gerçekleştirir ve atamanın nasıl yapıldığını gösteren bir List<Solution> nesnesi döndürür.

isValid metodu ise, bir Solution nesnesinin geçerli olup olmadığını kontrol eder. Eğer Solution geçerli ise, true değerini döndürür; aksi takdirde false değerini döndürür.
         */
/*
        // Öğrenci grubunun sınav salonlarına atamasını yap
        List<Solution> solutions = assignRooms(genes);
        // Sınav salonlarına atamanın başarılı olup olmadığını kontrol et
        for (Solution solution : solutions) {
            if (solution.isValid()) {
                // Sınav salonlarına atamanın başarılı olduğunu göster
                fitness += 1;
            }
        }

 */
        return fitness;
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