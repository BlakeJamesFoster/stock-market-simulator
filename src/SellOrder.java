/**
 * A request to sell shares of a stock. Sell orders are prioritized by
 * lowest price first; ties are broken by which order arrived first.
 */
public class SellOrder extends Order {

    public SellOrder(int timeStamp, Trader trader, String stockId, int price, int quantity, int id) {
        super(timeStamp, trader, stockId, price, quantity, id);
    }

    @Override
    public boolean isSell() {
        return true;
    }

    /**
     * Lower-priced sell orders are matched first. When two orders offer
     * the same price, the one placed earlier (lower id) wins.
     */
    @Override
    public int compareTo(Order other) {
        if (this.getPrice() == other.getPrice()) {
            return this.getID() - other.getID();
        }
        return this.getPrice() - other.getPrice();
    }
}
