import java.sql.*;

public class Database{
    private String url;
    private Connection conn;

    public Database(String url){
        this.url = url;
    }
    private boolean connect(){
        boolean success = true;
        try{
            //Create a connection to the database
            conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        } 
        return success;
    }
    private boolean close(){
       boolean success = true;
        try{
            //Close the connection to the database
            conn.close();   
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        }
        return success; 
    }
    public String selectData(String sql){
        String result = null;
        connect();
        //System.out.println(sql);
        try (Statement stmt  = conn.createStatement()){
            ResultSet rs    = stmt.executeQuery(sql);
            result = jsonify(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
        return result;
    }
    public boolean runQuery(String sql){
        boolean success = true;
        connect();
        try (Statement stmt  = conn.createStatement()){
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        } finally {     
            close();
            
        }return success;
    }
    public static String jsonify(ResultSet rs){
		String result = "";
        try{       
            //Get field names            
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();   

            String field, build = "[";
            // loop through the result set
            while (rs.next()) {
                build += "{";
                for (int i = 1; i <= columnCount; i++) {
                    field = metadata.getColumnName(i);
                    build += "\"" + field + "\":\"" + rs.getString(field) + "\",";
                }
                build = build.substring(0,build.length()-1) + "},";
            }
            result = build.substring(0,build.length()-1) + "]";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
	}
}