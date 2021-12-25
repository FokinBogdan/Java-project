import org.jfree.chart.JFreeChart;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public static void main(String[] args) {
        var statistics = new Products(Paths.get("Каталог.csv")
                .toAbsolutePath());
        SaveToDatabase(statistics);

    }

    public static void SaveToDatabase(Products statistics) {
        Connection connection = null;
        Statement priceStatement = null;
        Statement balanceStatement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            priceStatement = connection.createStatement();
            balanceStatement = connection.createStatement();
            for (var i:statistics.getRetailPrice()) {
                priceStatement.executeUpdate(String.format("INSERT INTO price values ('%s', '%s', '%s')",
                        i.getArticleNumber(),
                        i.getProductName(),
                        i.getRetailPrice()));
            }

            for (var i:statistics.getStockBalance()) {
                balanceStatement.executeUpdate(String.format("INSERT INTO balance values ('%s', '%s')",
                        i.getArticleNumber(),
                        i.getStockBalance()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert priceStatement != null;
                priceStatement.close();
                assert balanceStatement != null;
                balanceStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

