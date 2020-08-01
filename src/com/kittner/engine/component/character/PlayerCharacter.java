package com.kittner.engine.component.character;

import com.kittner.engine.component.item.Item;

import java.util.HashMap;

public class PlayerCharacter extends Creature
{
    private int age; //TODO: perhaps a birthdate is better, so as to get a more precise age?
    private HashMap<String, Item> inv, activeInv;



    @Override
    public String toString()
    {
        return getFullName();
    }

    @Override
    public void interact(Creature creature)
    {

    }
}
