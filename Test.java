import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.Map;
import java.io.IOException;

import java.util.Map;
import java.sql.*;

//javac -cp sqlite-jdbc-3.23.1.jar; Test.java
public class Test{
    public static void main(String[] args) throws IOException{
        Database db = new Database("jdbc:sqlite:pData.db");
        
        int port = 8500;
        HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
        
        String htmlFile = Input.readFile("AllPokemon.html");
        server.createContext("/AllPokemon",new RouteHandler(htmlFile));

        String htmlFile2 = Input.readFile("Types.html");
        server.createContext("/Types",new RouteHandler(htmlFile2));

        String htmlFile3 = Input.readFile("TypesLite.html");
        server.createContext("/TypesLite",new RouteHandler(htmlFile3));

        String htmlFile4 = Input.readFile("Search.html");
        server.createContext("/Search",new RouteHandler(htmlFile4));

        String htmlFile5 = Input.readFile("HomepageV2.html");
        server.createContext("/HomepageV2",new RouteHandler(htmlFile5));

        String htmlFile6 = Input.readFile("SearchLite.html");
        server.createContext("/SearchLite",new RouteHandler(htmlFile6));

        String sql = "SELECT * FROM Gen1;";
        server.createContext("/getID", new RouteHandler(db,sql));

        String sql2 = "SELECT * FROM Gen1Moves;";
        server.createContext("/getMoves", new RouteHandler(db,sql2));

        String selectAll = "SELECT * FROM Gen1 INNER JOIN Gen1Moves ON Gen1.Type=Gen1Moves.MoveType OR Gen1.SecondType = Gen1Moves.MoveType WHERE ";

        server.createContext("/getType", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> parameters = RouteHandler.parseParameters("get",exchange);
                   
					String id = parameters.get("id").toString(); 
                    //.get("id") refers to ?id=, ?id= requires quotes (?id="Name") or else id (variable) below will be recognized as a column (Name) instead of "Name" (Ex. WHERE Gen1.Type=Name   Instead Of   Gen1.Type="Name").
                    //Can also add quotes in here: after = (Ex. "... Gen1.Type='"+ id + "'")
                    String query = selectAll + "Gen1.Type=" + id + " OR Gen1.SecondType=" + id + ";";
                    String response = db.selectData(query);
					
                    RouteHandler.send(response,exchange);
            }
        });

        server.createContext("/getNames", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> parameters = RouteHandler.parseParameters("get",exchange);
                   
					String id = parameters.get("id").toString(); 
                    String query = selectAll + "Gen1.Name LIKE '%" + id + "%';";
                    String response = db.selectData(query);
					
                    RouteHandler.send(response,exchange);
            }
        });

        
        server.start();
        
        System.out.println("Server is listening on port "+port);
    }
}