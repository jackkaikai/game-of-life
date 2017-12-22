/**
 * Created by jiangkai on 2017/12/22.
 */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.*;

public class World extends JPanel implements Runnable{
    private final int rows;
    private final int columns;
    JLabel  record;
    boolean diy=false;
    boolean clean=false;
    private int speed=8;
    private int lnum;
    private static int shape[][]=new int [40][50];
    private static int zero[][]=new int [40][50];
    static  int pauseshape[][]=new int [40][50];
    public final CellStatus[][] generation1;
    public final CellStatus[][] generation2;
    private CellStatus[][] currentGeneration;
    private CellStatus[][] nextGeneration;
    private volatile boolean isChanging = false;
    public World(int rows, int columns)
    {
        this.rows=rows;
        this.columns=columns;
        record = new JLabel("活着的细胞数",JLabel.RIGHT);
        add(record);
        generation1=new CellStatus[rows][columns];//初始化一个rows x columns的方格，并且都是死的
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                generation1[i][j]=CellStatus.Dead;
            }
        }
        generation2=new CellStatus[rows][columns];
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                generation2[i][j]=CellStatus.Dead;
            }
        }
        currentGeneration=generation1;
        nextGeneration=generation2;
    }

    public void transfrom(CellStatus[][] generation, int pauseshape[][]) //转换状态 活着为1 死亡为0
    {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                if(generation[i][j]==CellStatus.Active)
                {
                    pauseshape[i][j]=1;
                }
                else if(generation[i][j]==CellStatus.Dead)
                {
                    pauseshape[i][j]=0;
                }
            }
        }
    }
    public void run()  //线程运行
    {
        while(true)
        {
            synchronized(this)
            {
                while(isChanging)
                {

                    try
                    {
                        this.wait();
                    }catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                sleep(speed);
                for(int i=0;i<rows;i++)
                {
                    for(int j=0;j<columns;j++)
                    {
                        evolve(i,j);//设置当前细胞状态
                    }
                }
                CellStatus[][] temp=null;
                temp=currentGeneration; //临时变量记录当前的细胞状态
                currentGeneration=nextGeneration;
                nextGeneration=temp;
                for(int i=0;i<rows;i++)
                {
                    for(int j=0;j<columns;j++)
                    {
                        nextGeneration[i][j]=CellStatus.Dead;
                    }
                }
                transfrom(currentGeneration,pauseshape);
                repaint();//重构图形
                updateNumber();
            }
        }
    }
    public void updateNumber()
    {
        String s = " 活着的细胞数： " + lnum ;
        record.setText(s);
        record.setFont(new java.awt.Font("Dialog",   1,   15));
        record.setForeground(Color.red);


    }

    public void paintComponent(Graphics g)//填充图形
    {
        lnum=0;
        super.paintComponent(g);
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                if(currentGeneration[i][j]==CellStatus.Active)
                {
                    g.fillRect(j*20, i*20, 20, 20); //活着的细胞个字填满
                    lnum++;
                }
                else
                {
                    g.drawRect(j*20, i*20, 20, 20); //死亡的只画出矩形，不填满
                }
            }
        }
    }

    public void setShape()
    {
        setShape(shape);
    }
    public void setRandom()  //随机设定一个细胞状态图
    {
        Random a=new Random();
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                shape[i][j]=Math.abs(a.nextInt(2));
                pauseshape[i][j]=shape[i][j];
            }
        }
        setShapetemp(shape);
    }
    public void setZero()
    {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                zero[i][j]=0;
            }
        }
    }
    public void setStop()//全部设为死亡，图形界面为空
    {
        setZero();
        shape=zero;
        setShape(shape);
        pauseshape=shape;
    }

    public void setPause()
    {
        shape=pauseshape;
        setShapetemp(pauseshape);
    }

    public void setDiy()
    {
        shape=pauseshape;
        setShapetemp(shape);
    }
    private void setShapetemp(int [][]shape) //画出
    {
        isChanging=true;
        int arrowsRows=shape.length; //列
        int arrowsColumns=shape[0].length; //行
        int minimumRows=(arrowsRows<rows)?arrowsRows:rows;
        int minimunColumns=(arrowsColumns<columns)?arrowsColumns:columns;
        synchronized(this)
        {
            for(int i=0;i<rows;i++)
            {
                for(int j=0;j<columns;j++)
                {
                    currentGeneration[i][j]=CellStatus.Dead;
                }
            }
            for(int i=0;i<minimumRows;i++)
            {
                for(int j=0;j<minimunColumns;j++)
                {
                    if(shape[i][j]==1)
                    {
                        currentGeneration[i][j]=CellStatus.Active;
                    }
                }
            }

            repaint();
            updateNumber();

        }
    }
    private void setShape(int [][]shape)
    {
        isChanging=true;
        int arrowsRows=shape.length;
        int arrowsColumns=shape[0].length;
        int minimumRows=(arrowsRows<rows)?arrowsRows:rows;
        int minimunColumns=(arrowsColumns<columns)?arrowsColumns:columns;
        synchronized(this)
        {
            for(int i=0;i<rows;i++)
            {
                for(int j=0;j<columns;j++)
                {
                    currentGeneration[i][j]=CellStatus.Dead;
                }
            }
            for(int i=0;i<minimumRows;i++)
            {
                for(int j=0;j<minimunColumns;j++)
                {
                    if(shape[i][j]==1)
                    {
                        currentGeneration[i][j]=CellStatus.Active;
                    }
                }
            }

            isChanging=false;
            this.notifyAll();
        }

    }

    public void evolve(int x,int y) //判断周围的细胞的下一个状态
    {
        int activeSurroundingCell=0;
        if(isVaildCell(x-1,y-1)&&(currentGeneration[x-1][y-1]==CellStatus.Active))// 判断周围的细胞是否为活着
            activeSurroundingCell++;
        if(isVaildCell(x,y-1)&&(currentGeneration[x][y-1]==CellStatus.Active))
            activeSurroundingCell++;
        if(isVaildCell(x+1,y-1)&&(currentGeneration[x+1][y-1]==CellStatus.Active))
            activeSurroundingCell++;
        if(isVaildCell(x+1,y)&&(currentGeneration[x+1][y]==CellStatus.Active))
            activeSurroundingCell++;
        if(isVaildCell(x+1,y+1)&&(currentGeneration[x+1][y+1]==CellStatus.Active))
            activeSurroundingCell++;
        if(isVaildCell(x,y+1)&&(currentGeneration[x][y+1]==CellStatus.Active))
            activeSurroundingCell++;
        if(isVaildCell(x-1,y+1)&&(currentGeneration[x-1][y+1]==CellStatus.Active))
            activeSurroundingCell++;
        if(isVaildCell(x-1,y)&&(currentGeneration[x-1][y]==CellStatus.Active))
            activeSurroundingCell++;
        //
        if(activeSurroundingCell==3) //周围活着的细胞等于三个
        {
            nextGeneration[x][y]=CellStatus.Active;  //三个时细胞为活着状态
        }
        else if(activeSurroundingCell==2) //周围活着的细胞等于两个
        {
            nextGeneration[x][y]=currentGeneration[x][y]; //两个时保持原样
        }
        else
        {
            nextGeneration[x][y]=CellStatus.Dead; //少于两个时或大于三个时为死亡状态
        }
    }
    private boolean isVaildCell(int x,int y) //判断是否为有效的细胞（判断是否在方格内）
    {
        if((x>=0)&&(x<rows)&&(y>=0)&&(y<columns))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void sleep(int x)  //线程睡眠
    {
        try {
            Thread.sleep(80*x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static enum CellStatus //定义一个细胞状态的枚举
    {
        Active,
        Dead;
    }



}


