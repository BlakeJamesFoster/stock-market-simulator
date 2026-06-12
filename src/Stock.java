import java.util.Collections;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Tracks the open buy and sell orders for a single stock, matches them
 * against each other, and maintains a running median of trade prices.
 *
 * <p>Buy and sell orders are each kept in a {@link PriorityQueue} ordered
 * by {@link Order#compareTo}, so the best-priced order on each side is
 * always at the front of its queue. The two sides are matchable whenever
 * the best buy price is at least the best sell price.
 *
 * <p>The running median is maintained with the classic two-heap trick:
 * {@code lowerHalf} is a max-heap of the smaller half of the prices seen
 * so far, {@code upperHalf} is a min-heap of the larger half, and the two
 * heaps are kept balanced so the median is always at one (or both) of
 * their tops.
 */
public class Stock {
    private final String stockId;
    private final PriorityQueue<Order> buys;
    private final PriorityQueue<Order> sells;
    private double runningMedian;
    private final PriorityQueue<Double> lowerHalf;
    private final PriorityQueue<Double> upperHalf;

    public Stock(String stockId) {
        this.stockId = stockId;
        runningMedian = Double.NaN;
        lowerHalf = new PriorityQueue<>(Collections.reverseOrder());
        upperHalf = new PriorityQueue<>();
        buys = new PriorityQueue<>();
        sells = new PriorityQueue<>();
    }

    public void addOrder(Order order) {
        if (order.isSell()) {
            sells.add(order);
        } else {
            buys.add(order);
        }
    }

    /**
     * Repeatedly matches the best buy against the best sell while their
     * prices cross, splitting partially-filled orders by quantity. Returns
     * the number of trades executed in this call.
     */
    public int processOrders(Options options) {
        int ordersProcessed = 0;

        while (matchable()) {
            Order bestBuy = buys.peek();
            Order bestSell = sells.peek();

            if (bestBuy == null || bestSell == null) {
                return ordersProcessed;
            }

            // The order that arrived later sets the trade price.
            int price = bestBuy.getID() > bestSell.getID() ? bestSell.getPrice() : bestBuy.getPrice();

            int amount = Math.min(bestBuy.getQuantity(), bestSell.getQuantity());

            bestBuy.setQuantity(bestBuy.getQuantity() - amount);
            bestSell.setQuantity(bestSell.getQuantity() - amount);
            ordersProcessed++;
            median(price);
            updateInfo(bestBuy, bestSell, amount, price);

            if (options.isVerbose()) {
                System.out.printf("Trader %d purchased %d shares of Stock %d from Trader %d for $%d/share\n",
                        bestBuy.getTraderID(), amount, bestBuy.getStockNum(), bestSell.getTraderID(), price);
            }

            if (bestBuy.getQuantity() == 0) {
                buys.remove(bestBuy);
            }
            if (bestSell.getQuantity() == 0) {
                sells.remove(bestSell);
            }
        }
        return ordersProcessed;
    }

    private void updateInfo(Order buyOrder, Order sellOrder, int quantityTraded, int price) {
        buyOrder.getTrader().setNumberBought(buyOrder.getTrader().getNumberBought() + quantityTraded);
        buyOrder.getTrader().setNetValueTraded(buyOrder.getTrader().getNetValueTraded() - quantityTraded * price);

        sellOrder.getTrader().setNumberSold(sellOrder.getTrader().getNumberSold() + quantityTraded);
        sellOrder.getTrader().setNetValueTraded(sellOrder.getTrader().getNetValueTraded() + quantityTraded * price);
    }

    /** Two orders can trade when the best buy price meets or beats the best sell price. */
    public boolean matchable() {
        if (buys.isEmpty() || sells.isEmpty()) {
            return false;
        }
        return buys.peek().getPrice() >= sells.peek().getPrice();
    }

    /**
     * Folds a new trade price into the running median using two heaps:
     * {@code lowerHalf} holds the smaller half of all prices seen so far
     * (largest on top), and {@code upperHalf} holds the larger half
     * (smallest on top). After each insertion the heaps are rebalanced so
     * their sizes differ by at most one, and the median is read off the
     * top(s) of the heaps.
     */
    public void median(double price) {
        if (price < runningMedian) {
            lowerHalf.add(price);
        } else {
            upperHalf.add(price);
        }

        if (lowerHalf.size() - upperHalf.size() == 2) {
            upperHalf.add(lowerHalf.remove());
        } else if (upperHalf.size() - lowerHalf.size() == 2) {
            lowerHalf.add(upperHalf.remove());
        }

        if (lowerHalf.size() > upperHalf.size()) {
            runningMedian = lowerHalf.peek();
        } else if (lowerHalf.size() < upperHalf.size()) {
            runningMedian = upperHalf.peek();
        } else {
            runningMedian = (upperHalf.peek() + lowerHalf.peek()) / 2.0;
        }

        setRunningMedian(runningMedian);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(stockId, stock.stockId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stockId);
    }

    public double getRunningMedian() {
        return runningMedian;
    }

    public void setRunningMedian(double runningMedian) {
        this.runningMedian = runningMedian;
    }

    /** Returns the stock's display number, e.g. "0" for stock "S0". */
    public String getStockNumber() {
        return stockId.substring(1);
    }
}
