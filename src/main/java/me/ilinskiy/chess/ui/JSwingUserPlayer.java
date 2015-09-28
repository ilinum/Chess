package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Board;
import me.ilinskiy.chess.chessBoard.Coordinates;
import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.chessBoard.PieceType;
import me.ilinskiy.chess.game.GameUtil;
import me.ilinskiy.chess.game.Move;
import me.ilinskiy.chess.game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static me.ilinskiy.chess.game.GameUtil.println;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/20/15
 */
public class JSwingUserPlayer implements Player {
    private Optional<Move> moveMade = Optional.empty();
    @NotNull
    private final Lock mouseLock;
    @NotNull
    private final Condition moveIsMade;
    private final PieceColor myColor;

    public JSwingUserPlayer(PieceColor color) {
        myColor = color;
        mouseLock = new ReentrantLock();
        moveIsMade = mouseLock.newCondition();
    }

    @NotNull
    @Override
    public Move getMove(@NotNull Board board) {
        BoardPanel panel = ((JSwingChessPainter) board.myPainter.get()).getPanel();
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(@NotNull MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(@NotNull MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(@NotNull MouseEvent mouseEvent) {
                mouseLock.lock();
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                int size = panel.getSize().width;
                int cellSize = size / Board.BOARD_SIZE;
                Coordinates location = new Coordinates((int) x / cellSize, (int) y / cellSize);
                Optional<Coordinates> selected = board.getSelected();
                if (selected.isPresent()) {
                    Set<Move> availableMovesForPiece = GameUtil.getAvailableMovesForPiece(selected.get(), board);
                    Move m = new Move(selected.get(), location);
                    Optional<Move> res = Optional.empty();
                    for (Move move : availableMovesForPiece) {
                        if (m.equals(move)) {
                            res = Optional.of(move);
                        }
                    }
                    if (res.isPresent()) {
                        moveMade = res;
                        moveIsMade.signal();
                    } else {
                        board.setSelected(location);
                    }
                } else {
                    board.setSelected(location);
                }
                mouseLock.unlock();
            }

            @Override
            public void mouseEntered(@NotNull MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(@NotNull MouseEvent mouseEvent) {
            }
        };
        panel.addMouseListener(mouseListener);

        try {
            mouseLock.lock();
            while (!moveMade.isPresent()) { //"You shall always wait in a while loop," - Alison Norman
                try {
                    moveIsMade.await();
                } catch (InterruptedException ignored) {

                }
            }
            Move move = moveMade.get();
            assert move != null;
            moveMade = Optional.empty();
            return move;
        } finally {
            assert panel.getMouseListeners().length > 0;
            panel.removeMouseListener(mouseListener);
            assert panel.getMouseListeners().length == 0;
            mouseLock.unlock();
        }
    }

    @NotNull
    @Override
    public PieceColor getPlayerColor() {
        return myColor;
    }

    @Override
    @NotNull
    public PieceType getPieceTypeForPromotedPawn() {
        return new ChoosePieceTypeForPromotedPawn().getChosenPiece();
    }
}

class ChoosePieceTypeForPromotedPawn extends JPanel {
    @NotNull
    private Optional<PieceType> selectedPiece = Optional.empty();
    @NotNull
    private final Lock buttonLock;
    @NotNull
    private final Condition buttonPressed;

    final JRadioButton[] buttons = new JRadioButton[]{
            new JRadioButton("Queen"),
            new JRadioButton("Rook"),
            new JRadioButton("Bishop"),
            new JRadioButton("Knight")
    };

    ChoosePieceTypeForPromotedPawn() {
        super(new GridLayout(0, 1));

        buttonLock = new ReentrantLock();
        buttonPressed = buttonLock.newCondition();
        ButtonGroup buttonGroup = new ButtonGroup();
        for (JRadioButton button : buttons) {
            buttonGroup.add(button);
        }

        add(new JLabel("Choose piece type for promoted pawn:"));
        for (JRadioButton button : buttons) {
            add(button);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(actionEvent -> {
            buttonLock.lock();
            buttonPressed.signal();
            for (JRadioButton button : buttons) {
                if (button.isSelected()) {
                    switch (button.getText()) {
                        case "Queen":
                            selectedPiece = Optional.of(PieceType.Queen);
                            break;
                        case "Rook":
                            selectedPiece = Optional.of(PieceType.Rook);
                            break;
                        case "Bishop":
                            selectedPiece = Optional.of(PieceType.Bishop);
                            break;
                        case "Knight":
                            selectedPiece = Optional.of(PieceType.Knight);
                            break;
                        default:
                            assert false;
                    }
                }
            }
            buttonLock.unlock();
        });
        add(submitButton);
    }

    @NotNull
    PieceType getChosenPiece() {
        buttonLock.lock();
        JFrame frame = new JFrame("Choose Piece Type");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        try {
            while (!selectedPiece.isPresent()) {
                try {
                    buttonPressed.await();
                } catch (InterruptedException ignored) {

                }
            }
            PieceType selected = selectedPiece.get();
            assert selected != null;
            println(selected.toString());
            selectedPiece = Optional.empty();
            return selected;
        } finally {
            frame.dispose();
            buttonLock.unlock();
        }
    }

}