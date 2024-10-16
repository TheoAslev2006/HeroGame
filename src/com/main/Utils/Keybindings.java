package com.main.Utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keybindings implements KeyListener {
    public Boolean up =false;
    public Boolean down=false;
    public Boolean right=false;
    public Boolean left=false;
    public Boolean destroy = false;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case KeyEvent.VK_W:
                up = true;
                //System.out.println("up");
                break;
            case KeyEvent.VK_S:
                down = true;
                //System.out.println("down");
                break;
            case KeyEvent.VK_A:
                left = true;
                //System.out.println("left");
                break;
            case KeyEvent.VK_D:
                right = true;
                //System.out.println("right");
                break;
            case KeyEvent.VK_SPACE:
                destroy =  true;
                //System.out.println("Destroy");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
        }
    }
}
