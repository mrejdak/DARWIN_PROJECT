package agh.ics.oop.model;

import static agh.ics.oop.model.MapDirection.*;

public class Animal implements WorldElement{
    private MapDirection direction;
    private Vector2d position;
    private static final Vector2d mapLowerLeft = new Vector2d(0, 0);
    private static final Vector2d mapUpperRight = new Vector2d(4, 4);


    public Animal() {
        this(new Vector2d(2, 2));
    }

    public Animal(Vector2d position) {
        this.direction = NORTH;
        this.position = position;
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
}
