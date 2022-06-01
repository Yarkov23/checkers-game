package com.example.checkergame;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static com.example.checkergame.CheckersApp.TILE_SIZE;

public class Piece extends StackPane {

    private PieceType type;

    private double mouseX, mouseY;
    private double oldX, oldY;

    public PieceType getType() {
        try {
            return type;
        } catch (NullPointerException e) {
            return null;
        }

    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Piece(PieceType type, int x, int y) {
        this.type = type;

        move(x, y);

        Ellipse bg = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        bg.setFill(Color.BLACK);

        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(TILE_SIZE * 0.03);

        bg.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        bg.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);

        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);

        ellipse.setFill(type == PieceType.RED
                ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);

        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(bg, ellipse);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }

    public void promote() {
        promoteImage();
        type = (type == PieceType.RED) ? PieceType.RED_SUP : PieceType.WHITE_SUP;
    }

    public void promoteImage() {
        Ellipse doubleEllipse = new Ellipse(CheckersApp.TILE_SIZE * 0.3125 * 0.5, CheckersApp.TILE_SIZE * 0.26 * 0.5);

        doubleEllipse.setFill(type == PieceType.RED ? Color.valueOf("#4F4F4F") : Color.valueOf("#CEB087"));

        doubleEllipse.setStroke(Color.BLACK);
        doubleEllipse.setStrokeWidth(CheckersApp.TILE_SIZE * 0.03);

        doubleEllipse.setTranslateX((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.3125 * 2) / 2);
        doubleEllipse.setTranslateY((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(doubleEllipse);
    }

}