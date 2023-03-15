package com.example.entitymaker;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    Button addBtn;
    @FXML
    AnchorPane draggableArea;

    public void newItem(){
        VBox vBox = new VBox();
        vBox.setStyle("-fx-border-color: black");
        vBox.setPrefWidth(50);
        vBox.setPrefHeight(50);
        draggableArea.getChildren().add(vBox);
        vBox.setOnMousePressed(e -> {
            startX = e.getSceneX() - vBox.getTranslateX();
            startY = e.getSceneY() - vBox.getTranslateY();
        });
        vBox.setOnMouseDragged(e -> {
            vBox.setTranslateX(e.getSceneX() - startX);
            vBox.setTranslateY(e.getSceneY() - startY);
        });
    }
    private double startX;
    private double startY;
    private void makeDraggable(Node node){
        node.setOnMousePressed(e -> {
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();
        });
        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - startX);
            node.setTranslateY(e.getSceneY() - startY);
        });
    }
}