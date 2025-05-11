package com.example.serializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DataWriterUtil {

    public void mergeAndInsertData(String headerFile, List<String> iniFile, String footerFile) {
        
    	FileUtil read = new FileUtil();
    	// StringBuilder可變的字串容器，用於高效地拼接字串
    	StringBuilder mergedData = new StringBuilder();
    	
    	// 讀取註解並轉arraylist
        List<String> headerLines = read.readFile(headerFile, 0);
        List<String> footerLines = read.readFile(footerFile, 0);

        // 合併
        for (String line : headerLines) {
        	// 將 line（一個字串）追加到 mergedData 的末尾
        	// 繼續用 StringBuilder 的 append 方法，將換行符 "\n" 追加到 mergedData 的末尾
            mergedData.append(line).append("\n");
        }
        for (String line : iniFile) {
            mergedData.append(line).append("\n");
        }
        for (String line : footerLines) {
            mergedData.append(line).append("\n");
        }

        // 合併後寫入
        insertData(mergedData.toString());
    }

    private void insertData(String data) {
    	// 將數據插入到 serialized_data 表中的 data 列
    	// VALUES(?) 中的 ? 將在後面使用 PreparedStatement 來設置實際的值
        String sql = "INSERT INTO serialized_data(data) VALUES(?)";
        try (Connection conn = DatabaseUtil.getDefault().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	// 將 data 字符串設置為 SQL 語句中的第一個參數(即？)
            pstmt.setString(1, data);
            // 執行 SQL 更新操作（插入數據）
            pstmt.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
