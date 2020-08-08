package com.kittner.game;

import com.kittner.engine.AbstractGame;
import com.kittner.engine.GameContainer;
import com.kittner.engine.Renderer;
import com.kittner.engine.gfx.Image;

import java.awt.*;
import java.awt.event.MouseEvent;
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

        /*for(int x = 0; x < worldSizeX; x++)
            for(int y = 0; y < worldSizeY; y++)
                System.out.println(worldSizeX * y + x + y);*/


        tileMap = new int[worldSizeX * worldSizeY + worldSizeX + 1];
        for(int i = 0; i < tileMap.length; i++)
        {
            //if(i >=1 && i < )
            tileMap[i] = rand.nextInt(5);
        }
    }

    @Override
    public void update(GameContainer gc, float dt)
    {
        //mouse coords
        mx = gc.getInput().getMouseX();
        my = gc.getInput().getMouseY();
        //screen cell coords
        cellX = mx / tileWidth;
        cellY = my / tileHeight;
        //screen cell offset of mouse coords
        int cellMX = mx % tileWidth;
        int cellMY = my % tileHeight;
        //normalised offset
        float cellMXNormal = (float)cellMX / tileWidth;
        float cellMYNormal = (float)cellMY / tileHeight;
        //the world cell the mouse is in
        selectedX = (cellY - worldOriginY) + (cellX - worldOriginX);
        selectedY = (cellY - worldOriginY) - (cellX - worldOriginX);

        //determine whether the selected tile must be changed according to the geometry of the tile and the mouse position
        double d = Math.sqrt(Math.abs(cellMX-tileWidth/2)+Math.abs(cellMY-tileHeight/2));
        double lb = Math.sqrt((double)tileWidth/4 + (double)tileHeight/4); //from the middle of the cell to the midpoint of the side of the tile
        double ub = Math.sqrt((double)tileWidth/2 + (double)tileHeight/2); //from the middle of the cell to the corner of the cell
        if(lb < d && d < ub) //if the mouse is within the four corners of the cell
            if (cellMYNormal >= cellMXNormal) //if the mouse is in the lower left half of the cell
            {
                if (cellMYNormal < 0.5) //if the mouse is in the upper half of the cell
                    selectedX -= 1;
                else if (cellMXNormal < 0.5) //if the mouse is in left half of the cell
                    selectedY += 1;
                else //if the mouse is in the right half of the cell
                    selectedX += 1;
            }
            else //if the mouse is in the upper right half of the cell
            {
                if(cellMXNormal < 0.5) //if the mouse is in the left half of the cell
                    selectedX -= 1;
                else if(cellMYNormal < 0.5) //if the mouse is in the upper half of the cell
                    selectedY -= 1;
                else //if the mouse is in the lower half of the cell
                    selectedX += 1;
            }
        worldX = (worldOriginX * tileWidth) + (cellX - cellY) * tileWidth / 2;
        worldY = (worldOriginY * tileHeight) + (cellX + cellY) * tileHeight / 2;


        if(selectedX >= 0 && selectedX < worldSizeX && selectedY >= 0 && selectedY < worldSizeY)
            if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
            {
                ++tileMap[selectedY * worldSizeX + selectedX];
                tileMap[selectedY * worldSizeX + selectedX] %= 5;
            }
            else if(gc.getInput().isButtonDown(MouseEvent.BUTTON3))
            {
                if(tileMap[selectedY * worldSizeX + selectedX] - 1 < 0) tileMap[selectedY * worldSizeX + selectedX] = 4;
                else --tileMap[selectedY * worldSizeX + selectedX];
            }

    }

    @Override
    public void render(GameContainer gc, Renderer r)
    {
        for(int y = 0; y < worldSizeY; y++)
            for(int x = 0; x < worldSizeX; x++)
            {
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
        GameContainer gc = new GameContainer(new GameManager(32, 16, 17, 17, 10 ,3),
                640, 360, 2);
        gc.start();
    }
}
