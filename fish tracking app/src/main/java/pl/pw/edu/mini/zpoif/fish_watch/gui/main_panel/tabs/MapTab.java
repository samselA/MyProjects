package pl.pw.edu.mini.zpoif.fish_watch.gui.main_panel.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;
import javafx.embed.swing.SwingNode;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import pl.pw.edu.mini.zpoif.fish_watch.events_api.FavouriteVessels;

import javax.swing.event.MouseInputListener;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapTab extends Tab implements IInitiable {

    private FavouriteVessels favVessels = new FavouriteVessels();
    private SwingNode swingNode = new SwingNode();

    public MapTab(String tabName) {

        super(tabName);

    }

    @Override
    public Void initiate() {

        this.setClosable(false);

        // arrange pane
        SplitPane mainPane = new SplitPane();
        mainPane.setDividerPositions(0.65);

        // left container = map + text
        VBox leftContainer = new VBox();
        leftContainer.setAlignment(Pos.CENTER);
        leftContainer.setFillWidth(true);
        leftContainer.setStyle("-fx-background-color: #303030;");

        Pane textContainer = new Pane();
        Label textLabel = new Label("Check your favourite vessels");
        textLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial'; -fx-font-size: 20px; -fx-font-weight: bold;");

        textContainer.getChildren().add(textLabel);
        // Center the label in the textContainer
        textLabel.layoutXProperty().bind(textContainer.widthProperty().subtract(textLabel.widthProperty()).divide(2));
        textLabel.layoutYProperty().bind(textContainer.heightProperty().subtract(textLabel.heightProperty()).divide(2));

        AnchorPane mapContainer = new AnchorPane();

        swingNode.setContent(get_map(favVessels));
        swingNode.minWidth(400);
        swingNode.minHeight(400);
        AnchorPane.setTopAnchor(swingNode, 0d);
        AnchorPane.setBottomAnchor(swingNode, 0d);
        AnchorPane.setRightAnchor(swingNode, 0d);
        AnchorPane.setLeftAnchor(swingNode, 0d);
        mapContainer.getChildren().add(swingNode);

        leftContainer.getChildren().addAll(textContainer, mapContainer);
        textContainer.prefHeightProperty().bind(mainPane.heightProperty().multiply(0.07));
        mapContainer.prefHeightProperty().bind(mainPane.heightProperty().multiply(0.93));

        // right container = logo + ???
        VBox rightContainer = new VBox();
        rightContainer.setAlignment(Pos.CENTER);
        rightContainer.setStyle("-fx-background-color: #333333;");

        String imageUrl = "/img/fish_watch_logo_2.png";

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/img/fish_watch_logo_2.png")));
        logo.setVisible(true);
        logo.setPreserveRatio(true);
        logo.setFitWidth(0.2 * 800);

        Pane lowerRightContainer = new Pane();
        lowerRightContainer.setStyle("-fx-background-color: #333333;");

        Label statisticsLabel = new Label("Vessels Statistics:");
        statisticsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");

        statisticsLabel.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(2));
        statisticsLabel.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(10));

        // Label for "fav_num:"
        Label favNumLabel = new Label("fav_num: 100");
        favNumLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        favNumLabel.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(7.5));
        favNumLabel.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(5));

// Label for the value of "fav_num"
        Label favNumValueLabel = new Label("100");
        favNumValueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        favNumValueLabel.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(1.35));
        favNumValueLabel.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(5));

// Label for "flag_enc:"
        Label flagEncLabel = new Label("flag_enc: 100");
        flagEncLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        flagEncLabel.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(1.01));
        flagEncLabel.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(5));


// Label for the value of "flag_enc"
        Label flagEncValueLabel = new Label("100");
        flagEncValueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        flagEncValueLabel.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(0.58));
        flagEncValueLabel.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(5));

// Label for the value of "flag_dom:"
        Label flagDomLabel = new Label("flag_dom: NOR");
        flagDomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        flagDomLabel.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(7.5));
        flagDomLabel.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(3.5));

// Label for the value of "flag_dom_pr:"
        Label flagDomLabelPr = new Label("flag_dom_pr: 68.58%");
        flagDomLabelPr.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        flagDomLabelPr.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(1.01));
        flagDomLabelPr.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(3.5));

// Label for the value of "mf_port_lon:"
        Label mfPortLat = new Label("mf_port_lat: 42.218");
        mfPortLat.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        mfPortLat.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(7.5));
        mfPortLat.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(2.75));

// Label for the value of "mf_port_lon:"
        Label mfPortLon = new Label("mf_port_lon: -8.791");
        mfPortLon.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        mfPortLon.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(1.01));
        mfPortLon.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(2.75));

