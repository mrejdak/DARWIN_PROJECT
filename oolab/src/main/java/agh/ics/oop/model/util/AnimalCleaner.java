package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AnimalCleaner {
    public static void cleanDeadAnimals(ArrayList<Animal> animals){

        List<Integer> indexes = new ArrayList<>();

        //Checking which animals are dead
        for(int i = 0; i < animals.size(); i++){
            Animal potentiallyDead = animals.get(i);
            if(potentiallyDead.getEnergyLevel() <= 0){
                indexes.add(i);
            }
        }

        if(!indexes.isEmpty()){
            //Cleaning them from the map
            for(int i = animals.size()-1; i > -1; i--){
                animals.remove((int) indexes.get(i));
            }
        }
    }

    public static HashSet<Vector2d> cleanDeadAnimalsFromSimulation(ArrayList<Animal> animals){

        List<Integer> indexes = new ArrayList<>();
        HashSet<Vector2d> positions = new HashSet<>();

        //Checking which animals are dead
        for(int i = 0; i < animals.size(); i++){
            Animal potentiallyDead = animals.get(i);
            if(potentiallyDead.getEnergyLevel() <= 0){
                indexes.add(i);
                positions.add(potentiallyDead.getPosition());
            }
        }

        if(!indexes.isEmpty()){
            //Cleaning them from the map
            for(int i = animals.size()-1; i > -1; i--){
                animals.remove((int) indexes.get(i));
            }
        }

        return positions;
    }

}
