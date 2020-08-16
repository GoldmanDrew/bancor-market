package data;

import lombok.Getter;

@Getter
public class Order {

    private Integer orderId;
    private String user;
    private String sourceToken;
    private String targetToken;
    private double quantity;

    /**
     * A data class to represent an order
     * @param user - The user who requested the order
     * @param sourceToken - The token the user is selling
     * @param targetToken - The token the user is buying
     * @param quantity - The number of fromTokens to sell
     */
    public Order(Integer orderId, String user, String sourceToken, String targetToken, double quantity) {
        this.orderId = orderId;
        this.user = user;
        this.sourceToken = sourceToken;
        this.targetToken = targetToken;
        this.quantity = quantity;
    }
}
