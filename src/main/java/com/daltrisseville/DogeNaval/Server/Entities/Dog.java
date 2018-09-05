package com.daltrisseville.DogeNaval.Server.Entities;

public class Dog {
	private int length;
	private int xStart;
	private int yStart;
	private DogDirection direction;
	
	public Dog(int length,int x, int y, DogDirection direction){
		
		this.length=length;
		this.xStart=x;
		this.yStart=y;
		this.direction=direction;
	}

	public DogDirection getDirection() {
		return direction;
	}

	public void setDirection(DogDirection direction) {
		this.direction = direction;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getxStart() {
		return xStart;
	}

	public void setxStart(int xStart) {
		this.xStart = xStart;
	}

	public int getyStart() {
		return yStart;
	}

	public void setyStart(int yStart) {
		this.yStart = yStart;
	}

	
}
