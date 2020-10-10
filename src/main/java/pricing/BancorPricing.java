package pricing;

import data.Token;
import lombok.Getter;

import java.util.Map;

public class BancorPricing {

    @Getter
    private Map<String, Token> tokenMap;
    private final String CONNECTOR_TOKEN_NAME = "Cash";
    private static final Double CONNECTOR_WEIGHT = .5;

    public BancorPricing(Map<String, Token> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public static Double getCONNECTOR_WEIGHT() {
        return CONNECTOR_WEIGHT;
    }

    /**
     * Calculates the price of a token
     */
    public static Double calculatePrice(double connectorBalance, double smartTokenSupply, double connectionWeight) {
        return (connectorBalance) / (smartTokenSupply * connectionWeight);
    }

    /**
     * Calculates the number of Smart Tokens issued OR the number of Smart Tokens needed
     */
    private static Double calculateSmartTokensIssued(double smartTokenSupply, double connectorTokensPaid, double connectorBalance,
                                                    double connectorWeight) {
        return smartTokenSupply * (Math.pow(1.0 + (connectorTokensPaid / connectorBalance), connectorWeight) - 1);
    }

    /**
     * Calculates the number of Connector Tokens issued OR the number of Connector Tokens needed
     */
    private static Double calculateConnectorTokensIssued(double connectorBalance, double smartTokensPaid, double smartTokenSupply,
                                                        double connectorWeight) {
        return connectorBalance * (Math.pow(1 + (smartTokensPaid / smartTokenSupply), (1 / connectorWeight)) - 1);
    }

    private static Double calculateEffectivePrice(double connectorTokensExchanged, double smartTokensExchanged) {
        return connectorTokensExchanged / smartTokensExchanged;
    }

    public Double exchangeTokens(String sourceToken, String targetToken, Double sourceQuantity, Double targetQuantity, String user) {
        // if you know how much you're paying/how many tokens you're selling but not how much you're getting back
            // this should really be used for just selling
        if (sourceQuantity != null) {
            return exchangeTokensWithSourceQuantityFunding(sourceToken, targetToken, sourceQuantity, user);
        }
        else if (targetQuantity != null) { 
        // if you know how much you want to buy or how much you're selling for
            // this should really be used for just buying
            return exchangeTokensWithTargetQuantity(sourceToken, targetToken, targetQuantity, user);
        }
        return null;
    }

    /**
     * Exchange tokens where the target quantity is known and the source quantity must be solved for
     * Example: Buy 10 tokens of A using cash
     * @param sourceToken - The originating token that the user is using for the order
     * @param targetToken - The token the user wishes receive from this order
     * @param targetQuantity - The quantity of targetToken that the user wishes to receive from this order
     * @param user - The user that placed the order
     * @return The number of sourceTokens needed to fund this order
     */
    private Double exchangeTokensWithTargetQuantity(String sourceToken, String targetToken, Double targetQuantity, String user) {

        // Sell specific cash amount of a token     //TODO: test this
        if (targetToken.equals(CONNECTOR_TOKEN_NAME)) {
            double sourceTokensNeeded = calculateSmartTokensIssued(tokenMap.get(sourceToken).getTokenSupply(), targetQuantity, tokenMap.get(sourceToken).getCashSupply(), CONNECTOR_WEIGHT);

            if (!OrderValidator.validateUserHasTokens(user, sourceToken, sourceTokensNeeded)) {
                return null;
            }

            tokenMap.get(sourceToken).updateCashSupply(-1 * targetQuantity);
            tokenMap.get(sourceToken).updateTokenSupply(-1 * sourceTokensNeeded);
            return sourceTokensNeeded;
        }

        // Buy a specific amount of a token using cash
        if (sourceToken.equals(CONNECTOR_TOKEN_NAME)) {
            double connectorTokensNeeded = calculateConnectorTokensIssued(tokenMap.get(targetToken).getCashSupply(), targetQuantity, tokenMap.get(targetToken).getTokenSupply(), CONNECTOR_WEIGHT);

            if (!OrderValidator.validateUserHasTokens(user, sourceToken, connectorTokensNeeded)) {
                return null;
            }

            tokenMap.get(targetToken).updateCashSupply(connectorTokensNeeded);
            tokenMap.get(targetToken).updateTokenSupply(targetQuantity);
            return connectorTokensNeeded;
        }

        // Buy a specific amount of a token using another token as funding //TODO: test this
        else {
            Double connectorTokensNeeded = calculateConnectorTokensIssued(tokenMap.get(targetToken).getCashSupply(), targetQuantity, tokenMap.get(targetToken).getTokenSupply(), CONNECTOR_WEIGHT);
            Double sourceTokensNeeded = calculateSmartTokensIssued(tokenMap.get(sourceToken).getTokenSupply(), connectorTokensNeeded, tokenMap.get(sourceToken).getCashSupply(), CONNECTOR_WEIGHT);

            if (!OrderValidator.validateUserHasTokens(user, sourceToken, sourceTokensNeeded)) {
                return null;
            }

            tokenMap.get(sourceToken).updateTokenSupply(-1 * sourceTokensNeeded);
            tokenMap.get(sourceToken).updateCashSupply(-1 * connectorTokensNeeded);
            tokenMap.get(targetToken).updateTokenSupply(sourceTokensNeeded);
            tokenMap.get(targetToken).updateCashSupply(connectorTokensNeeded);

            return sourceTokensNeeded;
        }
    }

    /**
     * Exchanges tokens where the source quantity is known and the target quantity must be solved for
     * Example: Buy $10 of token A
     * @param sourceToken - The originating token that the user is using for funding
     * @param targetToken - The token the user receives from the order
     * @param sourceQuantity - The number of sourceTokens the user is using to fund the order
     * @return The number of targetTokens issued for this order
     */
    private Double exchangeTokensWithSourceQuantityFunding(String sourceToken, String targetToken, double sourceQuantity, String user) {

        // User does not have enough of sourceToken to fund this purchase
        if (!OrderValidator.validateUserHasTokens(user, sourceToken, sourceQuantity)) {
            return null;
        }

        // Sell a set quantity of tokens into cash
        if (targetToken.equals(CONNECTOR_TOKEN_NAME)) {
            double connectorTokensIssued = -1 * calculateConnectorTokensIssued(tokenMap.get(sourceToken).getCashSupply(), -1 * sourceQuantity, tokenMap.get(sourceToken).getTokenSupply(), CONNECTOR_WEIGHT);
            tokenMap.get(sourceToken).updateTokenSupply(-1 * sourceQuantity); // destroying tokens
            tokenMap.get(sourceToken).updateCashSupply(-1 * connectorTokensIssued); // paying out cash

            return connectorTokensIssued;
        }

        // Buy a token using a set quantity cash   //TODO: test this
        if (sourceToken.equals(CONNECTOR_TOKEN_NAME)) {
            double smartTokensIssued = calculateSmartTokensIssued(tokenMap.get(targetToken).getTokenSupply(), sourceQuantity, tokenMap.get(targetToken).getCashSupply(), CONNECTOR_WEIGHT);
            tokenMap.get(targetToken).updateTokenSupply(smartTokensIssued);
            tokenMap.get(targetToken).updateCashSupply(sourceQuantity);

            return smartTokensIssued;
        }

        // Sell a set quantity of a token into cash to then buy another token //TODO: test this
        else {
            Double connectorTokensIssued = exchangeTokensWithSourceQuantityFunding(sourceToken, CONNECTOR_TOKEN_NAME, sourceQuantity, user);
            double smartTokensIssued = calculateSmartTokensIssued(tokenMap.get(targetToken).getTokenSupply(), connectorTokensIssued, tokenMap.get(targetToken).getCashSupply(), CONNECTOR_WEIGHT);

            tokenMap.get(targetToken).updateTokenSupply(smartTokensIssued);
            tokenMap.get(targetToken).updateCashSupply(sourceQuantity);

            return smartTokensIssued;
        }
    }
}
