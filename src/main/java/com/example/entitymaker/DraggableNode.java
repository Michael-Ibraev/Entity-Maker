package com.example.entitymaker;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DraggableNode extends AnchorPane {

    @FXML AnchorPane root_pane;
    private EventHandler mContextDragOver;
    private EventHandler mContextDragDropped;
    private DragIconType mType = null;

    private Point2D mDragOffset = new Point2D(0.0, 0.0);
    @FXML private Label title_bar;
    @FXML private Label close_button;
    private final DraggableNode self;
    public DraggableNode(){
        self = this;
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("DraggableNode.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (IOException exception){
            throw  new RuntimeException(exception);
        }
    }
    @FXML
    private  void initialize(){
        buildNodeDragHandlers();
    }
    public void relocateToPoint(Point2D p){
        Point2D localCoords = getParent().sceneToLocal(p);
        relocate(
                (int) (localCoords.getX() - mDragOffset.getX()),
                (int) (localCoords.getY() - mDragOffset.getY())
        );
    }

    public DragIconType getType(){ return mType;}
    public void setType(DragIconType type){
        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");
        switch (mType){

            case blue:
                getStyleClass().add("icon-blue");
                break;

            case red:
                getStyleClass().add("icon-red");
                break;

            case green:
                getStyleClass().add("icon-green");
                break;

            case grey:
                getStyleClass().add("icon-grey");
                break;

            case purple:
                getStyleClass().add("icon-purple");
                break;

            case yellow:
                getStyleClass().add("icon-yellow");
                break;

            case black:
                getStyleClass().add("icon-black");
                break;

            default:
                break;
        }
    }
    public void buildNodeDragHandlers(){
        title_bar.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver(mContextDragOver);
                getParent().setOnDragDropped(mContextDragDropped);

                mDragOffset = new Point2D(event.getX(), event.getY());

                relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData("type", mType.toString());
                content.put(DragContainer.DragNode, container);

                startDragAndDrop(TransferMode.ANY).setContent(content);
                event.consume();
            }
        });

        mContextDragOver = new EventHandler <DragEvent> () {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                event.consume();
            }
        };

        mContextDragDropped = new EventHandler <DragEvent> () {
            @Override
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                event.setDropCompleted(true);
                event.consume();
            }
        };

        close_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnchorPane parent = (AnchorPane) self.getParent();
                parent.getChildren().remove(self);
            }
        });
    }
}