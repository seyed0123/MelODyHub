module com.example.melodyhub {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.melodyhub to javafx.fxml;
    exports com.example.melodyhub ;
}