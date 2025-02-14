package tspgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TSPGame extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int SIZE = 3; // 3x3 grid for 8 puzzle
    private final int[] goalState = {1, 2, 3, 4, 5, 6, 7, 8, 0}; // Goal state (solved puzzle)
    private int[] currentState = new int[9]; // Initial state
    private JButton checkButton, resetButton;
    private boolean isSolutionCorrect = false;
    private int moveCount = 0; // Track the number of moves

    public TSPGame() {
        setPreferredSize(new Dimension(300, 300));
        setBackground(Color.WHITE);

        checkButton = new JButton("Check Solution");
        checkButton.addActionListener(e -> checkSolution());

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());

        JPanel panel = new JPanel();
        panel.add(checkButton);
        panel.add(resetButton);

        JFrame frame = new JFrame("8 Puzzle Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        shufflePuzzle(10);  // Shuffle only 10 times for easy difficulty
        
        // Add mouse listener for moving blocks
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int tileSize = 100;
                int x = e.getX();
                int y = e.getY();
                int col = x / tileSize;
                int row = y / tileSize;
                int index = row * SIZE + col;

                // If the clicked tile is adjacent to the empty space (0), swap them
                if (isValidMove(index)) {
                    swap(currentState, index, findZeroIndex(currentState));
                    moveCount++; // Increment move count after each valid move
                    repaint();
                }
            }
        });
    }

    private void checkSolution() {
        if (Arrays.equals(currentState, goalState)) {
            isSolutionCorrect = true;
            repaint();
            JOptionPane.showMessageDialog(this, "Puzzle Solved in " + moveCount + " moves!");
        } else {
            isSolutionCorrect = false;
            JOptionPane.showMessageDialog(this, "Puzzle is not solved yet!");
        }
    }

    private void resetGame() {
        shufflePuzzle(10);  // Reset and shuffle again with easy difficulty
        isSolutionCorrect = false;
        moveCount = 0; // Reset move count
        repaint();
    }

    // Shuffle the puzzle to generate a solvable and easy configuration
    private void shufflePuzzle(int shuffleCount) {
        currentState = Arrays.copyOf(goalState, goalState.length); // Reset to solved state
        Random rand = new Random();

        // Perform a number of random moves
        for (int i = 0; i < shuffleCount; i++) {
            int zeroIndex = findZeroIndex(currentState);
            List<Integer> validMoves = getValidMoves(zeroIndex);
            int move = validMoves.get(rand.nextInt(validMoves.size()));

            // Swap the zero with the selected move
            swap(currentState, zeroIndex, move);
        }
    }

    private int findZeroIndex(int[] state) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private List<Integer> getValidMoves(int zeroIndex) {
        List<Integer> validMoves = new ArrayList<>();
        int row = zeroIndex / SIZE;
        int col = zeroIndex % SIZE;

        // Check valid moves (left, right, up, down)
        if (col > 0) validMoves.add(zeroIndex - 1); // Move left
        if (col < SIZE - 1) validMoves.add(zeroIndex + 1); // Move right
        if (row > 0) validMoves.add(zeroIndex - SIZE); // Move up
        if (row < SIZE - 1) validMoves.add(zeroIndex + SIZE); // Move down

        return validMoves;
    }

    private boolean isValidMove(int index) {
        int zeroIndex = findZeroIndex(currentState);
        List<Integer> validMoves = getValidMoves(zeroIndex);
        return validMoves.contains(index);
    }

    private void swap(int[] state, int i, int j) {
        int temp = state[i];
        state[i] = state[j];
        state[j] = temp;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = 100;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int tileValue = currentState[i * SIZE + j];
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
                if (tileValue != 0) {
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(tileValue), j * tileSize + 40, i * tileSize + 60);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TSPGame());
    }
}
