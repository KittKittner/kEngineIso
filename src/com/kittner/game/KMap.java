package com.kittner.game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class KMap
{
    public static final int MAX_CACHED_MAPS = 8;

    public static Map<String, KMap> maps = new LinkedHashMap<String, KMap>(MAX_CACHED_MAPS);
    private Location rootLocation;
    private int worldSizeX, worldSizeY;
    private int[] tileMap;

    public KMap(String filepath)
    {
        if(maps.containsKey(filepath))
        {
            KMap temp = maps.get(filepath);
            worldSizeX = temp.getWorldSizeX();
            worldSizeY = temp.getWorldSizeY();
            tileMap = temp.getTileMap();
            updateGame();
        }
        else
        {
            if(maps.size() >= MAX_CACHED_MAPS)
            {
                Set<String> mapSet = maps.keySet();
                maps.remove(mapSet.iterator().next()); //remove the least recently cached map
                System.out.println("ininiininin");
            }
            this.parse(filepath);
        }

    }

    private void parse(String filepath)
    {
        try
        {
            this.createWorldMapBounds(filepath);

            int x = 0, y = 0;
            Scanner in = new Scanner(new FileReader(filepath)), line;
            in.useDelimiter("\\s+");
            while (in.hasNext())
            {
                String nextLine = in.nextLine();
                if (!isAComment(nextLine)) //if not a comment --- [########## this is a comment]
                {
                    //System.out.println(nextLine);
                    line = new Scanner(nextLine);
                    line.useDelimiter("[\\t\\s]+");
                    while(line.hasNext())
                    {
                        String next = line.next();
                        tileMap[y * worldSizeX + x] = TileType.forName(next) != null ? TileType.forName(next).id : 0;
                        x++;
                        //System.out.println(TileType.forName(next));
                    }
                    x = 0;
                    y++;
                    line.close();
                }
            }
            in.close();
            this.updateGame();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void updateGame()
    {
        GameManager.currentMap = this;
        GameManager.worldSizeX = worldSizeX;
        GameManager.worldSizeY = worldSizeY;
        GameManager.tileMap = tileMap;
    }

    //update the runner with the relevant changes to the world
    private void createWorldMapBounds(String path) throws FileNotFoundException
    {
        determineSizeOfWorldMap(path);
        tileMap = new int[worldSizeX * worldSizeY + worldSizeX + 1];
        maps.put(path, this);
    }
    //sets the KMap size of the world in tiles after one iteration through the given file
    private void determineSizeOfWorldMap(String path) throws FileNotFoundException
    {
        Scanner in = new Scanner(new FileReader(path)), line;
        int xCounter = 0, yCounter = 0;
        while(in.hasNextLine())
        {
            String nextLine = in.nextLine();
            if(!isAComment(nextLine))
            {
                yCounter++;
                line = new Scanner(nextLine);
                line.useDelimiter("[\\t\\s]+");
                while (line.hasNext())
                {
                    xCounter++;
                    line.next();
                }
                line.close();
            }
            worldSizeX = Math.max(xCounter, worldSizeX);
            worldSizeY = Math.max(yCounter, worldSizeY);
            xCounter = 0;
        }
        in.close();
    }

    private static boolean isAComment(String s)
    {
        return s.matches("^\\s*#{3,}.*"); //if not a comment --- [########## this is a comment]
    }

    private void updateTileMaps(int[] newMap)
    {
        this.tileMap = newMap;
        GameManager.tileMap = newMap;
    }


    public int getWorldSizeX()
    {
        return worldSizeX;
    }
    public void setWorldSizeX(int worldSizeX)
    {
        this.worldSizeX = worldSizeX;
    }

    public int getWorldSizeY()
    {
        return worldSizeY;
    }
    public void setWorldSizeY(int worldSizeY)
    {
        this.worldSizeY = worldSizeY;
    }

    public int[] getTileMap()
    {
        return tileMap;
    }
    public void setTileMap(int[] tileMap)
    {
        this.tileMap = tileMap;
    }
}
