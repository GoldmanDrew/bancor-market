package controller;

import order.Order;
import pricing.BancorPricing;
import storage.DataFetcher;

import java.util.List;
import java.util.Map;

public class MarketController {

    private DataFetcher dataFetcher;
    private BancorPricing pricer;
    private Map<String, Double> tokenSupplyChangeMap;

    public MarketController(DataFetcher dataFetcher, BancorPricing pricer) {
        this.dataFetcher = dataFetcher;
        this.pricer = pricer;
    }

    public void executeAllOrders() {
        List<Order> orders = dataFetcher.retrieveAllOrders();

        for (Order order : orders) {
            double tokensIssued = pricer.exchangeTokens(order.getFromToken(), order.getToToken(), order.getQuantity());

            // Round to two decimals
            tokensIssued = Math.round(tokensIssued * 100.0) / 100.0;

            System.out.println(order.getQuantity() + " tokens of " + order.getFromToken() + " were exchanged for " +
                tokensIssued + " tokens of " + order.getToToken());
            // Update user data
            // Update token info
        }

        Map<String, Double> prices = pricer.calculatePrices();
        for (String key : prices.keySet()) {
            System.out.println("The price of " + key + " is " + prices.get(key));
        }
    }
}
