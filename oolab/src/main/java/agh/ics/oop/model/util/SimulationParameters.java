package agh.ics.oop.model.util;

public record SimulationParameters(
        int mapWidth,
        int mapHeight,
        String mapVariant,
        int initialPlants,
        int plantEnergy,
        int dailyPlants,
        int initialAnimals,
        int animalEnergy,
        int requiredEnergy,
        int parentEnergy,
        int minMutations,
        int maxMutations,
        String mutationVariant,
        int genomeLength
) {
    public SimulationParameters {
        if (mapWidth <= 4) {
            throw new IllegalArgumentException("Map width must be greater than 4");
        }
        if (mapHeight <= 4) {
            throw new IllegalArgumentException("Map height must be greater than 4");
        }
        if (initialPlants < 0) {
            throw new IllegalArgumentException("Initial plant count must be non-negative");
        }
        if (plantEnergy <= 0) {
            throw new IllegalArgumentException("Energy given by a plant must be greater than 0");
        }
        if (dailyPlants <= 0) {
            throw new IllegalArgumentException("Plants per day must be positive");
        }
        if (initialAnimals <= 0) {
            throw new IllegalArgumentException("Initial animals must be positive");
        }
        if (animalEnergy <= 0) {
            throw new IllegalArgumentException("Energy of an animal must be greater than 0");
        }
        if (requiredEnergy <= 0) {
            throw new IllegalArgumentException("requiredEnergy must be greater than 0");
        }
        if (parentEnergy <= 0) {
            throw new IllegalArgumentException("parentEnergy must be greater than 0");
        }
        if (parentEnergy > requiredEnergy / 2) {
            throw new IllegalArgumentException("parentEnergy must not be greater than half of requiredEnergy");
        }
        if (minMutations < 0) {
            throw new IllegalArgumentException("Min number of mutations must be non-negative");
        }
        if (maxMutations < 0) {
            throw new IllegalArgumentException("Max number of mutations must be non-negative");
        }
        if (maxMutations < minMutations) {
            throw new IllegalArgumentException("Max number of mutations must be greater than or equal to Min number of mutations");
        }
        if (genomeLength <= 0) {
            throw new IllegalArgumentException("Length of genome must be greater than 0");
        }
        if (genomeLength > 10) {
            throw new IllegalArgumentException("Length of genome must be less than or equal to 10");
        }
        if (maxMutations > genomeLength) {
            throw new IllegalArgumentException("Max number of mutations must not be greater than length of genome");
        }

        dailyPlants = Math.min(dailyPlants, mapWidth*mapHeight);
        initialPlants = Math.min(initialPlants, mapHeight*mapWidth);
    }

    @Override
    public String toString() {
        return "SimulationParameters{" +
                "mapWidth=" + mapWidth +
                ", mapHeight=" + mapHeight +
                ", mapVariant='" + mapVariant + '\'' +
                ", initialPlants=" + initialPlants +
                ", plantEnergy=" + plantEnergy +
                ", dailyPlants=" + dailyPlants +
                ", initialAnimals=" + initialAnimals +
                ", animalEnergy=" + animalEnergy +
                ", requiredEnergy=" + requiredEnergy +
                ", parentEnergy=" + parentEnergy +
                ", minMutations=" + minMutations +
                ", maxMutations=" + maxMutations +
                ", mutationVariant='" + mutationVariant + '\'' +
                ", genomeLength=" + genomeLength +
                '}';
    }
}