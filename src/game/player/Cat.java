package game.player;

import game.Britto;
import sendable.Cell;
import game.Grid;
import util.NetworkUtil;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Cat extends Player {
    private boolean isHuman;

    public Cat(Grid grid, NetworkUtil server, String name, boolean isHuman) {
        super(grid, server, name);
        this.isHuman = isHuman;
    }

    public void makeMove() {
        Cell cell;
        if (isHuman==false) {
            if (server==null) cell = moveByAI();
            else cell = (Cell) super.readFromServer();
        } else {
            while (true) {
                if (Britto.getClickedCell() != null) {
                    cell = new Cell(Britto.getClickedCell().getX(), Britto.getClickedCell().getY());
                    Britto.setClickedCell(null);
                    break;
                }
            }
            if (server!=null) super.writeToServer(cell);
        }
        System.out.println("Cat moved to (" + cell.getX() + "," + cell.getY() + ")");

        grid.moveTo(cell.getX(), cell.getY());
    }

    public Cell moveByAI() {
        return moveByThinkingAhead();
        //return moveByShortestPath();
        //return moveByRandom();
    }

    public Cell moveByThinkingAhead() {

        int cX = grid.getCatPosition().getX(), cY = grid.getCatPosition().getY();
        int minIndex = -1, minD = 2000;

        for(int i = 0; i < 6; i++) {

            int x, y;

            if(cX%2 == 0) {
                x = cX+Britto.dxEven[i];
                y = cY+Britto.dyEven[i];
            }
            else {
                x = cX+Britto.dxOdd[i];
                y = cY+Britto.dyOdd[i];
            }

            if(grid.getStausOfBritto(x, y)) continue;
            if(minIndex == -1) minIndex = i;

            grid.moveToWithoutGraphics(x, y);

            int tempD = 0;

            for (int p = 0; p < 11; p++) {
                for (int q = 0; q < 11; q++) {
                    if (grid.getStausOfBritto(p, q)) continue;

                    grid.blockWithoutGraphics(p, q);
                    tempD += bfs(x, y, grid);
                    grid.unblockWithoutGraphics(p, q);
                }
            }

            grid.moveToWithoutGraphics(cX, cY);

            if(tempD == minD) {
                Random random = new Random();
                int choice = random.nextInt(2);
                if(choice%2 == 1) {
                    minD = tempD;
                    minIndex = i;
                }
            } else if(tempD < minD) {
                minD = tempD;
                minIndex = i;
            }
        }

        System.out.println("score = " + minD);

        if (minD==2000) {
            return moveByShortestPath();
        }

        int x, y;
        if(cX%2 == 0) {
            x = cX+Britto.dxEven[minIndex];
            y = cY+Britto.dyEven[minIndex];
        }
        else {
            x = cX+Britto.dxOdd[minIndex];
            y = cY+Britto.dyOdd[minIndex];
        }

        return new Cell(x, y);
    }

    public Cell moveByShortestPath() {

        int cX = grid.getCatPosition().getX(), cY = grid.getCatPosition().getY();
        int minIndex = -1, minD = 2000;

        for(int i = 0; i < 6; i++) {

            int x, y;

            if(cX%2 == 0) {
                x = cX+Britto.dxEven[i];
                y = cY+Britto.dyEven[i];
            }
            else {
                x = cX+Britto.dxOdd[i];
                y = cY+Britto.dyOdd[i];
            }

            if(grid.getStausOfBritto(x, y)) continue;
            if(minIndex == -1) minIndex = i;

            int tempD = bfs(x, y, grid);

            if(tempD == minD) {
                Random random = new Random();
                int choice = random.nextInt(2);
                if(choice%2 == 1) {
                    minD = tempD;
                    minIndex = i;
                }
            }

            if(tempD < minD) {
                minD = tempD;
                minIndex = i;
            }
        }

        //System.out.println(minIndex);

        int x, y;
        if(cX%2 == 0) {
            x = cX+Britto.dxEven[minIndex];
            y = cY+Britto.dyEven[minIndex];
        }
        else {
            x = cX+Britto.dxOdd[minIndex];
            y = cY+Britto.dyOdd[minIndex];
        }

        return new Cell(x, y);
    }

    public Cell moveByRandom() {
        Random random = new Random();
        Cell catPosition = grid.getCatPosition();

        while (true) {
            int idx = random.nextInt(5);
            int x, y;
            if (catPosition.getX() % 2 == 0) {
                x = catPosition.getX() + Britto.dxEven[idx];
                y = catPosition.getY() + Britto.dyEven[idx];
            } else {
                x = catPosition.getX() + Britto.dxOdd[idx];
                y = catPosition.getY() + Britto.dyOdd[idx];
            }

            if (grid.getStausOfBritto(x, y) == false) {
                return new Cell(x, y);
            }
        }
    }

    public static int bfs(int sX, int sY, Grid grid) {

        int[][] dist = new int[11][11];
        int[][] col = new int[11][11];

        for(int i = 0; i < 11; i++) {
            for(int j = 0; j < 11; j++) {
                dist[i][j] = 2000;
                if(grid.getStausOfBritto(i, j)) col[i][j] = 1;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        dist[sX][sY] = 0;
        col[sX][sY] = 1;
        queue.add(sX);
        queue.add(sY);

        while(!queue.isEmpty()) {
            int x = queue.remove();
            int y = queue.remove();

            for(int i = 0; i < 6; i++) {

                int p, q;

                if(x%2 == 0) {
                    p = x+Britto.dxEven[i];
                    q = y+Britto.dyEven[i];
                }
                else {
                    p = x+Britto.dxOdd[i];
                    q = y+Britto.dyOdd[i];
                }

                if(p < 0 || p > 10 || q < 0 || q > 10) continue;
                if(col[p][q] == 1) continue;

                dist[p][q] = dist[x][y]+1;
                col[p][q] = 1;
                queue.add(p);
                queue.add(q);
            }
        }

        int minDistance = 2000;
        for(int i = 0; i < 11; i++) {
            for(int j = 0; j < 11; j++) {
                if(i == 0 || i == 10 || j == 0 || j == 10) minDistance = Math.min(minDistance, dist[i][j]);
            }
        }
        return minDistance;

    }

    public boolean hasWon() {
        Cell catPosition = grid.getCatPosition();

        if (catPosition.getX()==0 || catPosition.getY() == 0 ||
                catPosition.getX()== 10 || catPosition.getY() == 10) return true;

        return false;
    }
}
