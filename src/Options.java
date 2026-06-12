import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * Parses the simulator's command-line flags using GNU getopt.
 *
 * <p>Supported flags:
 * <ul>
 *   <li>{@code -v} / {@code --verbose} - print every trade as it is matched</li>
 *   <li>{@code -m} / {@code --median} - print the running median match price
 *       for each stock whenever the simulation clock advances</li>
 *   <li>{@code -i} / {@code --trader-info} - print a per-trader summary at
 *       the end of the simulation</li>
 * </ul>
 */
public class Options {
    private boolean verbose;
    private boolean median;
    private boolean traderInfo;

    public Options(String[] args) {
        LongOpt[] longOptions = {
                new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, 'v'),
                new LongOpt("median", LongOpt.NO_ARGUMENT, null, 'm'),
                new LongOpt("trader-info", LongOpt.NO_ARGUMENT, null, 'i')
        };

        Getopt getopt = new Getopt("Main", args, "vmi", longOptions);

        int choice;
        while ((choice = getopt.getopt()) != -1) {
            switch (choice) {
                case 'v':
                    verbose = true;
                    break;
                case 'm':
                    median = true;
                    break;
                case 'i':
                    traderInfo = true;
                    break;
                default:
                    System.err.println("Not a valid input");
                    System.exit(1);
            }
        }
    }

    public boolean isTraderInfo() {
        return traderInfo;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isMedian() {
        return median;
    }
}
