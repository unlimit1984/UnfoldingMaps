package test;

import processing.core.PApplet;

public class MyApplet extends PApplet {
	
	public void setup() {
		size(400, 400);
		background(200,200,200);
	}

	public void draw(){
		drawButtons();
	}
	
	public void drawButtons(){
		fill(255,255,255);
		rect(100,100,25,25);

		fill(100,100,100);
		rect(100,150,25,25);
		
		fill(255);
		rect(100,100,50,20);
		
		fill(0);
		text("Text",105,115);
		
		noStroke();
		
	}

	public void mouseReleased() {
		if(100<=mouseX && mouseX<=125 && 100<=mouseY && mouseY<=125){
			background(255,255,255);
		}
		else if(100<=mouseX && mouseX<=125 && 150<=mouseY && mouseY<=175){
			background(100,100,100);
		}
	}
	
	
	
}
