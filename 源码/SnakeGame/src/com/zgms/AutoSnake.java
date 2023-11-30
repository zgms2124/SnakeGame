package com.zgms;

import java.util.LinkedList;

/**
 * 学习JAVA
 *
 * @项目名称：
 * @子庚木上
 * @Date：2023/10/25 - 10 - 25 - 8:41
 * @version： 1.0
 * @功能：
 */
public class AutoSnake{
    //初始化蛇身体
    private void initSnake() {
        //创建集合
        body=new LinkedList<>();
        int beginX=10,beginY=10;
        //创建6个节点，添加到集合中
        body.add(new Node(beginX++,beginY));
        body.add(new Node(beginX,beginY));
        body.add(new Node(beginX,beginY));
        body.add(new Node(beginX++,beginY));
        body.add(new Node(beginX++,beginY));
        body.add(new Node(beginX,beginY));

    }

    public void move(Node food){
        if(isLiving){
            //获取蛇头
            Node head = body.getFirst();
            if(head.getX()<food.getX()){
                direction=Direction.RIGHT;
            }
            else if(head.getX()> food.getX()){
                direction=Direction.LEFT;
            }
            else if(head.getY()<food.getY()){
                direction=Direction.DOWN;
            }
            else if(head.getY()>food.getY()){
                direction=Direction.UP;
            }
            switch (direction){
                case UP:
                    //在蛇头的上边添加一个节点
                    body.addFirst(new Node(head.getX(),head.getY()-1));
                    break;
                case DOWN:
                    body.addFirst(new Node(head.getX(),head.getY()+1));
                    break;
                case LEFT:
                    body.addFirst(new Node(head.getX()-1,head.getY()));
                    break;
                case RIGHT:
                    body.addFirst(new Node(head.getX()+1,head.getY()));
                    break;
            }
            body.removeLast();
        }
    }
    //蛇的身体
    private LinkedList<Node> body;
    //蛇的运动方向,默认向左
    private Direction direction=Direction.LEFT;
    //蛇是否活着
    private boolean isLiving=true;
    //构造方法，在创建Snake对象时执行
    public AutoSnake() {
        //初始化蛇身体
        initSnake();
    }


    public LinkedList<Node> getBody() {
        return body;
    }

    public void setBody(LinkedList<Node> body) {
        this.body = body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isLiving() {
        return isLiving;
    }

    public void setLiving(boolean living) {
        isLiving = living;
    }

}

