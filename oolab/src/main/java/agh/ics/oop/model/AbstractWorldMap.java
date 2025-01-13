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

    private final Set<Vector2d> junglePositions = new HashSet<>();
    private final Set<Vector2d> steppePositions = new HashSet<>();

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
        fillJungleAndSteppedPositions();

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
    public void growPlants(int plantsPerDay){
        for(int i = plantsPerDay; i > 0; i--){
            if(junglePositions.isEmpty() && steppePositions.isEmpty()){
                return;
            }

            boolean jungle = random.nextDouble() > 0.2;

            Vector2d growthPosition;
            Set<Vector2d> setToPickFrom;

            if(steppePositions.isEmpty() || (jungle && !junglePositions.isEmpty()) ){
                setToPickFrom = junglePositions;
            }else {
                setToPickFrom = steppePositions;
            }

            growthPosition = randomPositionFromSet(setToPickFrom);

            plants.put(growthPosition, new Plant(growthPosition));
            setToPickFrom.remove(growthPosition);
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
    public boolean plantAt(Vector2d position) {
        return (plants.get(position) != null);
    }

    @Override
    public void removePlant(Vector2d position){
        plants.remove(position);

        if(positionInJungle(position.getY())) {
            junglePositions.add(position);
        }else{
            steppePositions.add(position);
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

    private void fillJungleAndSteppedPositions(){
        for(int i = 0; i <= bounds.upperRight().getX(); i++){
            for(int j = 0; j<= bounds.upperRight().getY(); j++){
                if(positionInJungle(j)){
                    junglePositions.add(new Vector2d(i,j));
                }else{
                    steppePositions.add(new Vector2d(i,j));
                }
            }
        }
    }

    private boolean positionInJungle(int height){
        return height <= preferredStrip[1] && height >= preferredStrip[0];
    }

    private Vector2d randomPositionFromSet(Set<Vector2d> set){
        if(set.isEmpty()){
            return null;
        }

        int size = set.size();
        int randomPositionIndex = random.nextInt(0,size);
        int i = 0;

        Vector2d positionToBeReturned = null;

        for(Vector2d position: set){
            if(i == randomPositionIndex){
                positionToBeReturned = position;
                break;
            }
            i++;
        }

        set.remove(positionToBeReturned);
        return positionToBeReturned;
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
