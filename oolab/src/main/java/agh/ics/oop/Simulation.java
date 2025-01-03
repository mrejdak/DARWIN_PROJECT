package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.AnimalCleaner;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.*;

public class Simulation implements Runnable{

    private final ArrayList<Animal> animals;
    private final WorldMap map;
    private final int mutationVariant;
    private final int initialEnergyLevel;

    public Simulation(List<Vector2d> startingPoints, WorldMap map, int mutationVariant, int initialEnergyLevel) {

        this.map = map;
        this.mutationVariant = mutationVariant;
        this.initialEnergyLevel = initialEnergyLevel;
        this.animals = new ArrayList<>();

        placeAnimals(startingPoints);
    }

    @Override
    public void run(){
        plantsGrowth();
        while(!animals.isEmpty()){
            removeDeadAnimals();
            moveAnimals();
            //TODO
            feedAnimals();
            breedAllAnimals();
            //
            plantsGrowth();
        }
    }

    private void removeDeadAnimals(){
        HashSet<Vector2d> positions = AnimalCleaner.cleanDeadAnimalsFromSimulation(animals);

        //Cleaning animals off the map
        map.cleanDeadAnimals(positions);
    }

    private void moveAnimals(){
        for(Animal animal: animals){
            map.move(animal);
        }
    }

    private void feedAnimals(){

    }

    private void breedAllAnimals(){

    }

    private void plantsGrowth(){
        map.growPlants();
    }

    private void placeAnimals(List<Vector2d> startingPoints){
        for (Vector2d point : startingPoints) {
            Animal animal = new Animal(point, initialEnergyLevel);
            System.out.println(Arrays.toString(animal.getGenes().getGenesSequence()));
            try {
                map.place(animal);
                animals.add(animal);
            }
            catch(IncorrectPositionException e){
                // nothing, because the program will continue without adding the animal
            }
        }
    }

    private void breedAnimals(Animal firstParent, Animal secondParent){
        try {
            Animal child = new Animal(firstParent, secondParent, mutationVariant);
            map.place(child);
            animals.add(child);
            int firstParentsEnergyLoss = (int) Math.round(firstParent.getEnergyLevel() * 0.2);
            int secondParentsEnergyLoss = (int) Math.round(secondParent.getEnergyLevel() * 0.2);
            firstParent.loseEnergy(firstParentsEnergyLoss);
            firstParent.addChildren();
            secondParent.loseEnergy(secondParentsEnergyLoss);
            secondParent.addChildren();
            child.setEnergyLevel(firstParentsEnergyLoss + secondParentsEnergyLoss);
        }
        catch(IncorrectPositionException e){
            System.out.println("Exception: " + e.getMessage());
            // should never catch, since breeding already happens on coordinates that are accessible to animals
        }

    }

    private Animal[] resolveConflicts(List<Animal> conflictedAnimals){
        Animal[] prioritizedAnimals = new Animal[2];
        Collections.sort(conflictedAnimals);
        for(int i = 0; i < 2; i++){
            prioritizedAnimals[i] = conflictedAnimals.get(i);
        }
        return prioritizedAnimals;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }
}
