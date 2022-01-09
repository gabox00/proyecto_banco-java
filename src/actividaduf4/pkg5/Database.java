package actividaduf4.pkg5;
import java.sql.*;

public class Database {

    private static final String CLASS = "org.sqlite.JDBC";
    private final String url;
    private Connection conn;
    private Statement statement;
    private String errorMessage;

    /**
     * Constructor
     * Aqui almacenaremos los datos respectivos a la conexion
     * @param _url Direccion del archivo .db de sqlite
     */
    public Database(String _url) {
        this.url = _url;
    }

    protected boolean connection() {
        try {
            Class.forName(CLASS);
            this.conn = DriverManager.getConnection(this.url);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Se encarga de ejecutar una query, que no arrojara ningun rs
     * (INSERT, UPDATE, DELETE).
     *
     * @param query query a ejecutar
     * @return boolean rs de la operacion TRUE si se desconecto
     * exitosamente, FALSE en caso contrario.
     */
    protected boolean crud(String query) {
        int rs;

        try {
            this.statement = this.conn.createStatement();
            rs = this.statement.executeUpdate(query);
        } catch (SQLException e) {
            this.errorMessage = e.getMessage();
            return false;
        }
        return (rs > 0);
    }

    /**
     * Se encarga de ejecutar una query, que arrojara un rs (SELECT)
     *
     * @param query query a ejecutar
     * @return ArrayList Lista con los rss obtenidos de la query
     */
    protected ResultSet select(String query) {
        ResultSet rs;
        /* Realizamos la query */
        try {
            this.statement = this.conn.createStatement();
            rs = this.statement.executeQuery(query);
            return rs;

        } catch (SQLException e) {
            this.errorMessage = e.getMessage();

        }
        return null;
    }

    protected String getErrorMessage(){
        return this.errorMessage;
    }

}
