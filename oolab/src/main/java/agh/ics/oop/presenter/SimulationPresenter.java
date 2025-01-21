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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
    @FXML
    private CheckBox exportToCsvCheckBox;

    private GridPane simulationGrid;
    private Label dayLabel;
    private Label currentAnimals;
    private Label currentWater;
    private Label currentPlants;
    private Button pauseButton;


    private int mapHeight;
    private int mapWidth;
    private WorldMap worldMap;
    private Simulation simulation;
    private int cellWidth;
    private int cellHeight;
    private final SimulationApp simulationApp = new SimulationApp();
    private final Map<String, Image> images = new HashMap<>();
    private boolean isPaused = false;
    private Animal lastSelectedAnimal;
    private SimulationParameters simulationParameters;
    private VBox vboxBottom;
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

    public void initializeWindowElements(BorderPane root) {
        VBox vboxTop = new VBox();
        VBox vboxLeft = new VBox();
        VBox vboxRight = new VBox();
        vboxBottom = new VBox();

        dayLabel = new Label();
        currentAnimals = new Label();
        currentPlants = new Label();
        pauseButton = new Button("Pause");

        root.setCenter(simulationGrid);
        simulationGrid.setAlignment(Pos.CENTER);

        vboxTop.setAlignment(Pos.CENTER);
        vboxTop.getChildren().add(dayLabel);
        vboxTop.getStyleClass().add("vbox-top");
        dayLabel.getStyleClass().add("label-day");
        root.setTop(vboxTop);

        vboxLeft.getChildren().add(pauseButton);
        pauseButton.setOnAction(e -> {
            simulation.pause(pauseButton);
            isPaused = !isPaused;
            drawMap();
            if (!isPaused && lastSelectedAnimal == null) {
                vboxRight.getChildren().clear();
            }
        });
        vboxLeft.setPadding(new Insets(10));
        vboxLeft.getStyleClass().add("vbox-left");
        root.setLeft(vboxLeft);

        vboxBottom.getChildren().add(currentAnimals);
        vboxBottom.getChildren().add(currentPlants);
        vboxBottom.getStyleClass().add("vbox-bottom");
        root.setBottom(vboxBottom);

        vboxRight.getStyleClass().add("vbox-right");
        root.setRight(vboxRight);
    }

    private void setCellBackgrounds() {
        Image steppe = images.get("steppe");
        Image jungle = images.get("jungle");
        int[] prefStrip = worldMap.getPreferredStrip();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                ImageView imageView;
                if (y <= prefStrip[1] && y >= prefStrip[0]) {
                    imageView = new ImageView(jungle);
                } else {
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
        images.put("water", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/water.png")).toExternalForm()));
        images.put("jungle", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/prefered_background.png")).toExternalForm()));
        images.put("steppe", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/regular_background.png")).toExternalForm()));
        images.put("animal_shiny", new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/animal_shiny.png")).toExternalForm()));

    }
    private void drawMap() {
        if(simulationGrid != null) {
            clearGrid();
        }
        gridColumns();
        gridRows();
        setCellBackgrounds();
        addElements();

    }

    private void gridColumns(){
        simulationGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        for(int i=0; i<mapWidth-1; i++){
            simulationGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
    }

    private void gridRows(){
        simulationGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        for(int i=0; i<mapHeight-1; i++){
            simulationGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
    }

    private void addElements() {
        CopyOnWriteArrayList<WorldElement> elements = worldMap.getElements();
        for (WorldElement element : elements) {
            Vector2d pos = element.getPosition();
            VBox animalVBox = new VBox();
            animalVBox.setAlignment(Pos.CENTER);

            if (element.getClass() == Animal.class) {
                ImageView imageView;
                if(((Animal) element).getGenes().equals(simulation.getStatistics().getMostPopularGenes())){
                    imageView = new ImageView(images.get("animal_shiny"));
                }else{
                    imageView = new ImageView(images.get("animal"));

                }
                imageView.setFitWidth(cellWidth);
                imageView.setFitHeight(cellHeight - 10);

                ProgressBar energyBar = new ProgressBar();
                energyBar.setPrefWidth(cellWidth);
                energyBar.setStyle("-fx-accent: darkblue;");
                energyBar.setProgress((double) ((Animal) element).getEnergyLevel() / simulationParameters.requiredEnergy());

                animalVBox.getChildren().addAll(imageView, energyBar);
            } else if (element.getClass() == Plant.class) {
                ImageView imageView = new ImageView(images.get("plant"));
                imageView.setFitWidth(cellWidth);
                imageView.setFitHeight(cellHeight);
                animalVBox.getChildren().add(imageView);
            } else if (element.getClass() == Water.class) {
                ImageView imageView = new ImageView(images.get("water"));
                imageView.setFitWidth(cellWidth);
                imageView.setFitHeight(cellHeight);
                animalVBox.getChildren().add(imageView);
            }

            simulationGrid.add(animalVBox, pos.getX(), pos.getY());
            GridPane.setHalignment(animalVBox, HPos.CENTER);
        }

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Pane clickablePane = new Pane();
                clickablePane.setPrefSize(cellWidth, cellHeight);

                if (lastSelectedAnimal != null && lastSelectedAnimal.isAlive() && lastSelectedAnimal.getPosition().equals(new Vector2d(x, y))) {
                    clickablePane.setStyle("-fx-border-color: red; -fx-border-width: 4; -fx-background-color: transparent;");
                } else {
                    clickablePane.setStyle("-fx-background-color: transparent;");
                }

                final int finalX = x;
                final int finalY = y;
                clickablePane.setOnMouseClicked(event -> handleCellClick(finalX, finalY));
                simulationGrid.add(clickablePane, x, y);
            }
        }
    }


    private void clearGrid() {
        if (!simulationGrid.getChildren().isEmpty()) {
            simulationGrid.getChildren().retainAll(simulationGrid.getChildren().get(0));
        }
        simulationGrid.getColumnConstraints().clear();
        simulationGrid.getRowConstraints().clear();
    }

    private void handleCellClick(int x, int y) {
        if (!isPaused) {
            return;
        }

        System.out.println("Clicked on cell: " + x + ", " + y);
        VBox vboxRight = (VBox) simulationGrid.getScene().lookup(".vbox-right");
        vboxRight.getChildren().clear();

        List<Animal> animals = worldMap.getAnimalsAt(new Vector2d(x, y));
        if (animals.isEmpty()) {
            return;
        }

        VBox statisticsVBox = new VBox();
        statisticsVBox.setAlignment(Pos.BOTTOM_CENTER);

        for (Animal animal : animals) {
            VBox animalVBox = new VBox();
            animalVBox.setAlignment(Pos.CENTER);

            ImageView imageView;
            if(animal.getGenes().equals(simulation.getStatistics().getMostPopularGenes())){
                imageView = new ImageView(images.get("animal_shiny"));
            }else{
                imageView = new ImageView(images.get("animal"));

            }
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setOnMouseClicked(event -> {
                lastSelectedAnimal = animal;
                statisticsVBox.getChildren().clear();
                displayAnimalStatistics(animal, statisticsVBox);
            });

            animalVBox.getChildren().add(imageView);
            vboxRight.getChildren().add(animalVBox);
        }

        vboxRight.getChildren().add(statisticsVBox);
    }

    private void displayAnimalStatistics(Animal animal, VBox statisticsVBox) {
        statisticsVBox.getChildren().clear();
        if (animal.isAlive()) {
            ImageView imageView;
            if(animal.getGenes().equals(simulation.getStatistics().getMostPopularGenes())){
                imageView = new ImageView(images.get("animal_shiny"));
            }else{
                imageView = new ImageView(images.get("animal"));

            }
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);

            ProgressBar energyBar = new ProgressBar();
            energyBar.setPrefWidth(150);
            energyBar.setStyle("-fx-accent: darkblue;");
            energyBar.setProgress((double) animal.getEnergyLevel() / simulationParameters.requiredEnergy());

            Label ageLabel = new Label("Age: " + (simulation.getDate() - animal.getDateOfBirth()));
            Label childrenLabel = new Label("Children: " + animal.getChildrenCounter());
            Label descendantsLabel = new Label("Descendants: " + animal.getDescendantsCounter());
            Label plantsEatenLabel = new Label("Plants Eaten: " + animal.getPlantsEatenCounter());
            Label genesLabel = new Label("Genes: " + Arrays.toString(animal.getGenes().getGenesSequence()));

            statisticsVBox.getChildren().addAll(imageView, energyBar, ageLabel, childrenLabel, descendantsLabel, plantsEatenLabel, genesLabel);

            if (lastSelectedAnimal != null) {
                Button stopTrackingButton = new Button("Stop Tracking");
                stopTrackingButton.setPrefWidth(200);
                stopTrackingButton.setOnAction(e -> {
                    lastSelectedAnimal = null;
                    VBox vboxRight = (VBox) simulationGrid.getScene().lookup(".vbox-right");
                    vboxRight.getChildren().clear();
                });
                VBox.setMargin(stopTrackingButton, new Insets(10, 0, 0, 0));
                statisticsVBox.getChildren().add(stopTrackingButton);
            }
        } else {
            Label deadLabel = new Label("The tracked animal has died at day " + animal.getDeathDate());
            statisticsVBox.getChildren().add(deadLabel);
            Button stopTrackingButton = new Button("Stop Tracking");
            stopTrackingButton.setPrefWidth(200);
            stopTrackingButton.setOnAction(e -> {
                lastSelectedAnimal = null;
                VBox vboxRight = (VBox) simulationGrid.getScene().lookup(".vbox-right");
                vboxRight.getChildren().clear();
            });
            VBox.setMargin(stopTrackingButton, new Insets(10, 0, 0, 0));
            statisticsVBox.getChildren().add(stopTrackingButton);
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            dayLabel.setText(message);

            MapStatistics statistics = simulation.getStatistics();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(5);
            gridPane.setVgap(5);
            gridPane.setAlignment(Pos.CENTER);

            Label[] labels = {
                    new Label("Current animals: " + statistics.getAnimalCount()),
                    new Label("Current plants: " + statistics.getPlantCount()),
                    new Label("Free tiles: " + statistics.getFreeTiles()),
                    new Label("Average life span: " + String.format("%.2f", statistics.getAverageLifeSpan())),
                    new Label("Average energy: " + String.format("%.2f", statistics.getAverageEnergy())),
                    new Label("Average children: " + String.format("%.2f", statistics.getAverageChildrenNumber())),
                    new Label("Most popular genotype: " + Arrays.toString(statistics.getMostPopularGenes().getGenesSequence()))
            };

            for (Label label : labels) {
                label.setStyle("-fx-font-size: 10px;");
            }

            int rows = 2;
            int columns = (int) Math.ceil((double) labels.length / rows);

            for (int i = 0; i < labels.length; i++) {
                gridPane.add(labels[i], i / rows, i % rows);
            }

            if (lastSelectedAnimal != null) {
                VBox vboxRight = (VBox) simulationGrid.getScene().lookup(".vbox-right");
                vboxRight.getChildren().clear();

                ImageView imageView = new ImageView(images.get("animal"));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                VBox statisticsVBox = new VBox();
                statisticsVBox.setAlignment(Pos.BOTTOM_CENTER);
                displayAnimalStatistics(lastSelectedAnimal, statisticsVBox);

                vboxRight.getChildren().addAll(imageView, statisticsVBox);
            }

            vboxBottom.getChildren().clear();
            vboxBottom.getChildren().add(gridPane);

            if (exportToCsvCheckBox.isSelected()) {
                saveStatisticsToCsv(statistics);
            }
        });
    }

    private void saveStatisticsToCsv(MapStatistics statistics) {
        String fileName = "src/main/resources/logs/simulation_statistics" + worldMap.getID() +".csv";
        boolean fileExists = new File(fileName).exists();

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName, true))) {
            StringBuilder sb = new StringBuilder();

            if (!fileExists) {
                sb.append("Day,Current Animals,Current Plants,Free Tiles,Average Life Span,Average Energy,Average Children,Most Popular Genotype\n");
            }

            sb.append(simulation.getDate()).append(",");
            sb.append(statistics.getAnimalCount()).append(",");
            sb.append(statistics.getPlantCount()).append(",");
            sb.append(statistics.getFreeTiles()).append(",");
            sb.append(String.format("%.2f", statistics.getAverageLifeSpan())).append(",");
            sb.append(String.format("%.2f", statistics.getAverageEnergy())).append(",");
            sb.append(String.format("%.2f", statistics.getAverageChildrenNumber())).append(",");
            sb.append(Arrays.toString(statistics.getMostPopularGenes().getGenesSequence())).append("\n");

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    public Simulation getSimulation(){
        return simulation;
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
            cellHeight = Math.min(650 / mapHeight, 650 / mapWidth);
            cellWidth = Math.min(650 / mapHeight, 650 / mapWidth);
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

            simulationParameters = new SimulationParameters(mapWidth, mapHeight, mapVariant,
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

            simulationApp.openSimulationWindow(this);

            simulation = new Simulation(map, simulationParameters);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));

            engine.runAsyncInThreadPool();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
