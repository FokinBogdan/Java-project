public class StockBalance {
    private final String articleNumber;
    private final String stockBalance;

    public StockBalance(String articleNumber, String stockBalance) {
        this.articleNumber = articleNumber;
        this.stockBalance = stockBalance;
    }

    public String getStockBalance() {
        return stockBalance;
    }
    public String getArticleNumber() {
        return articleNumber;
    }
}
