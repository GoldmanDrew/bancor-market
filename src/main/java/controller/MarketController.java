package controller;

import data.Order;
import data.Token;
import pricing.BancorPricing;
import storage.DataFetcher;
import storage.DataUpdater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketController {

    private DataFetcher dataFetcher;
    private DataUpdater dataUpdater;
    private BancorPricing pricer;
    private Integer lastOrderIdExecuted = -1;

    public MarketController(DataFetcher dataFetcher, DataUpdater dataUpdater, BancorPricing pricer) {
        this.dataFetcher = dataFetcher;
        this.dataUpdater = dataUpdater;
        this.pricer = pricer;
    }

    public void executeAllOrders() {
        List<Order> orders = dataFetcher.retrieveAllOrders();

        for (Order order : orders) {
            Double tokensIssued = pricer.exchangeTokens(order.getSourceToken(), order.getTargetToken(), order.getSourceQuantity(), order.getTargetQuantity(), order.getUser());

            // Order was not stopped
            if (tokensIssued != null) {
                // Round to two decimals
                tokensIssued = Math.round(tokensIssued * 100.0) / 100.0;

                dataUpdater.updateUserShares(order, tokensIssued);
                dataUpdater.updateOrderFilledStatus(order.getOrderId(), "Y");
            } else {
                dataUpdater.updateOrderFilledStatus(order.getOrderId(), "C");
            }

        }

        for (String token : pricer.getTokenMap().keySet()) {
            if (!token.equals("Cash")) {
                dataUpdater.updateTokenSupplyAndPrice(pricer.getTokenMap().get(token));
            }
        }

    }
}
