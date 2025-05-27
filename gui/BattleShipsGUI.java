package gui;

import logic.GameController;
import util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
// AGGIUNTA IMPORT NECESSARIO
import javax.swing.border.EmptyBorder;

public class BattleShipsGUI extends JFrame {
    private final JButton[][] playerGridButtons = new JButton[Constants.SIZE][Constants.SIZE];
    private final JButton[][] enemyGridButtons = new JButton[Constants.SIZE][Constants.SIZE];
    private final JLabel statusLabel = new JLabel();
    private final GameController controller = new GameController();

    private boolean playerTurn = false;
    private int shipsToPlace = Constants.SHIPS;
    private JLabel playerStatsLabel;
    private JLabel computerStatsLabel;

    public BattleShipsGUI() {
        setTitle("🕹️ Battle Ship");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        showStartMenu(); // 👈 Mostra il menu iniziale

        setSize(950, 750);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showStartMenu() {
        ImageIcon bgImage = new ImageIcon("resources/background.jpg");
        JLabel background = new JLabel(bgImage);
        background.setLayout(new BorderLayout());

        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false);
        overlay.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Titolo in alto, nero, senza bordo
        JLabel title = new JLabel("BATTLESHIP ARCADE", SwingConstants.CENTER);
        title.setFont(new Font("Impact", Font.BOLD, 52));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        overlay.add(title, BorderLayout.NORTH);

        // Pannello centrale con il pulsante
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());

