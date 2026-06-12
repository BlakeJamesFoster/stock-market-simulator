/**
 * A single buy or sell order placed by a trader for a particular stock.
 * Subclasses {@link BuyOrder} and {@link SellOrder} define how orders of
 * each type are prioritized for matching.
 */
public abstract class Order implements Comparable<Order> {
    private final int id;
    private final Trader trader;
    private final String stockId;
    private final int price;
    private int quantity;
    private final int time;

    /**
     * @param timeStamp the simulation time at which this order arrives
     * @param trader the trader placing the order
     * @param stockId the stock identifier, e.g. "S0"
     * @param price the price per share (must be greater than 0)
     * @param quantity the number of shares (must be greater than 0)
     * @param id a sequence number used to break ties between orders
     */
    public Order(int timeStamp, Trader trader, String stockId, int price, int quantity, int id) {
        this.time = timeStamp;
        this.trader = trader;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
    }

    public abstract boolean isSell();

    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getStockID() { return stockId; }
    public int getStockNum() { return Integer.parseInt(stockId.substring(1)); }
    public Trader getTrader() { return trader; }
    public int getTraderID() { return Integer.parseInt(trader.getTraderId().substring(1)); }
    public int getTime() { return time; }
    public int getID() { return id; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", trader=" + trader +
                ", stockId='" + stockId + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", time=" + time +
                '}';
    }

    /**
     * Defines the matching priority between two orders of the same type.
     * Implementations order by price first (best price wins), then by
     * {@link #getID()} to break ties in favor of whichever order arrived
     * first.
     */
    @Override
    public abstract int compareTo(Order other);
}
