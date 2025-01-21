package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MapStatistics;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AnimalCleaner {
    public static void cleanDeadAnimals(ArrayList<Animal> animals, WorldMap map){

        List<Integer> indexes = findIndexes(animals, map);

        cleanAnimals(indexes, animals);
    }

    public static HashSet<Vector2d> cleanDeadAnimalsFromSimulation(ArrayList<Animal> animals, WorldMap map, int date, MapStatistics statistics){

        List<Integer> indexes = findIndexes(animals, map);
        HashSet<Vector2d> positions = findPositions(indexes, animals);

        int totalLifeSpan = 0;
        int totalAnimalsDead = indexes.size();
        for (int i : indexes) {
            totalLifeSpan += date - animals.get(i).getDateOfBirth();
            statistics.updateGenotypePopularity(animals.get(i), false);
        }
        statistics.updateAverageLifeSpan(totalLifeSpan, totalAnimalsDead);


        cleanAnimals(indexes, animals);

        return positions;
    }

    private static List<Integer> findIndexes(ArrayList<Animal> animals, WorldMap map){
        List<Integer> indexes = new ArrayList<>();

        for(int i = 0; i < animals.size(); i++){
            Animal potentiallyDead = animals.get(i);
            if(potentiallyDead.getEnergyLevel() <= 0 || map.isWaterPresent(potentiallyDead.getPosition())){
                indexes.add(i);
            }
        }

        return indexes;
    }

    private static HashSet<Vector2d> findPositions(List<Integer> indexes, List<Animal> animals){
        HashSet<Vector2d> positions = new HashSet<>();
        for(int i: indexes){
            positions.add(animals.get(i).getPosition());
        }
        return positions;
    }

    private static void cleanAnimals(List<Integer> indexes, List<Animal> animals){
        if(!indexes.isEmpty()){
            for(int i = indexes.size()-1; i > -1; i--){
                animals.remove((int) indexes.get(i));
            }
        }
    }

}
