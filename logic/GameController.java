package logic;

import util.Constants;

import javax.swing.*;
import java.util.Random;

public class GameController {
    private final Board playerBoard = new Board();
    private final Board computerBoard = new Board();
    private final ShipPlacer placer = new ShipPlacer();
    private final Random rand = new Random();

    public boolean placePlayerShip(int x, int y) {
        return playerBoard.placeShip(x, y);
    }

    public void placeComputerShips() {
        placer.placeRandomShips(computerBoard, Constants.SHIPS);
    }

    public boolean attackComputer(int x, int y) {
        return computerBoard.hit(x, y);
    }

    public boolean attackPlayer(int x, int y) {
        return playerBoard.hit(x, y);
    }

    public int[] getComputerTarget(JButton[][] grid) {
        int x, y;
        do {
            x = rand.nextInt(Constants.SIZE);
            y = rand.nextInt(Constants.SIZE);
        } while (!grid[x][y].isEnabled());
        return new int[] { x, y };
    }

    public int getComputerShipsLeft() {
        return computerBoard.getShipsLeft();
    }

    public int getPlayerShipsLeft() {
        return playerBoard.getShipsLeft();
    }
}
