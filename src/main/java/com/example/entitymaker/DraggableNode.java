package com.example.entitymaker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class DraggableNode extends AnchorPane {

    @FXML AnchorPane node_body;

    private final List mLinkIds = new ArrayList();
    private NodeLink mDragLink = null;
    private AnchorPane right_pane = null;
    private EventHandler <MouseEvent> mLinkHandlerDragDetected;
    private EventHandler <DragEvent> mLinkHandleDragDropped;
    private EventHandler <DragEvent> mContextLinkDragOver;
    private EventHandler <DragEvent> mContextLinkDragDropped;

    @FXML AnchorPane root_pane;
    private EventHandler mContextDragOver;
    private EventHandler mContextDragDropped;
    private DragIconType mType = null;

    private Point2D mDragOffset = new Point2D(0.0, 0.0);
    @FXML ImageView reload_img;
    @FXML public Label title_bar;
    @FXML private Label close_button;
    @FXML public TextField table_name;
    @FXML public Button submit_button;
    private final DraggableNode self;
    public DraggableNode(){
        setId(UUID.randomUUID().toString());
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
    private void initialize(){
        root_pane.isResizable();

        buildNodeDragHandlers();
        buildLinkHandlers();

        node_body.setOnDragDetected(mLinkHandlerDragDetected);
        node_body.setOnDragDetected(mLinkHandlerDragDetected);

        node_body.setOnDragDropped(mLinkHandleDragDropped);
        node_body.setOnDragDropped(mLinkHandleDragDropped);

        mDragLink = new NodeLink();
        mDragLink.setVisible(false);

        parentProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable,
                                Object oldValue, Object newValue){
                right_pane = (AnchorPane) getParent();
            }
        });

        reload_img.setImage(new Image("reload.png"));

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

                for(ListIterator <String> iterId = mLinkIds.listIterator(); iterId.hasNext();){
                    String id = iterId.next();

                    for(ListIterator <Node> iterNode = parent.getChildren().listIterator();iterNode.hasNext();){
                        Node node = iterNode.next();

                        if(node.getId() == null)
                            continue;
                        if(node.getId().equals(id))
                            iterNode.remove();
                    }
                    iterId.remove();
                }
            }
        });
        title_bar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TableEditor.tableName = title_bar.getText();
            }
        });
        reload_img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                title_bar.setText(TableEditor.tableName);
            }
        });



    }

    private void buildLinkHandlers(){
        mLinkHandlerDragDetected = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver(mContextLinkDragOver);
                getParent().setOnDragDropped(mContextDragDropped);

                right_pane.getChildren().add(0, mDragLink);

                mDragLink.setVisible(false);

                Point2D p = new Point2D(
                        getLayoutX() + (getWidth() / 2.0),
                        getLayoutY() + (getHeight() / 2.0)
                );

                mDragLink.setStart(p);

                /* *** */
                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

//                AnchorPane link_handle = (AnchorPane) event.getSource();
//                DraggableNode parent = (DraggableNode) link_handle.getParent().getParent().getParent();

                container.addData("source", getId());

                content.put(DragContainer.AddLink, container);

                /*parent.*/startDragAndDrop(TransferMode.ANY).setContent(content);
                event.consume();
                /* *** */
            }
        };

        mLinkHandleDragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                DragContainer container =
                        (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

                if(container == null){
                    return;
                }

                mDragLink.setVisible(false);
                right_pane.getChildren().remove(0);

                AnchorPane link_handle = (AnchorPane) event.getSource();
               /**/DraggableNode parent = (DraggableNode) link_handle.getParent().getParent();

                ClipboardContent content = new ClipboardContent();

                container.addData("target", getId());

                content.put(DragContainer.AddLink, container);

                event.getDragboard().setContent(content);

                event.setDropCompleted(true);
                event.consume();
            }
        };

        mContextLinkDragOver = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);

                if(!mDragLink.isVisible()){
                    mDragLink.setVisible(true);
                }

                mDragLink.setEnd(new Point2D(event.getX(), event.getY()));

                event.consume();
            }
        };

        mContextLinkDragDropped = new EventHandler <DragEvent> () {
            @Override
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                mDragLink.setVisible(false);
                right_pane.getChildren().remove(0);

                event.setDropCompleted(true);
                event.consume();
            }
        };
    }

    public void registerLink(String linkId){
        mLinkIds.add(linkId);
    }
}