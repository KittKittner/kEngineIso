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
    public Location(String name)
    {
        if(name.equalsIgnoreCase("world"))
        {
            this.name = name;
            this.parent = this;
            this.children = new HashMap<String, Location>();
        }
        else if(isInWorldTree(name))
        {
            this.name = Location.get(name).getName();
            this.parent = Location.get(name).getParent();
            this.children = Location.get(name).getChildren();
        }
        else
        {
            this.name = name;
            this.parent = world;
            this.children = new HashMap<String, Location>();
        }
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
        {
            this.parent = world;
            world.addChild(this);
            //throw new Exception("Parent location " + parent.getName() + " is not a part of the world tree and location " + this.name + " could not be added.");
        }
    }
    public Location(String name, String parent)
    {
        this.name = name;
        this.children = new HashMap<String, Location>();
        if(isInWorldTree(parent))
        {
            this.parent = get(parent);
        }
        else
        {
            this.parent = new Location(parent);
        }
        this.parent.addChild(this);
    }

    public static Location get(String name){return get(name, world);}
    public static Location get(String name, Location parentIfAbsent)
    {
        if(isInWorldTree(name))
        {
            return traverseAndGet(world, name);
        }
        else
        {
            try
            {
                return new Location(name, parentIfAbsent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return world;
            }
        }
    }
    protected static Location traverseAndGet(Location root, String target)
    {
        for(Location current : root.getChildren().values())
        {
            if(current.getName().equalsIgnoreCase(target))
                return current;
        }
        return world;
    }

    protected static boolean isInWorldTree(String target)
    {
        if(target.equalsIgnoreCase("world")) return true;
        return traverse(world, target);
    }
    private static boolean traverse(Location root, String target)
    {
        for(Location current : root.getChildren().values())
        {
            if(current.getName().equalsIgnoreCase(target) || traverse(current, target))
                return true;
        }
        return false;
    }
    protected static boolean isInWorldTree(Location target)
    {
        if(target.equals(world)) return true;
        return traverse(world, target);
    }
    private static boolean traverse(Location root, Location target)
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
