package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AnimalCleaner {
    public static void cleanDeadAnimals(ArrayList<Animal> animals){

        List<Integer> indexes = findIndexes(animals);

        cleanAnimals(indexes, animals);
    }

    public static HashSet<Vector2d> cleanDeadAnimalsFromSimulation(ArrayList<Animal> animals){

        List<Integer> indexes = findIndexes(animals);
        HashSet<Vector2d> positions = findPositions(indexes, animals);

        cleanAnimals(indexes, animals);

        return positions;
    }

    private static List<Integer> findIndexes(ArrayList<Animal> animals){
        List<Integer> indexes = new ArrayList<>();

        for(int i = 0; i < animals.size(); i++){
            Animal potentiallyDead = animals.get(i);
            if(potentiallyDead.getEnergyLevel() <= 0){
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
            for(int i = animals.size()-1; i > -1; i--){
                animals.remove((int) indexes.get(i));
            }
        }
    }

}
