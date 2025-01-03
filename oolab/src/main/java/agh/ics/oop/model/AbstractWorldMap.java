package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    private final MapVisualizer vis;
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID mapId;


    public void addObserver(MapChangeListener observer){
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }

    protected void notifyAllObservers(String message){
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }


    public AbstractWorldMap(){
        this.mapId = UUID.randomUUID();
        this.vis = new MapVisualizer(this);
    }


    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            notifyAllObservers("Animal placed on map");
            return;
        }
        throw new IncorrectPositionException(animal.getPosition());
    }


    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        MapDirection oldDirection = animal.getDirection();
        animal.move(this);
        animals.remove(oldPosition);
        animals.put(animal.getPosition(), animal);
        if (!oldPosition.equals(animal.getPosition())) {
            notifyAllObservers("Animal moved from " + oldPosition + " to " + animal.getPosition());
        }
        if (!oldDirection.equals(animal.getDirection())){
            notifyAllObservers("Animal on " + animal.getPosition() + " turned " + animal.getDirection());
        }
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return (this.animals.get(position) == null);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return this.animals.get(position);
    }

    @Override
    public Collection<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    @Override
    public UUID getID() {
        return mapId;
    }

    @Override
    public String toString() {
        Boundary bounds = getCurrentBounds();
        return vis.draw(bounds.lowerLeft(), bounds.upperRight());
    }
}
