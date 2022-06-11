package ru.bgpu.zombies;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.Console;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Zombies extends JFrame implements ActionListener , KeyListener, MouseListener, MouseMotionListener {

    public static final int Z_WIDTH  = 800;
    public static final int Z_HEIGHT = 500;

//    Image fon;



    int maxCount = 2;
    int time_maxCount = 4;
    int maxSpeed = 20;
    int interval = 10;
    boolean flock = false;
    int iteration = 0;

    ArrayList<Zombi> zombis = new ArrayList<>();

    Timer timer = new Timer(100, this);
    boolean timer_start = true;



    Menu menyuha = new Menu();

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if( state == STATE.PLAY)
        {
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                if(getMousePosition().x >= 470 && getMousePosition().x <= 530 && getMousePosition().y >= 0 && getMousePosition().y <= 60)
                {
                    set_pos_Sentry_Gun = true;
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }

    public enum STATE{MENU,PLAY};

public static STATE state = STATE.MENU;

    Player player = new Player(this);
    Live live = new Live();

    public boolean click = false;

    public ArrayList<Med_Kit_Help> med_kit_help = new ArrayList<>();
    public boolean med_kit_call = false;
    public boolean med_kit_use = false;
    public static int local_pos_med_x = 0;
    public static int local_pos_med_y = 0;
    BufferedImage fon = new BufferedImage(Z_WIDTH, Z_HEIGHT, BufferedImage.TYPE_INT_RGB);

    Sentry_Gun_Help _Sentry_Gun = new Sentry_Gun_Help(this);
    private Image _Gun_Image = new ImageIcon(getClass().getResource("/image/sentry_gun.png")).getImage();
    boolean set_pos_Sentry_Gun = false;

    JPanel fonPanel = new JPanel()
    {
        @Override
        public void paint(Graphics g)
        {

            if(state.equals(STATE.PLAY))
            {


                g.drawImage(fon, 0, 0, null);


                _Sentry_Gun.paint(g);
                player.paint(g);

                if(set_pos_Sentry_Gun)
                {
                    g.drawImage(_Gun_Image, fonPanel.getMousePosition().x - 50, fonPanel.getMousePosition().y - 55, null);
                }



                if(med_kit_call)
                {
                    med_kit_help.get(0).paint(g);
                }

                for(int i=0; i<zombis.size(); i++)
                {
                    if(zombis.get(i).x < -100)
                    {
                        zombis.remove(i);
                        live.kill();
                        if(live.live == 0)
                        {
                            zombis.clear();

                        }
                        i--; continue;
                    }
                    zombis.get(i).paint(g);
                }
                live.paint(g);


                if(med_kit_call)
                {
                    med_kit_help.get(0).paint(g);
                }


                if(live.money >= 6666)
                {
                    player.skin_666 = true;
                }
            }

            if (state.equals(STATE.MENU))
            {
                g.drawImage(fon, 0, 0, null);
                menyuha.draw(g);
                timer_start = true;
                live.money = 0;
                live.go = false;
                live.live = 4;
                live._time = 0;

                for(int i = 0;i < 4;i++)
                {
                    live.med_kit_icon_index[i] = 0;
                }

                player.skin_666 = false;
            }
        }
    };

    public Zombies()
    {
        setTitle("Zombies!");
        timer.start();
        fon.createGraphics().drawImage(new ImageIcon(getClass().getResource("/image/fon.jpg")).getImage(),0,0,null);
        fonPanel.setPreferredSize(new Dimension(Z_WIDTH, Z_HEIGHT));
        setContentPane(fonPanel);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        addMouseListener(this);
        pack();
    }

    public static void main(String[] args)
    {
        Zombies.playSound("Flight_to_LAPD_WAW.wav");
        Zombies zombies = new Zombies();
        zombies.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        zombies.setLocation(((width-Z_WIDTH)/2), ((height-Z_HEIGHT)/2));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(state.equals(STATE.PLAY))
        {


            sentry_gun_fire();
            if(live.live > 0)
            {
                fonPanel.repaint();

                       // if(set_pos_Sentry_Gun)
                       // {
                        //    System.out.println("клик");
                        //    set_pos_Sentry_Gun = true;
//
                        //}



                if(iteration++ > interval)
                {
                    Random r = new Random();
                    int count;
                    if(live._time > 60)
                    {
                        count = r.nextInt(time_maxCount);
                    }
                    else
                    {
                        count = r.nextInt(maxCount);
                    }

                    for(int i=0; i<count; i++) {

                        Zombi z = new Zombi(this, Z_WIDTH);
                        z.spid = 10+r.nextInt(maxSpeed);
                        z.pIndex = r.nextInt(3);
                        zombis.add(z);
                    }
                    iteration = 0;
                }

            }
            if(live.go)
            {
                live.money = 0;

                if(fonPanel.getMousePosition().x > 150 && fonPanel.getMousePosition().x < 450 &&
                        fonPanel.getMousePosition().y > 290 && fonPanel.getMousePosition().y < 560)
                {
                    live.font_size = 80;

                    if(click)
                    {
                        timer.restart();
                        state = STATE.MENU;
                        click = false;
                    }
                }
                else
                {
                    live.font_size = 60;
                }
                fonPanel.repaint();
            }
        }
        if(state.equals(STATE.MENU))
        {

            if(fonPanel.getMousePosition().x > menyuha.getX() && fonPanel.getMousePosition().x < menyuha.getX() + menyuha.getWidth() &&
                    fonPanel.getMousePosition().y > (menyuha.getY()+140)*0+100 && fonPanel.getMousePosition().y < (menyuha.getY()+140)*0 + 100 + menyuha.getHeight())
            {
                menyuha.btn_game_play = true;
                menyuha.scale_font[0] = 80;

                if(click)
                {
                    timer.restart();
                    zombis.clear();
                    state = STATE.PLAY;
                    click = false;
                }
            }else
            {
                menyuha.btn_game_play = false;
                menyuha.scale_font[0] = 60;
            }

            if(fonPanel.getMousePosition().x > menyuha.getX() && fonPanel.getMousePosition().x < menyuha.getX() + menyuha.getWidth() &&
                    fonPanel.getMousePosition().y > (menyuha.getY()+140)*1+100 && fonPanel.getMousePosition().y < (menyuha.getY()+140)*1+ 100 + menyuha.getHeight())
            {
                menyuha.scale_font[1] = 80;
                if(click)
                {
                    System.exit(0);
                }
            }
            else
            {
                menyuha.scale_font[1] = 60;
            }

            fonPanel.repaint();
        }


    }


    public void fire(int line)
    {
        int index = -1;
        for(int i=0; i<zombis.size(); i++)
        {
            if(zombis.get(i).x > 50 && !zombis.get(i).kill && zombis.get(i).pIndex == line)
            {
                if(index == -1)
                {
                    index = i;
                } else if(zombis.get(i).x < zombis.get(index).x)
                {
                    index = i;
                }
            }
        }
        if(index != -1) {
            zombis.get(index).fire();
        }


        if(med_kit_call == true)
        {
            if(med_kit_help.get(0).id==line)
            {
                med_kit_use = true;
                med_kit_help.get(0).fire(1);

                if(med_kit_help.get(0).index_med_kit == 2)
                {
                    med_kit_help.remove(0);
                    med_kit_call = false;
                    local_pos_med_x = 0;
                    local_pos_med_y = 0;
                    for(int i = 0; i < 4; i++)
                    {
                        live.med_kit_icon_index[i] = 0;
                    }
                }
            }
        }
    }

    public void sentry_gun_fire()
    {
        int index= -1;

            for(int i=0; i<zombis.size(); i++)
            {
                if(_Sentry_Gun.shot && zombis.get(i).x > 200 && zombis.get(i).x < 600 && !zombis.get(i).kill && zombis.get(i).pIndex == _Sentry_Gun.pose)
                {
                    index = i;
                    _Sentry_Gun.fire();
                }
            }

        if(index != -1)
        {
            zombis.get(index).fire();
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(state == STATE.PLAY)
        {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_V:
                    if(live.money > 10000)
                    {
                        med_kit_help();
                    }
                    break;
                case KeyEvent.VK_UP:
                    player.up();
                    break;
                case KeyEvent.VK_W:
                    player.up();
                    break;
                case KeyEvent.VK_DOWN:
                    player.down();
                    break;
                case KeyEvent.VK_S:
                    player.down();
                    break;
                case  KeyEvent.VK_ENTER:
                    if(live.go)
                    {
                        if(flock) break;
                        flock = true;
                        timer.start();
                        state = STATE.MENU;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(timer_start)
                    {
                        if(flock) break;
                        flock = true;
                        player.fire();
                        fire(player.pIndex);
                    }
                    break;
                case  KeyEvent.VK_M:
                    if(!live.go)
                    {
                        if(flock) break;
                        for(int i = 0; i<4 ;i++)
                        {
                            if(live.med_kit_icon_index[i] == 0){
                                live.med_kit_icon_index[i] = 1;
                                Zombies.playSound("13895-aptechki-half-life-2.wav");
                                if (live.live < 4)
                                {
                                    live.live++;
                                }
                                return;
                            }
                        }
                    }
                    break;

            }
        }
        if(state == STATE.MENU)
        {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_ENTER:
                    if(flock) break;
                    flock = true;
                    zombis.clear();
                    state = STATE.PLAY;
                    break;
            }
        }
    }

    public void med_kit_help()
    {
        med_kit_call = true;
        live.money -= 10000;
        playSound("airplane_sound.wav");

        Med_Kit_Help new_med_kit = new Med_Kit_Help();
        med_kit_help.add(new_med_kit);

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {
        //взять турель
        if( state == STATE.PLAY)
        {
            if(e.getButton() == MouseEvent.BUTTON3)
            {
                if(!live.go)
                {
                    if(!set_pos_Sentry_Gun)
                    {
                        if(fonPanel.getMousePosition().x >= 470 && fonPanel.getMousePosition().x <= 530 && fonPanel.getMousePosition().y >= 0 && fonPanel.getMousePosition().y <= 60)
                        {
                            set_pos_Sentry_Gun = true;
                        }
                    }
                    else
                    {
                        for(int i = 0; i < 3; i++)
                        {

                            if(fonPanel.getMousePosition().x >= 110 && fonPanel.getMousePosition().x <= 290 && fonPanel.getMousePosition().y >= _Sentry_Gun._pozitions[i] + 100 && fonPanel.getMousePosition().y <= _Sentry_Gun._pozitions[i] + 150)
                            {
                                _Sentry_Gun.pose = i;
                                set_pos_Sentry_Gun = false;
                            }
                        }

                    }


                }
            }
        }

        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(state == STATE.MENU)
            {
                click = true;
            }
            if( state == STATE.PLAY)
            {
                click = true;
                if(!live.go)
                {
                    if(flock) return;
                    flock = true;
                    player.fire();
                    fire(player.pIndex);
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(state == STATE.MENU)
            {
                click = false;
            }
            if(state == STATE.PLAY)
            {
                click = false;
                if(!live.go)
                {
                    flock = false;
                    return;
                }
            }
        }
        if(e.getButton() == MouseEvent.BUTTON3)
        {
            if(state == STATE.PLAY)
            {
                click = false;
                if(!live.go)
                {
                    flock = false;
                    return;
                }
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {

            case KeyEvent.VK_SPACE:
                flock = false;
                break;
            case KeyEvent.VK_ENTER:
                flock = false;
                break;
        }
    }


    public static synchronized void playSound(final String url)
    {
        new Thread(
                new Runnable()
                {
            @Override
            public void run()
            {
                try
                {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream( new BufferedInputStream(Zombies.class.getResourceAsStream("/sounds/" + url)));
                    clip.open(inputStream);
                    clip.start();
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }


}