import java.util.Arrays;

public class Population {

    private Chromosome[] chromosomes;
    private GeneticAlgorithm geneticAlgorithm;

    public Population(int populationSize, GeneticAlgorithm geneticAlgorithm) {
        chromosomes = new Chromosome[populationSize];
        this.geneticAlgorithm = geneticAlgorithm;
    }

    public Population initializePopulation(){
        //DEMO (VALUES FROM THE DATASET WILL BE USED LATER)
        for(int i=0;i<chromosomes.length;i++){
            chromosomes[i]= new Chromosome(geneticAlgorithm).initializeChromosome();
        }
        sortChromosomesByFitness();
        return this;
    }

    public Chromosome[] getChromosomes(){
        return chromosomes;
    }
    public void sortChromosomesByFitness(){
        Arrays.sort(chromosomes,(chromosome1,chromosome2)-> {
            int flag = 0;
            if(chromosome1.getFitness()>chromosome2.getFitness()) flag=-1;
            else if(chromosome1.getFitness()<chromosome2.getFitness()) flag=1;
            return flag;

        });
    }



}
