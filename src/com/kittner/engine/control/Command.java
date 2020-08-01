package com.kittner.engine.control;

public abstract class Command implements ICommand
{
    protected int priority;

    public Command()
    {
        this.priority = 0;
    }

    public Command(int priority)
    {
        this.priority = priority;
    }

    @Override
    public abstract void execute();
}
