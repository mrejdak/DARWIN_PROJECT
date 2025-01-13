package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.AnimalCleaner;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.*;

public class Simulation implements Runnable{

    private final ArrayList<Animal> animals;
    private final WorldMap map;
    private final int plantsPerDay;
    private final int mutationVariant;
    private final int initialEnergyLevel;
    private final int energyGainedFromFood;
    private final int startingPlantsCount;
    private int date = 0;


    public Simulation(List<Vector2d> startingPoints, WorldMap map, int mutationVariant, int initialEnergyLevel, int energyGainedFromFood, int plantsPerDay, int startingPlantsCount) {

        this.map = map;
        this.mutationVariant = mutationVariant;
        this.initialEnergyLevel = initialEnergyLevel;
        this.energyGainedFromFood = energyGainedFromFood;
        this.animals = new ArrayList<>();
        this.plantsPerDay = plantsPerDay;
        this.startingPlantsCount = startingPlantsCount;

        placeAnimals(startingPoints);
    }

    @Override
    public void run(){
        plantsGrowth(startingPlantsCount);
        while(!animals.isEmpty()){
            removeDeadAnimals();
            Map<Vector2d, ArrayList<Animal>> movedAnimals = moveAnimals();
            feedAnimals(movedAnimals);
            breedAnimalsOnMap(movedAnimals);
            //
            plantsGrowth(plantsPerDay);
        }
    }

    private void removeDeadAnimals(){
        HashSet<Vector2d> positions = AnimalCleaner.cleanDeadAnimalsFromSimulation(animals);

        //Cleaning animals off the map
        map.cleanDeadAnimals(positions);
    }

    private Map<Vector2d, ArrayList<Animal>> moveAnimals(){
        Map<Vector2d, ArrayList<Animal>> animalsMoved = new HashMap<>();
        Vector2d oldPosition;
        for(Animal animal: animals){
            oldPosition = animal.getPosition();
            map.move(animal);
            if (!oldPosition.equals(animal.getPosition())) {
                animalsMoved.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>());
                animalsMoved.get(animal.getPosition()).add(animal);
            }
        }

        return animalsMoved;
    }

    private void feedAnimals(Map<Vector2d, ArrayList<Animal>> movedAnimals){
        for (Vector2d position : movedAnimals.keySet()) {
            if (map.plantAt(position)) {
                ArrayList<Animal> conflictedAnimals = movedAnimals.get(position);
                resolveConflicts(conflictedAnimals);
                consumeGrass(conflictedAnimals.getFirst());
            }
        }
    }

    private void breedAnimalsOnMap(Map<Vector2d, ArrayList<Animal>> movedAnimals){
        for (Vector2d position : movedAnimals.keySet()) {
            ArrayList<Animal> conflictedAnimals = movedAnimals.get(position);
            if (conflictedAnimals.size() > 1) {
                resolveConflicts(conflictedAnimals);
                breedAnimals(conflictedAnimals.get(0), conflictedAnimals.get(1));
            }
        }
    }

    private void plantsGrowth(int plantsCount){
        map.growPlants(plantsCount);
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

    private void consumeGrass(Animal animal) {
        Vector2d position = animal.getPosition();
        map.removePlant(position);
        animal.gainEnergy(energyGainedFromFood);
    }

    private void resolveConflicts(ArrayList<Animal> conflictedAnimals){
        // might be unnecessary to make it a method, but it seems clearer
        Collections.sort(conflictedAnimals);
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }
}
