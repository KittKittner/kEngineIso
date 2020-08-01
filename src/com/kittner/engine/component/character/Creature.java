package com.kittner.engine.component.character;

import com.kittner.engine.component.quest.ICountable;

import java.util.HashMap;

public abstract class Creature implements ICountable
{
    //0 = first name, 1 = last name, 2 = title, 3 = nickname, 4 = middle name/s
    private String[] names;
    private HashMap<String, Stat> stats;

    public Creature(String... names)
    {
        this.names = names;
    }

    public String getFullName()
    {
        switch (names.length)
        {
            case 1:
                return names[0];
            case 2:
                return names[0] + " " + names[1];
            case 3:
                return names[2] + " " + names[0] + " " + names[1];
            case 4:
                return names[2] + " " + names[0] + " \"" + names[3] + "\" " + names[1];
            default:
                return names[2] + " " + names[0] + " \"" + names[3] + "\" " + names[4] + " " + names[1];
        }
    }

    @Override
    public String getObjectiveName()
    {
        return getFullName();
    }

    @Override
    public String toString()
    {
        return getFullName();
    }

}
