package com.example.serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLiteSerializer implements ISerializer {
	
	private SerializeOrDeserialize serializeOrDeserialize = new SerializeOrDeserialize();

    @Override
    // serialize(要序列化的物件)
    public void serialize(Object obj) {
    	DataWriterUtil dataWriter = new DataWriterUtil();
    	List<String> serializedData = new ArrayList<>();
    	DatabaseUtil dbUtil = DatabaseUtil.getDefault();
    	
    	// 先序列化
    	serializeOrDeserialize.serializeObject(obj, serializedData, "");
    	// 再寫進db:
    	// 1.建立table
    	// 靜態方法裡只能call靜態方法
    	// 但所有方法都能call靜態方法
    	DatabaseUtil.getDefault().createTable();
    	// 2.清空表
    	dbUtil.resetTable();
        // 3. merge並寫進db
    	dataWriter.mergeAndInsertData("header.txt", serializedData, "footer.txt");
    }

    
    @Override
    // deserialize(要反序列化的類別物件)
    public <T> T deserialize(Class<T> clazz) {
    	DataReaderUtil dataReader = new DataReaderUtil();
    	Map<String, String> dataMap;
    	//先讀db
    	dataMap = dataReader.readDataAndSplit();
    	//再反序列化
        return (T) serializeOrDeserialize.deserializeObject(dataMap, clazz, "");
    }
}
