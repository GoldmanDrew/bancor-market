package pricing;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class BancorPricing {

    @Getter
    private Map<String, Double> tokenSupplyMap;
    private final String CONNECTOR_TOKEN_NAME = "connector";
    private final double CONNECTOR_WEIGHT = .5;
    private final double GHOST_SUPPLY = 500;

    public BancorPricing(Map<String, Double> tokenSupplyMap) {
        this.tokenSupplyMap = tokenSupplyMap;
    }

    private static double calculatePrice(double connectorBalance, double smartTokenSupply, double connectionWeight) {
        //return (connectorBalance) / (smartTokenSupply * connectionWeight);
        return (smartTokenSupply) / (connectorBalance * connectionWeight);
    }

    private static double calculateSmartTokensIssued(double smartTokenSupply, double connectorTokensPaid, double connectorBalance,
                                                    double connectorWeight) {
        // return smartTokenSupply * (Math.pow(1.0 + (connectorTokensPaid / connectorBalance), connectorWeight) - 1);
        return connectorBalance * (Math.pow(1.0 + (connectorTokensPaid / smartTokenSupply), connectorWeight) - 1);
    }

    private static double calculateConnectorTokensIssued(double connectorBalance, double smartTokensPaid, double smartTokenSupply,
                                                        double connectorWeight) {
        // return connectorBalance * (Math.pow(1 + (smartTokensPaid / smartTokenSupply), (1 / connectorWeight)) - 1);
        return smartTokenSupply * (Math.pow(1 + (smartTokensPaid / connectorBalance), (1 / connectorWeight)) - 1);
    }

    private static double calculateEffectivePrice(double connectorTokensExchanged, double smartTokensExchanged) {
        return connectorTokensExchanged / smartTokensExchanged;
    }

    /**
     * Calculates the number of targetTokens that can be exchanged for the amount number of sourceTokens
     * @param sourceToken
     * @param targetToken
     * @param amount
     * @return
     */
    public double exchangeTokens(String sourceToken, String targetToken, double amount) {
        if (targetToken.equals(CONNECTOR_TOKEN_NAME)) {
            return calculateConnectorTokensIssued(GHOST_SUPPLY, amount, GHOST_SUPPLY + tokenSupplyMap.get(sourceToken), CONNECTOR_WEIGHT);
        }
        if (sourceToken.equals(CONNECTOR_TOKEN_NAME)) {
            return calculateSmartTokensIssued(GHOST_SUPPLY + tokenSupplyMap.get(targetToken), amount, GHOST_SUPPLY, CONNECTOR_WEIGHT);
        }
        // Smart to Smart exchange
        else {
            double connectorTokensIssued = calculateConnectorTokensIssued(GHOST_SUPPLY, amount, GHOST_SUPPLY + tokenSupplyMap.get(sourceToken), CONNECTOR_WEIGHT);
            return calculateSmartTokensIssued(GHOST_SUPPLY + tokenSupplyMap.get(targetToken), connectorTokensIssued, GHOST_SUPPLY, CONNECTOR_WEIGHT);
        }
    }

    public double calculatePrice(String token) {

        return calculatePrice(tokenSupplyMap.get(CONNECTOR_TOKEN_NAME) + GHOST_SUPPLY,
                        tokenSupplyMap.get(token) + GHOST_SUPPLY, CONNECTOR_WEIGHT);
    }
}
