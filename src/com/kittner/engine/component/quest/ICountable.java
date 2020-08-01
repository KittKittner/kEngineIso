package com.kittner.engine.component.quest;

import com.kittner.engine.component.character.Creature;

public interface ICountable
{
    void interact(Creature creature);
    String getObjectiveName();
}
