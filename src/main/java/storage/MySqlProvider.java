package storage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlProvider {

    /**
     * Connects to the vase MySQL database
     * @return Connection to the vase MySQL database
     */
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/unibursal?serverTimezone=EST";

        Properties credentialProperties = readCredentials("credentials.properties");
        String user = credentialProperties.getProperty("user");
        String password = credentialProperties.getProperty("password");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads a properties file containing the user and password for the MySQL database.
     * This file must reside in the resources folder
     * @param propertiesFilepath - The file containing the MySQL credentials
     * @return The file as a Properties object
     */
    private static Properties readCredentials(String propertiesFilepath) {
        InputStream stream = MySqlProvider.class.getClassLoader().getResourceAsStream(propertiesFilepath);
        Properties credentialsProperites = new Properties();

        try {
            credentialsProperites.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return credentialsProperites;

    }
}
