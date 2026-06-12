/**
 * Tracks a single trader's identity and running totals as orders are
 * matched throughout the simulation.
 */
public class Trader {
    private String traderId;
    private int numberBought;
    private int numberSold;
    private int netValueTraded;

    public Trader(String traderId) {
        this.traderId = traderId;
    }

    public String getTraderId() {
        return traderId;
    }

    public int getNumberBought() {
        return numberBought;
    }

    public int getNumberSold() {
        return numberSold;
    }

    public int getNetValueTraded() {
        return netValueTraded;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public void setNumberBought(int numberBought) {
        this.numberBought = numberBought;
    }

    public void setNumberSold(int numberSold) {
        this.numberSold = numberSold;
    }

    public void setNetValueTraded(int netValueTraded) {
        this.netValueTraded = netValueTraded;
    }

    @Override
    public String toString() {
        return "Trader{" +
                "traderId='" + traderId + '\'' +
                '}';
    }
}
