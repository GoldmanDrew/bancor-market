package controller;

import order.Order;
import storage.DataFetcher;

import java.sql.Connection;
import java.util.List;

public class MarketController {

    private Connection connection;
    private DataFetcher dataFetcher;

    public MarketController(Connection connection) {
        this.connection = connection;
        this.dataFetcher = new DataFetcher(connection);
    }

    public void executeAllOrders() {
        List<Order> orders = dataFetcher.retrieveAllOrders();
    }
}
