package com.example.serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IniSerializer implements ISerializer {
	
	private SerializeOrDeserialize serializeOrDeserialize = new SerializeOrDeserialize();
	
	@Override
	// serialize(要序列化的物件)
	public void serialize(Object obj) {
		FileWriterUtil fileWriter =new FileWriterUtil();
		List<String> serializedData = new ArrayList<>();
		//先序列化
		serializeOrDeserialize.serializeObject(obj, serializedData, "");
		//再寫檔案
		fileWriter.mergeFiles("header.txt", serializedData, "footer.txt", "output.ini");
	}

	@Override
	// <T>泛形宣告(表示泛形方法),T類型參數(代表任何類型),Class<T> clazz(類型T的類別物件)
	// deserialize(序列化結果,要反序列化的類別物件)
	public <T> T deserialize(Class<T> clazz) {
		FileReaderUtil read = new FileReaderUtil();
		Map<String, String> dataMap;
		//先讀檔案
		dataMap = read.readFileAndSplit("output.ini");
		//再反序列化
		return (T) serializeOrDeserialize.deserializeObject(dataMap, clazz, "");
	}
}
