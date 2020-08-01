package com.kittner.engine.component.quest;

import com.kittner.engine.component.character.Creature;
import com.kittner.engine.component.item.Item;

public class Objective
{
    private int current = 0, goal;
    private boolean isComplete = false;
    private ICountable target;

    public Objective(ICountable target, int goal)
    {
        this.target = target;
        this.goal = goal;
    }

    //Return a string that looks like one of:
    //    - Kill 3/12 Kobold
    //    - Collect 0/3 Books
    //    - Visit the Babbling Brooks
    //(the gap beforehand comes from Quest, not here)
    public String getFullText()
    {
        if(goal == -1) //-1 used for visiting locations
            return String.format("- %s", target.getObjectiveName());
        else
            return String.format("- %s %d/%d %s (%s)", target instanceof Item ? "Collect" : target instanceof Creature ? "Kill" : "", current, goal, target.getObjectiveName(), isComplete ? "Completed" : "Incomplete");
    }

    public void increment()
    {
        increment(1);
    }
    public void increment(int value)
    {
        if(current + value <= goal)
            current += value;

        if(current >= goal || goal == -1)
            isComplete = true;
    }

    public void decrement()
    {
        decrement(1);
    }
    public void decrement(int value)
    {
        if(current - value >= 0)
            current -= value;
        if(current < goal || goal == -1)
            isComplete = false;
    }

    public boolean isComplete()
    {
        return isComplete;
    }
    public void isComplete(boolean value)
    {
        isComplete = value;
    }

    public ICountable getTarget()
    {
        return target;
    }
}
