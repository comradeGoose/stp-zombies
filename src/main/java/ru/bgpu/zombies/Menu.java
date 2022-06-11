package ru.bgpu.zombies;

import javax.swing.*;
import java.awt.*;

public class Menu
{
    public boolean btn_game_play = false;
    Image fon = new ImageIcon(getClass().getResource("/image/krov.png")).getImage();
    Image game_play = new ImageIcon(getClass().getResource("/image/zombie.gif")).getImage();
    private Color buttno_text_color;
    private int x;
    private int y;
    private int width;
    private int height;
    public int scale_font_play = 60;
    public int scale_font_exit = 60;
    public int[] scale_font = new int[2];

    String[] list_btn = new String[2];


    public Menu()
    {

        x = 50;
        y = 50;
        height = 100;
        width = 200;
        list_btn[0] = "Play";
        list_btn[1] = "Exit";
        //list_btn[2] = "Control";
        buttno_text_color = Color.WHITE;
    }


    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return  height;
    }


    public void draw(Graphics g)
    {

        for(int i = 0; i < list_btn.length; i++)
        {
            g.drawImage(fon, (int)x,(int)(y+140)*i+100,null);

            g.setColor(buttno_text_color);
            g.setFont(new Font("Bauhaus 93",Font.ITALIC,scale_font[i]));

            long length_btn_name = (int)g.getFontMetrics().getStringBounds(list_btn[i], g ).getWidth();
            g.drawString(list_btn[i],(int)(x + width / 2)-(int)(length_btn_name / 2), (int)((y + 140) * i + 100 + (height / 3)*2));
        }

        g.drawImage(new ImageIcon(getClass().getResource("/image/LOGO.png")).getImage(), Zombies.Z_WIDTH/2 - 100, Zombies.Z_HEIGHT/2 - 200, null);
        //g.drawString("♂",Zombies.Z_WIDTH/2 - 50,  + 50);

        if(btn_game_play)
            {
                g.drawImage(game_play,Zombies.Z_WIDTH/2 - 50,Zombies.Z_HEIGHT/2 - 100,null);

            }

        }

    }
