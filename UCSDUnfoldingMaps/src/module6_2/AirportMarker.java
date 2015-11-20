package module6_2;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.pushStyle();
		
		pg.fill(150);
		pg.ellipse(x, y, 15, 15);
		
		pg.popStyle();
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		String title = getAirportCode()+"-"+getAirportName();
		pg.pushStyle();
		
//		pg.fill(255, 255, 255);
//		pg.textSize(12);
//		pg.rectMode(PConstants.CORNER);
//		pg.rect(x, y, pg.textWidth(title), 30);
//		pg.fill(0, 0, 0);
//		pg.textAlign(PConstants.LEFT, PConstants.TOP);
//		pg.text(title, x, y);
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);
		
		pg.popStyle();

		// show routes
		
		
		
	}
	private String getAirportCode(){
		return getStringProperty("code");
	}
	private String getAirportName(){
		return getStringProperty("name");
	}
}
