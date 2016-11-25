/**
 * Created by Nnamdi on 11/14/2016.
 */
import java.util.*;
import java.sql.*;
import java.util.Scanner;
public class RubicksDB {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/Rubicks";     //Connection string – where's the database?
    static final String USER = "********";   //TODO replace with your username
    static final String PASSWORD = "*****";   //TODO replace with your password

    public static void main(String[] args) {
        Scanner stringScanner = new Scanner(System.in);
        Scanner numberScanner = new Scanner(System.in);

        ResultSet rs = null;

        System.out.println("Rubicks Database Program");

        try {
            Class.forName(JDBC_DRIVER);

        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }


        /* Create a table, and insert some test data */

        try {
            Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
            Statement statement = conn.createStatement();
            {
            /* Create a table in the database. Stores dog's names, ages, weights and whether the dog has been vaccinated or not. */

                String createTableSQL = "CREATE TABLE IF NOT EXISTS rubicks ('Cube_solver' varchar(30), 'Seconds_to_solve' double)";
                statement.executeUpdate(createTableSQL);
                System.out.println("Created Rubicks table");

            /* Add one row of test data, using a prepared statement */

                String prepStatInsert = "INSERT INTO rubicks VALUES ( ? , ?)";
                PreparedStatement psInsert = conn.prepareStatement(prepStatInsert);

            /* And add some more data, this time the data comes from arrays. Loop over arrays and use data in the PreparedStatement */

                String[] solveNames = {"Cubestormer II robot", "Fakhri Raihaan(using his feet)", "Ruxin Liu(age 3)", "Mats Valk(human record holder)"};
                double[] timeTaken = {5.270, 27.93, 99.93, 6.27};

                for (int i = 0; i < solveNames.length; i++) {
                    psInsert.setString(1, solveNames[i]);
                    psInsert.setDouble(2, timeTaken[i]);
                    psInsert.executeUpdate();
                }
                //Mats Valk has broken his record.... Update his record

                System.out.println();
                System.out.println("Updating Mats Valk's record to 5.55");

                String updateLassie = "UPDATE rubicks SET timeTaken=5.55 WHERE solveNames='Mats Valk(human record holder)'";
                statement.executeUpdate(updateLassie);

                System.out.println("Added data to database");
                System.out.println("Add another time? Enter 'yes' ");
                String more = stringScanner.next();

                if (more.equalsIgnoreCase("yes")) {
                    System.out.println("Enter a new solver: ");
                    String moreName = stringScanner.nextLine();
                    System.out.println("Now enter how fast they solved the cube (In seconds): ");
                    double moreTime = numberScanner.nextDouble();
                    psInsert.setString(1, moreName);
                    psInsert.setDouble(2, moreTime);
                    psInsert.executeUpdate();
                } else {
                    //close connection, statement, prepared statement
                    psInsert.close();
                    statement.close();
                    conn.close();
                }
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }
    }
}
