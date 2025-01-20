package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Map;

public class MapStatistics {
    int date;
    int animalCount;
    int plantCount;
    int freeTiles;
    Genes mostPopularGenes;
    double averageEnergy;
    double averageLifeSpan;
    double averageChildrenNumber;

    public MapStatistics(){
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

    public void setAverageLifeSpan(double averageLifeSpan) {
        this.averageLifeSpan = averageLifeSpan;
    }

    public void setAverageChildrenNumber(double averageChildrenNumber) {
        this.averageChildrenNumber = averageChildrenNumber;
    }

    public void setMostPopularGenes(Genes mostPopularGenes) {
        this.mostPopularGenes = mostPopularGenes;
    }

    public void setDate(int date) {
        this.date = date;
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
        return mostPopularGenes;
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

    public int getDate() {
        return date;
    }
}
