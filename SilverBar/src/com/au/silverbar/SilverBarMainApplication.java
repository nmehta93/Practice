package com.au.silverbar;


import static com.au.silverbar.OrderType.BUY;
import static com.au.silverbar.OrderType.SELL;


public class SilverBarMainApplication {


    public static void main(String[] args) {

        LiveBoard liveBoard = new LiveBoard();

        liveBoard.add(new Order(1, BUY, 300, 55.5));
        liveBoard.add(new Order(2, BUY, 300, 62.5));
        liveBoard.add(new Order(3, BUY, 200, 94.0));
        liveBoard.add(new Order(5, SELL, 60, 200.0));
        liveBoard.add(new Order(6, SELL, 80, 10.0));

        liveBoard.summarize();
    }
}