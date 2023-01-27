package it.italiandudes.message_exchanger.server;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.message_exchanger.MessageExchanger;
import it.italiandudes.message_exchanger.server.command.CommandHandler;
import it.italiandudes.message_exchanger.server.list.AuthorizedUserList;
import it.italiandudes.message_exchanger.server.list.PeerList;
import it.italiandudes.message_exchanger.server.runnable.OnlineServer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Scanner;

@SuppressWarnings("unused")
public final class Server {

    //Attributes
    @NotNull private static String message = "";
    private static int port;

    //Server Start Method
    public static int start(String[] args){

        readConfig();

        try{
            OnlineServer.getInstance(port).start();
        }catch (Exception e){
            Logger.log(e);
            return MessageExchanger.Defs.ReturnCodes.SERVER_START_FAIL;
        }

        Logger.log("Server opened at port: "+port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            PeerList.clearPeerList();
            writeConfigFile();
            AuthorizedUserList.updateAuthorizedUsers();
        }));

        Scanner scan = new Scanner(System.in);
        boolean running = true;
        String buffer;
        while (running){

            buffer = scan.nextLine();

            if(buffer.equals(MessageExchanger.Defs.Server.Commands.STOP)){
                running = false;
            }else {
                CommandHandler.handleCommand(buffer);
            }
        }

        return 0;
    }

    //Methods
    public static int getPort(){
        return port;
    }
    public static void setPort(int port){
        Server.port = port;
    }
    private static void writeConfigFile(){

        File configFile = new File(MessageExchanger.Defs.Server.CONFIG_FILE_PATH);

        BufferedWriter outFile;
        try {
            outFile = new BufferedWriter(new FileWriter(configFile));
        }catch (IOException e){
            Logger.log("Can't write on config file.");
            return;
        }

        try{
            outFile.append("port::").append(String.valueOf(port)).append('\n');
            outFile.append("message::").append(message);
            outFile.flush();
            outFile.close();
        }catch (IOException e){
            Logger.log("Error on config file writing.");
            try {
                outFile.close();
            }catch (Exception ignored){}
        }
    }
    private static void readConfig(){

        File serverDir = new File(MessageExchanger.Defs.Server.SERVER_DIR);
        if(!serverDir.exists() || !serverDir.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            serverDir.mkdir();
        }

        Scanner inFile;
        try {
            inFile = new Scanner(new File(MessageExchanger.Defs.Server.CONFIG_FILE_PATH));
        }catch (FileNotFoundException e){
            Logger.log("Config doesn't exist, will be used default port "+MessageExchanger.Defs.Server.DEFAULT_PORT);
            port = MessageExchanger.Defs.Server.DEFAULT_PORT;
            return;
        }
        String line = inFile.nextLine();
        try {
            port = Integer.parseInt(line.split("::")[1]);
        }catch (Exception e){
            Logger.log("Error during config file reading, will be used default port "+MessageExchanger.Defs.Server.DEFAULT_PORT);
            port = MessageExchanger.Defs.Server.DEFAULT_PORT;
        }
        String[] messageComposed = inFile.nextLine().split("::");
        StringBuilder msgBuilder = new StringBuilder();
        for(int i=1;i<messageComposed.length;i++){
            msgBuilder.append(messageComposed[i]);
        }
        message = msgBuilder.toString();
        inFile.close();
    }
    @NotNull
    public static String getMessage(){
        return message;
    }
    public static void setMessage(@NotNull String message){
        Server.message = message;
    }

}
