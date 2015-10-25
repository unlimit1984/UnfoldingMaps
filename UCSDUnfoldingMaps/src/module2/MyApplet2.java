package module2;

import processing.core.PApplet;
import processing.core.PImage;

public class MyApplet2 extends PApplet{

	private String URL = "C:/ALL/workspace-coursera/UCSDUnfoldingMaps/data/palmTrees.jpg";
	//private String URL = "http://cseweb.ucsd.edu/~minnes/palmTrees.jpg";
	private PImage img;
	@Override
	public void setup() {
		size(400,400);
		background(255);
		stroke(0);
		
		img = loadImage(URL, "jpg");
		img.resize(0, height);
		image(img, 0, 0);
	}
	
	@Override
	public void draw() {
		int[] color = sunColorSec(second());
		fill(color[0],color[1], color[2]);
		ellipse(width/4, height/5, width/4, height/5);
	}

	private int[] sunColorSec(float second) {
		int[] rgb = new int[3];
		
		float diffFrom30 = Math.abs(30-second);
		float ratio = diffFrom30/30;
		rgb[0] = (int)(255*ratio);
		rgb[1] = (int)(255*ratio);
		rgb[2] = 0;
		
		System.out.println("R:" + rgb[0] + " G:" + rgb[1] +" B:" + rgb[0]);
		
		return rgb;
	}
}
