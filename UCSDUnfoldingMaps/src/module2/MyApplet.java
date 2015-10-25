package module2;

import processing.core.PApplet;
import processing.core.PImage;

public class MyApplet extends PApplet{

	private String URL = "C:/ALL/workspace-coursera/UCSDUnfoldingMaps/data/palmTrees.jpg";
	//private String URL = "http://cseweb.ucsd.edu/~minnes/palmTrees.jpg";
	private PImage backgroundImg;
	@Override
	public void setup() {
		size(400,400);
		backgroundImg = loadImage(URL);
	}
	
	@Override
	public void draw() {
		backgroundImg.resize(0, height);
		image(backgroundImg,0,0);
		fill(255,209,0);
		ellipse(width/4, height/5, width/5, height/5);
	}

	
}
