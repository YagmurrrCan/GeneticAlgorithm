import java.io.IOException;
import java.util.Random;

public class GeneticAlgorithm {

    public static final int populationSize = 30;
    public static final int noOfEliteChromosomes = 1;
    public static final int tournamentSelectionSize = 24; //3
    private static final double mutationRate = 0.5;

    public Population evolve(Population population) {
        return mutatePopulation(crossoverPopulation(population));
    }

    private Population crossoverPopulation(Population population) {
        Population crossoverPopulation = new Population(population.getChromosomes().length);
        for (int i = 0; i < noOfEliteChromosomes; i++) {
            crossoverPopulation.getChromosomes()[i] = population.getChromosomes()[i];

        }
        for (int i = noOfEliteChromosomes; i < population.getChromosomes().length; i++) {
            Chromosome chromosome1 = selectTournamentPopulation(population).getChromosomes()[0];
            Chromosome chromosome2 = selectTournamentPopulation(population).getChromosomes()[0];
            crossoverPopulation.getChromosomes()[i] = crossoverChromosome(chromosome1, chromosome2);


        }
        return crossoverPopulation;
    }

    private Population mutatePopulation(Population population) {
        Population mutatePopulation = new Population(population.getChromosomes().length);
        for (int i = 0; i < noOfEliteChromosomes; i++) {
            mutatePopulation.getChromosomes()[i] = population.getChromosomes()[i];

        }
        for (int i = noOfEliteChromosomes; i < population.getChromosomes().length; i++) {
            mutatePopulation.getChromosomes()[i] = mutateChromosome(population.getChromosomes()[i]);


        }


        return mutatePopulation;
    }


    private Chromosome crossoverChromosome(Chromosome chromosome1, Chromosome chromosome2) {
        // random genes selection from parent chromosomes
        Chromosome crossoverChromosome = new Chromosome();

        for (int i = 0; i < chromosome1.getGenes().length; i++) {
            if (Math.random() > 0.5) crossoverChromosome.getGenes()[i] = chromosome1.getGenes()[i];
            else crossoverChromosome.getGenes()[i] = chromosome2.getGenes()[i];
        }
        return crossoverChromosome;
    }

    private Chromosome mutateChromosome(Chromosome chromosome) {

        Chromosome mutateChromosome = new Chromosome();
        for (int i = 0; i < chromosome.getGenes().length; i++) {
            if (Math.random() < mutationRate) {
                if (Math.random() < 0.5) mutateChromosome.getGenes()[i] = 1;
                else mutateChromosome.getGenes()[i] = 0;

            } else mutateChromosome.getGenes()[i] = chromosome.getGenes()[i];
        }
        return mutateChromosome;
    }

    private Population selectTournamentPopulation(Population population) {
        Population tournamentPopulation = new Population(tournamentSelectionSize);

        for (int i = 0; i < tournamentSelectionSize; i++) {

            tournamentPopulation.getChromosomes()[i] = population.getChromosomes()[(int) (Math.random() * population.getChromosomes().length)];
        }
        tournamentPopulation.sortChromosomesByFitness();
        return tournamentPopulation;

    }
}

    /*

    public static final double  crossoverRate = 0.9;

    private int numGenerations;

    public GeneticAlgorithm(int numGenerations) {
        this.numGenerations = numGenerations;
    }
*/
    /* Bu yöntem, mevcut popülasyonu girdi olarak alır ve onu çaprazlama ve mutasyon uygulayarak geliştirir. Önce mevcut popülasyondan seçkin çözümleri tutar, ardından yeni yavrular oluşturmak için popülasyondaki en uygun çözümlerle çaprazlama gerçekleştirir. Son olarak, yavruya belirli bir mutasyon oranı ile mutasyon uygular ve evrimleşmiş popülasyonu döndürür.

     * // Keep the elite solutions from the current population.
    for (int i = 0; i < noOfEliteChromosomes; i++) {
        nextPopulation.getChromosomes()[i] = population.getFittestChromosomes(1).getChromosomes()[i];
    }

    // Perform crossover with the fittest solutions from the population.
    for (int i = noOfEliteChromosomes; i < population.getPopulationSize(); i++) {
        Solution offspring = population.selectFittestSolutions(tournamentSelectionSize).getFittestSolution().crossover(population.getFittestSolutions(tournamentSelectionSize).getFittestSolution());

        // Apply mutation to the offspring.
        if (Math.random() <= mutationRate) {
            offspring.mutate();
        }

        nextPopulation.getSolutions()[i] = offspring;
    }

    return nextPopulation;

     */
   /*
    public Population evolve(Population population) throws IOException {
        // Evolve the population by applying crossover and mutation.
        Population nextPopulation = new Population(population.getPopulationSize(), population.getChromosomes()[0].getGenesLength());

        // Keep the elite solutions from the current population.
        for (int i = 0; i < noOfEliteChromosomes; i++) {
            nextPopulation.getChromosomes()[i] = population.getFittestChromosomes(1).getChromosomes()[i];
        }

        // Perform crossover with the fittest solutions from the population.
        for (int i = noOfEliteChromosomes; i < population.getPopulationSize(); i++) {
            Chromosome offspring = population.selectFittestChromosomes(tournamentSelectionSize).getFittestChromosome().crossover(population.getFittestChromosomes(tournamentSelectionSize).getFittestChromosome());

            // Apply mutation to the offspring.
            if (Math.random() <= mutationRate) {
                offspring.mutate();
            }

            nextPopulation.getChromosomes()[i] = offspring;
        }

        return nextPopulation;
    }

    public Chromosome run() throws IOException {
        // Run the genetic algorithm to solve the university timetabling problem.
        Population population = new Population(populationSize, 75);
        population.initializePopulation();

        // Evolve the population over the specified number of generations.
        for (int i = 0; i < numGenerations; i++) {
            population = evolve(population);
        }

        // Return the fittest chromosome from the final evolved population.
        return population.getFittestChromosomes(1).getFittestChromosome();
    }
}

*/