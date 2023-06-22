package com.example.melodyhub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class SongsListController {

    @FXML
    private TableView<Song> songs_table;

    @FXML
    public void fillList(List<Song> songList) {

        TableColumn<Song, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(340);

        TableColumn<Song, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.setPrefWidth(200);

        TableColumn<Song, Double> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.setPrefWidth(100);

        TableColumn<Song, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearColumn.setPrefWidth(100);

        TableColumn<Song, Double> rateColumn = new TableColumn<>("Rate");
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        rateColumn.setPrefWidth(100);

        songs_table.getColumns().addAll(nameColumn, genreColumn, durationColumn, yearColumn, rateColumn);
        ObservableList<Song> list = FXCollections.observableList(songList);
        songs_table.setItems(list);
    }
}
