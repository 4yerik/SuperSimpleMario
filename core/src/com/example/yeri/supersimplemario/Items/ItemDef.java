package com.example.yeri.supersimplemario.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Yeri on 2016-12-30.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public  ItemDef(Vector2 position, Class<?> type)
    {
        this.position = position; // saves the coordinates
        this.type = type; // saves the class it will follow
    }
}
