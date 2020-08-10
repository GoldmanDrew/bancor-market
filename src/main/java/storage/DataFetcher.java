package storage;

import order.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataFetcher {

    private Connection connection;

    public DataFetcher(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all orders from MySQL
     * @return The list of all orders
     */
    public List<Order> retrieveAllOrders() {
        try {
            Statement statement = connection.createStatement();
            String getAllOrdersQuery = "SELECT * from orders";
            ResultSet resultSet = statement.executeQuery(getAllOrdersQuery);

            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = parseQueryIntoOrder(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parses a ResultSet object into an Order object
     * @param resultSet - The ResultSet containing all the orders
     * @return An Order object corresponding to the ResultSet
     */
    private static Order parseQueryIntoOrder(ResultSet resultSet) {
        try {
            return new Order(
                    resultSet.getString("User"),
                    resultSet.getString("SellToken"),
                    resultSet.getString("BuyToken"),
                    resultSet.getDouble("Quantity")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
