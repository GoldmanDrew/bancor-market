package storage;

import order.Order;

import java.sql.Connection;
import java.util.List;

public class DataFetcher {

    private Connection connection;

    public DataFetcher(Connection connection) {
        this.connection = connection;
    }

    public List<Order> retrieveAllOrders() {
        return null;
    }
}
