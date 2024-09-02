package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;

public class Main {
    public static void main(String[] args) {
        UserDaoHibernateImpl userDao = new UserDaoHibernateImpl();

        // create table
        userDao.createUsersTable();

        // add 4 users
        userDao.saveUser("Tony", "Soprano", (byte) 56);
        userDao.saveUser("Vito", "Spatafore", (byte) 56);
        userDao.saveUser("Christopher", "Moltisanti", (byte) 56);
        userDao.saveUser("Junior", "Soprano", (byte) 56);

        // getting all users
        for (User user : userDao.getAllUsers()) {
            System.out.println(user);
        }

        // deletion of table entries
        userDao.cleanUsersTable();

        // drop table
        userDao.dropUsersTable();
    }
}
