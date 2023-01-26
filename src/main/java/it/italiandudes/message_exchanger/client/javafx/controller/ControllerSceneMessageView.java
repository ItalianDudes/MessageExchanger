package it.italiandudes.message_exchanger.client.javafx.controller;

import it.italiandudes.idl.common.PeerSerializer;
import it.italiandudes.message_exchanger.MessageExchanger;
import it.italiandudes.message_exchanger.client.Client;
import it.italiandudes.message_exchanger.client.javafx.alert.ErrorAlert;
import it.italiandudes.message_exchanger.client.javafx.scene.SceneStartup;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

@SuppressWarnings("unused")
public final class ControllerSceneMessageView {

    //Attributes

    //Graphic Elements
    @FXML private AnchorPane mainPane;
    @FXML private TextArea messageArea;
    @FXML private Button messageGetterButton;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
    }

    //EDT Methods
    @FXML
    private void getMessageFromServer(){
        Service<Void> messageGetterService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        assert Client.getPeer()!=null;
                        try {
                            PeerSerializer.sendString(Client.getPeer(), MessageExchanger.Defs.Protocol.PROTOCOL_GET);
                            String message = PeerSerializer.receiveString(Client.getPeer());
                            Platform.runLater(() -> {
                                messageArea.setText(message);
                                messageGetterButton.setDisable(true);
                            });
                        }catch (Exception e){
                            try{
                                Client.getPeer().getPeerSocket().close();
                            }catch (Exception ignored){}
                            Client.setPeer(null);
                            Platform.runLater(() -> {
                                new ErrorAlert("Errore", "Errore di connessione col server", "Si è verificato un errore durante il recupero del messaggio. La connessione è stata chiusa.");
                                Client.getStage().setScene(SceneStartup.getScene());
                            });
                        }
                        return null;
                    }
                };
            }
        };
        messageGetterService.start();
    }
    @FXML
    private void disconnectFromServer(){
        Service<Void> disconnectService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        assert Client.getPeer()!=null;
                        try {
                            PeerSerializer.sendString(Client.getPeer(), MessageExchanger.Defs.Protocol.PROTOCOL_DISC);
                        }catch (Exception ignored){}
                        try{
                            Client.getPeer().getPeerSocket().close();
                        }catch (Exception ignored){}
                        Client.setPeer(null);
                        Platform.runLater(() -> Client.getStage().setScene(SceneStartup.getScene()));
                        return null;
                    }
                };
            }
        };
        disconnectService.start();
    }

}
