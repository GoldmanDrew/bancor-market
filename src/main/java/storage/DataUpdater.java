package storage;

import data.Order;
import data.Token;
import pricing.BancorPricing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DataUpdater {

    private Connection connection;

    public DataUpdater(Connection connection) { this.connection = connection; }

    public void updateFilledOrders(Integer lastOrderID) {
        String updateFilledOrdersQuery= "UPDATE orders SET Filled = 'Y' where OrderID<=" + lastOrderID;

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(updateFilledOrdersQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTokenSuppliesAndPrices(Map<String, Token> tokenMap) {

        BancorPricing pricer = new BancorPricing(tokenMap);

        for (String token : tokenMap.keySet()) {
            if (token.equals("Cash")) {
                continue;
            }

            String updateTokenSupplyQuery = "UPDATE tokens SET TokenSupply=" + tokenMap.get(token).getTokenSupply() +
                    " WHERE Name='" + token + "'";

            String updateTokenCashSupplyQuery = "UPDATE tokens SET CashSupply=" + tokenMap.get(token).getCashSupply() +
                    " WHERE Name='" + token + "'";

            String updateTokenPriceQuery = "UPDATE tokens SET Price=" + pricer.calculatePrice(token) +
                    " WHERE Name='" + token + "'";

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(updateTokenSupplyQuery);
                statement.executeUpdate(updateTokenCashSupplyQuery);
                statement.executeUpdate(updateTokenPriceQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUserShares(Order order, Double targetTokensIssued) {
        String updateSourceTokenQuery = String.format("UPDATE UserShares SET Quantity=Quantity - %f WHERE User='%s' AND Token='%s'",
                order.getQuantity(), order.getUser(), order.getSourceToken());

        String updateTargetTokenQuery = String.format("INSERT INTO UserShares (User, Token, Quantity) VALUES ('%s', '%s', '%f') " +
                "ON DUPLICATE KEY UPDATE Quantity=Quantity + %f", order.getUser(), order.getTargetToken(), targetTokensIssued, targetTokensIssued);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(updateSourceTokenQuery);
            statement.executeUpdate(updateTargetTokenQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
