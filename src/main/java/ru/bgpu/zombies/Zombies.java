package ru.bgpu.zombies;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

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



    int maxCount = 3;
    int maxSpeed = 15;
    int interval = 10;
    boolean flock = false;
    int iteration = 0;

    ArrayList<Zombi> zombis = new ArrayList<>();

    Timer timer = new Timer(100, this);
    boolean timer_start = true;

    Menu menyuha = new Menu();

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public enum STATE{MENU,PLAY};

public static STATE state = STATE.MENU;

    Player player = new Player(this);
    Live live = new Live();

    public boolean click = false;

    public Med_Kit_Help med_kit_help = new Med_Kit_Help();
    public boolean med_kit = false;
    public boolean med_kit_use = false;
    public static int local_pos_med_x = 800;
    public static int local_pos_med_y = 500;

    //private static FloatControl volumeControl = null;

    BufferedImage fon = new BufferedImage(Z_WIDTH, Z_HEIGHT, BufferedImage.TYPE_INT_RGB);

    JPanel fonPanel = new JPanel() {
        @Override
        public void paint(Graphics g) {

            if(state.equals(STATE.PLAY))
            {
                g.drawImage(fon, 0, 0, null);
                player.paint(g);
                for(int i=0; i<zombis.size(); i++) {
                    if(zombis.get(i).x < -100)  {
                        zombis.remove(i);
                        live.kill();
                        if(live.live == 0)
                        {
                            //timer.stop();
                            zombis.clear();

                        }
                        i--; continue;
                    }
                    zombis.get(i).paint(g);
                }
                live.paint(g);

                if(med_kit == true /*|| local_pos_med_y != 500*/)
                {
                    med_kit_help.paint(g);
                    med_kit_use = true;
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
                for(int i = 0;i < 4;i++) {
                    live.med_kit_icon_index[i] = 0;
                }
                player.skin_666 = false;

            }

        }
    };

    public Zombies() {

        //Zombies.playSound("Flight_to_LAPD_WAW.waw");

        //button = new JButton();
        //button.setBounds(100,100,100,100);
        //button.addActionListener(this);
        setTitle("Zombies!");
        timer.start();
        //gg = (Graphics) fon.getGraphics();
        fon.createGraphics().drawImage(new ImageIcon(getClass().getResource("/image/fon.jpg")).getImage(),0,0,null);
        fonPanel.setPreferredSize(new Dimension(Z_WIDTH, Z_HEIGHT));
        setContentPane(fonPanel);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        addMouseListener(this);
        pack();



        //this.add(button);


        //button = new JButton();
        //button.setBounds(200,100,100,50);
        //add(button);
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
            if(live.live > 0)
            {
                fonPanel.repaint();
                if(iteration++ > interval) {
                    Random r = new Random();
                    int count = r.nextInt(maxCount);
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

                }else
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
        if(med_kit_use == true)
        {
            for(int i=0; i<med_kit_help.positions.length; i++)
            {
                if(med_kit_help.id==line)
                {
                    med_kit_help.fire(1);
                }
            }
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
                    if(live.money > 1)
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
        live.money -= 1;
        playSound("airplane_sound.wav");
        med_kit = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1){
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
        if(e.getButton() == MouseEvent.BUTTON1){
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