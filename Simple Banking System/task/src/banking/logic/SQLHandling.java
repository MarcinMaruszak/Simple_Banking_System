package banking.logic;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class SQLHandling {
    private final SQLiteDataSource dataSource;

    public SQLHandling(String name) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + name);
    }

    public SQLHandling() {
        this("bank.db");
    }

    public void connectAndCheck() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "number VARCHAR(16)," +
                    "pin VARCHAR(4)," +
                    "balance INTEGER DEFAULT 0)");
        }
    }

    public boolean cardNmbTaken(String cardNmb) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM card WHERE number=?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setString(1, cardNmb);
            ResultSet resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addAccount(String card, String pin) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO card (number,pin) VALUES (?,?)";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setString(1, card);
            prepStatement.setString(2, pin);
            prepStatement.executeUpdate();
        }
    }

    public String login(String card, String pin) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM card WHERE number=? AND pin=?";
            PreparedStatement prepSt = conn.prepareStatement(sql);
            prepSt.setString(1, card);
            prepSt.setString(2, pin);
            ResultSet resultSet = prepSt.executeQuery();
            if (resultSet.next()) {
                return card;
            }
        }
        throw new Exception("Wrong card number or PIN!");
    }

    public void addIncome(String number, int income) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, income);
            prepStatement.setString(2, number);
            prepStatement.executeUpdate();
        }
    }

    public int getBalance(String cardNumber) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM card WHERE number = ?";
            PreparedStatement prepStm = connection.prepareStatement(sql);
            prepStm.setString(1, cardNumber);
            ResultSet resultSet = prepStm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("balance");
            }
        }
        throw new Exception("Couldn't retrieve balance");
    }

    public void doTransfer(String cardNumber, String targetCard, int amount) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
            PreparedStatement prepSt = connection.prepareStatement(sql);
            prepSt.setInt(1, amount * -1);
            prepSt.setString(2, cardNumber);
            prepSt.executeUpdate();

            prepSt.setInt(1, amount);
            prepSt.setString(2, targetCard);
            prepSt.executeUpdate();

            connection.commit();
        }
    }

    public void closeAccount(String card) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM card WHERE number = ?";
            PreparedStatement preSt = connection.prepareStatement(sql);
            preSt.setString(1, card);
            preSt.executeUpdate();
        }
    }
}
