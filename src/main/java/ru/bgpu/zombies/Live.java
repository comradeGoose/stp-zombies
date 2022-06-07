
package ru.bgpu.zombies;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;


public class Live {
    public int font_size = 60;
    Image button_fon = new ImageIcon(getClass().getResource("/image/mainmenu_1.png")).getImage();
    Image GO = new ImageIcon(getClass().getResource("/image/go.png")).getImage();
    Image h = new ImageIcon(getClass().getResource("/image/h.png")).getImage();
    Image d = new ImageIcon(getClass().getResource("/image/d.png")).getImage();
    Image airplane = new ImageIcon(getClass().getResource("/image/airplane.png")).getImage();
    int[] med_kit_icon_index = {0,0,0,0};
    Image[] med_kit_icon_ = new Image[4];
    
    boolean go = false;
    int live = 4; //улучшение на свое усмотрение.
    int money = 0;
    
    Font font = new Font(Font.DIALOG, Font.BOLD,30);
    
    int interval = 5;
    int iteration = 0;
    
    
    public void paint(Graphics g) {
        if(go) {
            g.drawImage(GO, (Zombies.Z_WIDTH-GO.getWidth(null))/2, (Zombies.Z_HEIGHT-GO.getHeight(null))/2 - 50, null);

            g.drawImage(button_fon, (Zombies.Z_WIDTH-GO.getWidth(null)),(Zombies.Z_HEIGHT-GO.getHeight(null))+100,null);

            g.setColor(Color.white);
            g.setFont(new Font("Bauhaus 93",Font.ITALIC,font_size));
            long length_btn_name = (int)g.getFontMetrics().getStringBounds("Menu", g ).getWidth();
            //g.drawString("Menu",(300 + 270 / 2)-(int)(length_btn_name / 2), ((425 + 140) + 100 + (100 / 3)*2));
            g.drawString("Menu",300, 425);



        } else {
            for(int i=0; i<live; i++) {
                g.drawImage(h, 30+i*35, 10, null);
            }
            for(int i=0; i<4; i++) {
                med_kit_icon_[i] = new ImageIcon(getClass().getResource("/image/med_kit_icon_"+ med_kit_icon_index[i] +".png")).getImage();
                g.drawImage(med_kit_icon_[i], 200+i*35, 10, null);
            }
            if(money > 1)
            {
                g.drawImage(airplane, 400 , 0 , null);
            }
        }
        g.drawImage(d, Zombies.Z_WIDTH-200, 10, null);
        g.setFont(font);
        g.setColor(Color.GREEN);
        g.drawString(Integer.toString(money), Zombies.Z_WIDTH-150, 37);
        if(++iteration == interval) {
            //money++;
        }
        iteration %= interval;
    }
    
    public void kill(){
        live--;
        go = live == 0;
        money -= 300;
    }
    
    public void up() {
        money += 100;
    }
}
