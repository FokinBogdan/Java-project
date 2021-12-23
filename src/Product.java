public class Product {
    private final String productName;
    private final String articleNumber;
    private final String retailPrice;
    private final String stockBalance;

    public Product(String productName, String articleNumber, String retailPrice, String stockBalance) {
        this.productName = productName;
        this.articleNumber = articleNumber;
        this.retailPrice = retailPrice;
        this.stockBalance = stockBalance;
    }

    public String getProductName() {
        return productName;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public String getStockBalance() {
        return stockBalance;
    }
}
