import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ProductCatalog {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Хотите распарсить CSV-файл в Базу Данных? (+ или -)");
        var line = in.next();
        Products products;

        if (line.equals("+")) {
            System.out.println("Пожалуйста, подождите...");
            products = new Products(Paths.get("Каталог.csv")
                    .toAbsolutePath());
            SaveToDatabase(products);
        }

        Exercise1();
        System.out.println("\nЗадание 2:\n");
        System.out.println("Средняя стоимость инфракрасной лампы:");
        Exercise2();
        System.out.println("\nЗадание 3:\n");
        System.out.println("Пятёрка самых дорогих товаров, которых на складе более 10:\n");
        Exercise3(10, 5);
    }

    public static void Exercise1() {
        JFreeChart chart = ChartFactory.createBarChart(
                "График средней цены 10 различных товаров",
                null,
                "Средняя цена",
                GetDataSet(MakeExecuteQuery("""
                        SELECT
                            newTable.category,
                            AVG(newTable.rt) as av
                        FROM
                            (
                            SELECT
                                category.category,
                                retailPrice as rt
                            FROM
                                category,
                                price
                            WHERE
                                category.articleNumber = price.articleNumber
                            ) as newTable
                        GROUP BY
                            newTable.category
                        ORDER BY
                            RANDOM()
                        LIMIT 10
                        """)));
        JFrame frame = new JFrame("Economy");
        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(600,400);
        frame.setVisible(true);
    }

    public static void Exercise2() {
        var set = MakeExecuteQuery("""
                SELECT
                    balance.stockBalance,
                    category.name,
                    price.retailPrice
                FROM
                    category
                LEFT JOIN balance ON balance.articleNumber = category.articleNumber
                LEFT JOIN price ON price.articleNumber = category.articleNumber
                WHERE
                    name LIKE 'Лампа инфракрасная%'
                """);
        try {
            var totalPrice = 0.0;
            var count = 0.0;
            while(true) {
                assert set != null;
                if (!set.next()) break;
                totalPrice += set.getDouble(1) * set.getDouble(3);
                count += set.getDouble(3);
            }
            System.out.printf("%.3f%n", totalPrice / count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Exercise3(int amount, int count) {
        var set = MakeExecuteQuery(String.format("""
                SELECT category.name, price.retailPrice, balance.stockBalance
                FROM category
                LEFT JOIN price ON price.articleNumber = category.articleNumber
                LEFT JOIN balance ON balance.articleNumber = category.articleNumber
                WHERE stockBalance > %s
                ORDER BY retailPrice DESC
                LIMIT %s
                """, amount, count));

        try {
            while(true) {
                assert set != null;
                if (!set.next()) break;
                System.out.println(set.getString(1) + " | "
                        + set.getString(2) + " | "
                        + set.getString(3));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static CategoryDataset GetDataSet(CachedRowSet set) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            while (set.next()) {
                dataset.addValue(set.getDouble(2), set.getString(1), "Группа товара");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public static CachedRowSet MakeExecuteQuery(String query)
    {
        Connection connection = null;
        Statement statement = null;

        try {
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet set = factory.createCachedRowSet();

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            statement = connection.createStatement();

            var resSet = statement.executeQuery(query);
            set.populate(resSet);

            return set;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void SaveToDatabase(Products statistics) {
        Connection connection = null;
        Statement priceStatement = null;
        Statement balanceStatement = null;
        Statement categoryStatement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            priceStatement = connection.createStatement();
            balanceStatement = connection.createStatement();
            categoryStatement = connection.createStatement();
            for (var i:statistics.getRetailPrice()) {
                priceStatement.executeUpdate(String.format("INSERT INTO price values ('%s', '%s')",
                        i.getArticleNumber(),
                        i.getRetailPrice()));
            }

            for (var i:statistics.getStockBalance()) {
                balanceStatement.executeUpdate(String.format("INSERT INTO balance values ('%s', '%s')",
                        i.getArticleNumber(),
                        i.getStockBalance()));
            }

            for (var i:statistics.getCategory()) {
                categoryStatement.executeUpdate(String.format("INSERT INTO category values ('%s', '%s', '%s')",
                        i.getArticleNumber(),
                        i.getProductName(),
                        i.getCategory()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert priceStatement != null;
                priceStatement.close();
                assert balanceStatement != null;
                balanceStatement.close();
                assert categoryStatement != null;
                categoryStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

