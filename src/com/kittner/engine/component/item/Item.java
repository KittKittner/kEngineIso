package com.kittner.engine.component.item;

import com.kittner.engine.component.quest.ICountable;

public abstract class Item implements ICountable
{
    protected int count;
    protected String name, desc;

    public Item(String name, String desc, int count)
    {
        this.name = name;
        this.desc = desc;
        this.count = count;
    }

    public String print()
    {
        return String.format("%s x %d%n    %s%n%n", this.name, this.count, this.desc);
    }

    @Override
    public String getObjectiveName()
    {
        return this.name;
    }
}
