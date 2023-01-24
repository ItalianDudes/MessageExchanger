package it.italiandudes.message_exchanger.client.javafx.alert;

import it.italiandudes.message_exchanger.client.javafx.JFXDefs;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public final class InformationAlert extends Alert {

    public InformationAlert(String title, String header, String content, boolean showAndWait){
        super(AlertType.INFORMATION);
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(JFXDefs.AppInfo.LOGO);
        if(title!=null) setTitle(title);
        if(header!=null) setHeaderText(header);
        if(content!=null) setContentText(content);
        if(showAndWait) showAndWait();
        else show();
    }
    public InformationAlert(String title, String header, String content){
        this(title, header, content, false);
    }
    public InformationAlert(String header, String content){
        this(null, header, content, false);
    }
    public InformationAlert(){
        this(null,null,null, false);
    }

}
