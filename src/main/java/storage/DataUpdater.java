package storage;

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

    public void updateTokenSupplies(Map<String, Double> tokenSupplies) {
        for (String token : tokenSupplies.keySet()) {
            String updateTokenSupplyQuery = "UPDATE tokens SET Supply=" + tokenSupplies.get(token) +
                    " WHERE Name='" + token + "'";

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(updateTokenSupplyQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
