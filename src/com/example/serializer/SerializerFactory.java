package com.example.serializer;

public class SerializerFactory {
	//Serializer工廠
	//成功：return ISerializer
	//失敗：return null
	public ISerializer createSerializer(String type) {
		if(type.equals("ini")) {
			return new IniSerializer();
		}else if(type.equals("sqlite")) {
			return new SQLiteSerializer();
		}else {
		 	System.out.println("Wrong model! (should be ini or sqlite)");
		 	return null;
		}
	}
}
