public class GeneticAlgorithm {

    public static final int populationSize = 50;
    public static final int noOfEliteChromosomes = 1;
    public static final int tournamentSelectionSize = 24; //3
    private static final double mutationRate = 50;

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
