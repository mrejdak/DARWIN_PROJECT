package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalCleaner;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final Random random = new Random();

    protected final Vector2d mapLowerLeft = new Vector2d(0, 0);
    protected final Vector2d mapUpperRight;
    private final Boundary bounds;
    private final int[] preferredStrip = new int[2];

    private final MapVisualizer vis;
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
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


    public AbstractWorldMap(int width, int height){
        mapUpperRight = new Vector2d(width-1, height-1);
        bounds = new Boundary(mapLowerLeft, this.mapUpperRight);

        calculatePreferredStrip();

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

    private void calculatePreferredStrip(){
        double highestRowNumber = bounds.upperRight().getY();
        double meanValue = (highestRowNumber)/2;

        int height = (int) highestRowNumber + 1;

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
        return new Boundary(bounds.lowerLeft(), bounds.upperRight());
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
