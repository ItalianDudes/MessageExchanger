package it.italiandudes.message_exchanger.client;

import it.italiandudes.idl.common.*;
import it.italiandudes.message_exchanger.MessageExchanger;
import it.italiandudes.message_exchanger.client.javafx.JFXDefs;
import it.italiandudes.message_exchanger.client.javafx.scene.SceneStartup;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.Scanner;

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
        Scanner scan = new Scanner(System.in);
        String[] domainAndPort;
        String domain = null, username, password;
        int port = 0;
        boolean correct;
        do {
            correct = true;
            try {
                System.out.print("Inserire il dominio con formato \"dominio:porta\" : ");
                domainAndPort = scan.nextLine().split(":");
                if(domainAndPort.length != 2)throw new Exception();
                port = Integer.parseInt(domainAndPort[1]);
                if (port <= 0) throw new Exception();
                domain = domainAndPort[0];
            }catch (Exception e){
                correct = false;
                System.out.println("Errore, assicurarsi di inserire un dominio che rispetti il formato \"dominio:porta\" e che la porta sia compresa tra 1 e 65535.");
            }
        }while (!correct);
        do {
            System.out.print("Inserire lo username: ");
            username = scan.nextLine();

        }while (username == null || username.equals("") || !isStringValid(username));
        password = DigestUtils.sha512Hex(readPassword());
        Credential credentials = new Credential(username, password, false);
        Socket serverConnection;
        try {
            serverConnection = new Socket(domain, port);
        }catch (Exception e){
            Logger.log(e);
            return MessageExchanger.Defs.ReturnCodes.CLIENT_CONNECTION_ATTEMPT_FAIL;
        }
        try {
            RawSerializer.sendString(serverConnection.getOutputStream(), MessageExchanger.Defs.Protocol.PROTOCOL_AUTH);
            RawSerializer.sendObject(serverConnection.getOutputStream(), credentials);
            if(RawSerializer.receiveBoolean(serverConnection.getInputStream())){
                Peer peer = new Peer(serverConnection, credentials);
                PeerSerializer.sendString(peer, MessageExchanger.Defs.Protocol.PROTOCOL_GET);
                String message = PeerSerializer.receiveString(peer);
                PeerSerializer.sendString(peer, MessageExchanger.Defs.Protocol.PROTOCOL_DISC);
                try {
                    serverConnection.close();
                }catch (Exception ignored){}
                System.out.println("Message: "+message);
            }else {
                try{
                    serverConnection.close();
                }catch (Exception ignored){}
                System.out.println("Authentication failed!");
                return MessageExchanger.Defs.ReturnCodes.CLIENT_AUTH_FAIL;
            }
        }catch (Exception e){
            try{
                serverConnection.close();
            }catch (Exception ignored){}
            Logger.log(e);
            return MessageExchanger.Defs.ReturnCodes.CLIENT_COM_FAIL;
        }
        return 0;
    }

    //Password Reader compat (IDE-Safe with no obfuscation)
    private static String readPassword(){
        String pwd;
        if(System.console() == null){
            System.out.println("WARNING: YOU'RE RUNNING THE APP INSIDE AN IDE, THE PASSWORD WILL BE DISPLAYED ON WRITING");
            Scanner scan = new Scanner(System.in);
            do {
                System.out.print("Inserire la password: ");
                pwd = scan.nextLine();
            }while (pwd == null || pwd.equals("") || !isStringValid(pwd));
        }else{
            do {
                System.out.print("Inserire la password: ");
                pwd = String.valueOf(System.console().readPassword());
            }while (pwd.equals("") || !isStringValid(pwd));
        }
        return pwd;
    }
    //String validator for Username and Password
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isStringValid(String str){
        return !str.contains("\n") && !str.contains(" ");
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
