package com.kittner.engine.component.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scene
{
    private static HashMap<String, Scene> sceneHashMap;
    private List uiLayer;
    private List charLayer;
    private List envLayer;
    private List bgLayer;

    public Scene()
    {
        uiLayer = new ArrayList<ArrayList<Node>>();
    }
}
