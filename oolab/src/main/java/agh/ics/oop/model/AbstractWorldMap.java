package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    private final MapVisualizer vis;
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID mapId;
    private final Map<Vector2d, Grass> grassBlocks = new HashMap<>();


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
            animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>());
            animals.get(animal.getPosition()).add(animal);
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
        if (!oldPosition.equals(animal.getPosition())) {
            animals.get(oldPosition).remove(animal);
            if (animals.get(oldPosition).isEmpty()){
                animals.remove(oldPosition);
            }
            animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>());
            animals.get(animal.getPosition()).add(animal);
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
//        return (this.animals.get(position) == null);
        return true;  // returns true, since multiple animals can now exist on the same square
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        ArrayList<Animal> objectsAt = this.animals.get(position);
        if (objectsAt != null) {
            return this.animals.get(position).getFirst();
            // a random animal occupying that square is returned for now
            // TODO: return list of all animals on that square (only if necessary)
        }
        return null;
    }

    public void removePlant(Vector2d position){
        grassBlocks.remove(position);
    }

    @Override
    public Collection<WorldElement> getElements() {
        Collection<WorldElement> elements = new ArrayList<>();
        for (ArrayList<Animal> animalsOnSquare : animals.values()) {
            elements.addAll(animalsOnSquare);
        }
        return elements;
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
