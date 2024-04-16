package game.player;

import sendable.Cell;
import game.Grid;
import util.NetworkUtil;

public abstract class Player {
    protected Grid grid;
    protected NetworkUtil server;
    protected String name;

    public Player(Grid grid, NetworkUtil server, String name) {
        this.grid = grid;
        this.server = server;
        this.name = name;
    }

    abstract public void makeMove();
    abstract public boolean hasWon();

    public Cell readFromServer() {
        return (Cell) server.read();
    }

    public void writeToServer(Cell cell) {
        if (server!=null) server.write(cell);
    }

    public String getName() {
        return name;
    }
}
