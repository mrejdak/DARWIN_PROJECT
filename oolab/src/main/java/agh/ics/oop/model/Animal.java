package agh.ics.oop.model;

import static agh.ics.oop.model.MapDirection.*;

public class Animal implements WorldElement{
    private MapDirection direction;
    private Vector2d position;
    private int energyLevel;
    private final Genes genes;

    //Constructor for initial animals
    public Animal(Vector2d position, int amountOfGenes, int initialEnergyLevel) {
        this.direction = NORTH;
        this.position = position;
        this.energyLevel = initialEnergyLevel;
        this.genes = new Genes(amountOfGenes);
    }

    //Constructor for children
    public Animal(Animal firstParent, Animal secondParent, int amountOfGenes, int simulationVariants){

        if(firstParent.energyLevel >= secondParent.energyLevel){
            genes = new Genes(amountOfGenes, firstParent, secondParent, simulationVariants);
        }else{
            genes = new Genes(amountOfGenes, secondParent, firstParent, simulationVariants);
        }

        this.energyLevel = 10; //Haven't touched on energy aspect during breeding yet, so for now the level is hard-coded
    }

    @Override
    public String toString() {
        return switch (this.direction){
            case NORTH -> "^";
            case WEST -> "<";
            case SOUTH -> "v";
            case EAST -> ">";
        };
    }

    boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction, WorldMap map){
        switch(direction) {
            case FORWARD -> {
                Vector2d potentialPosition = this.position.add(this.direction.toUnitVector());
                if (map.canMoveTo(potentialPosition)) {
                    this.position = potentialPosition;
                }
            }
            case BACKWARD -> {
                Vector2d potentialPosition = this.position.subtract(this.direction.toUnitVector());
                if (map.canMoveTo(potentialPosition)) {
                    this.position = potentialPosition;
                }
            }
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
        }
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
}
