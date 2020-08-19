package data;

import lombok.Getter;

@Getter
public class Order {

    private Integer orderId;
    private String user;
    private String sourceToken;
    private String targetToken;
    private Double sourceQuantity;
    private Double targetQuantity;

    /**
     * A data class to represent an order, note that exactly one of sourceQuantity or targetQuantity must be null
     * @param user - The user who requested the order
     * @param sourceToken - The token the user is selling
     * @param targetToken - The token the user is buying
     * @param sourceQuantity - The number of source tokens used to fund this order
     * @param targetQuantity - The number of target tokens to end up with for this order
     */
    public Order(Integer orderId, String user, String sourceToken, String targetToken, Double sourceQuantity, Double targetQuantity) {
        if ((sourceQuantity == null && targetQuantity == null) || (sourceQuantity != null && targetQuantity != null)) {
            throw new IllegalArgumentException("Exactly one of sourceQuantity and targetQuantity must be null");
        }

        this.orderId = orderId;
        this.user = user;
        this.sourceToken = sourceToken;
        this.targetToken = targetToken;
        this.sourceQuantity = sourceQuantity;
        this.targetQuantity = targetQuantity;
    }
}
