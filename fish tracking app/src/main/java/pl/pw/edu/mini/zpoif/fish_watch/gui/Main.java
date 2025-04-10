package pl.pw.edu.mini.zpoif.fish_watch.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.pw.edu.mini.zpoif.fish_watch.gui.main_panel.tabs.FourWingsTab;
import pl.pw.edu.mini.zpoif.fish_watch.gui.main_panel.tabs.MapTab;
import pl.pw.edu.mini.zpoif.fish_watch.gui.main_panel.tabs.SearchTab;

public class Main extends Application {

    MapTab tab1 = new MapTab("Fish watch");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Fish Watch");

        primaryStage.getIcons().add(new Image("img\\fish.png"));

        primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                primaryStage.setMaximized(false);
        });

        primaryStage.setResizable(false);

        TabPane tabPane = new TabPane();

        // Zakładki
        //pierwsza zakladka
        tab1.initiate();

        //druga zakladka
        FourWingsTab tab2 = new FourWingsTab("4Wings tiles");
        tab2.initiate();

        SearchTab tab4 = new SearchTab("Vessels API", primaryStage.widthProperty(), primaryStage.heightProperty(), () -> tab1.initiate());
        tab4.initiate();
        tabPane.getTabs().addAll(tab1, tab2, tab4);

        // Ustawianie widoczności zakładki 1
        tabPane.getSelectionModel().select(tab1);

        // Ustawianie widoku
        Scene scene = new Scene(tabPane, 800, 600);

        // Load the stylesheet using an absolute path
        scene.getStylesheets().add("dark-mode.css");

        // Wyświetlanie głównego okna
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createTab(String tabName, String buttonLabel) {
        Tab tab = new Tab(tabName);
        tab.setClosable(false); // Dezaktywacja możliwości zamykania zakładki

        Button button = new Button(buttonLabel);
        button.setOnAction(e -> switchTab(tab));

        StackPane stackPane = new StackPane(button);
        stackPane.setStyle("-fx-padding: 10px;"); // Dodanie odstępu między przyciskami

        tab.setContent(stackPane);

        return tab;
    }

    private void switchTab(Tab tab) {
        TabPane tabPane = tab.getTabPane();
        tabPane.getSelectionModel().select(tab);
    }
}
