package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.SimulationParameters;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Collection;
import java.util.List;

import static agh.ics.oop.OptionsParser.parseOptions;

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
    private TextField fullEnergyField;
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


    @FXML
    private Label infoLabel;

//    @FXML
//    private TextField mapWidthField;
//    @FXML
//    private TextField mapHeightField;
    @FXML
    private Label moveDescription;
    @FXML
    private GridPane mapGrid;
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
    private int cellWidth;
    private int cellHeight;


    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setMapDimensions(Boundary bounds){
        mapWidth = bounds.upperRight().getX() - bounds.lowerLeft().getX();
        mapHeight = bounds.upperRight().getY() - bounds.lowerLeft().getY();
        cellWidth = 450 / mapWidth;
        cellHeight = 450 / mapHeight;

    }


    private void drawMap() {
        clearGrid();
//        updateBoundaries();
        gridColumns();
        gridRows();
        addElements();

        infoLabel.setText(worldMap.toString());
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
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        for(int i=0; i<mapWidth; i++){
//            Label label = new Label(Integer.toString(i));
//            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
//            mapGrid.add(label, i+1, 0);
        }
    }

    private void gridRows(){
        mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        for(int i=0; i<mapHeight; i++){
//            Label label = new Label(Integer.toString(mapHeight - 1 - i));
//            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
//            mapGrid.add(label, 0, i+1);
        }
    }

    private void addElements(){

        Collection<WorldElement> elements = worldMap.getElements();
        for (WorldElement element : elements) {
            Vector2d pos = element.getPosition();
            Label elementLabel = new Label(worldMap.objectAt(pos).toString());
            mapGrid.add(elementLabel, pos.getX(), pos.getY());
            GridPane.setHalignment(elementLabel, HPos.CENTER);
        }
    }


    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
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
                    animalEnergyField.getText().isEmpty() || fullEnergyField.getText().isEmpty() || parentEnergyField.getText().isEmpty() ||
                    minMutationsField.getText().isEmpty() || maxMutationsField.getText().isEmpty() || genomeLengthField.getText().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled out");
            }

            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int mapWidth = Integer.parseInt(mapWidthField.getText());
            String mapVariant = mapVariantField.getValue();
            int initialPlants = Integer.parseInt(initialPlantsField.getText());
            int plantEnergy = Integer.parseInt(plantEnergyField.getText());
            int dailyPlants = Integer.parseInt(dailyPlantsField.getText());
            int initialAnimals = Integer.parseInt(initialAnimalsField.getText());
            int animalEnergy = Integer.parseInt(animalEnergyField.getText());
            int fullEnergy = Integer.parseInt(fullEnergyField.getText());
            int parentEnergy = Integer.parseInt(parentEnergyField.getText());
            int minMutations = Integer.parseInt(minMutationsField.getText());
            int maxMutations = Integer.parseInt(maxMutationsField.getText());
            String mutationVariant = mutationVariantField.getValue();
            int genomeLength = Integer.parseInt(genomeLengthField.getText());

            SimulationParameters simulationParameters = new SimulationParameters(mapWidth, mapHeight, mapVariant, initialPlants, plantEnergy, dailyPlants, initialAnimals,animalEnergy, fullEnergy, parentEnergy, minMutations, maxMutations, mutationVariant, genomeLength);
            System.out.println(simulationParameters);
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
        catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
