package logic;

import util.Constants;

import java.util.Random;

public class ShipPlacer {
    private final Random rand = new Random();

    public void placeRandomShips(Board board, int count) {
        int placed = 0;
        while (placed < count) {
            int x = rand.nextInt(Constants.SIZE);
            int y = rand.nextInt(Constants.SIZE);
            if (board.placeShip(x, y)) {
                placed++;
            }
        }
    }
}
