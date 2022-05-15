
package ru.bgpu.zombies;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;


public class Live {
    
    Image GO = new ImageIcon(getClass().getResource("/image/go.png")).getImage();
    Image h = new ImageIcon(getClass().getResource("/image/h.png")).getImage();
    Image d = new ImageIcon(getClass().getResource("/image/d.png")).getImage();
    
    boolean go = false;
    int live = 5;
    int money = 0;
    
    Font font = new Font(Font.DIALOG, Font.BOLD,30);
    
    int interval = 5;
    int iteration = 0;
    
    
    public void paint(Graphics g) {
        if(go) {
            g.drawImage(GO, (Zombies.Z_WIDTH-GO.getWidth(null))/2, (Zombies.Z_HEIGHT-GO.getHeight(null))/2, null);
        } else {
            for(int i=0; i<live; i++) {
                g.drawImage(h, 30+i*35, 10, null);
            }
        }
        g.drawImage(d, Zombies.Z_WIDTH-200, 10, null);
        g.setFont(font);
        g.setColor(Color.GREEN);
        g.drawString(Integer.toString(money), Zombies.Z_WIDTH-150, 37);
        if(++iteration == interval) {
            money++;
        }
        iteration %= interval;
    }
    
    public void kill(){
        live--;
        go = live == 0;
        money -= 300;
    }
    
    public void up() {
        money += 50;
    }
}
