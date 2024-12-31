package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Simulation implements Runnable{

    private final List<Animal> animals;
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

        while(animals.get(0).getEnergyLevel() > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
            map.move(animals.get(0));

            System.out.printf("Animal %d: %s%n", 0,
                    animals.get(0).getPosition());

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

    private Comparator<? super Animal> customComparator(){
        //TODO: custom comparator to chain all 4 comparisons in one sort
        return null;
    }

    private List<Animal> resolveConflicts(List<Animal> conflictedAnimals, int animalsWithPriority){
        List<Animal> prioritizedAnimals = new ArrayList<>();
        conflictedAnimals.sort(customComparator());
        for(int i = 0; i < animalsWithPriority; i++){
            prioritizedAnimals.add(conflictedAnimals.get(i));
        }
        return prioritizedAnimals;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }
}
