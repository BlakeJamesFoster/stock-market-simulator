/**
 * A request to buy shares of a stock. Buy orders are prioritized by
 * highest price first; ties are broken by which order arrived first.
 */
public class BuyOrder extends Order {

    public BuyOrder(int timeStamp, Trader trader, String stockId, int price, int quantity, int id) {
        super(timeStamp, trader, stockId, price, quantity, id);
    }

    @Override
    public boolean isSell() {
        return false;
    }

    /**
     * Higher-priced buy orders are matched first. When two orders offer
     * the same price, the one placed earlier (lower id) wins.
     */
    @Override
    public int compareTo(Order other) {
        if (this.getPrice() == other.getPrice()) {
            return this.getID() - other.getID();
        }
        return other.getPrice() - this.getPrice();
    }
}
