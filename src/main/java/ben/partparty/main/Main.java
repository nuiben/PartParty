package ben.partparty.main;

import ben.partparty.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
        Scene currentScene = new Scene(fxmlLoader.load());
        stage.setTitle("Part Party");
        stage.setScene(currentScene);
        stage.show();
    }

    public static void main(String[] args) {

        Part test_IH_Part = new InHouse(1, "Some InHouse Part", 5.00, 22, 12, 50, 900);
        Part test_OS_Part = new Outsourced(101, "Some Outsourced Part", 18.00, 50, 20, 100, "Big Brand International");
        Product test_Product = new Product(25, "Test Products", 69.99, 8, 0, 20);
        Inventory.addPart(test_IH_Part);
        Inventory.addPart(test_OS_Part);
        Inventory.addProduct(test_Product);
        launch(args);
    }

    public static void quit() {
        Platform.exit();
    }
}