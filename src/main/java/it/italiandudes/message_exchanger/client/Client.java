package it.italiandudes.message_exchanger.client;

import it.italiandudes.idl.common.Peer;
import it.italiandudes.message_exchanger.client.javafx.JFXDefs;
import it.italiandudes.message_exchanger.client.javafx.scene.SceneStartup;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class Client extends Application {

    //Attributes
    private static Stage stage = null;
    private static Peer peer;

    //JavaFX Start
    @Override
    public void start(Stage stage) {
        Client.stage = stage;
        stage.setTitle(JFXDefs.AppInfo.NAME);
        stage.getIcons().add(JFXDefs.AppInfo.LOGO);
        stage.setScene(SceneStartup.getScene());
        stage.show();
    }

    //Graphic Start Method
    public static int start(String[] args){
        launch(args);
        return 0;
    }

    //NoGui Start Method
    public static int noGuiStart(String[] args){
        return 0;
    }

    //Methods
    @NotNull
    public static Stage getStage(){
        return stage;
    }
    @Nullable
    public static Peer getPeer(){
        return peer;
    }
    public static void setPeer(@Nullable Peer peer){
        Client.peer = peer;
    }

}
