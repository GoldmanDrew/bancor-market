package storage;

import data.Order;
import data.Token;
import pricing.BancorPricing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DataUpdater {

    private Connection connection;

    public DataUpdater(Connection connection) { this.connection = connection; }

    public void updateOrderFilledStatus(Integer lastOrderID, String status) {
        String updateFilledOrdersQuery= String.format("UPDATE orders SET Filled = '%s' where OrderID=%d", status, lastOrderID);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(updateFilledOrdersQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTokenSupplyAndPrice(Token token) {

        Double price = BancorPricing.calculatePrice(token.getCashSupply(), token.getTokenSupply(), 0.5);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = format.format(LocalDateTime.now(ZoneOffset.UTC));

        String updateTokenQuery = "UPDATE tokens SET" +
                " TokenSupply=" + token.getTokenSupply() +
                ", CashSupply=" + token.getCashSupply() +
                ", Price=" + price +
                ", TimeUpdated='" + currentTime + "'" +
                " WHERE Name='" + token.getTokenName() + "'";

        String insertTokenHistoryPrice = String.format("INSERT INTO tokenHistory (Token, Time, Price) VALUES ('%s', '%s', %f)",
                token.getTokenName(), currentTime, price);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(updateTokenQuery);
            statement.executeUpdate(insertTokenHistoryPrice);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateUserShares(Order order, Double tokensIssued) {
        String updateSourceTokenQuery;
        String updateTargetTokenQuery;
        if (order.getSourceQuantity() != null) {
            updateSourceTokenQuery = String.format("UPDATE UserShares SET Quantity=Quantity - %f WHERE User='%s' AND Token='%s'",
                    order.getSourceQuantity(), order.getUser(), order.getSourceToken());

            updateTargetTokenQuery = String.format("INSERT INTO UserShares (User, Token, Quantity) VALUES ('%s', '%s', '%f') " +
                    "ON DUPLICATE KEY UPDATE Quantity=Quantity + %f", order.getUser(), order.getTargetToken(), tokensIssued, tokensIssued);
        }
        else {
            updateSourceTokenQuery = String.format("UPDATE UserShares SET Quantity=Quantity - %f WHERE User='%s' AND Token='%s'",
                    tokensIssued, order.getUser(), order.getSourceToken());

            updateTargetTokenQuery = String.format("INSERT INTO UserShares (User, Token, Quantity) VALUES ('%s', '%s', '%f') " +
                    "ON DUPLICATE KEY UPDATE Quantity=Quantity + %f", order.getUser(), order.getTargetToken(), order.getTargetQuantity(), order.getTargetQuantity());
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(updateSourceTokenQuery);
            statement.executeUpdate(updateTargetTokenQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
