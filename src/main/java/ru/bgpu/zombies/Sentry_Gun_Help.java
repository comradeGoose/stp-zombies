package ru.bgpu.zombies;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Sentry_Gun_Help
{
    private Image _Gun_Image = new ImageIcon(getClass().getResource("/image/sentry_gun.png")).getImage();
    private Image _Gun_Image_Icon = new ImageIcon(getClass().getResource("/image/sentry_gun_icon.png")).getImage();
    private Image _Shot1 = new ImageIcon(getClass().getResource("/image/fire1.png")).getImage();
    private Image _Shot2 = new ImageIcon(getClass().getResource("/image/fire2.png")).getImage();
    private Image _Shot3 = new ImageIcon(getClass().getResource("/image/fire3.png")).getImage();
    public Image _Placement = new ImageIcon(getClass().getResource("/image/placement_0.png")).getImage();
    public Image _Select_Placement = new ImageIcon(getClass().getResource("/image/placement_1.png")).getImage();

    public int _pozitions[] = {150, 250, 350};
    public Image[] _Shot = {_Shot1, _Shot2, _Shot3};
    public Image[] _Placements = {_Placement, _Placement, _Placement};

    private int _pIndex = 0;
    private int _fireIndex = -1;
    Zombies zombies;

    private int _interval = 10;
    private int _iteration = 0;
    private int _time = 0;

    public boolean shot = false;

    double r = Math.ceil(Math.random() * 3);
    public int pose = (int) r - 1;

    public Sentry_Gun_Help(Zombies zombies)
    {
        this.zombies = zombies;
        System.out.println(pose);
    }

    public void paint(Graphics g)
    {
        g.drawImage(_Gun_Image_Icon, 500, 0, null);


        g.drawImage(_Gun_Image, 245, _pozitions[pose] + 10, null);

        if(_fireIndex >= 0)
        {
            if(_fireIndex >= _Shot.length)
            {
                _fireIndex = -1;
            }
            else
            {
                g.drawImage(_Shot[_fireIndex++], 195, _pozitions[pose] -3, null);
            }
        }

        if(++_iteration == _interval)
        {
            _time++;
            if(_time == 2)
            {
                //System.out.println(_time);
                shot = true;
                _time = 0;
            }
            //System.out.println(_time);
        }
        _iteration %= _interval;

    }

    public void fire()
    {
        _fireIndex = 0;
        Zombies.playSound("fire.wav");
        shot = false;
    }

}
