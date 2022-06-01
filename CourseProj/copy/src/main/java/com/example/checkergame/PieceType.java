package com.example.checkergame;

public enum PieceType {
    RED(1, true),
    WHITE(-1, false),
    RED_SUP(2, true),
    WHITE_SUP(-2, false);

    int moveDir;
    boolean myTurn;

    PieceType(int moveDir, boolean myTurn) {
        this.moveDir = moveDir;
        this.myTurn = myTurn;
    }
}
