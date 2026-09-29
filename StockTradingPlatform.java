import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    void displayStock() {
        System.out.printf("%-10s %-20s ₹%.2f%n",
                symbol, name, price);
    }
}

class Transaction {
    String type;
    String symbol;
    int quantity;
    double price;

    Transaction(String type, String symbol, int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    void displayTransaction() {
        System.out.printf("%-8s %-8s %-10d ₹%.2f%n",
                type, symbol, quantity, price);
    }
}

class User {
    String name;
    double cash;
    HashMap<String, Integer> portfolio;
    ArrayList<Transaction> transactions;

    User(String name, double cash) {
        this.name = name;
        this.cash = cash;
        portfolio = new HashMap<>();
        transactions = new ArrayList<>();
    }

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (cash >= cost) {
            cash -= cost;

            portfolio.put(
                stock.symbol,
                portfolio.getOrDefault(stock.symbol, 0) + quantity
            );

            transactions.add(
                new Transaction("BUY", stock.symbol, quantity, stock.price)
            );

            System.out.println("Stock purchased successfully.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (owned >= quantity) {
            double amount = stock.price * quantity;

            cash += amount;

            if (owned == quantity) {
                portfolio.remove(stock.symbol);
            } else {
                portfolio.put(stock.symbol, owned - quantity);
            }

            transactions.add(
                new Transaction("SELL", stock.symbol, quantity, stock.price)
            );

            System.out.println("Stock sold successfully.");
        } else {
            System.out.println("You don't own enough shares.");
        }
    }

    void displayPortfolio(ArrayList<Stock> stocks) {
        double totalValue = 0;

        System.out.println("\n========== PORTFOLIO ==========");
        System.out.printf("%-10s %-10s %-15s%n",
                "Symbol", "Quantity", "Value");
        System.out.println("--------------------------------");

        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();

            for (Stock stock : stocks) {
                if (stock.symbol.equals(symbol)) {
                    double value = quantity * stock.price;
                    totalValue += value;

                    System.out.printf("%-10s %-10d ₹%-14.2f%n",
                            symbol, quantity, value);
                }
            }
        }

        System.out.println("--------------------------------");
        System.out.printf("Cash Balance : ₹%.2f%n", cash);
        System.out.printf("Stock Value  : ₹%.2f%n", totalValue);
        System.out.printf("Total Value  : ₹%.2f%n",
                cash + totalValue);
    }

    void displayTransactions() {
        System.out.println("\n========== TRANSACTION HISTORY ==========");
        System.out.printf("%-8s %-8s %-10s %-10s%n",
                "Type", "Symbol", "Quantity", "Price");
        System.out.println("-----------------------------------------");

        for (Transaction t : transactions) {
            t.displayTransaction();
        }

        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        }
    }
}

public class StockTradingPlatform {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Stock> stocks = new ArrayList<>();

        // Market data
        stocks.add(new Stock("AAPL", "Apple", 225.50));
        stocks.add(new Stock("GOOGL", "Alphabet", 195.25));
        stocks.add(new Stock("MSFT", "Microsoft", 510.75));
        stocks.add(new Stock("AMZN", "Amazon", 230.40));
        stocks.add(new Stock("TSLA", "Tesla", 345.60));

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        User user = new User(name, 100000);

        int choice;

        do {
            System.out.println("\n========== STOCK TRADING PLATFORM ==========");
            System.out.println("1. Display Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("\n========== MARKET DATA ==========");
                    System.out.printf("%-10s %-20s %-10s%n",
                            "Symbol", "Company", "Price");
                    System.out.println("------------------------------------------");

                    for (Stock stock : stocks) {
                        stock.displayStock();
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = sc.next().toUpperCase();

                    Stock buyStock = findStock(stocks, buySymbol);

                    if (buyStock != null) {
                        System.out.print("Enter quantity: ");
                        int quantity = sc.nextInt();

                        user.buyStock(buyStock, quantity);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = sc.next().toUpperCase();

                    Stock sellStock = findStock(stocks, sellSymbol);

                    if (sellStock != null) {
                        System.out.print("Enter quantity: ");
                        int quantity = sc.nextInt();

                        user.sellStock(sellStock, quantity);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 4:
                    user.displayPortfolio(stocks);
                    break;

                case 5:
                    user.displayTransactions();
                    break;

                case 6:
                    System.out.println("Thank you for using the Stock Trading Platform!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        sc.close();
    }

    static Stock findStock(ArrayList<Stock> stocks, String symbol) {

        for (Stock stock : stocks) {
            if (stock.symbol.equalsIgnoreCase(symbol)) {
                return stock;
            }
        }

        return null;
    }
}