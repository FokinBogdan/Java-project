import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Products {
    private final List<RetailPrice> RetailPrice;
    private final List<StockBalance> StockBalance;
    private final List<Category> Category;

    public Products(Path path) {
        var retailPrice = new ArrayList<RetailPrice>();
        var stockBalance = new ArrayList<StockBalance>();
        var category = new ArrayList<Category>();
        try (BufferedReader bR = Files.newBufferedReader(path, Charset.forName("Windows-1251"))) {
            bR.readLine();
            while (bR.ready()) {
                var row = SplitRow(bR.readLine());
                retailPrice.add(new RetailPrice(row[2], row[1]));
                stockBalance.add(new StockBalance(row[1], row[3]));
                category.add(new Category(row[1], row[0], GetCategory(row[0])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        RetailPrice = retailPrice;
        StockBalance = stockBalance;
        Category = category;
    }

    public String GetCategory(String name) {
        var sb = new StringBuilder();
        for (var i = 0; i < name.length(); i++) {
            var character = name.charAt(i);
            if (!Character.isLetter(character)) {
                break;
            }
            sb.append(character);
        }
        return sb.toString();
    }

    private String[] SplitRow(String row) {
        var result = row.split(";");
        return Arrays.copyOfRange(result, 0, 4);
    }

    public List<RetailPrice> getRetailPrice() {
        return RetailPrice;
    }
    public List<StockBalance> getStockBalance() {
        return StockBalance;
    }
    public List<Category> getCategory() { return Category; }
}
