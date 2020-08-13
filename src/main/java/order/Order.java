package order;

import lombok.Getter;

@Getter
public class Order {

    private Integer orderId;
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
    public Order(Integer orderId, String user, String fromToken, String toToken, double quantity) {
        this.orderId = orderId;
        this.user = user;
        this.fromToken = fromToken;
        this.toToken = toToken;
        this.quantity = quantity;
    }
}
