package com.mygdx.game;

public class GameField {
    private Tile[][] gameField;

    public GameField(int horizontalNumber, int verticalNumber){

        gameField = new Tile[verticalNumber][horizontalNumber];
        for(int i = 0; i < verticalNumber; i++){
            for(int j = 0; j < horizontalNumber; j++){
                gameField[i][j] = new Tile(
                        GameSettings.SCREEN_WIDTH / horizontalNumber,
                        GameSettings.SCREEN_HEIGHT / verticalNumber,
                        i * GameSettings.SCREEN_HEIGHT / verticalNumber,
                        j * GameSettings.SCREEN_WIDTH / horizontalNumber,
                        false, false, false);
                System.out.println(gameField[i][j].getX() + " " + gameField[i][j].getY());
            }
        }

    }

}
