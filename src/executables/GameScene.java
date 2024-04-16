package executables;

import game.Grid;
import game.Turn;
import game.player.Cat;
import game.player.Trapper;
import javafx.scene.Group;
import javafx.scene.Scene;
import sendable.Cell;
import sendable.PlayerInfo;
import server.Game;
import util.NetworkUtil;

import java.util.Random;
import java.util.Scanner;

public class GameScene {
    private PlayerInfo selfInfo;
    private PlayerInfo othersInfo;
    private NetworkUtil server;

    public GameScene(PlayerInfo selfInfo, PlayerInfo othersInfo, NetworkUtil server) {
        this.selfInfo = selfInfo;
        this.othersInfo = othersInfo;
        this.server = server;

        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);

        Turn turn = new Turn();
        Grid grid = new Grid(scene, root, turn, selfInfo.getPlayerType());

        Trapper trapper;
        Cat cat;

        if (selfInfo.getPlayerType()==0) {
            trapper = new Trapper(grid, server, selfInfo.getName(), true);
            cat = new Cat(grid, server, othersInfo.getName(), false);
            Main.window.setTitle("Trap The Cat - Trapper");
        } else {
            trapper = new Trapper(grid, server, othersInfo.getName(), false);
            cat = new Cat(grid, server, selfInfo.getName(), true);
            Main.window.setTitle("Trap The Cat - Cat");
        }

        /*for (int i = 0; i < Game.NUMBER_OF_BLOCKED_CELL; i++) {
            Cell cell = (Cell) server.read();
            if (cell.getX()==5 && cell.getY()==5) continue;
            grid.block(cell.getX(), cell.getY());
            //System.out.println(cell.getX() + " - " + cell.getY() + " blocked");
        }*/

        while(true) {
            Cell cell = (Cell) server.read();
            if(cell.getX() == -1 && cell.getY() == -1) break;
            grid.block(cell.getX(), cell.getY());
            //System.out.println(cell.getX() + " - " + cell.getY() + " blocked");

        }

        Main.window.setScene(scene);

        Flow flow = new Flow(trapper, cat, turn, scene, root);
    }

    public GameScene() {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);

        Turn turn = new Turn();
        Grid grid = new Grid(scene, root, turn, 0);

        Trapper trapper = new Trapper(grid, null, "Trapper", true);
        Cat cat = new Cat(grid, null, "Cat", false);

        Random random = new Random();

        int checker[] = new int[4];
        int cnt = 0;

        while(true) {

            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (x==5 && y==5) continue;
            grid.block(x, y);
            cnt++;

            if(x <= 5 && y <= 5) checker[0] = 1;
            else if(x <= 5) checker[1] = 1;
            else if(y <= 5) checker[2] = 1;
            else checker[3] = 1;

            int sum = 0;
            for(int i = 0; i < 4; i++) sum += checker[i];

            if(sum == 4 && cnt >= 6) break;
        }

        /*for (int i = 0; i < Game.NUMBER_OF_BLOCKED_CELL; i++) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (x==5 && y==5) continue;
            grid.block(x, y);

            System.out.println(x + " - " + y + " blocked");
        }*/

        Main.window.setScene(scene);
        Flow flow = new Flow(trapper, cat, turn, scene, root);
    }
}
