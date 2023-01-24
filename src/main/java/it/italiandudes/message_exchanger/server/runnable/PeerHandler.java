package it.italiandudes.message_exchanger.server.runnable;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.Peer;
import it.italiandudes.idl.common.PeerSerializer;
import it.italiandudes.message_exchanger.MessageExchanger.Defs.Protocol;
import it.italiandudes.message_exchanger.server.Server;
import it.italiandudes.message_exchanger.server.list.PeerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class PeerHandler extends Thread {

    //Attributes
    private final Peer peer;

    //Constructors
    public PeerHandler(@NotNull Peer peer) {
        this.peer = peer;
        setDaemon(true);
    }

    //Methods
    @NotNull
    public Peer getPeer() {
        return peer;
    }

    //Thread Method
    @Override
    public void run(){

        try {
            while (!peer.getPeerSocket().isClosed()) {

                String protocol = PeerSerializer.receiveString(peer);

                switch (protocol) {
                    case Protocol.PROTOCOL_GET:
                        Logger.log("["+peer.getCredential().getUsername()+"] Asked for requested message.");
                        PeerSerializer.sendString(peer, Server.getMessage());
                        break;
                    case Protocol.PROTOCOL_DISC:
                        Logger.log("["+peer.getCredential().getUsername()+"] Disconnected from server.");
                        try{
                            peer.getPeerSocket().close();
                        }catch (Exception ignored){}
                        break;
                    default:
                        throw new Exception("Protocol not Respected!");
                }

            }
        }catch (Exception e){
            Logger.log("["+peer.getCredential().getUsername()+"] An error has occurred, the connection has been terminated.");
            try{
                peer.getPeerSocket().close();
            }catch (Exception ignored){}
            PeerList.removePeer(peer.getCredential().getUsername());
        }
    }
}
