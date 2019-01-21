package resources.db;

import models.connection;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class migrate implements Serializable{
    private static void mirateFresh() throws SQLException, IOException {
        String current=new File("").getCanonicalPath();
        String path=current+"\\"+ "src" +"\\" + "resources" +"\\"+"db"+"\\"+"migrations";

        File migration = new File(path);

        String[] files= migration.list();
        Connection con=connection.getConnection();
        ScriptRunner runner=new ScriptRunner(con,false,false);

        dropdatabase();
        String filesql;
        assert files != null;
        for(String file:files){
            System.out.println(file+"\t\t migrated Successfully");
            filesql=path+"\\"+file;
            //System.out.println(filesql);
            runner.runScript(new BufferedReader(new FileReader(filesql)));
        }
    }
    private static void dropdatabase() throws SQLException{
        Connection con=connection.getConnection();
        ArrayList<String> tables = new ArrayList<>();
        //Drop Current Tables
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        PreparedStatement tableSelect = con.prepareStatement(query);
        ResultSet result=tableSelect.executeQuery();
        while (result.next()){
            String dropQ = "DROP TABLE IF EXISTS "+result.getString("name");
            PreparedStatement dropTable = con.prepareStatement(dropQ);
            dropTable.executeUpdate();
        }
    }
    public static void main(String args[]) throws SQLException, IOException {
        mirateFresh();
    }
}
