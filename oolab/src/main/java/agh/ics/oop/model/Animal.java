package agh.ics.oop.model;

import java.util.Random;

import static agh.ics.oop.model.MapDirection.*;

public class Animal implements WorldElement, Comparable<Animal>{
    private final Genes genes;

    private MapDirection direction;
    private Vector2d position;

    private static final int AMOUNT_OF_GENES = 8;  //= getAmountOfGenes() - TODO: Setting it to data from initial input

    private final Random random = new Random();

    private int geneTracker;
    private int energyLevel;
    private int childrenCounter = 0;
    private int dateOfBirth;  // TODO: add age functionality


    //Constructor for initial animals

    public Animal(Vector2d position, int initialEnergyLevel) {
        this.genes = new Genes(AMOUNT_OF_GENES);
        this.direction = NORTH;
        this.position = position;
        this.energyLevel = initialEnergyLevel;
    }

    //Constructor for children
    public Animal(Animal firstParent, Animal secondParent, int simulationVariants){
        this.geneTracker = chooseStartingGene();
        if(firstParent.energyLevel >= secondParent.energyLevel){
            genes = new Genes(AMOUNT_OF_GENES, firstParent, secondParent, simulationVariants);
        }else{
            genes = new Genes(AMOUNT_OF_GENES, secondParent, firstParent, simulationVariants);
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

        geneTracker = (geneTracker + 1) % AMOUNT_OF_GENES;
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

    public void addChildren(){
        this.childrenCounter += 1;
    }

    public int getChildrenCounter(){
        return childrenCounter;
    }

    public Genes getGenes(){
        return genes;
    }

    private int chooseStartingGene(){
        return random.nextInt(0, AMOUNT_OF_GENES);
    }

    @Override
    public int compareTo(Animal comparedTo) {

        int energyDifference = this.getEnergyLevel() - comparedTo.getEnergyLevel();
        if (energyDifference != 0) {
            return energyDifference;
        }

        int ageDifference = comparedTo.dateOfBirth - this.dateOfBirth;  // TODO: add age comparison
        if (ageDifference != 0) {
            return ageDifference;
        }

        return this.getChildrenCounter() - comparedTo.getChildrenCounter();
    }
}
