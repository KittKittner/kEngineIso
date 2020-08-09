package com.kittner.game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class KMap
{
    public static Map<String, KMap> maps = new LinkedHashMap<String, KMap>();
    private static int worldSizeX, worldSizeY;

    public KMap(String filepath)
    {

    }

    public static void parse(String filepath)
    {
        try
        {
            KMap.createWorldMapBounds(filepath);

            int x = 0, y = 0;
            Scanner in = new Scanner(new FileReader(filepath)), line;
            in.useDelimiter("\\s+");
            while (in.hasNext())
            {
                String nextLine = in.nextLine();
                if (!isAComment(nextLine)) //if not a comment --- [########## this is a comment]
                {
                    System.out.println(nextLine);
                    line = new Scanner(nextLine);
                    line.useDelimiter("[\\t\\s]+");
                    while(line.hasNext())
                    {
                        String next = line.next();
                        GameManager.tileMap[y * worldSizeX + x] = TileType.forName(next) != null ? TileType.forName(next).id : 0;
                        x++;
                        /*int[] temp = new int[]{0,0,0,0};
                        if(line.hasNextInt())
                            System.out.println(temp);
                        else*/
                            System.out.println(TileType.forName(next));
                    }
                    x = 0;
                    y++;
                    line.close();
                }
            }
            in.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static void createWorldMapBounds(String path) throws FileNotFoundException
    {
        determineSizeOfWorldMap(path);
        GameManager.worldSizeX = worldSizeX;
        GameManager.worldSizeY = worldSizeY;
        GameManager.tileMap = new int[worldSizeX * worldSizeY + worldSizeX + 1];
    }
    private static void determineSizeOfWorldMap(String path) throws FileNotFoundException
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

}
