package pl.pw.edu.mini.zpoif.fish_watch.gui.main_panel.tabs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Cell;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes.Entry;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.json_classes.Response;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.requests_converters.VesselsGETDeclaration;
import pl.pw.edu.mini.zpoif.fish_watch.vessels_api.requests_converters.VesselsGETDeclarationToObjects;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.ObservableList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SearchTab extends Tab implements IInitiable {

    private final ReadOnlyDoubleProperty width;
    private final ReadOnlyDoubleProperty height;
    ObservableList<ObservableList<String>> data;
    Callable<Void> favVesselBinding;

    public SearchTab(String tabName, ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height, Callable<Void> favVesselBinding) {
        super(tabName);
        this.width = width;
        this.height = height;
        this.favVesselBinding = favVesselBinding;
    }

    @Override
    public Void initiate() {
        this.setClosable(false);
        VBox leftSide = initiateMainArea("#333333", "#303030", 3.5 / 5.0);
        StackPane rightPanel = createColoredPanelWithTextAndDatePickers("#2B2B2B", 1.5 / 5.0, "Vessels API", "data filterer");
        // Ustawienie kontenera na pozycję obok siebie
        HBox hbox = new HBox(leftSide, rightPanel);

        this.setContent(hbox);
        return null;
    }

    private StackPane createColoredPanelWithTextAndDatePickers(String color, double widthRatio, String mainText, String subText) {
        StackPane panel = new StackPane();
        panel.setStyle("-fx-background-color: " + color + ";");
        panel.prefWidthProperty().bind(width.multiply(widthRatio));

        // Dodanie tekstu do panelu
        Label mainLabel = new Label(mainText);
        mainLabel.setTextFill(Color.WHITE);
        mainLabel.setFont(Font.font("Arial", 20)); // Ustawienie większej czcionki
        mainLabel.setPadding(new Insets(20, 0, 0, 0)); // Zwiększenie paddingu z góry

        Label subLabel = new Label(subText);
        subLabel.setTextFill(Color.WHITE);

        // Dodanie etykiety "to"
        Label toLabel = new Label("to");
        toLabel.setTextFill(Color.WHITE);
        toLabel.setFont(Font.font("Arial", 12)); // Ustawienie mniejszej czcionki
        toLabel.setPadding(new Insets(0, 5, 0, 5)); // Zmniejszenie paddingu z góry

        // Dodanie DatePickers dla wyboru dat
        DatePicker fromDatePicker = new DatePicker(LocalDate.now());
        DatePicker toDatePicker = new DatePicker(LocalDate.now());

        // Dodanie etykiety "Choose time interval"
        Label chooseTimeIntervalLabel = new Label("Choose time interval:");
        chooseTimeIntervalLabel.setTextFill(Color.WHITE);
        chooseTimeIntervalLabel.setFont(Font.font("Arial", 14));
        chooseTimeIntervalLabel.setPadding(new Insets(17, 0, 0, 0)); // Zwiększenie paddingu z góry

        // Dodanie pola tekstowego dla wprowadzenia nazwy statku
        Label shipNameLabel = new Label("Enter shipname (optional):");
        shipNameLabel.setTextFill(Color.WHITE);
        shipNameLabel.setFont(Font.font("Arial", 14));
        shipNameLabel.setPadding(new Insets(20, 0, 0, 0)); // Padding from the top
        TextField shipNameTextField = new TextField();
        shipNameTextField.setAlignment(Pos.CENTER);
        VBox.setMargin(shipNameTextField, new Insets(10, 0, 0, 0));


        // Dodanie pola tekstowego dla wprowadzenia MMSI
        Label mmsiLabel = new Label("Enter MMSI (optional):");
        mmsiLabel.setTextFill(Color.WHITE);
        mmsiLabel.setFont(Font.font("Arial", 14));
        mmsiLabel.setPadding(new Insets(20, 0, 0, 0)); // Padding from the top
        TextField mmsiTextField = new TextField();
        mmsiTextField.setAlignment(Pos.CENTER);
        VBox.setMargin(mmsiTextField, new Insets(10, 0, 0, 0));


        // Dodanie pola wyboru flagi z obszarem przewijania
        Label flagLabel = new Label("Add Flag (optional):");
        flagLabel.setTextFill(Color.WHITE);
        flagLabel.setFont(Font.font("Arial", 14));
        flagLabel.setPadding(new Insets(20, 0, 0, 0)); // Padding from the top

        ComboBox<String> flagComboBox = new ComboBox<>();
        flagComboBox.getItems().addAll(
                "KOR", "SWE", "FRA", "GIB", "TZA", "ATG", "PRT", "GRC", "TUV", "CAN", "THA", "NLD", "ESP", "NGA", "COK",
                "HRV", "MNE", "KNA", "COM", "BRB", "HKG", "GNB", "GHA", "CIV", "SOM", "CHN", "TGO", "LUX", "UKR", "SEN", "KIR",
                "CHE", "VNM", "VCT", "KOR", "SLV", "DNK", "BEL", "MLT", "TWN", "DEU", "AUS", "USA", "VUT", "NOR", "CYM", "TUR",
                "NIU", "PHL", "GBR", "POL", "BHS", "SLE", "SYC", "CYP", "BMU", "AGO", "PCN", "BLZ", "CPV", "PLW", "WLF", "SGP",
                "GIN", "MHL", "BES", "WSM", "RUS", "ITA", "GMB", "MAR", "LBR", "PAN"
        );
        flagComboBox.setVisibleRowCount(5); // Set the number of visible rows
        VBox.setMargin(flagComboBox, new Insets(10, 0, 0, 0));

        // Dodanie etykiety "Add limit"
        Label limitLabel = new Label("Add Limit:");
        limitLabel.setTextFill(Color.WHITE);
        limitLabel.setFont(Font.font("Arial", 14));
        limitLabel.setPadding(new Insets(20, 0, 0, 0)); // Padding from the top

        // Dodanie pola numerycznego dla wprowadzenia limitu
        TextField limitTextField = new TextField();
        limitTextField.setPrefWidth(100); // Set a smaller preferred width
        limitTextField.setMaxWidth(100);
        limitTextField.setAlignment(Pos.CENTER);
        VBox.setMargin(limitTextField, new Insets(10, 0, 0, 0));

        // Dodanie przycisku "Acquire"
        Button acquireButton = new Button("Acquire");
        acquireButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green background color
        acquireButton.setOnMouseEntered(e -> acquireButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white;")); // Darker green on hover
        acquireButton.setOnMouseExited(e -> acquireButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;")); // Restore original color

        VBox.setMargin(acquireButton, new Insets(20, 0, 0, 0));

        //Dodanie przycisku obslugi bledow
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setPadding(new Insets(10, 0, 0, 0));


        // Dodanie elementów do kontenera
        HBox datePickersBox = new HBox(fromDatePicker, toLabel, toDatePicker);
        datePickersBox.setAlignment(Pos.CENTER);
        datePickersBox.setSpacing(5); // Adjusted spacing
        datePickersBox.setPadding(new Insets(20, 0, 0, 0)); // Zwiększenie paddingu z góry, dostosowanie do reszty elementów

        VBox vbox = new VBox(mainLabel, subLabel, chooseTimeIntervalLabel, datePickersBox, shipNameLabel, shipNameTextField,
                mmsiLabel, mmsiTextField, flagLabel, flagComboBox, limitLabel, limitTextField, acquireButton, errorLabel);
        vbox.setAlignment(Pos.TOP_CENTER);

        panel.getChildren().add(vbox);

        // Dodanie akcji dla przycisku "Acquire"
        acquireButton.setOnAction(e -> {

            String shipName = shipNameTextField.getText();
            String mmsi = mmsiTextField.getText();
            String flag = flagComboBox.getValue();
            if (flag == null)
                flag = "";
            String limit = limitTextField.getText();

            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();

            //condition whether to send request on server side
            boolean condition = true;

            if (!mmsi.matches("[0-9]*") && condition) {
                errorLabel.setText("MMSI has to contain only digits");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> errorLabel.setText("")));
                timeline.play();

                condition = false;
            }
            if (!limit.matches("[0-9]*") && condition) {
                errorLabel.setText("Limit has to contain only digits");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> errorLabel.setText("")));
                timeline.play();

                condition = false;
            }

            if (condition) {
                updateData(limit, fromDate.toString(), toDate.toString(), shipName, mmsi, flag);
            }

        });

        return panel;
    }

    private VBox initiateMainArea(String color1, String color2, double widthRatio) {

        StackPane topPanel = initiateDataFrame(color1, 0.88, 1);
        StackPane bottomPanel = initiateFooter(color2, 0.12, 1);

        VBox vbox = new VBox(topPanel, bottomPanel);

        vbox.prefWidthProperty().bind(width.multiply(widthRatio));

        return vbox;
    }

    private StackPane initiateDataFrame(String color, double heightPercentage, double widthPercentage) {
        StackPane panel = new StackPane();
        panel.setStyle("-fx-background-color: " + color + ";");

        // Set the percentage dimensions
        panel.setMaxHeight(Double.MAX_VALUE);
        panel.setMaxWidth(Double.MAX_VALUE);

        VBox.setVgrow(panel, Priority.ALWAYS);
        panel.setMinHeight(heightPercentage * 100);
        panel.setMinWidth(widthPercentage * 100);
        panel.prefHeightProperty().bind(height.multiply(heightPercentage));

        ScrollPane scrollPane = new ScrollPane();
        VBox dataFrameContent = createInitialDataFrame();

        dataFrameContent.prefWidthProperty().bind(panel.widthProperty());
        dataFrameContent.prefHeightProperty().bind(panel.heightProperty());

        scrollPane.setContent(dataFrameContent);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        panel.getChildren().add(scrollPane);

        return panel;
    }

    private Optional<Response> requestData(String limit, String start_date, String stop_date, String shipName, String MMSI, String flag) {
        VesselsGETDeclaration getDeclaration = new VesselsGETDeclaration();
        if (!limit.isEmpty())
            getDeclaration.addLimit(limit);
        else
            getDeclaration.addLimit("10");
        if (!start_date.isEmpty() || !stop_date.isEmpty()) {
            getDeclaration.addTransmissionDate('\"' + stop_date + '\"', '\"' + start_date + '\"');
        }
        if (!shipName.isEmpty()) {
            getDeclaration.addShipName('\"' + shipName + '\"');
        }
        if (!MMSI.isEmpty()) {
            getDeclaration.addMMSI('\"' + MMSI + '\"');
        }
        if (!flag.isEmpty()) {
            getDeclaration.addFlag('\"' + flag + '\"');
        }
        Map<String, String> parameters = getDeclaration.getParametersMap();
        HttpRequest httpRequest = getDeclaration.createGETRequest(parameters);
        System.out.println(httpRequest);
        HttpResponse<String> response = getDeclaration.sendGETRequest(httpRequest);
        if (response.statusCode() != 200) {
            return Optional.empty();
        }
        return Optional.of(VesselsGETDeclarationToObjects.convertToObject(response.body()));
    }

    private VBox createInitialDataFrame() {
        TableView<ObservableList<String>> tableView = new TableView<>();
        tableView.setEditable(false);
        allowCopy(tableView);
        String[] columnNames = {"Ship name", "Flag", "SSVID", "Transmision from", "Transmision to", "Vessel id"};
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnIndex)));
            tableView.getColumns().add(column);
        }

        data = FXCollections.observableArrayList();
        tableView.setItems(data);
        updateData("", "", "", "", "", "SWE");

        // Add TableView to VBox
        VBox vbox = new VBox(tableView);
        vbox.setSpacing(10);

        return vbox;
    }

    private void allowCopy(TableView<ObservableList<String>> tableView) {
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MenuItem item = new MenuItem("Copy");
        item.setOnAction(e -> {
            ObservableList<TablePosition> posList = tableView.getSelectionModel().getSelectedCells();
            int old_r = -1;
            StringBuilder clipboardString = new StringBuilder();
            for (TablePosition p : posList) {
                int r = p.getRow();
                int c = p.getColumn();
                Object cell = tableView.getColumns().get(c).getCellData(r);
                if (cell == null)
                    cell = "";
                if (old_r == r)
                    clipboardString.append('\t');
                else if (old_r != -1)
                    clipboardString.append('\n');
                clipboardString.append(cell);
                old_r = r;
            }
            final ClipboardContent content = new ClipboardContent();
            content.putString(clipboardString.toString());
            Clipboard.getSystemClipboard().setContent(content);
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        tableView.setContextMenu(menu);
    }

    private void updateData(String limit, String start_date, String stop_date, String shipName, String MMSI, String flag) {
        data.clear();
        Optional<Response> req = requestData(limit, start_date, stop_date, shipName, MMSI, flag);
        if (req.isPresent())
            for (var element : req.get().getEntries()) {
                try {
                    ObservableList<String> row = getDataFrameEntry(element);
                    data.add(row);
                } catch (Exception e) {
                    continue;
                }
            }
    }

    private static ObservableList<String> getDataFrameEntry(Entry element) {
        ObservableList<String> row = FXCollections.observableArrayList();
        row.add(element.getRegistryInfo().get(0).getnShipname());
        row.add(element.getRegistryInfo().get(0).getFlag());
        row.add(element.getRegistryInfo().get(0).getSsvid());
        row.add(element.getRegistryInfo().get(0).getTransmissionDateFrom());
        row.add(element.getRegistryInfo().get(0).getTransmissionDateTo());
        row.add(element.getCombinedSourcesInfo().get(0).getVesselId());
        return row;
    }

    private StackPane initiateFooter(String color, double heightPercentage, double widthPercentage) {
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

        panel.prefHeightProperty().bind(height.multiply(heightPercentage));

        // Create label and text field
        Label label = new Label("Vessel id:");

        label.setMinWidth(80);
        label.setTextFill(Color.WHITE);

        TextField textField = new TextField();
        textField.setPrefHeight(20);
        textField.setStyle("-fx-font-size: 10;");

        // Create a green button
        Button greenButton = new Button("Add to favourites");
        greenButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        greenButton.setOnMouseEntered(e -> greenButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white;"));
        greenButton.setOnMouseExited(e -> greenButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));

        greenButton.setOnAction(event -> {
            String textToSave = textField.getText();
            try {
                saveTextToFile(textToSave);
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            textField.clear();
        });


        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"));

        saveButton.setOnAction(event -> {
            saveTableToExcel(data, "my-vessels-info.xlsx");
            showSaveConfirmation();
        });


        // Wrap the button in an HBox with spacing
        HBox buttonBox = new HBox(greenButton, saveButton);
        buttonBox.setPadding(new Insets(8, 0, 0, 10)); // Adjust the left padding as needed
        buttonBox.setSpacing(10);

        // Layout for label, text field, and button with spacing only for the button
        HBox labelTextFieldAndButton = new HBox(label, textField, buttonBox);
        labelTextFieldAndButton.setAlignment(Pos.CENTER_LEFT);

        // Add label, text field, and button to the panel
        panel.getChildren().add(labelTextFieldAndButton);

        return panel;
    }

    private void saveTextToFile(String text) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
            writer.write(text);
            writer.newLine(); // Add a new line after each entry
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            favVesselBinding.call();
        } catch (Exception e) {

        }

    }

    public static void saveTableToExcel(ObservableList<ObservableList<String>> data, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowNum = 0;
        for (ObservableList<String> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String field : rowData) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(field);
            }
        }

        for (int i = 0; i < rowNum; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSaveConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("File Saved");
        alert.setHeaderText(null); // You can set a header text here if needed
        alert.setContentText("The file was saved successfully.");

        alert.showAndWait(); // Show the alert and wait for the user to close it
    }
}

