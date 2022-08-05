package com.app.misc.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbService {

    private static ResultSet RESULT;

    private static void start() {
        System.out.println("START");
        //String url = "jdbc:jtds:sqlserver://GCUDWH.gcu.edu:1433;" +
        String url = "jdbc:jtds:sqlserver://qdwhdbps2001.gcu.edu:1433/LCU4;integratedSecurity=true;domain=GCU" +
                "databaseName=Campusvue;" +
                "integratedSecurity=true;" +
                "domain=GCU;" +
                "user=svc_LopesCloud;" +
                "password=02vx94819k7A94i65724xuZG;" +
                "applicationIntent=ReadOnly";
        //String url = "jdbc:mysql://localhost:3306/classroom?user=root&password=root";
        //String query = "select  sg.AdClassSchedID,sg.AdGradeLetterCode,sg.DateGradePosted from adenrollsched sg ";
        //String query = "select * from login";

        String query = "select  * from lcs_tbl_forum";
        //" inner join syStudent s on s.SyStudentId = sg.SyStudentId " +
        //"where AdClassSchedID  in  ('605978','605998','606006','610462','606888','607028','607098','607026','606982','605942','609510','610730','610458','611846')" +
        //" and sg.AdGradeLetterCode <> '' and sg.DateGradePosted IS NOT NULL ";

        ResultSet rs = getData(query,url);
        try {
            while (rs.next()) {
                System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("END");
    }

    public static ResultSet  getData(String query,String url) {
        Connection CON = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //Class.forName("com.mysql.cj.jdbc.Driver");
            CON = DriverManager.getConnection(url);
            RESULT = CON.createStatement().executeQuery(query);
            //CON.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RESULT;
    }

    public static void main(String[] args) {start();}


}
