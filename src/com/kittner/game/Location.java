package com.kittner.game;

import java.util.HashMap;
import java.util.Map;

public class Location
{
    private Map<String, Location> children;
    private Location parent;
    private static Location world = new Location("World");
    private String name;

    static
    {

    }

    //constructor for the root location with a cyclical parent pointer
    private Location(String name)
    {
        this.name = name;
        this.parent = this;
        this.children = new HashMap<String, Location>();
    }

    public Location(String name, Location parent) throws Exception
    {
        this.name = name;
        this.children = new HashMap<String, Location>();
        if(isInWorldTree(parent))
        {
            this.parent = parent;
            parent.addChild(this);
        }
        else
            throw new Exception("Parent location " + parent.getName() + " is not a part of the world tree and location " + this.name + " could not be added.");
    }


    protected boolean isInWorldTree(Location target)
    {
        if(target.equals(world)) return true;
        return traverse(world, target);
    }
    private boolean traverse(Location root, Location target)
    {
        for(Location current : root.getChildren().values())
        {
            if(current.equals(target) || traverse(current, target))
                return true;
        }
        return false;
    }

    public void addChild(Location loc)
    {
        children.put(loc.getName(), loc);
    }
    public Map<String, Location> getChildren()
    {
        return children;
    }

    public Map<String, Location> getSiblings()
    {
        return parent.getChildren();
    }

    public Location getParent()
    {
        return parent;
    }

    public static Location getRoot()
    {
        return world;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
}
