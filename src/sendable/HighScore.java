package sendable;

import java.io.Serializable;

public class HighScore implements Serializable {
    private String name;
    private int moves;

    public HighScore(String name, int moves) {
        this.name = name;
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }
}
