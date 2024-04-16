package server;

import sendable.HighScore;
import sendable.RequestType;
import sendable.PlayerInfo;
import util.FileIO;
import util.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) {

        ArrayList<PlayerInfo> adminsList = new ArrayList<>();
        ArrayList<NetworkUtil> adminsNc = new ArrayList<>();

        try {
            ServerSocket ss = new ServerSocket(44444);
            while (true) {
                Socket cs = ss.accept();
                NetworkUtil nc = new NetworkUtil(cs);

                RequestType requestType = (RequestType) nc.read();

                if (requestType.getType()==RequestType.SHOW_HIGH_SCORE) {
                    showHighScore(nc);
                } else if (requestType.getType()==RequestType.CREATE_GAME) {
                    PlayerInfo playerInfo = (PlayerInfo) nc.read();
                    adminsList.add(playerInfo);
                    adminsNc.add(nc);

                    System.out.println(playerInfo.getName() + " has created " +
                            (String) (playerInfo.getPlayerType() == 0? " trapper" : " cat") );
                } else if (requestType.getType()==RequestType.JOIN_GAME) {
                    PlayerInfo playerInfo = (PlayerInfo) nc.read();

                    System.out.println(playerInfo.getName() + " has joined as " +
                            (String) (playerInfo.getPlayerType() == 0? " trapper" : " cat") );
                    new Kamla(adminsList, adminsNc, playerInfo, nc);
//                    cs.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Server status: " + e);
        }
    }

    private static void showHighScore(NetworkUtil nc) {
        System.out.println("request for showing high score");

        ArrayList<HighScore> highScores;

        highScores = new ArrayList<>();
        FileIO.readFile(highScores, FileIO.TRAPPERS_FILE_NAME);
        nc.write(highScores.toArray(new HighScore[highScores.size()]));


        highScores = new ArrayList<>();
        FileIO.readFile(highScores, FileIO.CATS_FILE_NAME);
        nc.write(highScores.toArray(new HighScore[highScores.size()]));
    }
}
