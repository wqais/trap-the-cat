package sendable;

import java.io.Serializable;

public class PlayerInfo implements Serializable{
    private String name;
    private int playerType;
    private boolean available;

    public PlayerInfo(String name, int playerType, boolean available) {
        this.name = name;
        this.playerType = playerType;
        this.available = available;
    }

    public PlayerInfo() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public int getPlayerType() {
        return playerType;
    }

    public boolean isAvailable() {
        return available;
    }
}
