/**
 * Created by jiangkai on 2017/12/22.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class LifeGame extends JFrame implements MouseMotionListener{
    private final World world;
    static JMenu location=new JMenu();
    public LifeGame(int rows,int columns)
    {
        world=new World(rows, columns);
        world.setBackground(Color.LIGHT_GRAY);
        new Thread(world).start();
        add(world);
    }
    public static void main(String[]args)
    {
        LifeGame frame=new LifeGame(30, 30);

        frame.addMouseMotionListener(frame);
        JMenuBar menu=new JMenuBar();
        frame.setJMenuBar(menu);


        JMenu options =new JMenu("菜单栏");
        menu.add(options);

        JMenuItem random=options.add("设置初始值");
        random.addActionListener(frame.new RandomActionListener());
        JMenuItem start=options.add("开始变化");
        start.addActionListener(frame.new StartActionListener());
        JMenuItem pause=options.add("暂停");
        pause.addActionListener(frame.new PauseActionListener());
        JMenuItem stop=options.add("重置");
        stop.addActionListener(frame.new StopActionListener());

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1007,859);

        frame.setTitle("生命游戏");
        frame.setVisible(true);
        frame.setResizable(false);
    }

    class RandomActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            world.diy=false;
            world.clean=false;
            world.setBackground(Color.LIGHT_GRAY);
            world.setRandom();
        }
    }
    class StartActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            world.setBackground(Color.LIGHT_GRAY);
            world.diy=false;
            world.clean=false;
            world.setShape();
        }
    }
    class StopActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            world.setBackground(Color.LIGHT_GRAY);
            world.diy=false;
            world.clean=false;
            world.setStop();
        }
    }
    class PauseActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            world.setBackground(Color.LIGHT_GRAY);
            world.diy=false;
            world.clean=false;
            world.setPause();
        }
    }


    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        if(world.diy){
            int x=e.getX();
            int y=e.getY();
            World.pauseshape[(y-50)/20][x/20]=1;
            world.setDiy();
        }
    }

    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        if(world.clean){
            int x=e.getX();
            int y=e.getY();
            World.pauseshape[(y-50)/20][x/20]=0;
            world.setDiy();
        }
    }
}

