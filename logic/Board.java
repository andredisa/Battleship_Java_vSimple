package logic;

import util.Constants;

public class Board {
    private final boolean[][] ships = new boolean[Constants.SIZE][Constants.SIZE];
    private int shipsLeft = 0;

    public boolean placeShip(int x, int y) {
        if (!ships[x][y]) {
            ships[x][y] = true;
            shipsLeft++;
            return true;
        }
        return false;
    }

    public boolean hit(int x, int y) {
        if (ships[x][y]) {
            ships[x][y] = false;
            shipsLeft--;
            return true;
        }
        return false;
    }

    public int getShipsLeft() {
        return shipsLeft;
    }

    public boolean isOccupied(int x, int y) {
        return ships[x][y];
    }
}
