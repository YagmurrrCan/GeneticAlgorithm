import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Population {
/*
r. Population sınıfında, chromosomesLength parametresi ile oluşturulan Chromosome dizisini initializePopulation() metodu ile rastgele değerler atıyor ve sortChromosomesByFitness() metodu ile bu diziyi fitness değerine göre sıralıyor. Chromosome sınıfında ise initializeChromosome() metodu ile rastgele 0 ve 1 değerleri atanıyor ve calculateFitness() metodu ile fitness değeri hesaplanıyor.
 */
    private Chromosome[] chromosomes;

    public Population(int chromosomesLength) {
        chromosomes = new Chromosome[chromosomesLength];

    }

    public Population initializePopulation(){

        for(int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new Chromosome().initializeChromosome();
        }

        sortChromosomesByFitness();
        return this;
    }


    public void sortChromosomesByFitness(){
        //  System.out.println("SORTING CHROMOSOMES\n");
        Arrays.sort(chromosomes,(chromosome1,chromosome2) ->{
            int returnValue = 0;
            try {
                if(chromosome1.getFitness() > chromosome2.getFitness()) returnValue = -1;
                else if(chromosome1.getFitness() < chromosome2.getFitness()) returnValue = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return returnValue;
        });
    }

    public Chromosome[] getChromosomes() {
        return chromosomes;
    }
}