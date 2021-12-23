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
    private final List<Product> Products;

    public Products(Path path) {
        var products = new ArrayList<Product>();
        try (BufferedReader bR = Files.newBufferedReader(path, Charset.forName("Windows-1251"))) {
            bR.readLine();
            while (bR.ready()) {
                var row = splitRow(bR.readLine());
                products.add(new Product(row[0], row[1], row[2], row[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Products = products;
    }

    private String[] splitRow(String row) {
        var result = row.split(";");
        if (result.length > 4) {
            result[3] += "." + result[4];
        }
        return Arrays.copyOfRange(result, 0, 4);
    }
}
