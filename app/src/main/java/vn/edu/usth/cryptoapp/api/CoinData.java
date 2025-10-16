package vn.edu.usth.cryptoapp.api;

public class CoinData {
    private int rank;
    private String name;
    private String symbol;
    private double price;
    private double change1h;
    private String iconUrl;
    private String id;


    public CoinData(int rank, String name, String symbol, double price,
                    double change1h, String iconUrl) {
        this.rank = rank;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.change1h = change1h;
        this.iconUrl = iconUrl;
    }

    public int getRank() { return rank; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getChange1h() { return change1h; }
    public String getIconUrl() { return iconUrl; }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
