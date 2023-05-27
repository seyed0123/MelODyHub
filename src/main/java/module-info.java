module com.example.melodyhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.sql;
    requires com.google.gson;


    opens com.example.melodyhub to javafx.fxml;
    exports com.example.melodyhub ;
}