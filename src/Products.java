import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Products {
    private final List<RetailPrice> RetailPrice;
    private final List<StockBalance> StockBalance;

    public Products(Path path) {
        var retailPrice = new ArrayList<RetailPrice>();
        var stockBalance = new ArrayList<StockBalance>();
        try (BufferedReader bR = Files.newBufferedReader(path, Charset.forName("Windows-1251"))) {
            bR.readLine();
            while (bR.ready()) {
                var row = splitRow(bR.readLine());
                retailPrice.add(new RetailPrice(row[1], row[0], row[2]));
                stockBalance.add(new StockBalance(row[1], row[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        RetailPrice = retailPrice;
        StockBalance = stockBalance;
    }

    private String[] splitRow(String row) {
        var result = row.split(";");
        return Arrays.copyOfRange(result, 0, 4);
    }
}
