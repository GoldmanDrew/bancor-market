import controller.MarketController;
import pricing.BancorPricing;
import storage.DataFetcher;
import storage.MySqlProvider;

import java.sql.Connection;

public class BancorMarket {
    public static void main(String[] args) {
        Connection connection = MySqlProvider.getConnection();
        DataFetcher dataFetcher = new DataFetcher(connection);
        BancorPricing pricer = new BancorPricing(dataFetcher.retrieveTokenSupplies());

        MarketController controller = new MarketController(dataFetcher, pricer);
        controller.executeAllOrders();
    }
}
