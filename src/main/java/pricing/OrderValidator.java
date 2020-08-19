package pricing;

import storage.DataFetcher;

public class OrderValidator {

    private static DataFetcher dataFetcher;

    public static void setDataFetcher(DataFetcher dataFetcher) {
        OrderValidator.dataFetcher = dataFetcher;
    }

    public static boolean validateUserHasSourceTokens(String user, String sourceToken, Double sourceQuantity) {
        return sourceQuantity <= dataFetcher.retrieveUserTokenQuantity(user, sourceToken);
    }
}
