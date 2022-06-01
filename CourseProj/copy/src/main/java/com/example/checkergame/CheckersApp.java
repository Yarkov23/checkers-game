package com.example.checkergame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class CheckersApp extends Application {

    public static final int TILE_SIZE = 80;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private int redCount = 12;
    private int whiteCount = 12;
    private boolean isItMyTurn = false;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private final Label colorLabel = new Label();

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    private Window createContent() {
        Stage stage = new Stage();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Actions");

        MenuItem exitButton = new MenuItem("Exit");
        exitButton.setOnAction(actionEvent -> System.exit(0));

        MenuItem restartButton = new MenuItem("Restart");
        restartButton.setOnAction(actionEvent -> restart());

        menu.getItems().addAll(restartButton, exitButton);
        menuBar.getMenus().add(menu);

        Pane root = new Pane();


        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.RED, x, y);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.WHITE, x, y);
                }
                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }
        }

        colorLabel.setTextFill(Color.WHITE);
        colorLabel.setFont(new Font("ARIAL", 15));
        colorLabel.setAlignment(Pos.TOP_CENTER);
        colorLabel.setLayoutX(540);
        colorLabel.setLayoutY(0);

        root.getChildren().addAll(menuBar, colorLabel);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        return stage;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY) {

        if ((!isItMyTurn & piece.getType() == PieceType.WHITE) | (!isItMyTurn & piece.getType() == PieceType.WHITE_SUP)) {
            if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
                return new MoveResult(MoveType.NONE);
            }

            int oldX = toBoard(piece.getOldX());
            int oldY = toBoard(piece.getOldY());

            if (Math.abs(newX - oldX) == 1 && newY - oldY == piece.getType().moveDir) {
                colorLabel.setText((!isItMyTurn ? "RED TURN" : "WHITE TURN"));
                isItMyTurn = true;
                return new MoveResult(MoveType.NORMAL);
            } else if (Math.abs(newX - oldX) == 2 && Math.abs(newY - oldY) == 2) {
                int middleX = oldX + (newX - oldX) / 2;
                int middleY = oldY + (newY - oldY) / 2;

                if (board[middleX][middleY].hasPiece() && board[middleX][middleY].getPiece().getType() != piece.getType()) {
                    colorLabel.setText((!isItMyTurn ? "RED TURN" : "WHITE TURN"));
                    isItMyTurn = true;
                    return new MoveResult(MoveType.KILL, board[middleX][middleY].getPiece());
                }
            } else if ((piece.getType() == PieceType.RED_SUP || piece.getType() == PieceType.WHITE_SUP) && (Math.abs(newX - oldX) == 1 && Math.abs(newY - oldY) == 1)) {
                colorLabel.setText((!isItMyTurn ? "RED TURN" : "WHITE TURN"));
                isItMyTurn = true;
                return new MoveResult(MoveType.NORMAL);
            }
        } else if ((isItMyTurn & piece.getType() == PieceType.RED) | (isItMyTurn & piece.getType() == PieceType.RED_SUP)) {
            if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
                return new MoveResult(MoveType.NONE);
            }

            int oldX = toBoard(piece.getOldX());
            int oldY = toBoard(piece.getOldY());

            if (Math.abs(newX - oldX) == 1 && newY - oldY == piece.getType().moveDir) {
                colorLabel.setText((!isItMyTurn ? "RED TURN" : "WHITE TURN"));
                isItMyTurn = false;
                return new MoveResult(MoveType.NORMAL);
            } else if (Math.abs(newX - oldX) == 2 && Math.abs(newY - oldY) == 2) {
                int middleX = oldX + (newX - oldX) / 2;
                int middleY = oldY + (newY - oldY) / 2;

                if (board[middleX][middleY].hasPiece() && board[middleX][middleY].getPiece().getType() != piece.getType()) {
                    colorLabel.setText((!isItMyTurn ? "RED TURN" : "WHITE TURN"));
                    isItMyTurn = false;
                    return new MoveResult(MoveType.KILL, board[middleX][middleY].getPiece());
                }
            } else if ((piece.getType() == PieceType.RED_SUP || piece.getType() == PieceType.WHITE_SUP) && (Math.abs(newX - oldX) == 1 && Math.abs(newY - oldY) == 1)) {
                colorLabel.setText((!isItMyTurn ? "RED TURN" : "WHITE TURN"));
                isItMyTurn = false;
                return new MoveResult(MoveType.NORMAL);
            }
        } else {
            piece.abortMove();
        }
        return new MoveResult(MoveType.NONE);
    }


    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Window");

        Button btn = new Button();
        Button btn1 = new Button();

        btn.setText("Play");
        btn1.setText("Exit");
        btn.setPrefSize(90, 60);
        btn1.setPrefSize(90, 60);
        btn1.setLayoutX(250);
        btn1.setLayoutY(300);

        VBox root = new VBox();

        primaryStage.setResizable(false);
        root.setSpacing(15);
        root.setStyle("-fx-background-color: #C4D7D2;");

        root.setAlignment(Pos.BASELINE_CENTER);
        root.getChildren().addAll(btn, btn1);

        primaryStage.setScene(new Scene(root, 400, 300));


        primaryStage.show();

        btn1.setOnAction(actionEvent -> Platform.exit());

        btn.setOnAction(event -> {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            createContent();
        });

    }

    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);

        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());

            MoveResult result;

            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                result = new MoveResult(MoveType.NONE);
            } else {
                result = tryMove(piece, newX, newY);
            }

            switch (result.getType()) {
                case NONE:
                    piece.abortMove();
                    break;
                case NORMAL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    if ((newY == 7 && piece.getType() == PieceType.RED) || (newY == 0 && piece.getType() == PieceType.WHITE)) {
                        Platform.runLater(piece::promote);
                    }
                    break;
                case KILL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);

                    Piece otherPiece = result.getPiece();
                    board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                    Platform.runLater(() -> pieceGroup.getChildren().remove(otherPiece));

                    if ((newY == 7 && piece.getType() == PieceType.RED) || (newY == 0 && piece.getType() == PieceType.WHITE)) {
                        Platform.runLater(piece::promote);
                    }

                    if (isBeatAvailable(piece, newX, newY)) {
                        if (piece.getType() == PieceType.WHITE) {
                            isItMyTurn = false;
                        } else {
                            isItMyTurn = true;
                        }
                    }

                    if (otherPiece.getType() == PieceType.RED) {
                        check();
                        redCount--;
                    } else {
                        check();
                        whiteCount--;
                    }
                    break;
            }
        });

        return piece;
    }

    private boolean isBeatAvailable(Piece currentPiece, int newX, int newY) {

        return isEnemyPiecePersistOnTile(currentPiece, newX, newY, 1, -1) && !isPiecePersistOnTile(board[Math.abs(newX + 2)][Math.abs(newY - 2)]) ||
                isEnemyPiecePersistOnTile(currentPiece, newX, newY, 1, 1) && !isPiecePersistOnTile(board[Math.abs(newX + 2)][Math.abs(newY + 2)]) ||
                isEnemyPiecePersistOnTile(currentPiece, newX, newY, -1, -1) && !isPiecePersistOnTile(board[Math.abs(newX - 2)][Math.abs(newY - 2)]) ||
                isEnemyPiecePersistOnTile(currentPiece, newX, newY, -1, 1) && !isPiecePersistOnTile(board[Math.abs(newX - 2)][Math.abs(newY + 2)]);
    }

    private boolean isEnemyPiecePersistOnTile(Piece currentPiece, int currentX, int currentY, int x, int y) {
        Tile tile;
        int tileX = currentX + x;
        int tileY = currentY + y;

        if (tileY > 0 && tileX > 0 && board.length > tileX && board[tileX].length > tileY) {
            tile = board[tileX][tileY];
        } else return false;
        if (!isPiecePersistOnTile(tile))
            return false;
        return tile.getPiece().getType() != currentPiece.getType();
    }

    private boolean isPiecePersistOnTile(Tile tile) {
        return tile.getPiece() != null;
    }

    private void restart() {

        redCount = 12;
        whiteCount = 12;
        isItMyTurn = false;
        colorLabel.setText("WHITE TURN");

        tileGroup.getChildren().clear();
        pieceGroup.getChildren().clear();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.RED, x, y);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.WHITE, x, y);
                }
                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }
        }
    }

    private void check() {
        if (redCount == 1 || whiteCount == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("The End");
            alert.setHeaderText("Train over");
            alert.setContentText("Choose your option");

            ButtonType buttonType1 = new ButtonType("Restart");
            ButtonType buttonType2 = new ButtonType("Exit");

            alert.getButtonTypes().setAll(buttonType1, buttonType2);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonType1) {
                restart();
            } else if (result.get() == buttonType2) {
                System.exit(0);
            }
        } else {
            return;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}