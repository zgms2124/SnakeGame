package com.zgms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.JarEntry;

import static java.lang.Thread.sleep;

class MyJpanel {

    //包装期盼，锁住paint方法，防止线程冲突

    private JPanel jPanel;

    public JPanel getjPanel() {
        return jPanel;
    }

    public void setjPanel(JPanel jPanel) {
        this.jPanel = jPanel;
    }

    public MyJpanel() {
    }

    public MyJpanel(JPanel jPanel) {
        this.jPanel = jPanel;
    }

    public synchronized void myPaint() {
        jPanel.repaint();
    }
}

class MainFrame extends JFrame {
    private Snake snake; //蛇
    private AutoSnake autoSnake;
    private MyJpanel jPanel; //游戏棋盘
    private Timer timer1; //定时器，在规定的时间内调用蛇移动的方法
    private Timer timer2; //定时器，在规定的时间内修改自动蛇速度
    private Timer timer3; //定时器，在规定的时间内调用自动蛇移动的方法
    private int gridLength=15;

    final int[] speed = {150};

    private Node food; //食物
    private static long startTime;
    static MainFrame mainFrame = null;
    //控件
    private JPanel jp_center = new JPanel(); //声明一个面板
    private JPanel jp_north = new JPanel();//声明一个面板

    //
    public static void main(String[] args) {
        //创建窗体对象，并显示
        startTime = System.currentTimeMillis();
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }


    public MainFrame() throws HeadlessException {
        //初始化窗体参数
        initFrame();
        //初始化游戏棋盘
        this.addCenterButton();//加载调用中间的控件
        //初始化蛇
        initSnake();
        //初始化食物
        initFood();
        //初始化定时器
        initTimer1();
        initTimer2();
        //设置键盘监听，让蛇随着上下左右方向移动
        setKeyListener();
    }

    //初始化窗口
    public void initFrame() {
        this.setTitle("计科2201李志杨-贪吃蛇程序"); //标题
        this.setSize(610, 640);//大小
        setLocation(400, 50);
        this.setLayout(new BorderLayout());//边框布局
        this.setResizable(true);//窗体可以拉伸
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口的时候，退出程序
    }


    //初始化游戏棋盘
    private void addCenterButton() {
        jPanel = new MyJpanel(new JPanel() {
            //绘制游戏棋盘中的内容
            @Override
            public void paint(Graphics g) {
                //清空棋盘
                g.clearRect(0, 0, 600, 600);

                //绘制40条横线
                for (int i = 0; i <= 40; i++) {
                    g.drawLine(0, i * gridLength, 600, i * gridLength);
                }
                //绘制40条竖线
                for (int i = 0; i <= 40; i++) {
                    g.drawLine(i * gridLength, 0, i * gridLength, 600);
                }

                //绘制蛇
                LinkedList<Node> body = snake.getBody();
                LinkedList<Node> aiBody = autoSnake.getBody();
                for (Node node : body) {
                    g.fillRect(node.getX() * gridLength, node.getY() * gridLength, gridLength, gridLength);
                }
                g.setColor(Color.GRAY);
                for (Node node : aiBody) {
                    g.fillRect(node.getX() * gridLength, node.getY() * gridLength, gridLength, gridLength);
                }

                //绘制食物
                g.setColor(Color.RED);
                g.fillRect(food.getX() * gridLength, food.getY() * gridLength, gridLength, gridLength);
                g.setColor(Color.BLUE);

                g.drawString("当前得分：" + (snake.getBody().size()-9), 50, 50);
            }
        });
        this.add(jPanel.getjPanel());
    }

    //初始化蛇
    private void initSnake() {
        snake = new Snake();
        autoSnake = new AutoSnake();
    }
    //初始化食物
    private void initFood() {
        food = new Node();
        food.random();
    }

    //初始化定时器
    private void initTimer1() {
        //创建定时器对象
        timer1 = new Timer();

        //初始化定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                snake.move(autoSnake);
                //判断蛇头是否和食物重合
                Node head = snake.getBody().getFirst();
                if (head.getX() == food.getX() && head.getY() == food.getY()) {
                    snake.eat(food);
                    food.random();
                }
                //重绘游戏棋盘
                jPanel.myPaint();
                if (!snake.isLiving()) {
                    timer1.cancel();
                    timer2.cancel();
                    timer3.cancel();
                }
            }
        };

        //每100毫秒，执行一次定时任务
        timer1.scheduleAtFixedRate(timerTask, 0, 100);
//        timer1.scheduleAtFixedRate(timerTask,0,30);

    }

    //初始化定时器
    private void initTimer2() {
        //创建定时器对象
        timer2 = new Timer();
        //初始化定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                speed[0] -= 5;
                initTimer3(speed[0]);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer3.cancel();
            }
        };
        //每100毫秒，执行一次定时任务
        timer2.scheduleAtFixedRate(timerTask, 0, 3000);

    }

    private void initTimer3(int speed) {
        //创建定时器对象
        timer3 = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                autoSnake.move(food);
                //判断蛇头是否和食物重合
                Node head = autoSnake.getBody().getFirst();
                if (head.getX() == food.getX() && head.getY() == food.getY()) {
//                    snake.eat(food);
                    food.random();
                }
                if (!snake.isLiving()) {
                    timer2.cancel();
                }
                //重绘游戏棋盘
                jPanel.myPaint();
            }
        };

        //每speed毫秒，执行一次定时任务
        timer3.scheduleAtFixedRate(timerTask, 0, Math.max(speed, 50));

    }


    //设置键盘监听
    private void setKeyListener() {
        addKeyListener(new KeyAdapter() {
            //当键盘按下时，会自动掉此方法
            @Override
            public void keyPressed(KeyEvent e) {
                //键盘中的一个键都有一个编号
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_UP: //上键
                        if (snake.getDirection() != Direction.DOWN) {
                            snake.setDirection(Direction.UP);
                        }
                        break;
                    case KeyEvent.VK_DOWN: //下键
                        if (snake.getDirection() != Direction.UP) {
                            snake.setDirection(Direction.DOWN);
                        }
                        break;
                    case KeyEvent.VK_LEFT: //左键
                        if (snake.getDirection() != Direction.RIGHT) {
                            snake.setDirection(Direction.LEFT);
                        }
                        break;
                    case KeyEvent.VK_RIGHT: //右键
                        if (snake.getDirection() != Direction.LEFT) {
                            snake.setDirection(Direction.RIGHT);
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                        if (!snake.isLiving()) {
                            mainFrame.dispose();
                            mainFrame = new MainFrame();
                            mainFrame.setVisible(true);
                        }
                        break;
                }
            }
        });
    }



}