// Label for the value of "avg_fish_act:"
        Label avgFishAct = new Label("avg_fish_act: 17");
        avgFishAct.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        avgFishAct.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(7.5));
        avgFishAct.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(2.3));

// Label for the value of "big_fish_act:"
        Label bigFishAct = new Label("big_fish_act: 98");
        bigFishAct.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        bigFishAct.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(1.01));
        bigFishAct.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(2.3));

// Label for the value of "big_bb_lx:"
        Label bigBbLx = new Label("big_bb_lx: -63.21");
        bigBbLx.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        bigBbLx.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(7.5));
        bigBbLx.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(2));

// Label for the value of "big_bb_rx:"
        Label bigBbRx = new Label("big_bb_rx: -46.16");
        bigBbRx.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        bigBbRx.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(1.01));
        bigBbRx.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(2));

// Label for the value of "big_bb_lx:"
        Label bigBbLy = new Label("big_bb_ly: --63.23");
        bigBbLy.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        bigBbLy.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(7.5));
        bigBbLy.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(1.75));

// Label for the value of "big_bb_ry:"
        Label bigBbRy = new Label("big_bb_ry: -46.16");
        bigBbRy.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        bigBbRy.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(1.01));
        bigBbRy.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(1.75));


        Label chooseTimeIntervalLabel = new Label("Choose time interval:");
        chooseTimeIntervalLabel.setTextFill(Color.WHITE);
        chooseTimeIntervalLabel.setFont(Font.font("Arial", 14));
        chooseTimeIntervalLabel.setStyle("-fx-font-weight: bold;");

        chooseTimeIntervalLabel.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(chooseTimeIntervalLabel.widthProperty()).divide(2));
        chooseTimeIntervalLabel.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseTimeIntervalLabel.heightProperty()).divide(1.54));

        DatePicker fromDatePicker = new DatePicker(LocalDate.now());
        DatePicker toDatePicker = new DatePicker(LocalDate.now());

        fromDatePicker.setPrefWidth(100); // Set preferred width for the DatePicker
        fromDatePicker.setMaxWidth(100);

        toDatePicker.setPrefWidth(100); // Set preferred width for the DatePicker
        toDatePicker.setMaxWidth(100);


        Label toLabel = new Label("to");
        toLabel.setTextFill(Color.WHITE);
        toLabel.setFont(Font.font("Arial", 12)); // Ustawienie mniejszej czcionki
        toLabel.setPadding(new Insets(0, 5, 0, 5));


        // Dodanie elementów do kontenera
        HBox datePickersBox = new HBox(fromDatePicker, toLabel, toDatePicker);
        datePickersBox.setAlignment(Pos.CENTER);
        datePickersBox.setSpacing(5); // Adjusted spacing
        datePickersBox.setPadding(new Insets(20, 0, 0, 0)); // Zwiększenie paddingu z góry, dostosowanie do reszty elementów

        VBox vbox = new VBox(datePickersBox);
        vbox.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(vbox.widthProperty()).divide(2));
        vbox.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseTimeIntervalLabel.heightProperty()).divide(1.5));

        Button acquireButton = new Button("Search");
        acquireButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green background color
        acquireButton.setOnMouseEntered(e -> acquireButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white;")); // Darker green on hover
        acquireButton.setOnMouseExited(e -> acquireButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;")); // Restore original color

        acquireButton.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(acquireButton.widthProperty()).divide(2));
        acquireButton.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseTimeIntervalLabel.heightProperty()).divide(1.25));

        Label deleteVessel = new Label("Delete Vessel:");
        deleteVessel.setTextFill(Color.WHITE);
        deleteVessel.setFont(Font.font("Arial", 14));
        deleteVessel.setStyle("-fx-font-weight: bold;");
        deleteVessel.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(deleteVessel.widthProperty()).divide(2));
        deleteVessel.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseTimeIntervalLabel.heightProperty()).divide(1.12));


        StackPane stackPane = createColoredPanelAddingFavourites("#323232", 0.2, 1);

        VBox vboxAnother = new VBox(stackPane);

        vboxAnother.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(vboxAnother.widthProperty()).divide(2));
        vboxAnother.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseTimeIntervalLabel.heightProperty()).divide(1.06));


