package pl.pw.edu.mini.zpoif.fish_watch.gui.main_panel.tabs;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
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
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.json_classes.StyleConfiguration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsGETDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsPOSTMapDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.FourWingsPOSTMapDeclarationToObjects;
import pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters.IntervalValues4WingsApi;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Objects;

public class FourWingsTab extends Tab implements IInitiable {

    public FourWingsTab(String tabName) {

        super(tabName);

    }

    ImageView imageView;
    TextField textFieldZ;
    TextField textFieldY;
    TextField textFieldX;
    ColorPicker colorPicker;
    DatePicker fromDatePicker;
    DatePicker toDatePicker;
    ComboBox<String> timeBox;

    SwingNode swingNodeMap;

    int[] indexTiles;
    int zoom;

    @Override
    public Void initiate() {
        this.setClosable(false); // Dezaktywacja możliwości zamykania zakładki

        double appWidth = 800;
        double maxMapSize = appWidth * 0.65;

        // arrange pane
        SplitPane mainPane = new SplitPane();
        mainPane.setDividerPositions(0.65);

        // left container
        VBox leftContainer = new VBox();
        leftContainer.setAlignment(Pos.CENTER);
        leftContainer.setFillWidth(true);
        leftContainer.setStyle("-fx-background-color: #eeeeee;");


        Pane textContainer = new Pane();
        Label textLabel = new Label("Generate PNG tiles with filter");
        textLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial'; -fx-font-size: 22px; -fx-font-weight: bold;");

        textContainer.setStyle("-fx-background-color: #303030;");


        textContainer.getChildren().add(textLabel);
        // Center the label in the textContainer
        textLabel.layoutXProperty().bind(textContainer.widthProperty().subtract(textLabel.widthProperty()).divide(2));
        textLabel.layoutYProperty().bind(textContainer.heightProperty().subtract(textLabel.heightProperty()).divide(3));

        // map
        AnchorPane mapContainer = new AnchorPane();
        swingNodeMap = new SwingNode();

        // initial values
        Point2D center = getTileCenter(3, 1, 2);
        swingNodeMap.setContent(get_map(center.getX(), center.getY(), 18 - 2));

        AnchorPane.setTopAnchor(swingNodeMap, 0d);
        AnchorPane.setBottomAnchor(swingNodeMap, 0d);
        AnchorPane.setRightAnchor(swingNodeMap, 0d);
        AnchorPane.setLeftAnchor(swingNodeMap, 0d);
        mapContainer.getChildren().add(swingNodeMap);

        // heat map
        imageView = new ImageView();
        imageView.setImage(getImage());

        StackPane stackPaneMap = new StackPane();
        stackPaneMap.getChildren().addAll(mapContainer, imageView);
        stackPaneMap.prefHeightProperty().bind(stackPaneMap.widthProperty());
        textContainer.prefHeightProperty().bind(leftContainer.heightProperty().subtract(stackPaneMap.heightProperty()));
        imageView.fitHeightProperty().bind(mapContainer.heightProperty().multiply(1));
        imageView.fitWidthProperty().bind(mapContainer.widthProperty().multiply(1));
        stackPaneMap.setMaxSize(maxMapSize, maxMapSize);

        leftContainer.getChildren().addAll(textContainer, stackPaneMap);

        // right container = logo + ???
        VBox rightContainer = new VBox();
        rightContainer.setAlignment(Pos.CENTER);
        rightContainer.setStyle("-fx-background-color: #333333;");


        ImageView logo = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/fish_watch_logo_2.png"))));
        logo.setVisible(true);
        logo.setPreserveRatio(true);
        logo.setFitWidth(0.2 * 800);

        Pane lowerRightContainer = new Pane();
        lowerRightContainer.setStyle("-fx-background-color: #333333;");

        Label statisticsLabel = new Label("Data filterer:");
        statisticsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");

        statisticsLabel.layoutXProperty().bind(lowerRightContainer.widthProperty().subtract(statisticsLabel.widthProperty()).divide(2));
        statisticsLabel.layoutYProperty().bind(lowerRightContainer.heightProperty().subtract(statisticsLabel.heightProperty()).divide(10));


        //wrzucanie wybierania czasu

        Label chooseTimeIntervalLabel = new Label("Choose date range:");
        chooseTimeIntervalLabel.setTextFill(Color.WHITE);
        chooseTimeIntervalLabel.setFont(Font.font("Arial", 14));
        chooseTimeIntervalLabel.setStyle("-fx-font-weight: bold;");

        chooseTimeIntervalLabel.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(chooseTimeIntervalLabel.widthProperty()).divide(2));
        chooseTimeIntervalLabel.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseTimeIntervalLabel.heightProperty()).divide(5.5));


        fromDatePicker = new DatePicker(LocalDate.now());
        toDatePicker = new DatePicker(LocalDate.now());

        fromDatePicker.setPrefWidth(100);
        fromDatePicker.setMaxWidth(100);

        toDatePicker.setPrefWidth(100);
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
                .subtract(chooseTimeIntervalLabel.heightProperty()).divide(4.75));


        Label chooseTimeInterval = new Label("Choose time interval:");
        chooseTimeInterval.setTextFill(Color.WHITE);
        chooseTimeInterval.setFont(Font.font("Arial", 14));
        chooseTimeInterval.setStyle("-fx-font-weight: bold;");

        chooseTimeInterval.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(chooseTimeInterval.widthProperty()).divide(2));
        chooseTimeInterval.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseTimeInterval.heightProperty()).divide(2.75));


        timeBox = new ComboBox<>();
        timeBox.getItems().addAll(
                "10DAYS", "DAY", "HOUR", "MONTH", "YEAR"
        );
        timeBox.setVisibleRowCount(5);
        VBox.setMargin(timeBox, new Insets(10, 0, 0, 0));

        timeBox.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(timeBox.widthProperty()).divide(2));
        timeBox.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(timeBox.heightProperty()).divide(2.25));


        Label chooseColor = new Label("Choose color:");
        chooseColor.setTextFill(Color.WHITE);
        chooseColor.setFont(Font.font("Arial", 14));
        chooseColor.setStyle("-fx-font-weight: bold;");

        chooseColor.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(chooseColor.widthProperty()).divide(2));
        chooseColor.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseColor.heightProperty()).divide(1.85));


        // Create a StackPane
        StackPane stackPane = new StackPane();

        // Create a ColorPicker
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.GREEN);

        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();
            System.out.println("Selected Color: " + selectedColor.toString());
            stackPane.setStyle("-fx-background-color: " + toHex(selectedColor) + ";");
        });

        stackPane.getChildren().add(colorPicker);


        stackPane.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(stackPane.widthProperty()).divide(2));
        stackPane.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(stackPane.heightProperty()).divide(1.6));


        Label chooseCoordinates = new Label("Choose coordinates:");
        chooseCoordinates.setTextFill(Color.WHITE);
        chooseCoordinates.setFont(Font.font("Arial", 14));
        chooseCoordinates.setStyle("-fx-font-weight: bold;");

        chooseCoordinates.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(chooseCoordinates.widthProperty()).divide(2));
        chooseCoordinates.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(chooseCoordinates.heightProperty()).divide(1.4));


        HBox labelInputBox = new HBox(10); // 10 is the spacing between nodes
        labelInputBox.setPadding(new Insets(10, 0, 0, 0));

        // Create labels
        Label labelX = new Label("Lat:");
        Label labelY = new Label("Lng:");
        Label labelZ = new Label("Zoom:");


        labelX.setTextFill(Color.WHITE);
        labelX.setFont(Font.font("Arial", 12));
        labelX.setStyle("-fx-font-weight: bold;");

        labelY.setTextFill(Color.WHITE);
        labelY.setFont(Font.font("Arial", 12));
        labelY.setStyle("-fx-font-weight: bold;");

        labelZ.setTextFill(Color.WHITE);
        labelZ.setFont(Font.font("Arial", 12));
        labelZ.setStyle("-fx-font-weight: bold;");

        // Create text inputs
        textFieldZ = new TextField();
        textFieldY = new TextField();
        textFieldX = new TextField();

        textFieldZ.setPrefWidth(45);
        textFieldZ.setMaxWidth(45);

        textFieldZ.setStyle("-fx-font-size: 10;");

        textFieldY.setPrefWidth(45);
        textFieldY.setMaxWidth(45);

        textFieldY.setStyle("-fx-font-size: 10;");

        textFieldX.setPrefWidth(45);
        textFieldX.setMaxWidth(45);

        textFieldX.setStyle("-fx-font-size: 10;");


        labelInputBox.getChildren().addAll(labelX, textFieldX, labelY, textFieldY, labelZ, textFieldZ);

        labelInputBox.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(labelInputBox.widthProperty()).divide(2));
        labelInputBox.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(labelInputBox.heightProperty()).divide(1.25));


        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        // Create buttons
        Button acquireButton = new Button("Acquire");
        Button saveButton = new Button("Save");

        saveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"));


        acquireButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green background color
        acquireButton.setOnMouseEntered(e -> acquireButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white;")); // Darker green on hover
        acquireButton.setOnMouseExited(e -> acquireButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;")); // Restore original color

        // Set up event handler for the Acquire button
        acquireButton.setOnAction(event -> handleAcquireButtonClick());

        // Add buttons to the HBox
        buttonBox.getChildren().addAll(acquireButton, saveButton);

        // Position the button HBox
        buttonBox.layoutXProperty().bind(lowerRightContainer.widthProperty()
                .subtract(buttonBox.widthProperty()).divide(2));
        buttonBox.layoutYProperty().bind(lowerRightContainer.heightProperty()
                .subtract(buttonBox.heightProperty()).divide(1.1));

        lowerRightContainer.getChildren().addAll(statisticsLabel,
                chooseTimeIntervalLabel, vbox, chooseTimeInterval, timeBox,
                chooseColor, stackPane, chooseCoordinates, labelInputBox, buttonBox);


        rightContainer.getChildren().addAll(logo, lowerRightContainer);
        VBox.setVgrow(lowerRightContainer, Priority.ALWAYS);

        mainPane.getItems().addAll(leftContainer, rightContainer);


        this.setContent(mainPane);
        return null;
    }

    private void handleAcquireButtonClick() {
        System.out.println("handleAcquireButtonClick");

        Platform.runLater(() -> {
            try {
                Integer userInputZ = Integer.parseInt(textFieldZ.getText());
                Double userInputY = Double.parseDouble(textFieldY.getText());
                Double userInputX = Double.parseDouble(textFieldX.getText());
                String hexColor = toHex(colorPicker.getValue());
                LocalDate fromDate = fromDatePicker.getValue();
                LocalDate toDate = toDatePicker.getValue();
                String selectedItem = timeBox.getSelectionModel().getSelectedItem();

                new Thread(() -> renderImageFromAcquisition(userInputZ, userInputY, userInputX, hexColor, fromDate, toDate, selectedItem)).start();

            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter valid numeric values.");
            }
        });
    }

    private void renderImageFromAcquisition(Integer userInputZ, Double userInputY, Double userInputX,
                                            String hexColor, LocalDate fromDate, LocalDate toDate,
                                            String selectedItem) {

        IntervalValues4WingsApi interval = switch (selectedItem) {
            case "10DAYS" -> IntervalValues4WingsApi._10DAYS;
            case "DAY" -> IntervalValues4WingsApi.DAY;
            case "HOUR" -> IntervalValues4WingsApi.HOUR;
            case "MONTH" -> IntervalValues4WingsApi.MONTH;
            default -> IntervalValues4WingsApi.YEAR;
        };


        FourWingsPOSTMapDeclaration postDeclaration = new FourWingsPOSTMapDeclaration();
        postDeclaration.addInterval(interval);
        postDeclaration.addDateRange(fromDate.toString(), toDate.toString());
        postDeclaration.addColor(hexColor);

        HttpRequest postRequest = postDeclaration.createPOSTRequest(postDeclaration.getParametersMap());

        HttpResponse<String> response = postDeclaration.sendPOSTRequest(postRequest);

        System.out.println(response.statusCode());
        System.out.println(response.body());

        StyleConfiguration styleConfiguration = FourWingsPOSTMapDeclarationToObjects.convertToObject(response.body());
        System.out.println(styleConfiguration.getUrl());

        String urlNeeded = styleConfiguration.getUrl();

        indexTiles = getTiles(userInputX, userInputY, userInputZ);
        zoom = userInputZ;

        FourWingsGETDeclaration getRequest = new FourWingsGETDeclaration(urlNeeded, userInputZ, indexTiles[0], indexTiles[1]);

        System.out.println(getRequest.getChangedURL());

        HttpRequest httpRequest = getRequest.createGETRequest();

        HttpResponse<byte[]> anotherResponse = getRequest.sendGETRequest(httpRequest);


        String contentType = anotherResponse.headers().firstValue("Content-Type").orElse("");
        System.out.println("Content-Type: " + contentType);

        byte[] responseBytes = anotherResponse.body();

        String fileName = "img\\created_image.png";
        getRequest.savePNG(responseBytes, fileName);

        Platform.runLater(() -> {
            try {
                String filePath = new File("src\\main\\resources\\img\\created_image.png").getAbsolutePath();
                BufferedImage bufferedImage = ImageIO.read(new File(filePath));
                if (bufferedImage != null) {
                    System.out.println("Image loaded successfully");
                    Image newImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(newImage);
                    System.out.println("Image updated");
                } else {
                    System.out.println("Unable to load image");
                }

                Point2D center = getTileCenter(indexTiles[0], indexTiles[1], zoom);
                swingNodeMap.setContent(get_map(center.getX(), center.getY(), 18 - zoom));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load image");
            }
        });
    }


    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


    private Image getImage() {

        try {
            InputStream is = getClass().getResourceAsStream("/img/zdjecie3.png");
            if (is == null) {
                throw new RuntimeException("Resource not found: img/zdjecie3.png");
            }
            BufferedImage bufferedImage = ImageIO.read(is);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JXMapViewer get_map(Double latitude, Double longitude, int zoom) {

        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        GeoPosition geo = new GeoPosition(latitude, longitude);
        mapViewer.setZoom(zoom);
        mapViewer.setAddressLocation(geo);
        return mapViewer;
    }

    public static Point2D getTileCenter(int xTile, int yTile, int zoom) {
        return new Point2D.Double(tile2lat(yTile + 0.5, zoom), tile2lon(xTile + 0.5, zoom));
    }

    private static double tile2lon(double x, int z) {
        return ((360 * x) / Math.pow(2.0, z)) - 180;
    }

    private static double tile2lat(double y, int z) {
        double n = Math.PI - ((2.0 * Math.PI * y) / Math.pow(2.0, z));
        return Math.atan(Math.sinh(n)) * (180 / Math.PI);
    }

    public static int[] getTiles(final double lat, final double lon, final int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
        if (xtile < 0)
            xtile = 0;
        if (xtile >= (1 << zoom))
            xtile = ((1 << zoom) - 1);
        if (ytile < 0)
            ytile = 0;
        if (ytile >= (1 << zoom))
            ytile = ((1 << zoom) - 1);
        return new int[]{xtile, ytile};
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
