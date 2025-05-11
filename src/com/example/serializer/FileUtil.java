package com.example.serializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
	//flag == 0 ：純讀檔
	//flag == 1 : 讀檔＋過濾註解
    public List<String> readFile(String fileName, int flag) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        	String line;
            while ((line = br.readLine()) != null) {
            	if(flag == 0) {
            		lines.add(line);
            	}else if(flag == 1){
            		if (!line.startsWith(";") && !line.startsWith("#") && !line.isEmpty()) {
    					lines.add(line);
    				}
            	}else {
            		System.out.println("readFile mode does not exist,"
            				+ "please check your readFile parament setting!");
            		System.out.println("(hint:0,1)");
            		System.exit(1);
            	}
            	
            }
        } catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
        return lines;
    }
    
    public Map<String, String> stringSplit(List<String> serializedData){
		Map<String, String> dataMap = new HashMap<String, String>();
		// 遍歷ini讀出來的結果(一次一行)(List<String>)
		for (String line : serializedData) {
			// 用"="分隔字串
			String[] parts = line.split("=", 2);
			// 如果字串有"="
			if (parts.length == 2) {
				// 空白去頭去尾,放進map裡
				String key = parts[0].trim();
				String value = parts[1].trim();
				dataMap.put(key, value);
			}
		}
		// 回傳所有ini的內容
		return dataMap;
    }   
}
