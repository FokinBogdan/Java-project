public class Category {
    private final String articleNumber;
    private final String productName;
    private final String category;

    public Category(String articleNumber, String productName, String category) {
        this.articleNumber = articleNumber;
        this.productName = productName;
        this.category = category;
    }

    public String getProductName() { return productName; }
    public String getArticleNumber() { return articleNumber; }
    public String getCategory() { return category; }
}
