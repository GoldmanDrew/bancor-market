import controller.MarketController;
import storage.MySqlProvider;

import java.sql.Connection;

public class BancorMarket {
    public static void main(String[] args) {
        Connection connection = MySqlProvider.getConnection();
        MarketController controller = new MarketController(connection);
        controller.executeAllOrders();
    }
}
