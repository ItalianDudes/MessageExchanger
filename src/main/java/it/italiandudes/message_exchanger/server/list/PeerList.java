package it.italiandudes.message_exchanger.server.list;

import it.italiandudes.idl.common.Peer;

import java.util.ArrayList;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class PeerList {

    //List
    private static final ArrayList<Peer> peerList = new ArrayList<>();

    //Methods
    public static Peer getPeer(String username){
        for(Peer peer : peerList){
            if(peer.getCredential().getUsername().equals(username)){
                return peer;
            }
        }
        return null;
    }
    public static boolean addPeer(Peer newPeer){
        for(Peer peer : peerList){
            if(peer.getCredential().equals(newPeer.getCredential())) return false;
        }
        return peerList.add(newPeer);
    }
    public static boolean removePeer(String username){
        Peer peer = getPeer(username);
        if(peer == null) return false;
        return peerList.remove(peer);
    }
    public static void clearPeerList(){
        for(Peer peer : peerList){
            try {
                peer.getPeerSocket().close();
            }catch (Exception ignored){}
        }
        peerList.clear();
    }

}
