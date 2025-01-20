package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.SimulationParameters;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


import java.util.*;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    private TextField plantEnergyField;
    @FXML
    private TextField dailyPlantsField;
    @FXML
    private TextField initialAnimalsField;
    @FXML
    private TextField animalEnergyField;
    @FXML
    private TextField requiredEnergyField;
    @FXML
    private TextField parentEnergyField;
    @FXML
    private TextField minMutationsField;
    @FXML
    private TextField maxMutationsField;
    @FXML
    private ComboBox<String> mutationVariantField;
    @FXML
    private TextField genomeLengthField;
    @FXML
    private TextField mapHeightField;
    @FXML
    private TextField mapWidthField;
    @FXML
    private ComboBox<String> mapVariantField;
    @FXML
    private TextField initialPlantsField;


    private GridPane simulationGrid;

    // Other methods and fields

    @FXML
    public void initialize() {
        // Initialization code if needed
    }

    @FXML
    private Label moveDescription;

//
//    private int mapWidth = Integer.parseInt(mapWidthField.getText());
//    private int mapHeight = Integer.parseInt(mapHeightField.getText());
    private int mapHeight;
    private int mapWidth;
//    private int xMin;
//    private int yMin;
//    private int xMax;
//    private int yMax;
    private WorldMap worldMap;
    private int cellWidth = 50;
    private int cellHeight = 50;
    private final SimulationApp simulationApp = new SimulationApp();
    private final Map<String, Image> images = new HashMap<>();
    public void initializeGrid() {
        simulationGrid = new GridPane();
        simulationGrid.setGridLinesVisible(true);

        gridColumns();
        gridRows();
        setCellBackgrounds();
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setMapDimensions(Boundary bounds){
        mapWidth = bounds.upperRight().getX() - bounds.lowerLeft().getX();
        mapHeight = bounds.upperRight().getY() - bounds.lowerLeft().getY();
        cellWidth = 450 / mapWidth;
        cellHeight = 450 / mapHeight;

    }

    private void setCellBackgrounds(){
        Image steppe = images.get("steppe");
        Image jungle = images.get("jungle");
        int[] prefStrip = worldMap.getPreferredStrip();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                ImageView imageView;
                if(y <= prefStrip[1] && y >= prefStrip[0]){
                    imageView = new ImageView(jungle);
                }else{
                    imageView = new ImageView(steppe);
                }

                imageView.setFitWidth(cellWidth);
                imageView.setFitHeight(cellHeight);
                simulationGrid.add(imageView, x, y);
            }
        }
    }
    private void initializeImages() {
        images.put("animal", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/animal.png")).toExternalForm()));
        images.put("plant", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/plant.png")).toExternalForm()));
        images.put("jungle", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/prefered_background.png")).toExternalForm()));
        images.put("steppe", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/regular_background.png")).toExternalForm()));
        images.put("water", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/water.png")).toExternalForm()));

    }
    private void drawMap() {
        if(simulationGrid != null) {
            clearGrid();
        }
//        updateBoundaries();
        gridColumns();
        gridRows();
        setCellBackgrounds();
        addElements();

    }


//    private void updateBoundaries(){
//        Boundary bounds =  worldMap.getCurrentBounds();
//        xMin = bounds.lowerLeft().getX();
//        yMin = bounds.lowerLeft().getY();
//        xMax = bounds.upperRight().getX();
//        yMax = bounds.upperRight().getY();
//        mapWidth = xMax - xMin + 1;
//        mapHeight = yMax - yMin + 1;
//        System.out.println(bounds.upperRight());
//        System.out.println(bounds.lowerLeft());
//        System.out.println(mapWidth);
//        System.out.println(mapHeight);
//    }


    private void gridColumns(){
        simulationGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        for(int i=0; i<mapWidth-1; i++){
//            Label label = new Label(Integer.toString(i));
//            GridPane.setHalignment(label, HPos.CENTER);
            simulationGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
//            mapGrid.add(label, i+1, 0);
        }
    }

    private void gridRows(){
        simulationGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        for(int i=0; i<mapHeight-1; i++){
//            Label label = new Label(Integer.toString(mapHeight - 1 - i));
//            GridPane.setHalignment(label, HPos.CENTER);
            simulationGrid.getRowConstraints().add(new RowConstraints(cellHeight));
//            mapGrid.add(label, 0, i+1);
        }
    }

    private void addElements() {
        Collection<WorldElement> elements = worldMap.getElements();
        for (WorldElement element : elements) {
            Vector2d pos = element.getPosition();
            ImageView imageView = null;

            if (element.getClass() == Animal.class) {
                imageView = new ImageView(images.get("animal"));
            } else if (element.getClass() == Plant.class) {
                imageView = new ImageView(images.get("plant"));
            } else if (element.getClass() == Water.class) {
                imageView = new ImageView(images.get("water"));
            }

            if (imageView != null) {
                imageView.setFitWidth(cellWidth);
                imageView.setFitHeight(cellHeight);
                simulationGrid.add(imageView, pos.getX(), pos.getY());
                GridPane.setHalignment(imageView, HPos.CENTER);
            }
        }
    }


    private void clearGrid() {
        if (!simulationGrid.getChildren().isEmpty()) {
            simulationGrid.getChildren().retainAll(simulationGrid.getChildren().get(0)); // hack to retain visible grid lines
        }
        simulationGrid.getColumnConstraints().clear();
        simulationGrid.getRowConstraints().clear();
    }


    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            moveDescription.setText(message);
        });
    }


    @FXML
    public void onSimulationStartClicked(){
        try {
            if (mapHeightField.getText().isEmpty() || mapWidthField.getText().isEmpty() || initialPlantsField.getText().isEmpty() ||
                    plantEnergyField.getText().isEmpty() || dailyPlantsField.getText().isEmpty() || initialAnimalsField.getText().isEmpty() ||
                    animalEnergyField.getText().isEmpty() || requiredEnergyField.getText().isEmpty() || parentEnergyField.getText().isEmpty() ||
                    minMutationsField.getText().isEmpty() || maxMutationsField.getText().isEmpty() || genomeLengthField.getText().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled out");
            }

            mapHeight = Integer.parseInt(mapHeightField.getText());
            mapWidth = Integer.parseInt(mapWidthField.getText());
            String mapVariant = mapVariantField.getValue();
            int initialPlants = Integer.parseInt(initialPlantsField.getText());
            int plantEnergy = Integer.parseInt(plantEnergyField.getText());
            int dailyPlants = Integer.parseInt(dailyPlantsField.getText());
            int initialAnimals = Integer.parseInt(initialAnimalsField.getText());
            int animalEnergy = Integer.parseInt(animalEnergyField.getText());
            int requiredEnergy = Integer.parseInt(requiredEnergyField.getText());
            int parentEnergy = Integer.parseInt(parentEnergyField.getText());
            int minMutations = Integer.parseInt(minMutationsField.getText());
            int maxMutations = Integer.parseInt(maxMutationsField.getText());
            String mutationVariant = mutationVariantField.getValue();
            int genomeLength = Integer.parseInt(genomeLengthField.getText());

            SimulationParameters simulationParameters = new SimulationParameters(mapWidth, mapHeight, mapVariant,
                    initialPlants, plantEnergy, dailyPlants, initialAnimals, animalEnergy, requiredEnergy, parentEnergy,
                    minMutations, maxMutations, mutationVariant, genomeLength);
            System.out.println(simulationParameters);

            AbstractWorldMap map;
            switch (mapVariant){
                case "Earth" -> map = new Earth(mapWidth, mapHeight);
                case "Tides" -> map = new LowAndHighTides(mapWidth, mapHeight);
                default -> throw new IllegalArgumentException("Wrong map type"); //should never throw
            }

            map.addObserver(this);
            this.setWorldMap(map);

            initializeImages();

            BorderPane root = simulationApp.openSimulationWindow(this);
            root.setCenter(simulationGrid);
            simulationGrid.setAlignment(Pos.CENTER);

            Simulation simulation = new Simulation(map, simulationParameters);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.runAsync();

            //            String moveInput = moveListField.getText();
//            List<MoveDirection> directions = parseOptions(moveInput.split(" "));
//            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
//            AbstractWorldMap earthMap = new Earth(10, 10);
//            earthMap.addObserver(this);
//            this.setWorldMap(earthMap);
//            this.setMapDimensions(earthMap.getCurrentBounds());
//
//            Simulation simulation = new Simulation(positions, earthMap, 1, 2, 8, 10, 10, 7, 4, 6);
//            // simulation parameters are hard-coded for now
//            SimulationEngine engine = new SimulationEngine(List.of(simulation));
//            engine.runAsync();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
