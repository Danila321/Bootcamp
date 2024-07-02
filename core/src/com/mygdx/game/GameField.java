package com.mygdx.game;

public class GameField {
    private Tile[][] gameField;

    public GameField(int horizontalNumber, int verticalNumber){

        gameField = new Tile[horizontalNumber][verticalNumber];
        for(int i = 0; i < GameSettings.SCREEN_HEIGHT; i++){
            for(int j = 0; j < GameSettings.SCREEN_WIDTH; j++){
                gameField[i][j] = new Tile(
                        GameSettings.SCREEN_WIDTH / horizontalNumber,
                        GameSettings.SCREEN_WIDTH / verticalNumber,
                        i, j,
                        false, false, false);
            }
        }

    }

}
