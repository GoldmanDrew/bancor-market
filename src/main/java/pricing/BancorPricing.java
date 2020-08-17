package pricing;

import data.Token;
import lombok.Getter;

import java.util.Map;

public class BancorPricing {

    @Getter
    private Map<String, Token> tokenMap;
    private final String CONNECTOR_TOKEN_NAME = "Cash";
    private final double CONNECTOR_WEIGHT = .5;

    public BancorPricing(Map<String, Token> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public static double calculatePrice(double connectorBalance, double smartTokenSupply, double connectionWeight) {
        return (connectorBalance) / (smartTokenSupply * connectionWeight);
    }

    private static double calculateSmartTokensIssued(double smartTokenSupply, double connectorTokensPaid, double connectorBalance,
                                                    double connectorWeight) {
        return smartTokenSupply * (Math.pow(1.0 + (connectorTokensPaid / connectorBalance), connectorWeight) - 1);
    }

    private static double calculateConnectorTokensIssued(double connectorBalance, double smartTokensPaid, double smartTokenSupply,
                                                        double connectorWeight) {
        return connectorBalance * (Math.pow(1 + (smartTokensPaid / smartTokenSupply), (1 / connectorWeight)) - 1);
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

        // Sell a token into cash
        if (targetToken.equals(CONNECTOR_TOKEN_NAME)) {
            double connectorTokensIssued = calculateConnectorTokensIssued(tokenMap.get(sourceToken).getCashSupply(), amount, tokenMap.get(sourceToken).getTokenSupply(), CONNECTOR_WEIGHT);
            tokenMap.get(sourceToken).updateTokenSupply(-1 * amount);
            tokenMap.get(sourceToken).updateCashSupply(-1 * connectorTokensIssued);

            return connectorTokensIssued;
        }

        // Buy a token using cash
        if (sourceToken.equals(CONNECTOR_TOKEN_NAME)) {
            double smartTokensIssued = calculateSmartTokensIssued(tokenMap.get(targetToken).getTokenSupply(), amount, tokenMap.get(targetToken).getCashSupply(), CONNECTOR_WEIGHT);
            tokenMap.get(targetToken).updateTokenSupply(smartTokensIssued);
            tokenMap.get(targetToken).updateCashSupply(amount);

            return smartTokensIssued;
        }
        // Sell a token into cash to then buy another token
        else {
            double connectorTokensIssued = exchangeTokens(sourceToken, CONNECTOR_TOKEN_NAME, amount);
            return exchangeTokens(CONNECTOR_TOKEN_NAME, targetToken, amount);
        }
    }

    public double calculatePrice(String token) {
        return calculatePrice(tokenMap.get(token).getCashSupply(), tokenMap.get(token).getTokenSupply(), CONNECTOR_WEIGHT);
    }
}
