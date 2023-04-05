package com.meowing.loud.arms.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

    private static String url = "jdbc:mysql://gz-cynosdbmysql-grp-mit0vlwz.sql.tencentcdb.com:21166/music?characterEncoding=utf-8&useSSL=false";

    private static String user = "xuyang";// 用户名

    private static String password = "xuyang1119";// 密码

    public static Connection getConn(){

        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类

            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection(url, user, password);

        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection){
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
