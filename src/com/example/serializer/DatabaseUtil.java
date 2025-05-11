package com.example.serializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private String URL = "jdbc:sqlite:sample.db";
    private static DatabaseUtil instance;
    private static Object singletonLock = new Object();
    public static synchronized DatabaseUtil getDefault() {
    	synchronized(singletonLock) {
	    	if(instance == null) {
	    		instance = new DatabaseUtil(null);
	    	}
    	}
    	return instance;
    }
    public DatabaseUtil(String filename) {
    	// 不是null/空白
    	if(filename != null && !filename.isBlank()) {
    		this.URL = "jdbc:sqlite:"+filename;
    	}
    }
    
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS serialized_data (" +
                     //AUTOINCREMENT : 自動生成唯一的數字作為某一列的值
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "data TEXT NOT NULL)";
        try (Connection conn = connect();
        	 //執行sql查詢和更新
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetTable() {
        String clearTableSql = "DELETE FROM serialized_data";
        //sqlite_sequence : 儲存autoincrement的表
        String resetIdSql = "DELETE FROM sqlite_sequence WHERE name='serialized_data'";
        try (Connection conn = DatabaseUtil.getDefault().connect();
             PreparedStatement clearStmt = conn.prepareStatement(clearTableSql);
             PreparedStatement resetStmt = conn.prepareStatement(resetIdSql)) {
            clearStmt.executeUpdate();
            resetStmt.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
