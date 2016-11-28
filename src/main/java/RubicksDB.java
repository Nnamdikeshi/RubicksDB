/**
 * Created by Nnamdi on 11/14/2016.
 */
import java.sql.*;

public class RubicksDB {
    public final static String RUBICK_TABLE_NAME = "cube_solver";
    // Each solver will have a unique ID
    public final static String PK_COLUMN = "id";
    // A primary key is needed to allow updates to the database on modifications to ResultSet
    public final static String SOLVER_COLUMN = "title";
    public final static String TIME_COLUMN = "year_released";
    // Configure driver
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // Establish our URL connection
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    // Set username and pass
    static final String USER = "*****";   //TODO replace with your username
    static final String PASSWORD = "******";   //TODO replace with your password
    // Name our database
    static private final String DB_NAME = "rubicks";
    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;
    // Create out data model
    private static RubickDataModel rubickDataModel;
    public static void main(String[] args) {

        if (!setup()) {
            System.exit(-1);
        }

        if (! loadSolvers ()) {
            System.exit(-1);
        }

        //If no errors, then start GUI
        RubicksGUI tableGUI = new RubicksGUI (rubickDataModel);

    }

    //Create or recreate a ResultSet containing the whole database, and give it to rubickDataModel
    public static boolean loadSolvers (){

        try{

            if (rs!=null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM " + RUBICK_TABLE_NAME;
            rs = statement.executeQuery(getAllData);

            if (rubickDataModel == null) {
                //If no current rubickDataModel, then make one
                rubickDataModel = new RubickDataModel (rs);
            } else {
                //Or, if one already exists, update its ResultSet
                rubickDataModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading solver times");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

    public static boolean setup(){
        try {

            //Load driver class
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD);

            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Does the table exist? If not, create it.
            if (! rubickTableExists ()) {

                //Create a table in the database with 2 columns: Solver, and Time
                String createTableSQL = "CREATE TABLE " + RUBICK_TABLE_NAME + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + SOLVER_COLUMN + " varchar(50), " + TIME_COLUMN + " int, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);

                System.out.println("Created movie_reviews table");
                String addDataSQL = "INSERT INTO " + RUBICK_TABLE_NAME + "(" + SOLVER_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Cubestormer II robot', 5.270)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + RUBICK_TABLE_NAME +  "(" + SOLVER_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES('Fakhri Raihaan(using his feet)', 27.93)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + RUBICK_TABLE_NAME +  "(" + SOLVER_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Ruxin Liu(age 3)', 99.93)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + RUBICK_TABLE_NAME +  "(" + SOLVER_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Mats Valk(human record holder)', 6.27)";
                statement.executeUpdate(addDataSQL);
            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean rubickTableExists () throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE '" + RUBICK_TABLE_NAME + "'";
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        return tablesRS.next ( );

    }

        public static void shutdown(){
            try {
                if (rs != null) {
                    rs.close();
                    System.out.println("Result set closed");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

            try {
                if (statement != null) {
                    statement.close();
                    System.out.println("Statement closed");
                }
            } catch (SQLException se){
                //Closing the connection could throw an exception too
                se.printStackTrace();
            }

            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("Database connection closed");
                }
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

