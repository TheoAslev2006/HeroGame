package com.main.Sprites;

import java.awt.geom.Rectangle2D;

public class Sprite {
    public Rectangle2D hitbox;
    public int maximalHP;
    public Sprite(Rectangle2D hitbox, int maximalHP){
        this.hitbox = hitbox;
        this.maximalHP = maximalHP;

    }
}
