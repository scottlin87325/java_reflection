package com.example.serializer;

public class Scene {
	public RectangleShape background = new RectangleShape(); // scene background
	public int i = 0;
	public Integer shapeLength;
	@ArrayDesc(lengthVarName = "rectLength")
	public RECT[] rects; // shapes on screen
	//**
	public ShapeType shapeType;
}
