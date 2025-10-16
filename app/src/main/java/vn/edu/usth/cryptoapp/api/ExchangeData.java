package vn.edu.usth.cryptoapp.api;

public class ExchangeData {
    private final int rank;
    private final String name;
    private final String country;
    private final double volume24h;
    private final String url;
    private final String imageUrl;

    public ExchangeData(int rank, String name, String country, double volume24h, String url, String imageUrl) {
        this.rank = rank;
        this.name = name;
        this.country = country;
        this.volume24h = volume24h;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public int getRank() { return rank; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public double getVolume24h() { return volume24h; }
    public String getUrl() { return url; }
    public String getImageUrl() { return imageUrl; }
}
