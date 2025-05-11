package com.example.serializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataReaderUtil {
    public Map<String, String> readDataAndSplit() {
        String sql = "SELECT data FROM serialized_data";
        Map<String, String> dataMap = new HashMap<>();
        try (Connection conn = DatabaseUtil.getDefault().connect();
        	 // 宣告一個預編譯語句物件 pstmt
        	 // 使用連接物件 conn 和 SQL 查詢語句 sql 來創建 PreparedStatement 物件
        	 // PreparedStatement 用於執行預編譯的 SQL 語句	 
             PreparedStatement pstmt = conn.prepareStatement(sql);
        	 // ResultSet:結果集
        	 // pstmt.executeQuery()：執行 SQL 查詢並返回結果集 ResultSet
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
            	// 在寫入時用\n分隔字串,讀出來要把它分開
                String data = rs.getString("data");
                String[] dataLines = data.split("\n");
                for (String line : dataLines) {
                    if (!line.startsWith(";") && !line.startsWith("#") && !line.isEmpty()) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            dataMap.put(key, value);
                        }
                    }
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
        return dataMap;
    }
}
