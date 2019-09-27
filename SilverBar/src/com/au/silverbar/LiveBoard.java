package com.au.silverbar;

import static com.au.silverbar.OrderType.BUY;
import static com.au.silverbar.OrderType.SELL;
import static java.text.NumberFormat.getCurrencyInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * This solution is for calculate totals.
 */
class LiveBoard {

    private final Map<Integer, List<Order>> orderBoard; 
    private final Map<Integer, Double> qtyBuyPrice; 
    private final Map<Integer, Double> qtySellPrice; 
    
    LiveBoard() {

        orderBoard = new HashMap<>();
        qtyBuyPrice = new HashMap<>();
        qtySellPrice = new HashMap<>();
    }

    void add(Order order) {
        Integer customerId = order.getCustomerId();

        if(!orderBoard.containsKey(customerId)){
            addNewCustomer(customerId);
        }

        List<Order> customerOrders = orderBoard.get(customerId);
        if(!customerOrders.contains(order)) {
            customerOrders.add(order);
            updateQtyPrice(order, tranlsateOrderTypeToMap(order.getOrderType()));
        }
    }

    private Map<Integer, Double> tranlsateOrderTypeToMap(OrderType type) {
        return type.equals(BUY) ? qtyBuyPrice : qtySellPrice;
    }

    private void updateQtyPrice(Order order, Map<Integer, Double> qtyPrice) {
        double totalqtyPrice;

        if(qtyPrice.containsKey(order.getPriceAsInteger())){
            totalqtyPrice = qtyPrice.get(order.getPriceAsInteger()) + order.getQuantity();
        } else {
            totalqtyPrice = order.getQuantity();
        }

        qtyPrice.put(order.getPriceAsInteger(), totalqtyPrice);
    }

    void remove(Order order) {
        List<Order> customerOrders = orderBoard.get(order.getCustomerId());
        customerOrders.remove(order);
    }

    double totalBuying() {
        return totalForOrderType(BUY);
    }

    double totalSelling() {
        return totalForOrderType(SELL);
    }

    double totalBuyingForCustomer(Integer customerId) {
        return totalOrderTypeForCustomer(customerId, BUY);
    }

    double totalSelling(Integer customerId) {
        return totalOrderTypeForCustomer(customerId, SELL);
    }

    double qtyBuyPrice(Integer price) {
        return qtyBuyPrice.get(price);
    }

    double qtySellPrice(Integer price) {
        return qtySellPrice.get(price);
    }

    private double totalForOrderType(OrderType type) {
        double total = 0;

        for (List<Order> customerOrders : orderBoard.values()) {
            for (Order order : customerOrders) {
                if (order.typeIs(type)) {
                    total += order.getValue();
                }
            }
        }
        
       
        

        return total;
    }

    private double totalOrderTypeForCustomer(Integer customer, OrderType type) {
        double total = 0;

        for (Order order : orderBoard.get(customer)) {
            if (order.typeIs(type)) {
                total += order.getValue();
            }
        }
        

        return total;
    }

    private void addNewCustomer(Integer customerId) {
        orderBoard.put(customerId, new ArrayList<Order>());
    }


    void summarize() {
        System.out.println("LiveOrderBoard !!! \n");
        System.out.println("Buy:");
        summarizeOrderType(BUY);
        System.out.println("\nSell:");
        summarizeOrderType(SELL);
        System.out.println("END\n\n");
    }

    private void summarizeOrderType(OrderType type) {

        Map<Integer, Double> qtyPrice = tranlsateOrderTypeToMap(type);
        qtyPrice.forEach((k,v)-> System.out.println(k + " kg for " + getCurrencyInstance(Locale.UK).format(v)));
        
        
    }
}