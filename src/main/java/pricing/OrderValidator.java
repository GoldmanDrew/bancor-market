package pricing;

import storage.DataFetcher;

public class OrderValidator {

    private static DataFetcher dataFetcher;

    public static void setDataFetcher(DataFetcher dataFetcher) {
        OrderValidator.dataFetcher = dataFetcher;
    }

    public static boolean validateUserHasTokens(String user, String token, Double quantity) {
        return quantity <= dataFetcher.retrieveUserTokenQuantity(user, token);
    }
}
