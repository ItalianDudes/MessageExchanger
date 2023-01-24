package it.italiandudes.message_exchanger.server.runnable;

import it.italiandudes.idl.common.Logger;

import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("unused")
public final class OnlineServer extends Thread {

    //Attributes
    private static OnlineServer onlineServerInstance = null;
    private final ServerSocket serverSocket;

    //Constructors
    private OnlineServer(int port) throws Exception {
        this.serverSocket = new ServerSocket(port);
        this.setDaemon(true);
    }

    //Singleton Getter
    public static OnlineServer getInstance(int port) throws Exception {
        if(onlineServerInstance==null){
            onlineServerInstance = new OnlineServer(port);
        }
        return onlineServerInstance;
    }

    //Methods
    @Override
    public void run() {
        Socket connection;
        //noinspection InfiniteLoopStatement
        while (true) {
            try{
                connection = serverSocket.accept();
                Logger.log(connection.getInetAddress().getHostAddress()+":"+connection.getPort()+" has been connected, launching authenticator...");
                new Authenticator(connection).start();
            }catch (Exception ignored){}
        }
    }

}
