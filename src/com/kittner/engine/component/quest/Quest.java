package com.kittner.engine.component.quest;

import com.kittner.engine.component.character.NonPlayerCharacter;

import java.util.Arrays;
import java.util.List;

public class Quest implements IQuestable
{
    private String name, desc;
    private NonPlayerCharacter giver, ender; //TODO: can quests be given by items? or are they given by a "god" character?
    private List<Objective> objectives;

    public Quest(String name, String desc, NonPlayerCharacter giver, NonPlayerCharacter ender, Objective... objectives)
    {
        this.name = name;
        this.desc = desc;
        this.giver = giver;
        this.ender = ender;
        this.objectives = Arrays.asList(objectives);
    }

    //when the giver and ender are the same character
    public Quest(String name, String desc, NonPlayerCharacter giver, Objective... objectives)
    {
        this(name, desc, giver, giver, objectives);
    }

    @Override
    public String getSmallLogText()
    {
        String finalString = name + ":\n";
        for(Objective obj : objectives)
            finalString += "\t" + obj.getFullText() + "\n";
        return finalString + "Given by: " + giver + "\nTurn in to: " + ender + "\n----------";
    }

    @Override
    public String getBigLogText()
    {
        String finalString = name + ":\n" + desc + "\n";
        for(Objective obj : objectives)
            finalString += "\t" + obj.getFullText() + "\n";
        return finalString + "Given by: " + giver + "\nTurn in to: " + ender + "\n----------";
    }


    public Objective getObjective(int index)
    {
        return objectives.get(index);
    }
    public List<Objective> getObjectives()
    {
        return objectives;
    }

}
