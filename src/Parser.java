import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public static void main(String[] args) {
        var statistics = new Products(Paths.get("Каталог.csv")
                .toAbsolutePath());
    }
}

