package agh.ics.oop.model;

import java.util.Random;

import static agh.ics.oop.model.MapDirection.*;

public class Animal implements WorldElement{
    private final Genes genes;

    private MapDirection direction;
    private Vector2d position;

    private static final int AMOUNT_OF_GENES = 8;  //= getAmountOfGenes() - TODO: Setting it to data from initial input

    private int geneTracker;
    private int energyLevel;


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

        this.energyLevel = 10; //Haven't touched on energy aspect during breeding yet, so for now the level is hard-coded
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

//    public void move(MoveDirection direction, WorldMap map){
//        switch(direction) {
//            case FORWARD -> {
//                Vector2d potentialPosition = this.position.add(this.direction.toUnitVector());
//                if (map.canMoveTo(potentialPosition)) {
//                    this.position = potentialPosition;
//                }
//            }
//            case BACKWARD -> {
//                Vector2d potentialPosition = this.position.subtract(this.direction.toUnitVector());
//                if (map.canMoveTo(potentialPosition)) {
//                    this.position = potentialPosition;
//                }
//            }
//            case LEFT -> this.direction = this.direction.previous();
//            case RIGHT -> this.direction = this.direction.next();
//        }
//    }
    public void move(MoveValidator map){
        direction = MapDirection.values()[(direction.ordinal() + genes.getGeneAtIndex(geneTracker))%8];

        Vector2d newPosition = position.add(direction.toUnitVector());
        if(map.canMoveTo(newPosition)){
            position = newPosition;
        }

        //Assuming that even if he cannot move to the position, he still turns towards it

        geneTracker = (geneTracker + 1) % AMOUNT_OF_GENES;
        energyLevel -= 1;
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

    public int getEnergyLevel(){
        return energyLevel;
    }

    public Genes getGenes(){
        return genes;
    }

    private int chooseStartingGene(){
        return new Random().nextInt(0, AMOUNT_OF_GENES);
    }
}