        JButton startBtn = new JButton("Start the battle!");
        startBtn.setFont(new Font("Arial Black", Font.BOLD, 28));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setFocusPainted(false);
        startBtn.setBackground(new Color(30, 30, 30)); // sfondo scuro
        startBtn.setForeground(new Color(255, 102, 0)); // arancione neon
        startBtn.setMaximumSize(new Dimension(320, 70));
        startBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 102, 0), 2));
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Effetto hover
        startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startBtn.setBackground(new Color(45, 45, 45));
                startBtn.setForeground(new Color(255, 160, 0));
                startBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 160, 0), 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startBtn.setBackground(new Color(30, 30, 30));
                startBtn.setForeground(new Color(255, 102, 0));
                startBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 102, 0), 2));
            }
        });

        startBtn.addActionListener(e -> showBattleTransition());

        centerPanel.add(startBtn);
        centerPanel.add(Box.createVerticalGlue());
        overlay.add(centerPanel, BorderLayout.CENTER);

        // Copyright in basso
        JLabel copyright = new JLabel("© 2025 AndreDisa - All rights reserved", SwingConstants.CENTER);
        copyright.setFont(new Font("Arial", Font.PLAIN, 18));
        copyright.setForeground(Color.white);
        copyright.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        overlay.add(copyright, BorderLayout.SOUTH);

        background.add(overlay, BorderLayout.CENTER);

        getContentPane().removeAll();
        add(background);
        revalidate();
        repaint();
    }

    private void showBattleTransition() {
        JPanel animPanel = new JPanel(new GridBagLayout());
        animPanel.setBackground(Color.BLACK);

        JLabel animLabel = new JLabel(" BATTLE ");
        animLabel.setFont(new Font("Impact", Font.BOLD, 64));
        animLabel.setForeground(Color.RED);
        animLabel.setOpaque(false);
        animLabel.setVisible(true);

        animPanel.add(animLabel);

        getContentPane().removeAll();
        add(animPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        // Entrata: effetto zoom e opacità
        Timer enterAnim = new Timer(20, null);
        final float[] alpha = { 0f };
        final int[] fontSize = { 20 };

        enterAnim.addActionListener(e -> {
            alpha[0] += 0.05f;
            fontSize[0] += 2;
            animLabel.setFont(new Font("Impact", Font.BOLD, fontSize[0]));
            animLabel.setForeground(new Color(255, 0, 0, Math.min((int) (alpha[0] * 255), 255)));
            animLabel.repaint();

            if (alpha[0] >= 1.0f) {
                ((Timer) e.getSource()).stop();

                // Dopo una breve pausa, dissolvi l’etichetta
                Timer fadeOut = new Timer(50, null);
                final float[] fade = { 1.0f };
                fadeOut.addActionListener(ev -> {
                    fade[0] -= 0.05f;
                    animLabel.setForeground(new Color(255, 0, 0, Math.max((int) (fade[0] * 255), 0)));
                    animLabel.repaint();

                    if (fade[0] <= 0.0f) {
                        ((Timer) ev.getSource()).stop();
                        getContentPane().removeAll();
                        initGame();
                        revalidate();
                        repaint();
                    }
                });
                fadeOut.start();
            }
        });
        enterAnim.start();
    }

    private void initGame() {
        JPanel playerPanel = createGridPanel(true);
        JPanel enemyPanel = createGridPanel(false);
        JPanel gridContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        gridContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gridContainer.add(enemyPanel);
        gridContainer.add(playerPanel);

        statusLabel.setText("Position your " + Constants.SHIPS + " ships. 🌊");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 🔧 Pannello statistiche
        JPanel statsPanel = new JPanel(new GridLayout(1, 3));
        statsPanel.setBackground(new Color(20, 20, 30));
        statsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // 👈 COMPUTER
        computerStatsLabel = new JLabel();
        computerStatsLabel.setForeground(new Color(255, 20, 20)); // rosso brillante/neon
        computerStatsLabel.setFont(new Font("Consolas", Font.BOLD, 15));
        computerStatsLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // 🏷️ CENTRALE
        JLabel statsTitle = new JLabel("📊 STATISTICS");
        statsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        statsTitle.setForeground(Color.LIGHT_GRAY);
        statsTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // 👉 GIOCATORE
        playerStatsLabel = new JLabel();
        playerStatsLabel.setForeground(Color.GREEN);
        playerStatsLabel.setFont(new Font("Consolas", Font.BOLD, 15));
        playerStatsLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        statsPanel.add(computerStatsLabel);
        statsPanel.add(statsTitle);
        statsPanel.add(playerStatsLabel);

        getContentPane().removeAll();

        add(gridContainer, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // 👇 Mostra il pannello solo dopo il piazzamento
        Timer statsDelay = new Timer(100, null);
        statsDelay.addActionListener(e -> {
            if (shipsToPlace == 0) {
                add(statsPanel, BorderLayout.NORTH);
                updateStatsLabels(); // aggiorna i testi delle statistiche QUI
                revalidate();
                repaint();
                ((Timer) e.getSource()).stop();
            }
        });
        statsDelay.start();
    }

    private String getPlayerStatsText() {
        int remaining = controller.getPlayerShipsLeft(); // Navi rimaste del giocatore
        int destroyed = Constants.SHIPS - remaining; // Navi affondate del giocatore
        return "👤 Your ships: " + remaining + "  |  Sunk: " + destroyed;
    }

    private String getComputerStatsText() {
        int remaining = controller.getComputerShipsLeft(); // Navi rimaste del computer
        int destroyed = Constants.SHIPS - remaining; // Navi affondate del computer
        return "🤖 Enemy ships: " + remaining + "  |  Sunk: " + destroyed;
    }

    private void updateStatsLabels() {
        playerStatsLabel.setText(getPlayerStatsText());
        computerStatsLabel.setText(getComputerStatsText());
    }

    private JPanel createGridPanel(boolean isPlayer) {
        JPanel panel = new JPanel(new GridLayout(Constants.SIZE, Constants.SIZE));
        TitledBorder border = BorderFactory.createTitledBorder(isPlayer ? "🛡️ Your ships" : "🎯 Attacks on the enemy");
        border.setTitleColor(Color.WHITE); // Imposta il colore del titolo a bianco
        panel.setBorder(border);
        panel.setBackground(new Color(30, 30, 60));

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
                            statusLabel.setText("Ships to position: " + shipsToPlace);
                            if (shipsToPlace == 0) {
                                controller.placeComputerShips();
                                JOptionPane.showMessageDialog(this, "All ships placed! Start the game.");
                                statusLabel.setText("It's your turn. Attack the enemy grid!");
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
        boolean hit = controller.attackComputer(x, y);

        if (hit) {
            btn.setText("x");
            btn.setForeground(Color.RED);
            btn.setBackground(new Color(255, 140, 0));
            statusLabel.setText("Hit! Enemy ships remaining: " + controller.getComputerShipsLeft());
            updateStatsLabels();
            Timer t = new Timer(500, e -> {
                btn.setText("☠");
                btn.setBackground(new Color(139, 0, 0));
                checkWin();
                // Il giocatore continua a sparare
                enableEnemyGrid(true);
            });
            t.setRepeats(false);
            t.start();

        } else {
            btn.setText("≈");
            btn.setBackground(new Color(30, 30, 100));
            statusLabel.setText("Missed! Computer's turn...");
            playerTurn = false;
            enableEnemyGrid(false);

            Timer delay = new Timer(500, e -> handleComputerTurn());
            delay.setRepeats(false);
            delay.start();
        }
    }

    private void handleComputerTurn() {
        Timer t = new Timer(500, null);
        t.setRepeats(false);
        t.addActionListener(e -> {
            int[] target = controller.getComputerTarget(playerGridButtons);
            int x = target[0], y = target[1];
            JButton btn = playerGridButtons[x][y];
            btn.setEnabled(false);

            boolean hit = controller.attackPlayer(x, y);

            if (hit) {
                btn.setText("x");
                btn.setForeground(Color.RED);
                btn.setBackground(new Color(255, 140, 0));
                statusLabel.setText("The computer hit! Your ships remaining: " + controller.getPlayerShipsLeft());
                updateStatsLabels();

                Timer hitDelay = new Timer(500, ev -> {
                    btn.setText("☠");
                    btn.setBackground(new Color(90, 0, 90));
                    checkWin();
                    // Il computer continua a colpire
                    handleComputerTurn();
                });
                hitDelay.setRepeats(false);
                hitDelay.start();

            } else {
                btn.setText("≈");
                btn.setBackground(new Color(0, 30, 100));
                statusLabel.setText("The computer missed. Your turn.");
                playerTurn = true;
                enableEnemyGrid(true);
            }
        });
        t.start();
    }

    private void checkWin() {
        if (controller.getComputerShipsLeft() == 0) {
            showEndScreen("🏆 You won!", new Color(0, 128, 0));
        } else if (controller.getPlayerShipsLeft() == 0) {
            showEndScreen("💀 You lost!", new Color(128, 0, 0));
        }
    }

    private void showEndScreen(String message, Color bgColor) {
        getContentPane().removeAll(); // Pulisce tutto

        JPanel endPanel = new JPanel();
        endPanel.setBackground(bgColor);
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
        endPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(new Font("Arial", Font.BOLD, 36));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        msgLabel.setForeground(Color.WHITE);

        JButton replayBtn = new JButton("🔁 Restart");
        replayBtn.setFont(new Font("Arial", Font.BOLD, 20));
        replayBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        replayBtn.setFocusPainted(false);
        replayBtn.setBackground(new Color(30, 144, 255));
        replayBtn.setForeground(Color.WHITE);
        replayBtn.setMaximumSize(new Dimension(200, 50));
        replayBtn.addActionListener(e -> {
            dispose(); // Chiude la finestra corrente
            new BattleShipsGUI(); // Crea una nuova partita
        });

        JButton exitBtn = new JButton("❌ Exit");
        exitBtn.setFont(new Font("Arial", Font.BOLD, 20));
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setFocusPainted(false);
        exitBtn.setBackground(new Color(220, 20, 60));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setMaximumSize(new Dimension(200, 50));
        exitBtn.addActionListener(e -> System.exit(0));

        endPanel.add(msgLabel);
        endPanel.add(Box.createVerticalStrut(30));
        endPanel.add(replayBtn);
        endPanel.add(Box.createVerticalStrut(15));
        endPanel.add(exitBtn);

        add(endPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

}
