package com.au.silverbar;

import static org.junit.Assert.assertEquals;
import static com.au.silverbar.OrderType.BUY;
import static com.au.silverbar.OrderType.SELL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LiveOrderBoardTest {

    private static final double ARBITRARY_ACCEPTABLE_DELTA = 0.00000001;

    private static final Integer CUSTOMER_1 = 1;
    private static final Integer CUSTOMER_2 = 2;
    private static final Integer CUSTOMER_3 = 3;
    private static final Integer CUSTOMER_4 = 4;

    private static final double PRICE_600 = 600;
    private static final double PRICE_500 = 500;
    private static final double PRICE_450 = 450;
    private static final double PRICE_425 = 425;
    private static final double PRICE_560 = 560;

    private static final double QUANTITY_10 = 10;
    private static final double QUANTITY_12_POINT_5 = 12.5;
    private static final double QUANTITY_27_POINT_5 = 27.5;
    private static final double QUANTITY_40 = 40;


    private LiveBoard liveBoard;

    private final Order Customer1_Order1 = new Order(CUSTOMER_1, BUY, PRICE_500, QUANTITY_10);
    private final Order Customer1_Order2 = new Order(CUSTOMER_1, BUY, PRICE_450, QUANTITY_12_POINT_5);
    private final Order Customer2_Order1 = new Order(CUSTOMER_2, BUY, PRICE_425, QUANTITY_27_POINT_5);
    private final Order Customer2_Order2 = new Order(CUSTOMER_2, BUY, PRICE_450, QUANTITY_10);

    private final Order Customer3_Order3 = new Order(CUSTOMER_3, SELL, PRICE_600, QUANTITY_40);
    private final Order Customer4_Order1 = new Order(CUSTOMER_4, SELL, PRICE_560, QUANTITY_40);
    private final Order Customer4_Order2 = new Order(CUSTOMER_4, SELL, PRICE_600, QUANTITY_10);

    @Before
    public void setup() {
        liveBoard = new LiveBoard();
    }

    @After
    public void summarize(){
        liveBoard.summarize();
    }

    @Test
    public void addOneBuyOrder(){

        liveBoard.add(Customer1_Order1);

        assertEquals(expectedValue(Customer1_Order1), liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addTwoBuyOrdersForTheSameCustomer(){

        liveBoard.add(Customer1_Order1);
        liveBoard.add(Customer1_Order2);

        assertEquals(expectedValue(Customer1_Order1, Customer1_Order2), liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addTwoBuyOrdersForTwoDifferentCustomers(){

        liveBoard.add(Customer1_Order1);
        liveBoard.add(Customer2_Order1);

        assertEquals(Customer1_Order1.getValue(), liveBoard.totalBuyingForCustomer(Customer1_Order1.getCustomerId()), ARBITRARY_ACCEPTABLE_DELTA);
        assertEquals(Customer2_Order1.getValue(), liveBoard.totalBuyingForCustomer(Customer2_Order1.getCustomerId()), ARBITRARY_ACCEPTABLE_DELTA);
        assertEquals(expectedValue(Customer1_Order1, Customer2_Order1), liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addOneSellOrder(){

        liveBoard.add(Customer3_Order3);

        assertEquals(expectedValue(Customer3_Order3), liveBoard.totalSelling(), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addTwoSellOrdersAndTwoBuyOrdersForDifferentCustomers(){

        liveBoard.add(Customer1_Order1);
        liveBoard.add(Customer2_Order1);
        liveBoard.add(Customer3_Order3);
        liveBoard.add(Customer4_Order1);

        assertEquals(expectedValue(Customer1_Order1, Customer2_Order1), liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
        assertEquals(expectedValue(Customer3_Order3, Customer4_Order1), liveBoard.totalSelling(), ARBITRARY_ACCEPTABLE_DELTA);

        assertEquals(expectedValue(Customer1_Order1), liveBoard.totalBuyingForCustomer(Customer1_Order1.getCustomerId()), ARBITRARY_ACCEPTABLE_DELTA);
        assertEquals(0.0, liveBoard.totalSelling(Customer1_Order1.getCustomerId()), ARBITRARY_ACCEPTABLE_DELTA);

        assertEquals(expectedValue(Customer3_Order3), liveBoard.totalSelling(Customer3_Order3.getCustomerId()), ARBITRARY_ACCEPTABLE_DELTA);
        assertEquals(0.0, liveBoard.totalBuyingForCustomer(Customer3_Order3.getCustomerId()), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addAndRemoveAnOrder(){

        liveBoard.add(Customer1_Order1);
        assertEquals(expectedValue(Customer1_Order1), liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
        liveBoard.remove(Customer1_Order1);
        assertEquals(0.0, liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void anOrderCannotBeAddedTwice(){

        liveBoard.add(Customer1_Order1);
        liveBoard.add(Customer1_Order1);

        assertEquals(expectedValue(Customer1_Order1), liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addFiveOrdersAndRemoveTwo(){

        liveBoard.add(Customer1_Order1);
        liveBoard.add(Customer1_Order2);
        liveBoard.add(Customer2_Order1);
        liveBoard.add(Customer3_Order3);
        liveBoard.add(Customer4_Order1);

        liveBoard.remove(Customer1_Order1);
        liveBoard.remove(Customer4_Order1);

        assertEquals(expectedValue(Customer1_Order2, Customer2_Order1), liveBoard.totalBuying(), ARBITRARY_ACCEPTABLE_DELTA);
        assertEquals(expectedValue(Customer3_Order3), liveBoard.totalSelling(), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addTwoBuyOrdersAtTheSamePrice(){

        liveBoard.add(Customer1_Order2);
        liveBoard.add(Customer2_Order2);

        assertEquals(expectedQuantity(Customer1_Order2, Customer2_Order2), liveBoard.qtyBuyPrice(Customer1_Order2.getPriceAsInteger()), ARBITRARY_ACCEPTABLE_DELTA);
    }

    @Test
    public void addTwoSellOrdersAtTheSamePrice(){

        liveBoard.add(Customer3_Order3);
        liveBoard.add(Customer4_Order2);

        assertEquals(expectedQuantity(Customer3_Order3, Customer4_Order2), liveBoard.qtySellPrice(Customer3_Order3.getPriceAsInteger()), ARBITRARY_ACCEPTABLE_DELTA);
    }

    private double expectedQuantity(Order... orders) {
        double total = 0.0;

        for (Order order : orders) {
            total += order.getQuantity();
        }

        return total;
    }

    private double expectedValue(Order... orders) {
        double total = 0.0;

        for (Order order : orders) {
            total += order.getValue();
        }

        return total;
    }
}
