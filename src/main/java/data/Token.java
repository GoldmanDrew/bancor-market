package data;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple data class to represent a token
 */
@Getter
public class Token {

    private String tokenName;
    private String ticker;
    private Double tokenSupply;
    private Double cashSupply;
    private Double price;

    public Token(String tokenName, String ticker, Double tokenSupply, Double cashSupply, Double price) {
        this.tokenName = tokenName;
        this.ticker = ticker;
        this.tokenSupply = tokenSupply;
        this.cashSupply = cashSupply;
        this.price = price;
    }

    public void updateTokenSupply(Double supplyChange) {
        tokenSupply += supplyChange;
    }

    public void updateCashSupply(Double supplyChange) {
        cashSupply += supplyChange;
    }
}
