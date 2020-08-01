package com.kittner.engine.control;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Broker
{
    //TODO: PriorityBlockingQueue could help with multi-threading and priority
    protected List<Command> commandList = new ArrayList<Command>();

    protected void add(Command command)
    {
        commandList.add(command);
    }
    protected void addAll(List<Command> commands)
    {
        commandList.addAll(commands);
    }

    //peek at the first command added to the list.  TODO: Add a sort so the first command to be executed is shown instead
    public Command peek()
    {
        return commandList.get(0);
    }

    //return the first n commands added to the list.  TODO: Add a sort so the first commands to be executed are shown instead
    public ArrayList<Command> peek(int n)
    {
        return new ArrayList<Command>(commandList.subList(0, n));
    }

    public void doAll()
    {
        commandList.sort(new Comparator<Command>() {
            @Override
            public int compare(Command c1, Command c2) {
                if(c1.priority < c2.priority)
                    return 1; //higher priority is first in list
                else if(c1.priority > c2.priority)
                    return -1;

                return 0;
            }
        });

        for(Command command : commandList)
        {
            command.execute();
        }

        commandList.clear();
    }
}
