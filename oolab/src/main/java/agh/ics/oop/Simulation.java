package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Simulation implements Runnable{

    private final List<Animal> animals;
    private final WorldMap map;
    private final int mutationVariant;
    private final int initialEnergyLevel;
    private final int energyGainedFromFood;
    private int date = 0;

    public Simulation(List<Vector2d> startingPoints, WorldMap map, int mutationVariant, int initialEnergyLevel, int energyGainedFromFood) {

        this.map = map;
        this.mutationVariant = mutationVariant;
        this.initialEnergyLevel = initialEnergyLevel;
        this.energyGainedFromFood = energyGainedFromFood;
        this.animals = new ArrayList<>();

        placeAnimals(startingPoints);
    }

    @Override
    public void run(){
        while (!animals.isEmpty()) {
            date += 1;
            for (Animal animal : animals) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException: " + e.getMessage());
                }
                map.move(animal);

                System.out.printf("Animal %d: %s%n", 0,
                        animal.getPosition());

            }
        }
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
            Animal child = new Animal(firstParent, secondParent, mutationVariant, date);
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

    private List<Animal> resolveConflicts(List<Animal> conflictedAnimals, int animalsWithPriority){
        List<Animal> prioritizedAnimals = new ArrayList<>();
        Collections.sort(conflictedAnimals);
        for(int i = 0; i < animalsWithPriority; i++){
            prioritizedAnimals.add(conflictedAnimals.get(i));
        }
        return prioritizedAnimals;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }
}
