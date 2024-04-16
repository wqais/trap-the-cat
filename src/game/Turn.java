package game;

public class Turn {
    private int turn = 0;

    public int getTurn() {
        return turn;
    }

    public void toggle() {
        turn ^= 1;
    }
}
