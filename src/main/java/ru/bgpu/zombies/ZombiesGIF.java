
package ru.bgpu.zombies;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ZombiesGIF {
    
    private final static Image zi[] = new Image[8];
    private final static Image zb[] = new Image[3];
    private final static Image zbz[] = new Image[2];
    private final static Image zkb[] = new Image[8];
    
    static {
        for(int i=0; i<zi.length; i++)
            zi[i] = new ImageIcon(ZombiesGIF.class.getResource("/image/o_751a9bd95f6efb21-"+i+".png")).getImage();
        for(int i=0; i<zb.length; i++)
            zb[i] = new ImageIcon(ZombiesGIF.class.getResource("/image/b"+i+".png")).getImage();
        for(int i=0; i<zkb.length; i++)
            zkb[i] = new ImageIcon(ZombiesGIF.class.getResource("/image/k"+i+".png")).getImage();
        for(int i=0; i<zbz.length; i++)
            zbz[i] = new ImageIcon(ZombiesGIF.class.getResource("/image/bz"+i+".png")).getImage();
    }
    
    public static Image getZombiesImage(int index) {
        return zi[index];
    }
    public static Image getZombiesBlood(int index) {
        return zb[index];
    }
    public static Image getZombiesKill(int index) {
        return zkb[index];
    }
    public static Image getBlood(int index) {
        return zbz[index];
    }
}
