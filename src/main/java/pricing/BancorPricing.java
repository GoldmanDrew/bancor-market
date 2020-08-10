package pricing;

public class BancorPricing {

    public static double calculatePrice(int connectorBalance, int smartTokenSupply, double connectionWeight) {
        return (connectorBalance) / (smartTokenSupply * connectionWeight);
    }

    public static double calculateSmartTokensIssued(int smartTokenSupply, double connectorTokensPaid, int connectorBalance,
                                                    double connectorWeight) {
        return smartTokenSupply * (Math.pow(1.0 + (connectorTokensPaid / connectorBalance), connectorWeight) - 1);
    }

    public static double calculateConnectorTokensIssued() {
        return 0;
    }

    public static double calculateEffectivePrice() {
        return 0;
    }

    public static double exchangeTokens() {
        return 0;
    }
}
