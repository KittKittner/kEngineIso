package com.kittner.game;

import com.kittner.engine.AbstractGame;
import com.kittner.engine.GameContainer;
import com.kittner.engine.Renderer;
import com.kittner.engine.gfx.Image;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

public class GameManager extends AbstractGame
{
    private static int tileWidth, tileHeight, worldSizeX, worldSizeY, worldOriginX, worldOriginY, mx, my, cellX, cellY, worldX, worldY,  selectedX, selectedY;
    private int[] tileMap;
    private Random rand = new Random();



    public GameManager(int newTileWidth, int newTileHeight, int newWorldSizeX, int newWorldSizeY, int newWorldOriginX, int newWorldOriginY)
    {
        tileWidth = newTileWidth;
        tileHeight = newTileHeight;
        worldSizeX = newWorldSizeX;
        worldSizeY = newWorldSizeY;
        worldOriginX = newWorldOriginX;
        worldOriginY = newWorldOriginY;

        tileMap = new int[worldSizeX * worldSizeY];
        for(int i = 0; i < tileMap.length; i++)
            tileMap[i] = rand.nextInt(5);
    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        mx = gc.getInput().getMouseX();
        my = gc.getInput().getMouseY();
        cellX = mx / tileWidth;
        cellY = my / tileHeight;
        int cellMX = mx % tileWidth;
        int cellMY = my % tileHeight;
        float cellMXNormal = (float)cellMX / tileWidth;
        float cellMYNormal = (float)cellMY / tileHeight;
        selectedX = (cellY - worldOriginY) + (cellX - worldOriginX);
        selectedY = (cellY - worldOriginY) - (cellX - worldOriginX);
        if((2.0/3.0) < Math.sqrt(Math.abs(0.5-cellMXNormal)+Math.abs(0.5-cellMYNormal))) //find the cartesian distance using normalised values
            if(cellMXNormal < 0.5 && cellMYNormal < 0.5) //top left
                selectedX -= 1;
            else if(cellMXNormal >= 0.5 && cellMYNormal < 0.5) //top right
                selectedY -= 1;
            else if(cellMXNormal < 0.5 && cellMYNormal >= 0.5) //bottom left
                selectedY += 1;
            else if(cellMXNormal > 0.5 && cellMYNormal > 0.5) //bottom right
                selectedX += 1;
        worldX = (worldOriginX * tileWidth) + (cellX - cellY) * tileWidth / 2;
        worldY = (worldOriginY * tileHeight) + (cellX + cellY) * tileHeight / 2;

        if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
        {
            ++tileMap[selectedY * worldSizeX + selectedX];
            tileMap[selectedY * worldSizeX + selectedX] %= 5;
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r)
    {
        for(int y = 0; y < worldSizeY; y++)
            for(int x = 0; x < worldSizeX; x++)
            {
                //System.out.println(x + " " + y + " " + (y * worldSizeX + x));
                //System.out.println(tileMap[y * worldSizeX + x]);
                int[] points = calcScreenCoordsFromWorldCoords(x, y);
                drawTile(r,
                        tileMap[y * worldSizeX + x],
                        points[0],
                        points[1]);
            }

        r.drawText("Mouse: " + mx + ", " + my + "\nCell: " + cellX + ", " + cellY + "\nSelected: " + selectedX + ", " + selectedY, 0, 12, Color.WHITE.getRGB());
        r.drawRect(cellX * tileWidth, cellY * tileHeight, tileWidth, tileHeight, Color.YELLOW.getRGB());
        int[] points = calcScreenCoordsFromWorldCoords(selectedX, selectedY);
        r.drawImage(new Image("/tiles/highlight.png"), points[0], points[1]);

    }

    public void drawTile(Renderer r, int tileType, int x, int y)
    {
        Image toDraw = null;
        int offX = 0, offY = 0;
        switch(tileType)
        {
            case 1:
                toDraw = new Image("/tiles/grass.png");
                break;
            case 2:
                toDraw = new Image("/tiles/water.png");
                break;
            case 3:
                toDraw = new Image("/tiles/hole.png");
                break;
            case 4:
                toDraw = new Image("/tiles/tree.png");
                offY -= tileHeight;
                break;
            case 0:
            default:
                toDraw = new Image("/tiles/wall.png");
                offY -= tileHeight;
                break;
        }

        r.drawImage(toDraw, x + offX, y + offY);
    }

    public int[]  calcScreenCoordsFromWorldCoords(int x, int y)
    {
        return new int[]{
                (worldOriginX * tileWidth) + (x - y) * (tileWidth / 2),
                (worldOriginY * tileHeight) + (x + y) * (tileHeight / 2)
        };
    }


    public static void main(String[] args)
    {
        GameContainer gc = new GameContainer(new GameManager(32, 16, 14, 10, 5 ,1),
                640, 360, 2);
        gc.start();
    }
}
