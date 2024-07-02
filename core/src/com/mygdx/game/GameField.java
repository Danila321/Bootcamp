package com.mygdx.game;

public class GameField {
    private Tile[][] gameField;

    public GameField(int horizontalNumber, int verticalNumber){

        gameField = new Tile[horizontalNumber][verticalNumber];
        for(int i = 0; i < GameSettings.SCREEN_HEIGHT; i += GameSettings.SCREEN_HEIGHT / verticalNumber){
            for(int j = 0; j < GameSettings.SCREEN_WIDTH; j += GameSettings.SCREEN_WIDTH / horizontalNumber){
                gameField[i][j] = new Tile(
                        GameSettings.SCREEN_WIDTH / horizontalNumber,
                        GameSettings.SCREEN_WIDTH / verticalNumber,
                        i, j,
                        false, false, false);
            }
        }
    }
}
