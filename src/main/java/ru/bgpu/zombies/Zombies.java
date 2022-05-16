
package ru.bgpu.zombies;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Zombies extends JFrame implements ActionListener, KeyListener {

    public static enum STATE {MENU,PLAY} //объявил перечисления

    public static STATE state = STATE.PLAY; //изначально игра в режиме меню

    public static final int Z_WIDTH  = 800;
    public static final int Z_HEIGHT = 500;

    BufferedImage fon;
    private static Graphics2D g;

    private int buttno_n;
    private float button_height_;
    private float button_width_;
    private float x;
    private float y;
    String[] list = new String[2];
    Image blood_button = new ImageIcon(getClass().getResource("/image/krov.png")).getImage();
    private Color _color_button_text;


//    Image fon;

    int maxCount = 3;
    int maxSpeed = 15;
    int interval = 10;
    boolean flock = false;
    int iteration = 0;
    
    ArrayList<Zombi> zombis = new ArrayList<>();
    
    Timer timer = new Timer(100, this);
    
    Player player = new Player(this);
    Live  live = new Live();


    JPanel fonPanel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            if(state.equals(STATE.PLAY)){
                g.drawImage(fon, 0, 0, null);
                player.paint(g);
                for(int i=0; i<zombis.size(); i++) {
                    if(zombis.get(i).x < -100)  {
                        zombis.remove(i);
                        live.kill();
                        if(live.live == 0) timer.stop();
                        i--; continue;
                    }
                    zombis.get(i).paint(g);
                }
                live.paint(g);
            }
            if(state.equals(STATE.MENU)){
                g.drawImage(fon, 0, 0, null);
            }
        }
    };

    public Zombies() {
        fon = new BufferedImage(Z_WIDTH, Z_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) fon.getGraphics();

        if(state.equals(STATE.MENU)){

            buttno_n = 2;
            button_height_ = 100;
            button_width_ = 200;
            y = 0;
            x = 100;
            _color_button_text = Color.WHITE;


            list[0] = "Играть";
            list[1] = "Выход";

            setTitle("Zombies!");
            timer.start();
            fon.createGraphics().drawImage(new ImageIcon(getClass().getResource("/image/fon.jpg")).getImage(),0,0,null);
            fonPanel.setPreferredSize(new Dimension(Z_WIDTH, Z_HEIGHT));
            setContentPane(fonPanel);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            addKeyListener(this);
            pack();
        }
        if(state.equals(STATE.PLAY)){
            setTitle("Zombies!");
            timer.start();
            fon.createGraphics().drawImage(new ImageIcon(getClass().getResource("/image/fon.jpg")).getImage(),0,0,null);
            fonPanel.setPreferredSize(new Dimension(Z_WIDTH, Z_HEIGHT));
            setContentPane(fonPanel);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            addKeyListener(this);
            pack();
        }




    }
    public void draw(Graphics graphics)
    {
        for(int i = 0; i < list.length; i++)
        {
            //распологаю в необходимых координатах
            graphics.drawImage(blood_button, (int) x, (int) (y + 150) * i + 50, null);

            graphics.setColor(_color_button_text);
            Font font = new Font("Arial", Font.ITALIC,50);
            graphics.setFont((font));//рисую шрифт

            //длина надписи в пикселях
            long length = (int) graphics.getFontMetrics().getStringBounds(list[i], graphics).getWidth();
            //ставлю надпись по центру кнопки
            graphics.drawString(list[i], (int)(x + button_width_ + 100) / 2 - (int)(length / 2), (int)((y + 140) * i + (button_height_ / 3) * 2) + 50);
        }
    }

    public static void main(String[] args)
    {
        if(state.equals(STATE.MENU))
        {
            Zombies zombies = new Zombies();
            zombies.draw(g);
            zombies.setVisible(true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            zombies.setLocation(((width-Z_WIDTH)/2), ((height-Z_HEIGHT)/2));
        }

        if(state.equals(STATE.PLAY))
        {
            Zombies zombies = new Zombies();
            zombies.setVisible(true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            zombies.setLocation(((width-Z_WIDTH)/2), ((height-Z_HEIGHT)/2));
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
    
    
    public void fire(int line) {
        int index = -1;
        for(int i=0; i<zombis.size(); i++) {
            if(zombis.get(i).x > 50 && !zombis.get(i).kill && zombis.get(i).pIndex == line) {
                if(index == -1) {
                    index = i;
                } else if(zombis.get(i).x < zombis.get(index).x) {
                    index = i;
                }
            }
        }
        if(index != -1) {
            zombis.get(index).fire();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                player.up();
                break;
            case KeyEvent.VK_DOWN:
                player.down();
                break;
            case KeyEvent.VK_SPACE:
                if(flock) break;
                flock = true;
                player.fire();
                fire(player.pIndex);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {

            case KeyEvent.VK_SPACE:
                flock = false;
        }
    }
    
    
    public static synchronized void playSound(final String url) {
    new Thread(new Runnable() {
    // The wrapper thread is unnecessary, unless it blocks on the
    // Clip finishing; see comments.
      @Override
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream( new BufferedInputStream(
            Zombies.class.getResourceAsStream("/sounds/" + url)));
//          InputStream bufferedIn = new BufferedInputStream(audioSrc);
//AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
          clip.open(inputStream);
          clip.start(); 
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    }
    ).start();
  }
}
