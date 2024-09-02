package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection = Util.getConnection();

    private static final String CREATE_USERS_TABLE = "CREATE TABLE user (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) , last_name VARCHAR(255) , age INT)";
    private static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS user";
    private static final String SAVE_USER = "INSERT INTO user (name, last_name, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USER_BY_ID = "DELETE FROM user WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM user";
    private static final String CLEAN_USERS_TABLE = "TRUNCATE TABLE user";

    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        createAndDropTable(CREATE_USERS_TABLE);
    }

    @Override
    public void dropUsersTable() {
        createAndDropTable(DROP_USERS_TABLE);
    }

    private void createAndDropTable(String sql) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet != 0) {
                System.out.println("таблица создана / удалена");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER_BY_ID)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> resultList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                resultList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultList;
    }

    @Override
    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CLEAN_USERS_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        try {
            Util.closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}