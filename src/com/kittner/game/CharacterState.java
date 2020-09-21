package com.kittner.game;

public enum CharacterState
{
    IDLE(100),
    MOVING_NORTH(201),
    MOVING_EAST(202),
    MOVING_SOUTH(203),
    MOVING_WEST(204),
    USING_NORTH(211),
    USING_EAST(212),
    USING_SOUTH(213),
    USING_WEST(214);

    private int state;
    private CharacterState(int state)
    {
        this.state = state;
    }
}
