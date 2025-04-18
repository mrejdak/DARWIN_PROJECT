package agh.ics.oop.model;

import java.util.Collections;
import java.util.HashMap;

public class MapStatistics {
    private int animalCount;
    private int plantCount;
    private int freeTiles;
    private HashMap<Genes, Integer> genotypePopularity = new HashMap<>();
    private double averageEnergy;
    private double averageLifeSpan = 0.0;
    private double averageChildrenNumber;
    private int totalLifeSpan = 0;
    private int totalAnimalsDead = 0;

    public MapStatistics(){
    }

    public void updateAverageLifeSpan(int removedAnimalsLifeSpan, int removedAnimalsCount){
        this.totalLifeSpan += removedAnimalsLifeSpan;
        this.totalAnimalsDead += removedAnimalsCount;
        if (totalAnimalsDead != 0){
            averageLifeSpan = (double) totalLifeSpan/totalAnimalsDead;
        }
    }

    public void updateGenotypePopularity(Animal animal, boolean newAnimal) {
        int change;
        if (newAnimal) {
            change = 1;
        } else {
            change = -1;
        }
        genotypePopularity.put(animal.getGenes(), genotypePopularity.getOrDefault(animal.getGenes(), 0) + change);
    }

    public void setAnimalCount(int animalCount) {
        this.animalCount = animalCount;
    }

    public void setPlantCount(int plantCount) {
        this.plantCount = plantCount;
    }

    public void setFreeTiles(int freeTiles) {
        this.freeTiles = freeTiles;
    }

    public void setAverageEnergy(double averageEnergy) {
        this.averageEnergy = averageEnergy;
    }


    public void setAverageChildrenNumber(double averageChildrenNumber) {
        this.averageChildrenNumber = averageChildrenNumber;
    }


    public double getAverageChildrenNumber() {
        return averageChildrenNumber;
    }

    public double getAverageEnergy() {
        return averageEnergy;
    }

    public double getAverageLifeSpan() {
        return averageLifeSpan;
    }

    public Genes getMostPopularGenes() {
        return Collections.max(genotypePopularity.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    public int getAnimalCount() {
        return animalCount;
    }

    public int getFreeTiles() {
        return freeTiles;
    }

    public int getPlantCount() {
        return plantCount;
    }

}
