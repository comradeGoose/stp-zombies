
package ru.bgpu.zombies;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;


public class Player
{
    Image fon = new ImageIcon(getClass().getResource("/image/player-m-0.png")).getImage();
    Image fon_6666 = new ImageIcon(getClass().getResource("/image/player.png")).getImage();
    Image f1 = new ImageIcon(getClass().getResource("/image/fire1.png")).getImage();
    Image f2 = new ImageIcon(getClass().getResource("/image/fire2.png")).getImage();
    Image f3 = new ImageIcon(getClass().getResource("/image/fire3.png")).getImage();
    
    public final static int pozitions[] = {150, 250, 350};
    public Image[] fire = {f1,f2,f3};
    
    int pIndex = 0;
    boolean lock = false;

    boolean skin_666 = false;

    int fireIndex = -1;
    
    Zombies zombies;



    public Player(Zombies zombies)
    {
        this.zombies = zombies;
    }

    public void paint(Graphics g)
    {

        if(skin_666)
        {
            g.drawImage(fon_6666, 50, pozitions[pIndex], null);
        }
        else
        {
            g.drawImage(fon, 50, pozitions[pIndex], null);
        }
        if(fireIndex >= 0)
        {
            if(fireIndex >= fire.length)
            {
                fireIndex = -1;
                lock = false;
            }
            else
            {
                g.drawImage(fire[fireIndex++], 50, pozitions[pIndex], null);
            }
        }
    }
    
    public void up()
    {
        if(lock)
        {
            return;
        }
        if(--pIndex < 0)
        {
            pIndex = 0;
        }
    }
    
    public void down()
    {
        if(lock)
        {
            return;
        }
        if(++pIndex >= pozitions.length)
        {
            pIndex = pozitions.length-1;
        }
    }
    
    public void fire()
    {
        if(lock)
        {
            return;
        }
        Zombies.playSound("fire.wav");
        lock = true;
        fireIndex = 0;
    }
}
