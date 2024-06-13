import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TicTacToeAI extends JFrame implements ActionListener {
    private static final int SIZE = 3;
    private final JButton[][] buttons;
    private final char[][] board;
    private boolean playerTurn;

    public TicTacToeAI() {
        buttons = new JButton[SIZE][SIZE];
        board = new char[SIZE][SIZE];
        playerTurn = true;

        setTitle("Tic-Tac-Toe with AI");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
                board[i][j] = '-';
            }
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (buttonClicked == buttons[i][j]) {
                    if (board[i][j] == '-') {
                        makeMove(i, j, 'X');
                        playerTurn = false;
                        if (!checkWin('X')) {
                            aiMove();
                            playerTurn = true;
                        }
                    }
                }
            }
        }
    }

    private void makeMove(int row, int col, char player) {
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setEnabled(false);
        if (checkWin(player)) {
            JOptionPane.showMessageDialog(this, player + " wins!");
            resetBoard();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a tie!");
            resetBoard();
        }
    }

    private boolean checkWin(char player) {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player)
                return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player)
                return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player)
            return true;
        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '-')
                    return false;
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '-';
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        playerTurn = true;
    }

    private void aiMove() {
        int[] bestMove = minimax(board, 0, 'O');
        makeMove(bestMove[0], bestMove[1], 'O');
    }

    private int[] minimax(char[][] board, int depth, char currentPlayer) {
        char opponent = (currentPlayer == 'O') ? 'X' : 'O';

        if (checkWin(opponent)) {
            return new int[] { -1, -1, (currentPlayer == 'O') ? -1 : 1 };
        } else if (isBoardFull()) {
            return new int[] { -1, -1, 0 };
        }

        int bestScore = (currentPlayer == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = new int[] { -1, -1 };

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '-') {
                    board[i][j] = currentPlayer;
                    int score = minimax(board, depth + 1, opponent)[2];
                    board[i][j] = '-';

                    if (currentPlayer == 'O') {
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new int[] { i, j, bestScore };
                        }
                    } else {
                        if (score < bestScore) {
                            bestScore = score;
                            bestMove = new int[] { i, j, bestScore };
                        }
                    }
                }
            }
        }

        return bestMove;
    }

    public static void main(String[] args) {
        new TicTacToeAI();
    }
}