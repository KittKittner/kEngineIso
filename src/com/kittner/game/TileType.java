package com.kittner.game;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TileType
{
    WATER(0),
    GRASS(1),
    WALL(2),
    HOLE(3),
    TREE(4);

    // Deal with caching the values for reference
    //Thanks to user Jon Skeet: https://stackoverflow.com/questions/1167982/check-if-enum-exists-in-java/15154737
    private static final Map<String, TileType> nameToValueMap = new HashMap<String, TileType>();
    static
    {
        for(TileType tileType : EnumSet.allOf(TileType.class))
        {
            nameToValueMap.put(tileType.name(), tileType);
        }
    }
    public static TileType forName(String name)
    {
        return nameToValueMap.get(name.toUpperCase());
    }
    //

    public int id;
    private TileType(int id)
    {
        this.id = id;
    }


    @Override
    public String toString()
    {
        return "" + this.id;
    }
}
