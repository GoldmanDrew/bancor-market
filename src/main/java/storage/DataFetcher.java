package storage;

import order.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFetcher {

    private Connection connection;

    public DataFetcher(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves the supply for each token in the "tokens" table in MySQl
     * and puts the results into a Map
     * @return A map from token name to token supply
     */
    public Map<String, Double> retrieveTokenSupplies() {
        Map<String, Double> tokenSupplyMap = new HashMap<>();
        try {
            Statement statement = connection.createStatement();
            String tokenSupplyQuery = "SELECT Name, Supply from tokens";
            ResultSet resultSet = statement.executeQuery(tokenSupplyQuery);

            while (resultSet.next()) {
                tokenSupplyMap.put(resultSet.getString("Name"), resultSet.getDouble("Supply"));
            }

            return tokenSupplyMap;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves all orders from MySQL
     * @return The list of all orders
     */
    public List<Order> retrieveAllOrders() {
        try {
            Statement statement = connection.createStatement();
            String allOrdersQuery = "SELECT * from orders WHERE Filled='N'";
            ResultSet resultSet = statement.executeQuery(allOrdersQuery);

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
                    resultSet.getInt("OrderID"),
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
