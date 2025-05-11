package com.example.serializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
	public static void main(String[] args) throws Exception {
		Scene2 scene = new Scene2();
		scene.background.rect.setRect(1, 2, 3, 4);
		scene.background.strokeWidth = 5;
		scene.background.strokeColor = 6;
		scene.background.backgroundColor = 888;
		scene.background.image = "8";
		scene.rects = new RECT[] { new RECT(), new RECT() };
		scene.rects[0].setRect(11, 12, 13, 14);
		scene.rects[1].setRect(21, 22, 23, 24);
		scene.dummy = true;
		scene.shapeType = ShapeType.TRIANGLE;
		
		Scene2 deserializedScene = null;
		Scene2 deserializedScene2 = null;
		
		ISerializer iniSerializer = ISerializer.createSerializer("ini");
		
		if(iniSerializer == null) {
			System.exit(1);
		}
		
		ISerializer SQLiteSerializer = ISerializer.createSerializer("sqlite");
		
		if(SQLiteSerializer == null) {
			System.exit(1);
		}
		
		iniSerializer.serialize(scene);
		deserializedScene = iniSerializer.deserialize(Scene2.class);
		
		SQLiteSerializer.serialize(scene);
		deserializedScene2 = SQLiteSerializer.deserialize(Scene2.class);
		
		test(deserializedScene, "ini:");
		
		System.out.println("=================================================\n");
		
		//查看db內容
        String sql = "SELECT * FROM serialized_data";
        try (Connection conn = DatabaseUtil.getDefault().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String data = rs.getString("data");
                System.out.println("table:\n");
                System.out.println("ID: " + id);
                System.out.println("Data: ");
                System.out.println(data);
                System.out.println("-------------\n");
            }
        }
        
        test(deserializedScene2, "sqlite:");
	}
	public static void test(Scene2 obj ,String title) {
		System.out.println(title + '\n');
		System.out.println("Deserialized Scene Background:");
		System.out.println("dummy: " + obj.dummy);
		System.out.println("RECT: left=" + obj.background.rect.left + ", top="
				+ obj.background.rect.top + ", width=" + obj.background.rect.width
				+ ", height=" + obj.background.rect.height);
		System.out.println("Stroke Width: " + obj.background.strokeWidth);
		System.out.println("Stroke Color: " + obj.background.strokeColor);
		System.out.println("Background Color: " + obj.background.backgroundColor);
		System.out.println("Image: " + obj.background.image);
		System.out.println("i: " + obj.i);
		System.out.println("shapeLength: " + obj.shapeLength);
		
		System.out.println("rectlength: " + obj.rects.length);
		System.out.println("Shape Type: " + obj.shapeType);
		System.out.println("\nDeserialized RECTs:");

		RECT rect;
		for (int i = 0; i < obj.rects.length; i++) {
			rect = obj.rects[i];
			System.out.println("RECT" + i + ": left=" + rect.left + ", top=" + rect.top + ", width=" + rect.width
					+ ", height=" + rect.height);
		}
		System.out.println();
	}
}
