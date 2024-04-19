import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionExample {
    public static void main(String[] args) throws Exception {
        String connectionUrl =
        "jdbc:sqlserver://cxp-sql-02\\jkd50;" 
        + "database=GrandSlamStats;" 
        + "user=db_user;"
        + "password=ThisIsANewPassword123@;" 
        + "encrypt=true;"
        + "trustServerCertificate=true;"
        + "loginTimeout=15;";


        ResultSet resultSet = null;
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
        String selectSql =  "Select * from Leagues";

        resultSet = statement.executeQuery(selectSql);
        
        while (resultSet.next()) {
            System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
        }
  
        }
        catch (SQLException e) {
        e.printStackTrace();
        }
    }
}
