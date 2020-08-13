import controller.MarketController;
import pricing.BancorPricing;
import storage.DataFetcher;
import storage.DataUpdater;
import storage.MySqlProvider;

import java.sql.Connection;

public class BancorMarket {
    public static void main(String[] args) {
        Connection connection = MySqlProvider.getConnection();
        DataFetcher dataFetcher = new DataFetcher(connection);
        DataUpdater dataUpdater = new DataUpdater(connection);
        BancorPricing pricer = new BancorPricing(dataFetcher.retrieveTokenSupplies());

        MarketController controller = new MarketController(dataFetcher, dataUpdater, pricer);
        controller.executeAllOrders();
    }
}
