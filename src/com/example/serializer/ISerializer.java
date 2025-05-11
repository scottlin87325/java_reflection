package com.example.serializer;

public interface ISerializer {
	
	public static ISerializer createSerializer(String type) {
		if(type.equals("ini")) {
			return new IniSerializer();
		}else if(type.equals("sqlite")) {
			return new SQLiteSerializer();
		}else {
		 	System.out.println("Error : createSerializer mod does not exist! (should be ini or sqlite)");
		 	return null;
		}
	}
	
	// serialize(要序列化的物件)
	void serialize(Object obj);

	// deserialize(要反序列化的類別物件)
	<T> T deserialize(Class<T> clazz);
}