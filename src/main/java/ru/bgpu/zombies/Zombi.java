
package ru.bgpu.zombies;

import java.awt.Graphics;
import java.util.Random;

public class Zombi
{
    
    public final static int pozitions[] = {70, 170, 270};
    
    int pIndex = 0;

    int spid = 10;
    int iterator = 0;
    int x;
    Zombies zombies;
    int fireIndex = -1;
    boolean kill = false;
    

    public Zombi(Zombies zombies, int x)
    {
        this.zombies = zombies;
        Random r = new Random();
        iterator = r.nextInt(8);
        this.x = x;
    }
    
    
    public void paint(Graphics g)
    {
        if(!kill)
        {
            g.drawImage(ZombiesGIF.getZombiesImage(iterator++), x, pozitions[pIndex], null);
            iterator %= 8;
            x-=spid;
            if(fireIndex >= 0)
            {
                if(fireIndex >= 3)
                {
                    fireIndex = -1;
                    kill = true;
                    iterator = 0;
                }
                else
                {
                    g.drawImage(ZombiesGIF.getZombiesBlood(fireIndex++), x, pozitions[pIndex], null);
                }
            }
        }
        else
        {
            if(iterator < 8)
            {
                g.drawImage(ZombiesGIF.getZombiesKill(iterator++), x, pozitions[pIndex], null);
                if(iterator < 4)
                {
                    x-=spid;
                    zombies.live.up();
                }
            }
            else
            {
                if(iterator < 25)
                {
                    g.drawImage(ZombiesGIF.getBlood(0), x, pozitions[pIndex], null);
                    iterator++;
                }
                else
                {
                    g.drawImage(ZombiesGIF.getBlood(1), x, pozitions[pIndex], null);
                    zombies.zombis.remove(this);
                }
            }
        }
    }

    void fire()
    {
         fireIndex = 0;
    }
}