// Add all labels to lowerRightContainer


        lowerRightContainer.getChildren().addAll(statisticsLabel, favNumLabel, flagEncLabel
                , flagDomLabel, flagDomLabelPr, mfPortLat, mfPortLon, avgFishAct, bigFishAct,
                bigBbLx, bigBbRx, bigBbLy, bigBbRy,
                chooseTimeIntervalLabel, vbox, acquireButton, deleteVessel, vboxAnother);


        rightContainer.getChildren().addAll(logo, lowerRightContainer);
        VBox.setVgrow(lowerRightContainer, Priority.ALWAYS);

        mainPane.getItems().addAll(leftContainer, rightContainer);


        this.setContent(mainPane);

        return null;
    }


    private StackPane createColoredPanelAddingFavourites(String color, double heightPercentage, double widthPercentage) {
        StackPane panel = new StackPane();
        panel.setStyle("-fx-background-color: " + color + ";");

        // Set the percentage dimensions
        panel.setMaxHeight(Double.MAX_VALUE);
        panel.setMaxWidth(Double.MAX_VALUE);

        VBox.setVgrow(panel, Priority.ALWAYS);
        HBox.setHgrow(panel, Priority.ALWAYS);

        // Set the percentage dimensions
        panel.setMinHeight(heightPercentage * 100);
        panel.setMinWidth(widthPercentage * 100);

        // Create label and text field
        Label label = new Label("Vessel id:");

        label.setMinWidth(80);
        label.setTextFill(Color.WHITE);

        TextField textField = new TextField();
        textField.setPrefWidth(100);
        textField.setMaxWidth(100);

        textField.setStyle("-fx-font-size: 10;"); // Set padding to move the text field to the left

        Button saveButton = new Button("Del");
        saveButton.setStyle("-fx-background-color: #D11920; -fx-text-fill: white;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #B3151B; -fx-text-fill: white;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: #D11920; -fx-text-fill: white;"));

        saveButton.setOnAction(event -> {
            String textToSave = textField.getText();

            int index = favVessels.getFavouriteVessels().indexOf(textToSave);
            favVessels.delVessel(index);

            textField.clear(); // Clear the input field after saving

            String filePath = "output.txt";

            try {
                // Read lines from the file
                ArrayList<String> lines = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        // Check if the line contains the word to delete
                        if (!line.contains(textToSave)) {
                            lines.add(line);
                        }
                    }
                }

                // Write the remaining lines back to the file
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                    for (String line : lines) {
                        bw.write(line);
                        bw.newLine();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            swingNode.setContent(get_map(favVessels));

        });


        // Wrap the button in an HBox with spacing
        HBox buttonBox = new HBox(saveButton);
        buttonBox.setPadding(new Insets(0, 0, 0, 5)); // Adjust the left padding as needed

        // Layout for label, text field, and button with spacing only for the button
        HBox labelTextFieldAndButton = new HBox(label, textField, buttonBox);
        labelTextFieldAndButton.setAlignment(Pos.CENTER_LEFT);
        labelTextFieldAndButton.setPadding(new Insets(0, -20, 0, 0));

        // Add label, text field, and button to the panel
        panel.getChildren().add(labelTextFieldAndButton);

        return panel;
    }

    private JXMapViewer get_map(FavouriteVessels favVessels) {

        String filePath = "output.txt";

        // Create an ArrayList to store the words
        ArrayList<String> wordsList = new ArrayList<>();

        // Read words from the file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Add each word to the ArrayList
                wordsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String word : wordsList) {
            System.out.println(word);

            //adding new vessels if the do not exist
            favVessels.addVesel(word);
        }


        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        GeoPosition geo = new GeoPosition(52.4873739, 23.0147696);
        mapViewer.setAddressLocation(geo);
        mapViewer.setZoom(15);

        MouseInputListener mouseListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseListener);
        mapViewer.addMouseMotionListener(mouseListener);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

// Create a waypoint at the desired location
        GeoPosition waypointPosition = new GeoPosition(52.4873739, 21.0147696);
        Waypoint waypoint = new DefaultWaypoint(waypointPosition);

        //adding another points to the map

        Set<Waypoint> waypoints = new HashSet<>();

        List<Double> latitudes = favVessels.getLatitudes();
        List<Double> longtitudes = favVessels.getLongtitude();

        for (int i = 0; i < latitudes.size(); i = i + 1) {
            double lat = latitudes.get(i);
            double lon = longtitudes.get(i);

            GeoPosition way = new GeoPosition(lat, lon);
            Waypoint wayNew = new DefaultWaypoint(way);

            waypoints.add(wayNew);
        }


// Create a waypoint painter
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        DotWaypointRenderer dotWaypointRenderer = new DotWaypointRenderer();
//        dotWaypointRenderer.test();
        waypointPainter.setRenderer(dotWaypointRenderer);

//        waypoints.add(waypoint);


        waypointPainter.setWaypoints(waypoints);

// Add the waypoint painter to the map viewer
// If you already have other painters, use a CompoundPainter to combine them
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>();
        painter.addPainter(waypointPainter);
        mapViewer.setOverlayPainter(painter);

        return mapViewer;
    }


}
