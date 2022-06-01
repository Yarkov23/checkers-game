package com.example.checkergame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Piece piece;

    public boolean hasPiece() {
        return piece != null;
    }


    public Piece getPiece() {
        try {
            return piece;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(CheckersApp.TILE_SIZE);
        setHeight(CheckersApp.TILE_SIZE);

        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
        setFill(light ? Color.valueOf("#CBC66A") : Color.valueOf("#5A1A1A"));
    }

}
