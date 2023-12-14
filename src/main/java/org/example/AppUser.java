package org.example;

import jakarta.persistence.*;
import lombok.Getter;

import java.beans.Transient;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;

    private LocalDate dob;
    private int age;

    public AppUser() {
    }

    public AppUser(int id, String email, LocalDate dob, int age) {
        this.id = id;
        this.email = email;
        this.dob = dob;
        this.age = age;
    }


    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS app_user (" +
            "id SERIAL PRIMARY KEY," +
            "email VARCHAR(255) UNIQUE NOT NULL," +
            "dob DATE NOT NULL," +
            "age INT NOT NULL" +
            ")";

    private static final String INSERT_USER_QUERY = "INSERT INTO app_user (email, dob, age) VALUES (?, ?, ?)";

    private static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT * FROM app_user WHERE email = ?";

    private static final String DELETE_USER_QUERY = "DELETE FROM app_user WHERE email = ?";

    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM app_user";

    static {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUserToDB() {
        if (!findUserByEmail(this.email).isPresent()) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY)) {
                preparedStatement.setString(1, this.email);
                preparedStatement.setDate(2, Date.valueOf(this.dob));
                preparedStatement.setInt(3, this.age);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User with email " + this.email + " already exists. Not adding to the database.");
        }
    }

    public static List<AppUser> getAllUsers() {
        List<AppUser> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                LocalDate dob = resultSet.getDate("dob").toLocalDate();
                int age = resultSet.getInt("age");

                AppUser user = new AppUser(id, email, dob, age);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static Optional<AppUser> findUserByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL_QUERY)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String userEmail = resultSet.getString("email");
                    LocalDate dob = resultSet.getDate("dob").toLocalDate();
                    int age = resultSet.getInt("age");

                    AppUser user = new AppUser(id, userEmail, dob, age);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void deleteUserFromDB() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_QUERY)) {
            preparedStatement.setString(1, this.email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getter using @Transient
    @Transient
    public LocalDate getDob() {
        return dob;
    }

    // New method for calculating age
    public int calculateAge() {
        LocalDate now = LocalDate.now();
        return Period.between(this.dob, now).getYears();
    }

    // Main method for testing
}
