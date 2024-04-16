package sendable;

import java.io.Serializable;

public class RequestType implements Serializable {
    public static final int SHOW_HIGH_SCORE = 0;
    public static final int CREATE_GAME = 1;
    public static final int JOIN_GAME = 2;
    public static final int REQUEST_ACCEPTED = 3;
    public static final int REQUEST_DENIED = 4;
    public static final int SEND_ADMINS_LIST = 5;
    public static final int RECEIVE_PLAYER_INDEX = 6;

    private int type;

    public RequestType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
