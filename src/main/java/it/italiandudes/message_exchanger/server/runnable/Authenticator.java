package it.italiandudes.message_exchanger.server.runnable;

import it.italiandudes.idl.common.Credential;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.Peer;
import it.italiandudes.idl.common.RawSerializer;
import it.italiandudes.message_exchanger.MessageExchanger;
import it.italiandudes.message_exchanger.server.list.AuthorizedUserList;
import it.italiandudes.message_exchanger.server.list.PeerList;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public final class Authenticator extends Thread {

    //Attributes
    @NotNull private final Socket connection;

    //Constructors
    public Authenticator(@NotNull Socket connection){
        this.connection = connection;
        setDaemon(true);
    }

    //Thread Method
    @Override
    public void run(){
        try{

            String protocol = RawSerializer.receiveString(connection.getInputStream());

            if(protocol.equals(MessageExchanger.Defs.Protocol.PROTOCOL_AUTH)){
                Credential credential = (Credential) RawSerializer.receiveObject(connection.getInputStream());
                if(AuthorizedUserList.isAuthorized(credential)){
                    Peer peer = new Peer(connection, credential);
                    if(PeerList.addPeer(peer)){
                        Logger.log("["+connection.getInetAddress().getHostAddress()+":"+connection.getPort()+"] Connected with username \""+credential.getUsername()+"\".");
                        RawSerializer.sendBoolean(connection.getOutputStream(), true);
                        new PeerHandler(peer).start();
                    }else{
                        Logger.log("["+connection.getInetAddress().getHostAddress()+":"+connection.getPort()+"] The user \""+credential.getUsername()+"\" is already connected, the connection has been terminated.");
                        RawSerializer.sendBoolean(connection.getOutputStream(), false);
                    }
                }else{
                    Logger.log("["+connection.getInetAddress().getHostAddress()+":"+connection.getPort()+"] Username or password mismatch, the connection has been terminated.");
                    RawSerializer.sendBoolean(connection.getOutputStream(), false);
                    try {
                        connection.close();
                    }catch (Exception ignored){}
                }
            }else{
                Logger.log("["+connection.getInetAddress().getHostAddress()+":"+connection.getPort()+"] Protocol not respected, the connection has been terminated.");
                RawSerializer.sendBoolean(connection.getOutputStream(), false);
                try {
                    connection.close();
                }catch (Exception ignored){}
            }

        }catch (Exception e){
            Logger.log(e);
            Logger.log("["+connection.getInetAddress().getHostAddress()+":"+connection.getPort()+"] An error has occurred, the connection has been terminated.");
            try {
                connection.close();
            }catch (Exception ignored){}
        }
    }

}
