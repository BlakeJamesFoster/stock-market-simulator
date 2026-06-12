public class Main {
    /**
     * Entry point for the stock market simulator. Parses command-line
     * options, then runs the order-matching simulation over the orders
     * read from standard input.
     */
    public static void main(String[] args) {
        Options options = new Options(args);
        StockMarket stockMarket = new StockMarket(options);
        stockMarket.runSimulation();
    }
}
