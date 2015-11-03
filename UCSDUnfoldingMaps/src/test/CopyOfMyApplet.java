package test;

import processing.core.PApplet;
import processing.core.PGraphics;

public class CopyOfMyApplet extends PApplet {
	
	float rotateAmount;
	int boxColorR = 255;
	int boxColorG = 255;
	int boxColorB = 255;
	int boxW = 480;

	//create a buffer to draw boxes to
	PGraphics buffer;

	public void setup () {
	  size(640, 480);


	  buffer = createGraphics(640, 480);
	}

	void drawText() {
	  //translate(width/2,height/2);
	  textAlign(LEFT, CENTER);
	  fill(255, 255, 255);
	  textSize(32);
	  text("RED: " + boxColorR,width/2,height/2);
	  text("GREEN: " + boxColorG,width/2,height/2+30);
	  text("BLUE: " + boxColorB,width/2,height/2+60);
	  text("Box Width: " + boxW,width/2,height/2+90);
	}

	//draw boxes to buffer
	void drawBox() {

	  buffer.beginDraw();
	  buffer.rectMode(CENTER);

	  buffer.translate(width/2, height/2);
	  rotateAmount += 12;
	  if (boxColorR <= 0) {
	    boxColorG--;
	  }
	  if (boxColorG <= 0) {
	    boxColorB--;
	  }
	  boxColorR--;
	  boxW--;
	  rotateAmount += .05;
	  buffer.rotate(rotateAmount);
	  buffer.fill(boxColorR, boxColorG, boxColorB);
	  buffer.rect(0, 0, boxW, boxW);
	  buffer.resetMatrix();

	  buffer.endDraw();
	}



	public void draw() {




	  //draw the boxes to the buffer
	  drawBox();
	  //draw the buffer to the screen
	  image(buffer, 0, 0);

	  //draw the text on top of the buffer
	  drawText();
	}
	
}
