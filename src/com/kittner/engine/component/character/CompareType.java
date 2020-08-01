package com.kittner.engine.component.character;

public enum CompareType
{
    COMPARE_BY_NAME(1),
    COMPARE_BY_VALUE(2),
    COMPARE_BY_CHARACTER(3);

    public final int type;
    private CompareType(int type)
    {
        this.type = type;
    }
}
