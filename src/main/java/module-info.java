module com.example.melodyhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.sql;
    requires com.google.gson;
    requires java.mail;
    requires googleauth;
    requires org.apache.commons.codec;
    requires dropbox.core.sdk;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;
    requires org.testng;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires javafx.media;

    opens com.example.melodyhub to javafx.fxml;
    exports com.example.melodyhub ;

}