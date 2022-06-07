package ru.bgpu.zombies;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.Random;

public class Med_Kit_Help
{
    int pos_x;
    int pos_y;
    Zombies med_;
    public int positions[] = {50, 150, 250};
    public int id;

    int index_med_kit = 0;



    public void paint(Graphics g)
    {
        double y = Math.ceil(Math.random() * 2);
        double x = Math.ceil(Math.random() * 600);
        id = (int) y - 1;
        if(x<300)
        {
            x += 100;
        }

        if(Zombies.local_pos_med_y != 500)
        {
            pos_x = Zombies.local_pos_med_x;
            pos_y = Zombies.local_pos_med_y;
        }
        else
        {
            pos_x = (int) x;
            pos_y = positions[(int) y];
            Zombies.local_pos_med_x = (int) x;
            Zombies.local_pos_med_y = positions[(int) y];
        }

        g.drawImage(new ImageIcon(getClass().getResource("/image/med_kit_" + index_med_kit + ".png")).getImage(), pos_x, pos_y, null);


    }

    void fire(int i)
    {
        index_med_kit += i;

        if(index_med_kit == 2)
        {
            Zombies.local_pos_med_x = 800;
            Zombies.local_pos_med_y = 500;
            index_med_kit = 0;
            med_.med_kit = false;
        }




    }

}