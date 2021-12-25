public class RetailPrice {
    private final String retailPrice;
    private final String articleNumber;
    private final String productName;

    public RetailPrice(String retailPrice, String articleNumber, String productName) {
        this.retailPrice = retailPrice;
        this.articleNumber = articleNumber;
        this.productName = productName;
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
}
