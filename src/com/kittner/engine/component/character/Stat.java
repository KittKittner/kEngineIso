package com.kittner.engine.component.character;

public abstract class Stat
{
    private String name;
    private int value, lBound, uBound;

    private Character character;

    public Stat()
    {
        this.character = null;
        this.name = "BAD STAT";
        this.value = -1;
        this.lBound = -1;
        this.uBound = -1;
    }

    public Stat(Character character, String name, int value, int lBound, int uBound)
    {
        this.character = character;
        this.name = name;
        this.value = value;
        this.lBound = lBound;
        this.uBound = uBound;
    }

    public boolean add(int toAdd)
    {
        int afterAdd = this.value + toAdd;
        if(afterAdd > this.uBound)
        {
            this.value = afterAdd;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean remove(int toTake)
    {
        int afterTake = this.value - toTake;
        if(afterTake < this.lBound)
        {
            this.value = afterTake;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean compare(Stat toCompare)
    {
        return toCompare.getName().equals(this.name);
    }
    public boolean compare(Stat toCompare, CompareType type)
    {
        switch(type)
        {
            case COMPARE_BY_NAME:
                return toCompare.getName().equals(this.name);
            case COMPARE_BY_VALUE:
                return toCompare.getValue() == this.value;
            case COMPARE_BY_CHARACTER:
                return toCompare.getCharacter().equals(this.character);
            default:
                return false;
        }
    }

    public void setCharacter(Character character)
    {
        this.character = character;
    }
    public Character getCharacter()
    {
        return this.character;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
    public int getValue()
    {
        return this.value;
    }
}
