package controller;

import order.Order;
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
    private Map<String, Double> tokenSupplyChangeMap;
    private Integer lastOrderIdExecuted = -1;

    public MarketController(DataFetcher dataFetcher, DataUpdater dataUpdater, BancorPricing pricer) {
        this.dataFetcher = dataFetcher;
        this.dataUpdater = dataUpdater;
        this.pricer = pricer;
        this.tokenSupplyChangeMap = new HashMap<>();
    }

    public void executeAllOrders() {
        List<Order> orders = dataFetcher.retrieveAllOrders();

        for (Order order : orders) {
            double tokensIssued = pricer.exchangeTokens(order.getSourceToken(), order.getTargetToken(), order.getQuantity());
            // Round to two decimals
            tokensIssued = Math.round(tokensIssued * 100.0) / 100.0;

            updateTokenSupplyMap(order, order.getQuantity(), tokensIssued);

            if (order.getOrderId() > lastOrderIdExecuted) {
                lastOrderIdExecuted = order.getOrderId();
            }
        }

        dataUpdater.updateFilledOrders(lastOrderIdExecuted);
        dataUpdater.updateTokenSupplies(updateTokenSupplies());
    }

    private Map<String, Double> updateTokenSupplies() {
        Map<String, Double> tokenSupplies = pricer.getTokenSupplyMap();
        for (String token : tokenSupplyChangeMap.keySet()) {
            tokenSupplies.put(token, tokenSupplies.get(token) + tokenSupplyChangeMap.get(token));
        }

        return tokenSupplies;
    }

    private void updateTokenSupplyMap(Order order, double sourceTokenAmount, double targetTokenAmount) {
        tokenSupplyChangeMap.put(
                order.getSourceToken(),
                tokenSupplyChangeMap.getOrDefault(order.getSourceToken(), 0.0) - sourceTokenAmount
        );

        tokenSupplyChangeMap.put(
                order.getTargetToken(),
                tokenSupplyChangeMap.getOrDefault(order.getTargetToken(), 0.0) + targetTokenAmount
        );
    }
}
