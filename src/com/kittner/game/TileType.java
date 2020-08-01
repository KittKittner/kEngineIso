package com.kittner.game;

public enum TileType
{
    WALL(0),
    GRASS(1),
    WATER(2),
    HOLE(3),
    TREE(4);



    private int id;
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
