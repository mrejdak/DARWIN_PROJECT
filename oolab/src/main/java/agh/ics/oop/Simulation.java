package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.AnimalCleaner;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.SimulationParameters;
import javafx.scene.control.Button;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation implements Runnable{

    private final ArrayList<Animal> animals;
    private final WorldMap map;
    private final int plantsPerDay;
    private final String mutationVariant;
    private final int frequencyOfTideChanges;
    private final int amountOfGenes;
    private final int initialEnergyLevel;
    private final int energyGainedFromFood;
    private final int energyRequiredForBreeding;
    private final int parentEnergyLoss;
    private final int startingPlantsCount;
    private final int minAmountOfMutations;
    private final int maxAmountOfMutations;
    private final int mapWidth;
    private final int mapHeight;
    private int date = 0;
    private final Random random = new Random();
    private volatile boolean running = true;
    private final MapStatistics statistics = new MapStatistics();
    public Simulation(WorldMap map, SimulationParameters simulationParameters) {

        this.map = map;
        int startingAnimalCount = simulationParameters.initialAnimals();
        this.mutationVariant = simulationParameters.mutationVariant();
        this.frequencyOfTideChanges = 4;
        this.amountOfGenes = simulationParameters.genomeLength();
        this.initialEnergyLevel = simulationParameters.animalEnergy();
        this.energyGainedFromFood = simulationParameters.plantEnergy();
        this.energyRequiredForBreeding = simulationParameters.requiredEnergy();
        this.parentEnergyLoss = simulationParameters.parentEnergy();
        this.animals = new ArrayList<>();
        this.plantsPerDay = simulationParameters.dailyPlants();
        this.startingPlantsCount = simulationParameters.initialPlants();
        this.minAmountOfMutations = simulationParameters.minMutations();
        this.maxAmountOfMutations = simulationParameters.maxMutations();
        this.mapWidth = simulationParameters.mapWidth();
        this.mapHeight = simulationParameters.mapHeight();

        placeAnimals(startingAnimalCount);
    }

    @Override
    public void run(){
        plantsGrowth(startingPlantsCount);
        while(!animals.isEmpty()){
            date += 1;
            if(date % frequencyOfTideChanges == 0) map.changeTide();
            removeDeadAnimals(statistics);
            setStatistics(statistics);
            map.newDay(date);
            if(!running){
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException: " + e.getMessage());
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
            Map<Vector2d, ArrayList<Animal>> movedAnimals = moveAnimals();
            feedAnimals(movedAnimals);
            breedAnimalsOnMap(movedAnimals, statistics);
            plantsGrowth(plantsPerDay);
        }

    }

    private void setStatistics(MapStatistics statistics){
        int animalCount = 0;
        int plantCount = 0;
        int totalEnergy = 0;
        int totalChildrenCount = 0;
        int waterCount = 0;
        double averageEnergy = 0.0;
        double averageChildrenNumber = 0.0;
        HashSet<Vector2d> occupiedTiles = new HashSet<>();
        int freeTiles;

        for (WorldElement element : map.getElements()){
            if (element.getClass() == Animal.class){
                animalCount += 1;
                totalEnergy += ((Animal) element).getEnergyLevel();
                totalChildrenCount += ((Animal) element).getChildrenCounter();
            } else if (element.getClass() == Plant.class) {
                plantCount += 1;
            } else if (element.getClass() == Water.class) {
                waterCount += 1;
            }
            occupiedTiles.add(element.getPosition());
        }
        if (animalCount != 0) {
            averageEnergy = (double) totalEnergy / animalCount;
            averageChildrenNumber = (double) totalChildrenCount / animalCount;
        }
        freeTiles = mapWidth*mapHeight - occupiedTiles.size();

        statistics.setAnimalCount(animalCount);
        statistics.setPlantCount(plantCount);
        statistics.setFreeTiles(freeTiles);
        statistics.setAverageEnergy(averageEnergy);
        statistics.setAverageChildrenNumber(averageChildrenNumber);
    }

    private void removeDeadAnimals(MapStatistics statistics){
        HashSet<Vector2d> positions = AnimalCleaner.cleanDeadAnimalsFromSimulation(animals, map, date, statistics);
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

    private void breedAnimalsOnMap(Map<Vector2d, ArrayList<Animal>> movedAnimals, MapStatistics statistics){
        for (Vector2d position : movedAnimals.keySet()) {
            ArrayList<Animal> conflictedAnimals = movedAnimals.get(position).stream()
                    .filter(animal -> animal.getEnergyLevel() >= energyRequiredForBreeding)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (conflictedAnimals.size() > 1) {
                resolveConflicts(conflictedAnimals);
                breedAnimals(conflictedAnimals.get(0), conflictedAnimals.get(1), statistics);
            }

        }
    }

    private void plantsGrowth(int plantsCount){
        map.growPlants(plantsCount);
    }

    private void placeAnimals(int startingAnimalCount){
        Boundary mapBoundary = map.getCurrentBounds();
        for (int i = 0; i < startingAnimalCount; i++) {
            Vector2d position = new Vector2d(
                    random.nextInt(0, mapBoundary.upperRight().getX() + 1),
                    random.nextInt(0, mapBoundary.upperRight().getY() + 1)
            );
            Animal animal = new Animal(position, amountOfGenes, initialEnergyLevel);
            System.out.println(Arrays.toString(animal.getGenes().getGenesSequence()));
            try {
                map.place(animal);
                animals.add(animal);
                statistics.updateGenotypePopularity(animal, true);
            }
            catch(IncorrectPositionException e){
                // nothing, because the program will continue without adding the animal
            }
        }
    }

    private void breedAnimals(Animal firstParent, Animal secondParent, MapStatistics statistics){
        try {
            Animal child = new Animal(firstParent, secondParent, mutationVariant, amountOfGenes, date, random.nextInt(minAmountOfMutations, maxAmountOfMutations+1));
            map.place(child);
            animals.add(child);
            firstParent.loseEnergy(parentEnergyLoss);
            firstParent.addChildren(child);
            secondParent.loseEnergy(parentEnergyLoss);
            secondParent.addChildren(child);
            child.setEnergyLevel(parentEnergyLoss * 2);
            statistics.updateGenotypePopularity(child, true);
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
        animal.addPlantEaten();
    }

    private void resolveConflicts(ArrayList<Animal> conflictedAnimals){
        Collections.sort(conflictedAnimals);
        Collections.reverse(conflictedAnimals);
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    public void pause(Button button){
        running = false;
        button.setText("Resume");
        button.setOnAction(e -> resume(button));
    }

    public void resume(Button button){
        running = true;
        synchronized (this) {
            notify();
        }
        button.setText("Pause");
        button.setOnAction(e -> pause(button));
    }

    public int getDate() {
        return date;
    }
    public MapStatistics getStatistics() {
        return statistics;
    }

}
