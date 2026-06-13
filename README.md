# Stock Market Simulator

A Java simulation of a simplified stock exchange. It reads a stream of
timestamped buy/sell orders, matches them against each other using
price-then-time priority, and reports trade activity, per-stock running
medians, and per-trader summaries.

## Overview

The simulator reads an input "day" of trading activity:

```
COMMENT: <free text>
MODE: TL | PR
NUM_TRADERS: <n>
NUM_STOCKS: <n>
<timestamp> BUY|SELL T<traderId> S<stockId> $<price> #<quantity>
...
```

- **TL mode** ("trade log") reads the orders directly from the lines above.
- **PR mode** ("pseudo-random") instead generates the orders using a
  provided Mersenne Twister-based generator, seeded for reproducibility.

For each order, the simulator:
1. Validates the order (valid trader/stock IDs, positive price & quantity,
   non-decreasing timestamps).
2. Adds it to the order book for its stock.
3. Matches it against any open orders on the other side, as long as the
   best buy price is at least the best sell price.
4. Updates a running median of trade prices and each trader's bought/sold
   totals and net cash flow.

At the end, it prints the total number of orders processed and (optionally)
a per-trader summary.

## Concepts & skills demonstrated

- **Abstraction & polymorphism** – `Order` is an abstract class with
  `BuyOrder` and `SellOrder` subclasses that each define their own match
  priority via `compareTo`.
- **Priority queues** – open orders are stored in `PriorityQueue`s so the
  best-priced order is always matched first.
- **The two-heap running median algorithm** – `Stock.median()` maintains a
  running median across an unbounded stream of trade prices using two
  heaps (a max-heap for the lower half, a min-heap for the upper half),
  rebalanced after every insertion.
- **Command-line argument parsing** – `Options` uses GNU `getopt` to support
  both short (`-v`) and long (`--verbose`) flags.
- **Hash maps & object modeling** – traders and stocks are looked up and
  created on demand via `HashMap`.

## Project structure

```
src/
├── Main.java          entry point
├── Options.java        command-line flag parsing
├── StockMarket.java    reads input, validates orders, drives the simulation
├── Stock.java          per-stock order book, matching, running median
├── Trader.java         per-trader stats
├── Order.java           abstract order
├── BuyOrder.java        buy-side matching priority
├── SellOrder.java        sell-side matching priority
└── P2Random.java        provided pseudo-random order generator (see Credits)
lib/
└── java-getopt-1.0.14.jar
samples/
└── example input/output files (see below)
```

## Building & running

Requires a JDK (developed against Java 17+) and the bundled `java-getopt`
library.

```bash
# Compile
javac -d out -cp lib/java-getopt-1.0.14.jar src/*.java

# Run, reading an input file from stdin
java -cp "out:lib/java-getopt-1.0.14.jar" Main -v -m -i < samples/Small-input-TL.txt
```

On Windows, use `;` instead of `:` in the classpath.

### Flags

| Flag | Long form        | Effect                                              |
|------|-------------------|-----------------------------------------------------|
| `-v` | `--verbose`       | Print each trade as it's matched                    |
| `-m` | `--median`        | Print each stock's running median price over time   |
| `-i` | `--trader-info`   | Print a per-trader summary at the end                |

### Sample data

`samples/` contains small example inputs/outputs for both TL and PR mode:

- `Spec-*` – a short hand-written example (matches the assignment spec)
- `Small-*` – a larger (~100 order) example

Each `*-output-*.txt` file shows the expected output for the corresponding
input with a given flag combination (`-all` = all flags, `-i`/`-m`/`-v` =
that flag only). Running the commands above and diffing against these files
is a quick way to confirm everything still works after making changes.

A much larger (100,000-order) stress test was used during development but
isn't included here to keep the repo small.

## Credits

- `src/P2Random.java`, including the embedded `MTRandom` (Mersenne Twister)
  implementation, was provided as starter code for the course assignment
  this project is based on and is included here unmodified, with its
  original author attribution intact.
- [`java-getopt`](http://www.urbanophile.com/arenn/hacking/getopt/) is a
  third-party library used for command-line argument parsing.
