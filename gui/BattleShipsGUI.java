package gui;

import logic.GameController;
import util.Constants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BattleShipsGUI extends JFrame {
    private final JButton[][] playerGridButtons = new JButton[Constants.SIZE][Constants.SIZE];
    private final JButton[][] enemyGridButtons = new JButton[Constants.SIZE][Constants.SIZE];
    private final JLabel statusLabel = new JLabel();
    private final GameController controller = new GameController();

    private boolean playerTurn = false;
    private int shipsToPlace = Constants.SHIPS;

    public BattleShipsGUI() {
        setTitle("🕹️ Battaglia Navale");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel playerPanel = createGridPanel(true);
        JPanel enemyPanel = createGridPanel(false);
        JPanel gridContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        gridContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gridContainer.add(enemyPanel);
        gridContainer.add(playerPanel);

        statusLabel.setText("Posiziona le tue " + Constants.SHIPS + " navi. 🌊");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(gridContainer, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        setSize(950, 750);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createGridPanel(boolean isPlayer) {
        JPanel panel = new JPanel(new GridLayout(Constants.SIZE, Constants.SIZE));
        panel.setBorder(BorderFactory.createTitledBorder(isPlayer ? "🛡️ Le tue navi" : "🎯 Attacchi al nemico"));
        panel.setBackground(isPlayer ? new Color(30, 30, 60) : new Color(190, 100, 0));

        for (int x = 0; x < Constants.SIZE; x++) {
            for (int y = 0; y < Constants.SIZE; y++) {
                JButton btn = createStyledButton(isPlayer ? new Color(0, 70, 140) : new Color(255, 140, 0));
                final int i = x, j = y;

                if (isPlayer) {
                    btn.addActionListener(e -> {
                        if (shipsToPlace > 0 && controller.placePlayerShip(i, j)) {
                            btn.setBackground(new Color(0, 100, 0));
                            btn.setText("■");
                            shipsToPlace--;
                            statusLabel.setText("Navi da posizionare: " + shipsToPlace);
                            if (shipsToPlace == 0) {
                                controller.placeComputerShips();
                                JOptionPane.showMessageDialog(this, "Tutte le navi piazzate! Inizia il gioco.");
                                statusLabel.setText("È il tuo turno. Attacca la griglia nemica!");
                                playerTurn = true;
                                enableEnemyGrid(true);
                            }
                        }
                    });
                    playerGridButtons[x][y] = btn;
                } else {
                    btn.setEnabled(false);
                    btn.addActionListener(e -> {
                        if (playerTurn && btn.isEnabled()) {
                            handlePlayerAttack(btn, i, j);
                        }
                    });
                    enemyGridButtons[x][y] = btn;
                }

                panel.add(btn);
            }
        }

        return panel;
    }

    private JButton createStyledButton(Color color) {
        JButton btn = new JButton();
        btn.setFont(new Font("Noto Color Emoji", Font.PLAIN, 24));
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setBorder(new LineBorder(Color.BLACK, 1));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void enableEnemyGrid(boolean enable) {
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                if (enemyGridButtons[i][j].getText().isEmpty()) {
                    enemyGridButtons[i][j].setEnabled(enable);
                }
            }
        }
    }

    private void handlePlayerAttack(JButton btn, int x, int y) {
        btn.setEnabled(false);
        if (controller.attackComputer(x, y)) {
            btn.setText("x");
            btn.setForeground(Color.RED);
            btn.setBackground(new Color(255, 140, 0));
            statusLabel.setText("Colpito! Navi nemiche rimaste: " + controller.getComputerShipsLeft());

            new Timer(800, e -> {
                btn.setText("☠");
                btn.setBackground(new Color(139, 0, 0));
                checkWin();
            }).start();
        } else {
            btn.setText("≈");
            btn.setBackground(new Color(30, 30, 100));
            statusLabel.setText("Mancato! Turno del computer...");
            playerTurn = false;
            enableEnemyGrid(false);
            handleComputerTurn();
        }
    }

    private void handleComputerTurn() {
        new Timer(1000, e -> {
            int[] target = controller.getComputerTarget(playerGridButtons);
            int x = target[0], y = target[1];
            JButton btn = playerGridButtons[x][y];
            btn.setEnabled(false);

            if (controller.attackPlayer(x, y)) {
                btn.setText("x");
                btn.setForeground(Color.RED);
                btn.setBackground(new Color(255, 140, 0));
                statusLabel.setText("Il computer ha colpito! Navi tue rimaste: " + controller.getPlayerShipsLeft());

                new Timer(800, e2 -> {
                    btn.setText("☠");
                    btn.setBackground(new Color(90, 0, 90));
                    checkWin();
                    playerTurn = true;
                    enableEnemyGrid(true);
                }).start();
            } else {
                btn.setText("≈");
                btn.setBackground(new Color(0, 30, 100));
                statusLabel.setText("Il computer ha mancato. Tocca a te.");
                playerTurn = true;
                enableEnemyGrid(true);
            }
        }).start();
    }

    private void checkWin() {
        if (controller.getComputerShipsLeft() == 0) {
            JOptionPane.showMessageDialog(this, "🏆 Hai vinto!");
            System.exit(0);
        } else if (controller.getPlayerShipsLeft() == 0) {
            JOptionPane.showMessageDialog(this, "💀 Hai perso!");
            System.exit(0);
        }
    }
}
