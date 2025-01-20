package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Map;

public class MapStatistics {
    int date;
    int animalCount;
    int plantCount;
    int freeTiles;
    Genes mostPopularGenes;
    int averageEnergy;
    int averageLifeSpan;
    int averageChildrenNumber;

    public MapStatistics(){
    }

    public void setAnimalCount(int animalCount) {
        this.animalCount = animalCount;
    }

    public void setPlantCount(int plantCount) {
        this.plantCount = plantCount;
    }
}
