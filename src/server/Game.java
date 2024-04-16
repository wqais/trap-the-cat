package server;

import game.Turn;
import sendable.Cell;
import sendable.HighScore;
import sendable.PlayerInfo;
import util.FileIO;
import util.NetworkUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Game implements Runnable {
    public static final int NUMBER_OF_BLOCKED_CELL = 7;
    private NetworkUtil trapper;
    private NetworkUtil cat;
    private PlayerInfo trapperInfo;
    private PlayerInfo catInfo;

    public Game(NetworkUtil trapper, NetworkUtil cat, PlayerInfo trapperInfo, PlayerInfo catInfo) {
        this.trapper = trapper;
        this.cat = cat;
        this.trapperInfo = trapperInfo;
        this.catInfo = catInfo;

        new Thread(this).start();
    }

    public void run() {

        Random random = new Random();

        /*for (int i = 0; i < NUMBER_OF_BLOCKED_CELL; i++) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = new Cell(x, y);
            trapper.write(cell);
            cat.write(cell);
            System.out.println(cell.getX() + " - " + cell.getY() + " blocked");
        }*/

        int checker[] = new int[4];
        int cnt = 0;

        while(true) {

            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (x==5 && y==5) continue;
            cnt++;

            Cell cell = new Cell(x, y);
            trapper.write(cell);
            cat.write(cell);
            System.out.println(cell.getX() + " - " + cell.getY() + " blocked");

            if(x <= 5 && y <= 5) checker[0] = 1;
            else if(x <= 5) checker[1] = 1;
            else if(y <= 5) checker[2] = 1;
            else checker[3] = 1;

            int sum = 0;
            for(int i = 0; i < 4; i++) sum += checker[i];

            if(sum == 4 && cnt >= 6) break;
        }

        trapper.write(new Cell(-1, -1));
        cat.write(new Cell(-1, -1));

        Turn turn = new Turn();

        int moveCount = 0;

        while (true) {
            Cell cell;
            if (turn.getTurn()==0) {
                cell = (Cell) trapper.read();
                cat.write(cell);

                System.out.println(trapperInfo.getName() + " blocked (" + cell.getX() + "," + cell.getY() + ")");
            }
            else {
                cell = (Cell) cat.read();
                trapper.write(cell);

                System.out.println(catInfo.getName() + " moved to (" + cell.getX() + "," + cell.getY() + ")");
            }

            if (cell.getX()==-1 && cell.getY()==-1) {
                if (turn.getTurn()==1) System.out.println("Trapper has won!");
                else System.out.println("Cat has won!");
                System.out.println("Game finished!");
                saveScoreToFile(turn.getTurn()^1, moveCount);
                break;
            }

            moveCount++;
            turn.toggle();
        }
    }

    private void saveScoreToFile(int winnerType, int moveCount) {
        moveCount = (moveCount+1)/2;
        PlayerInfo winner;
        String fileName;

        if (winnerType==1) {
            winner = catInfo;
            fileName = FileIO.CATS_FILE_NAME;
        }
        else {
            winner = trapperInfo;
            fileName = FileIO.TRAPPERS_FILE_NAME;
        }

        ArrayList<HighScore> highScores = new ArrayList<>();
        highScores.add(new HighScore(winner.getName(), moveCount));

        FileIO.readFile(highScores, fileName);

        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore o1, HighScore o2) {
                if (o1.getMoves()==o2.getMoves()) {
                    return 0;
                } else {
                    return o1.getMoves() < o2.getMoves() ? -1 : 1;
                }
            }
        });

        FileIO.writeFile(highScores, fileName);
    }
}
