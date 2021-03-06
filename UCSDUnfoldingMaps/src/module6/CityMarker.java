package module6;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	private List<Marker> threatQuakes = new ArrayList<>();
	public List<Marker> getThreatQuakes() {
		return threatQuakes;
	}


	public CityMarker(Location location) {
		super(location);
	}	
	public CityMarker(Location location, PImage img) {
		super(location,img);
	}	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}
	public CityMarker(Feature city, PImage img) {
		super(((PointFeature)city).getLocation(), city.getProperties(), img);
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)

	}
	
	
	// pg is the graphics object on which you call the graphics
	// methods.  e.g. pg.fill(255, 0, 0) will set the color to red
	// x and y are the center of the object to draw. 
	// They will be used to calculate the coordinates to pass
	// into any shape drawing methods.  
	// e.g. pg.rect(x, y, 10, 10) will draw a 10x10 square
	// whose upper left corner is at position x, y
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		//System.out.println("Drawing a city");
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		if(img!=null){
			//pg.image(img, x, y);
			pg.image(img, x-img.width/4,y-img.height/4,img.width/2,img.height/2);
		}
		else{
			pg.fill(150, 30, 30);
			pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		}
		
		// Restore previous drawing style
		pg.popStyle();
		
		if(clicked){			
			pg.fill(255, 250, 240);
			pg.rect(5, 5, 400, 50);
			pg.fill(0);
			pg.textAlign(PConstants.LEFT, PConstants.CENTER);
			pg.textSize(12);
			pg.text("Count Quakes: "+threatQuakes.size(), 10, 15);
			pg.text("Average Magnitude: "+getMiddleMagnitude(), 10, 30);
			pg.text("Last: "+getRecentQuake(), 10, 45);



		}
	}

	private String getMiddleMagnitude(){
		float middleMagnitude = 0;			
		
		for(Marker marker: threatQuakes){
			middleMagnitude+=((EarthquakeMarker)marker).getMagnitude();
		}
		if(middleMagnitude>0){
			middleMagnitude/=threatQuakes.size();
		}
		DecimalFormat f = new DecimalFormat("#0.0");

		return f.format(middleMagnitude);
	}
	
	private String getRecentQuake() {
		String recent_quake=null;
		Map<String,String> map = new HashMap<>();
		for(Marker marker: threatQuakes){
			String age = getStringProperty("age");
			if("Past Hour".equals(age)){
				map.put("Past Hour", ((EarthquakeMarker)marker).getTitle());
				break;
			}
			else if("Past Day".equals(age)){
				map.put("Past Day", ((EarthquakeMarker)marker).getTitle());
			}
		}
		if(!map.isEmpty()){
			recent_quake = map.get("Past Hour");
			if(recent_quake == null){
				recent_quake = map.get("Past Day");
			}
		}
		if(recent_quake == null && !threatQuakes.isEmpty()){
			recent_quake = ((EarthquakeMarker)threatQuakes.get(0)).getTitle();
		}
		else{
			recent_quake = "NONE";
		}
		return recent_quake;
	}

	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getCity() + " " + getCountry() + " ";
		String pop = "Pop: " + getPopulation() + " Million";
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(pop)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(pop, x+3, y - TRI_SIZE -18);
		
		pg.popStyle();
	}
	
	private String getCity()
	{
		return getStringProperty("name");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	private float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}
