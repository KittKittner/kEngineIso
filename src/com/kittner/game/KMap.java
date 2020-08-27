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

    public KMap(int[] tileMap, int x, int y)
    {
        rootLocation = Location.getRoot();
        this.tileMap = tileMap;
        worldSizeX = x;
        worldSizeY = y;
        maps.put("random", this);
    }

    public KMap(String filepath)
    {
        if(maps.containsKey(filepath))
        {
            KMap temp = maps.get(filepath);
            worldSizeX = temp.getWorldSizeX();
            worldSizeY = temp.getWorldSizeY();
            tileMap = temp.getTileMap();
            rootLocation = temp.getRootLocation();
            updateGame();
        }
        else
        {
            if(maps.size() >= MAX_CACHED_MAPS)
            {
                Set<String> mapSet = maps.keySet();
                maps.remove(mapSet.iterator().next()); //remove the least recently cached map //TODO: might remove the most recently added
            }
            try
            {
                this.parse(filepath);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void parse(String filepath) throws Exception
    {
        try
        {
            this.createWorldMapBounds(filepath);

            int x = 0, y = 0;
            Scanner in = new Scanner(new FileReader(filepath));
            in.useDelimiter("\\s+");
            Location currentFirst = null, currentSecond = null, currentThird = null;
            while (in.hasNext())
            {
                String nextLine = in.nextLine();
                if (!isAComment(nextLine)) //if not a comment --- [########## this is a comment]
                {
                    Scanner line;
                    if(isMetaData(nextLine))
                    {
                        line = new Scanner(nextLine);
                        line.useDelimiter("\\s*:\\s*");
                        while(line.hasNext())
                        {
                            String next = line.next();
                            String tempName;
                            switch(next.trim())
                            {
                                case "root":
                                    this.rootLocation = new Location(line.next());
                                    break;
                                case "first":
                                    tempName = line.next();
                                    assert this.rootLocation != null;
                                    this.rootLocation.getChildren().putIfAbsent(tempName, new Location(tempName, this.rootLocation));
                                    currentFirst = Location.get(tempName, this.rootLocation);
                                    break;
                                case "second":
                                    tempName = line.next();
                                    assert currentFirst != null;
                                    currentFirst.getChildren().putIfAbsent(tempName, new Location(tempName, currentFirst));
                                    currentSecond = Location.get(tempName);
                                    break;
                                case "third":
                                    tempName = line.next();
                                    assert  currentSecond != null;
                                    currentThird = currentSecond.getChildren().putIfAbsent(tempName, new Location(tempName, currentSecond));
                                    break;
                                default:
                                    throw new Exception("Cannot read meta data [" + next + "]");
                            }
                        }
                    }
                    else
                    {
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
                    }
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
            if(!isAComment(nextLine) && !isMetaData(nextLine))
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
        //At the start of the line, if there is any amount of whitespace followed by at least three hashes, match to the rest of the line's characters too
        return s.matches("^\\s*#{3,}.*"); //if not a comment --- [########## this is a comment]
    }

    private static boolean isMetaData(String s)
    {
        //At the start of the line and after any amount of whitespace, if there is a dash, match with it and the rest of the characters
        return s.matches("^\\s*.+\\s*:\\s*.+$");
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

    public Location getRootLocation()
    {
        return rootLocation;
    }
    public void setRootLocation(Location loc)
    {
        rootLocation = loc;
    }
}
