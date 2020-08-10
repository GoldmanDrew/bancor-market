package order;

import lombok.Getter;

@Getter
public class Order {

    private String user;
    private String fromToken;
    private String toToken;
    private double quantity;

    /**
     * A data class to represent an order
     * @param user - The user who requested the order
     * @param fromToken - The token the user is selling
     * @param toToken - The token the user is buying
     * @param quantity - The number of fromTokens to sell
     */
    public Order(String user, String fromToken, String toToken, double quantity) {
        this.user = user;
        this.fromToken = fromToken;
        this.toToken = toToken;
        this.quantity = quantity;
    }
}
