module ben.partparty {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens view to javafx.fxml;
    exports ben.partparty.controller;
    opens ben.partparty.controller to javafx.fxml;
    exports ben.partparty.model;
    opens ben.partparty.model to javafx.fxml;
    exports ben.partparty.main;
}