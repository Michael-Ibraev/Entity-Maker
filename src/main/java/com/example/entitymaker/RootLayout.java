package com.example.entitymaker;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Objects;

import javafx.geometry.Point2D;
import javafx.stage.Stage;

public class RootLayout extends AnchorPane{
    private EventHandler mIconDragOverRoot = null;
    private EventHandler update = null;
    private EventHandler mIconDragDropped = null;
    private EventHandler mIconDragOverRightPane = null;
    @FXML SplitPane base_pane;
    @FXML SplitPane editor_pane;
    @FXML AnchorPane right_pane;
    @FXML VBox left_pane;
    @FXML Button refresh_button;
    @FXML Button submit_button;
    @FXML TextField table_name;
    @FXML Button edit_button;
    private DragIcon mDragOverIcon = null;
    public RootLayout(){

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("RootLayout.fxml")
        );
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize(){
        mDragOverIcon = new DragIcon();

        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        getChildren().add(mDragOverIcon);
        DragIcon icn = new DragIcon();
        addDragDetection(icn);
        icn.setType(DragIconType.values()[6]);
        left_pane.getChildren().add(icn);

        buildDragHandlers();
        dataUpdate();
    }

    private void addDragDetection(DragIcon dragIcon){
        dragIcon.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                base_pane.setOnDragOver(mIconDragOverRoot);
                right_pane.setOnDragOver(mIconDragOverRightPane);
                right_pane.setOnDragDropped(mIconDragDropped);

                DragIcon icn = (DragIcon) event.getSource();

                mDragOverIcon.setType(icn.getType());
                mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData("type", mDragOverIcon.getType().toString());
                content.put(DragContainer.AddNode, container);

                mDragOverIcon.startDragAndDrop(TransferMode.ANY).setContent(content);
                mDragOverIcon.setVisible(true);
                mDragOverIcon.setMouseTransparent(true);
                event.consume();
            }
        });
    }
    private void buildDragHandlers(){
        mIconDragOverRoot = new EventHandler <DragEvent>(){
            @Override
            public void handle(DragEvent event){
                Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

                if(!right_pane.boundsInLocalProperty().get().contains(p)){
                    mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                    return;
                }
                event.consume();
            }
        };

        mIconDragOverRightPane = new EventHandler <DragEvent>(){
            @Override
            public void handle(DragEvent event){
                event.acceptTransferModes(TransferMode.ANY);

                mDragOverIcon.relocateToPoint(
                        new Point2D(event.getSceneX(), event.getSceneY())
                );
                event.consume();
            }
        };

        mIconDragDropped = new EventHandler <DragEvent> (){
            @Override
            public void handle(DragEvent event){
                DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

                container.addData("scene_coords", new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                content.put(DragContainer.AddNode, container);

                event.getDragboard().setContent(content);
                event.setDropCompleted(true);
            }
        };

        this.setOnDragDone(new EventHandler <DragEvent> () {
            @Override
            public void handle(DragEvent event) {
                right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
                right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
                base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

                mDragOverIcon.setVisible(false);

                DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

                if(container != null){
                    if(container.getValue("scene_coords") != null){

                        DraggableNode node = new DraggableNode();

                        node.setType(DragIconType.valueOf(container.getValue("type")));
                        right_pane.getChildren().add(node);

                        Point2D cursorPoint = container.getValue("scene_coords");
                        node.relocateToPoint(
                                new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)

                        );
                    }
                }

                container = (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);
                if(container != null){
                    String sourceId = container.getValue("source");
                    String targetId = container.getValue("target");

                    if (sourceId != null && targetId != null){
                        NodeLink link = new NodeLink();

                        right_pane.getChildren().add(0, link);

                        DraggableNode source = null;
                        DraggableNode target = null;

                        for(Node n: right_pane.getChildren()){
                            if(n.getId() == null)
                                continue;
                            if(n.getId().equals(sourceId))
                                source = (DraggableNode) n;
                            if(n.getId().equals(targetId))
                                target = (DraggableNode) n;
                        }
                        if (source != null && target != null)
                            link.bindEnds(source, target);
                    }

                }
                event.consume();
            }
        });
    }


    public void dataUpdate(){
        refresh_button.setOnMouseClicked(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                table_name.setText(TableEditor.nameMap.get(TableEditor.tableId));
            }
        });
        submit_button.setOnMouseClicked(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                TableEditor.nameMap.put(TableEditor.tableId, table_name.getText());
            }
        });
    }

    public void toTableConstructor() throws IOException {
        Stage tableConstructor = new Stage();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("tableConstructor.fxml")));
        tableConstructor.setTitle("Table Constructor");
        tableConstructor.setScene(new Scene(parent, 525, 450));
        tableConstructor.setResizable(false);
        tableConstructor.show();
    }
}

