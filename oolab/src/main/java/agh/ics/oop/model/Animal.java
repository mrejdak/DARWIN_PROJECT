package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static agh.ics.oop.model.MapDirection.*;

public class Animal implements WorldElement, Comparable<Animal>{
    private final Genes genes;
    private int deathDate;
    private MapDirection direction;
    private Vector2d position;

    private final int amountOfGenes;

    private final Random random = new Random();

    private int geneTracker;
    private int energyLevel;
    private int plantsEatenCounter = 0;
    private final int dateOfBirth;
    private final ArrayList<Animal> children = new ArrayList<>();

    //Constructor for initial animals
    public Animal(Vector2d position, int amountOfGenes, int initialEnergyLevel) {
        this.genes = new Genes(amountOfGenes);
        this.amountOfGenes = amountOfGenes;
        this.direction = NORTH;
        this.position = position;
        this.energyLevel = initialEnergyLevel;
        this.dateOfBirth = 0;
    }

    //Constructor for children
    public Animal(Animal firstParent, Animal secondParent, String simulationVariants, int amountOfGenes, int dateOfBirth, int amountOfMutations){
        this.amountOfGenes = amountOfGenes;
        this.geneTracker = chooseStartingGene();
        this.dateOfBirth = dateOfBirth;
        if(firstParent.energyLevel >= secondParent.energyLevel){
            genes = new Genes(amountOfGenes, firstParent, secondParent, simulationVariants, amountOfMutations);
        }else{
            genes = new Genes(amountOfGenes, secondParent, firstParent, simulationVariants, amountOfMutations);
        }
        this.direction = MapDirection.values()[random.nextInt(8)];
        this.position = firstParent.getPosition();
        // energy is set during breeding
    }

    @Override
    public String toString() {
        return switch (this.direction){
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }

    boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveValidator map){
        direction = MapDirection.values()[(direction.ordinal() + genes.getGeneAtIndex(geneTracker))%8];

        Vector2d newPosition = position.add(direction.toUnitVector());

        if(map.getClass() == Earth.class){
            Vector2d lowerLeft = ((Earth) map).getCurrentBounds().lowerLeft();
            Vector2d upperRight = ((Earth) map).getCurrentBounds().upperRight();

            if(!newPosition.precedesVertically(upperRight) || !newPosition.followsVertically(lowerLeft)){
                direction = MapDirection.values()[(direction.ordinal() + 4)%8];
            }

            if(!newPosition.precedes(upperRight)){
                newPosition = new Vector2d(lowerLeft.getX(), newPosition.getY());
            }

            if(!newPosition.follows(lowerLeft)){
                newPosition = new Vector2d(upperRight.getX(), newPosition.getY());
            }

        }

        if(map.canMoveTo(newPosition)){
            position = newPosition;
        }

        //Assuming that even if he cannot move to the position, he still turns towards it

        geneTracker = (geneTracker + 1) % amountOfGenes;
        this.loseEnergy(1);
    }


    public MapDirection getDirection() {
        return direction;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public void loseEnergy(int amountLost){
        energyLevel = Math.max(0, energyLevel - amountLost);
    }

    public void gainEnergy(int amountGained){
        energyLevel += amountGained;
    }

    public void setEnergyLevel(int newEnergyLevel){
        energyLevel = newEnergyLevel;
    }

    public int getEnergyLevel(){
        return energyLevel;
    }

    public void addChildren(Animal child){
        children.add(child);
    }

    public int getDescendantsCounter(){
        return this.getAllDescendants().size();
    }

    public HashSet<Animal> getAllDescendants(){
        HashSet<Animal> descendants = new HashSet<Animal>();
        for (Animal child : children){
            if (!descendants.contains(child)){
                descendants.addAll(child.getAllDescendants());
            }
        }
        descendants.addAll(children);
        return descendants;
    }

    public int getChildrenCounter(){
        return children.size();
    }

    public Genes getGenes(){
        return genes;
    }

    private int chooseStartingGene(){
        return random.nextInt(0, amountOfGenes);
    }

    public int getGeneTracker() {
        return geneTracker;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public void addPlantEaten() {
        plantsEatenCounter += 1;
    }

    public int getActiveGene(){
        return genes.getGeneAtIndex(geneTracker);
    }

    public int getPlantsEatenCounter() {
        return plantsEatenCounter;
    }

    @Override
    public int compareTo(Animal comparedTo) {

        int energyDifference = this.getEnergyLevel() - comparedTo.getEnergyLevel();
        if (energyDifference != 0) {
            return energyDifference;
        }

        int ageDifference = comparedTo.dateOfBirth - this.dateOfBirth;
        if (ageDifference != 0) {
            return ageDifference;
        }

        return this.getChildrenCounter() - comparedTo.getChildrenCounter();
    }

    public boolean isAlive(){
        return energyLevel > 0;
    }

    public void setDeathDate(int deathDate){
        this.deathDate = deathDate;
    }

    public int getDeathDate(){
        return deathDate;
    }
}
