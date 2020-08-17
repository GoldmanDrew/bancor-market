package storage;

import data.Order;
import data.Token;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import pricing.BancorPricing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

        System.out.println(updateTokenQuery);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(updateTokenQuery);
        } catch (SQLException e) {
            e.printStackTrace();
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
