package storage;

import data.Order;
import data.Token;

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

    public Double retrieveUserTokenQuantity(String user, String token) {
        try {
            Statement statement = connection.createStatement();
            String userTokenSupplyQuery = String.format("SELECT Quantity from UserShares WHERE User='%s' AND Token='%s'",
                    user, token);

            ResultSet resultSet = statement.executeQuery(userTokenSupplyQuery);
            if (resultSet.next()) { // if there are no table rows that means that the user does not have that token
                return resultSet.getDouble("Quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Retrieves the supply for each token in the "tokens" table in MySQL
     * and puts the results into a Map
     * @return A map from token name to token supply
     */
    public Map<String, Token> retrieveTokens() {
        Map<String, Token> tokenSupplyMap = new HashMap<>();
        try {
            Statement statement = connection.createStatement();
            String tokenSupplyQuery = "SELECT * from tokens";
            ResultSet resultSet = statement.executeQuery(tokenSupplyQuery);

            while (resultSet.next()) {
                tokenSupplyMap.put(resultSet.getString("Name"), parseTokenQueryIntoToken(resultSet));
            }

            return tokenSupplyMap;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Token parseTokenQueryIntoToken(ResultSet resultSet) {
        try {
            return new Token(
                    resultSet.getString("Name"),
                    resultSet.getString("Ticker"),
                    resultSet.getDouble("TokenSupply"),
                    resultSet.getDouble("CashSupply"),
                    resultSet.getDouble("Price")
            );
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
                    resultSet.getString("SourceToken"),
                    resultSet.getString("TargetToken"),
                    resultSet.getObject("SourceQuantity") != null ? resultSet.getDouble("SourceQuantity") : null,
                    resultSet.getObject("TargetQuantity") != null ? resultSet.getDouble("TargetQuantity") : null
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
