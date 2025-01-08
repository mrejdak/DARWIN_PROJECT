package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalCleaner;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final Random random = new Random();
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID mapId;
    private final MapVisualizer vis;

    private final int[] preferredStrip = new int[2];

    protected final Boundary bounds;
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();



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


    public AbstractWorldMap(int width, int height){
        bounds = new Boundary(new Vector2d(0,0), new Vector2d(width-1,height-1));

        calculatePreferredStrip(height);

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
    public void cleanDeadAnimals(HashSet<Vector2d> positions){
        for(Vector2d position: positions){
            ArrayList<Animal> animalsAtPosition = animals.get(position);
            AnimalCleaner.cleanDeadAnimals(animalsAtPosition);
        }
    }

    @Override
    public void growPlants(){
        for(int i = 0; i <= bounds.upperRight().getX(); i++){
            for(int j = 0; j <= bounds.upperRight().getY(); j++){
                boolean grow;
                Vector2d position = new Vector2d(i, j);
                if(!plants.containsKey(position)){
                    if(j >= preferredStrip[0] && j <= preferredStrip[1]){
                        grow = random.nextDouble() > 0.2;
                    }else{
                        grow = random.nextDouble() > 0.8;
                    }

                    if(grow){
                        plants.put(position, new Plant(position));
                    }
                }
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.followsVertically(bounds.lowerLeft()) && position.precedesVertically(bounds.upperRight());
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        ArrayList<Animal> objectsAt = this.animals.get(position);
        if (objectsAt != null) {
            return this.animals.get(position).getFirst();
            // a random animal occupying that square is returned for now
            // TODO: return list of all animals on that square (only if necessary)
        }else{
            return plants.get(position);
        }
    }

    @Override
    public Collection<WorldElement> getElements() {
        Collection<WorldElement> elements = new ArrayList<>();
        for (ArrayList<Animal> animalsOnSquare : animals.values()) {
            elements.addAll(animalsOnSquare);
        }
        elements.addAll(plants.values());
        return elements;
    }

    private void calculatePreferredStrip(int height){
        double highestRowNumber = height - 1;
        double meanValue = (highestRowNumber)/2;

        if(Math.floor(meanValue) == Math.ceil(meanValue)){
            preferredStrip[0] = (int) meanValue;
            preferredStrip[1] = (int) meanValue;
        }else{
            preferredStrip[0] = (int) Math.floor(meanValue);
            preferredStrip[1] = (int) Math.ceil(meanValue);
        }

        while( preferredStrip[0] >= 0 && preferredStrip[1] - preferredStrip[0] + 1 < 0.2 * height  ){
            preferredStrip[0] -= 1;
            preferredStrip[1] += 1;
        }
    }

    @Override
    public Boundary getCurrentBounds() {
        return bounds;
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
