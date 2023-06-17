package com.example.melodyhub;

import com.example.melodyhub.User;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class UserPanelController {

    User userLogin;

    @FXML
    private AnchorPane mainPane;

    public void setPage(String page) {
        for (Node node : mainPane.getChildren()) {
            if (Objects.equals(node.getId(), page)) {
                node.setVisible(true);
                continue;
            }
            node.setVisible(false);
        }
    }
}
