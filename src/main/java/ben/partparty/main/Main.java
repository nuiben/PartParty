package ben.partparty.main;

import ben.partparty.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


/** This is the Main Class */
public class Main extends Application {

    /** Program Loads in Sample Inputs for InHouse, OutSourced, and Product then launches application.
     * @param args
     * */
    public static void main(String[] args) {
        String[] words = {"Widget", "Gadget", "Transformer", "Conduit", "Assembly", "Pulley", "Gizmo", "Apparatus",
                "Oscillator", "Resistor", "Diode", "Inverter", "Switch", "Mechanism", "Valve", "Relay", "Pump",
                "Sensor", "Rig", "Actuator"};
        String[] techBuzzwords = {
                "Intelligent", "Cutting-edge", "Innovative", "Future-proof",
                "State-of-the-art", "High-performance", "Revolutionary", "Sleek", "Dynamic",
                "Proactive", "Advanced Analytics", "Augmented Reality", "Virtual Reality",
                "Precision", "Predictive", "Adaptive", "Smart-Connected", "Autonomous Vehicles",
                "Next-gen", "Cloud-native", "Digital Twin", "Edge Computing",
                "Intelligent Automation", "Machine Learning", "AI",
                "Neural Networks", "Quantum Computing", "RPA",
                "Sensor-driven", "Streaming Analytics", "Wearable Technology", "Precision-engineered",
                "High-precision Sensors", "5G Connectivity", "Data-Driven",
                "Self-Learning"};

        for (int i = 0; i < 3; i++ ) {
            String InHouseName = words[(int)(Math.random() * words.length)];
            String OutsourcedName = words[(int)(Math.random() * words.length)];
            String ProductName = techBuzzwords[(int)(Math.random() * techBuzzwords.length)] + " " + words[(int)(Math.random() * words.length)];
            Inventory.addPart(new InHouse(i+1, InHouseName, (int)(Math.random()*10) + 0.99, 10, 0, 50, 101044));
            Inventory.addPart(new Outsourced(i+4, OutsourcedName, (int)(Math.random()*10) + 0.99, 150, 50, 200, "OmegaMart & Subsidiaries"));
            Inventory.addProduct(new Product(i+100, ProductName, (int)(Math.random()*100) + 0.99, 8, 0, 20));
        }
        launch(args);
    }

    /** Loads in MainView.fxml file and displays on the screen
     * @params stage
     * */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Scene currentScene = new Scene(fxmlLoader.load());
        stage.setTitle("Part Party");
        stage.setScene(currentScene);
        stage.show();
    }

    /** Only Used by "Exit" button on Main View */
    public static void quit() {
        Platform.exit();
    }
}