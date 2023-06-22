package com.example.melodyhub;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class SongHBox extends HBox {
    public SongHBox(Song song) {
        super();
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(0, 0, 0, 10));

        Label songName = new Label(song.getName());
        songName.setAlignment(Pos.CENTER_LEFT);
        songName.setFont(Font.font("Arial", 18));
        songName.setPrefWidth(225);

        Label songDuration = new Label(String.valueOf(song.getDuration()));
        songDuration.setAlignment(Pos.CENTER_LEFT);
        songDuration.setFont(Font.font("Arial", 12));
        songDuration.setPrefWidth(31);

        this.getChildren().add(songName);
        this.getChildren().add(songDuration);
    }
}
