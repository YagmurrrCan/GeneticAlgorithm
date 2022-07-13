import java.io.IOException;
import java.util.Arrays;

public class Population {

    private Chromosome[] chromosomes;

    public Population(int chromosomesLength) {
        chromosomes = new Chromosome[chromosomesLength];
    }

    public Population initializePopulation(){
        for(int i=0; i < chromosomes.length; i++){
            chromosomes[i]= new Chromosome().initializeChromosome();
        }

        sortChromosomesByFitness();
        return this;
    }

    //  System.out.println("SORTING CHROMOSOMES\n");
    public void sortChromosomesByFitness(){

        Arrays.sort(chromosomes,(chromosome1,chromosome2) -> {

            int flag = 0;

            try {
                if(chromosome1.getFitness() > chromosome2.getFitness()) {
                    flag = -1;
                }
                else if(chromosome1.getFitness() < chromosome2.getFitness()) {
                    flag = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flag;


        });

        //  System.out.println("CHROMOSOMES SORTED\n");

    }

    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

}