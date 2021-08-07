module dev.azn9.ntviewer {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.azn9.ntviewer to javafx.fxml;
    exports dev.azn9.ntviewer;
}