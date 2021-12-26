public class RetailPrice {
    private final String retailPrice;
    private final String articleNumber;

    public RetailPrice(String retailPrice, String articleNumber) {
        this.retailPrice = retailPrice;
        this.articleNumber = articleNumber;
    }

    public String getArticleNumber() {
        return articleNumber;
    }
    public String getRetailPrice() {
        return retailPrice;
    }
}
