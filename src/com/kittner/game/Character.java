package com.kittner.game;

import com.kittner.engine.gfx.Image;
import com.kittner.engine.gfx.ImageTile;

import static com.kittner.game.CharacterState.IDLE;

public class Character
{
    private final int SPEED = 20;
    private ImageTile smallChar, largeChar;
    private int x = 10, y = 10, targetX, targetY;
    private int[][] targetPath;
    private boolean isShown = false;
    private CharacterState state = IDLE;

    public Character(String pathToSmallChar, String pathToLargeChar)
    {
        smallChar = new ImageTile(pathToSmallChar, 20, 40, 0, 0);
        largeChar = new ImageTile(pathToLargeChar, 160, 400);
    }

    /*
    First row is the idle animation
    */
    public Image getSmallCharCurrentFrame()
    {
        return smallChar.getCurrentAnimation();
    }

    public int getX()
    {
        return GameManager.calcScreenCoordsFromWorldCoords(x, y)[0] + GameManager.tileWidth / 2 - smallChar.getTileW() / 2;
    }
    public int getY()
    {
        return GameManager.calcScreenCoordsFromWorldCoords(x, y)[1] - GameManager.tileHeight;
    }
    public int[] getCoords()
    {
        return GameManager.calcScreenCoordsFromWorldCoords(x + GameManager.tileWidth / 2 - smallChar.getTileW() / 2, y - GameManager.tileHeight);
    }

    public void setTargetX(int x)
    {
        targetX = x;
    }
    public void setTargetY(int y)
    {
        targetY = y;
    }
    public void setTargetCoords(int x, int y)
    {
        targetX = x;
        targetY = y;
    }
    public void setTargetCoords(int[] targetCoords)
    {
        targetX = targetCoords[0];
        targetY = targetCoords[1];
    }
    public void findPathToTarget()
    {

    }

    public boolean isShown()
    {
        return isShown;
    }
    public void isShown(boolean shown)
    {
        isShown = shown;
    }
    public void toggleIsShown()
    {
        isShown = !isShown;
    }

    public ImageTile getSmallChar()
    {
        return smallChar;
    }
    public ImageTile getLargeChar()
    {
        return largeChar;
    }
}
