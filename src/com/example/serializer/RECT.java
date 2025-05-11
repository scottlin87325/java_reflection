package com.example.serializer;

public class RECT {
	public int left;
	public int top;
	public int width;
	public int height;

	public void setPos(int x, int y) {
		left = x;
		top = y;
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
	}

	public void setRect(int x, int y, int w, int h) {
		setPos(x, y);
		setSize(w, h);
	}
}
