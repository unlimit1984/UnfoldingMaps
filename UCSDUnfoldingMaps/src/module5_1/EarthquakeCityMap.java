package module5_1;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setup and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature,map));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	// 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// TODO: Implement this method
		if(lastSelected == null){
			for(Marker marker : markers){
				if(marker.isInside(map, this.mouseX, this.mouseY)){
					lastSelected = (CommonMarker)marker;
					marker.setSelected(true);
					break; 
				}
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		// TODO: Implement this method
		// Hint: You probably want a helper method or two to keep this code
		// from getting too long/disorganized
		
		if(lastClicked != null){//если нажато
			//вернуть все
			lastClicked.setClicked(false);
			if(lastClicked.getClass() == OceanQuakeMarker.class){//удаляем рисование линий к близким городам от водных землетрясений
				((OceanQuakeMarker)lastClicked).getThreatCities().clear();
			}
			lastClicked = null;
			unhideMarkers();//показать все
		}
		else{//если не нажато
			
			if(lastSelected!=null){//если выделен город или землетрясение
				hideMarkers();//спрятать все
				lastClicked = lastSelected;//сделать кликнутым
				lastClicked.setClicked(true);
				lastClicked.setHidden(false);//пусть рисуется
				
				if(lastClicked.getClass() == CityMarker.class){//Если город
					unhideThreatMarkers(lastClicked,quakeMarkers);//показываем город и связанные землетрясения 
				}
				else{//если землетрясение
					unhideThreatMarkers(lastClicked,cityMarkers);//показываем землетрясение и связанные города
					//Дополнительно рисуем линии от водных землетрясений к близким городам
					if(lastClicked.getClass() == OceanQuakeMarker.class){
						double threatDistance = ((EarthquakeMarker)lastClicked).threatCircle();
						((OceanQuakeMarker)lastClicked).getThreatCities().clear();
						for(Marker marker : cityMarkers) {
							if(marker.getDistanceTo(lastClicked.getLocation()) <= threatDistance){
								((OceanQuakeMarker)lastClicked).getThreatCities().add(marker);
							}
						}
						
					}
				}
			}
		}
	}
	
	
	private void unhideThreatMarkers(CommonMarker point, List<Marker> markers) {
		if(point.getClass() == CityMarker.class){//Если город
			for(Marker marker: markers){//перебираем землетрясения
				double threatDistance = ((EarthquakeMarker)marker).threatCircle();
				if(marker.getDistanceTo(point.getLocation()) <= threatDistance){
					marker.setHidden(false);//показываем только опасные близкие землетрясения
				}
			}
		}
		else{//Если землетрясение
			double threatDistance = ((EarthquakeMarker)point).threatCircle();
			for(Marker marker: markers){
				if(marker.getDistanceTo(point.getLocation()) <= threatDistance){
					marker.setHidden(false);//показываем только близкие города
				}
			}
		}
	}

	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	private void hideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(true);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(true);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);
			
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.	
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
/*
Мое:
Hello Christine, Mia and Leo.
For implementation selectMarkerIfHover method I go through markers on the map using for-loop and if the city or quake is in current location I save this marker in variable lastSelected and select this marker by calling the setSelected(true) method. After finding the first marker under cursor I "break" loop to make sure I don't select more than one marker.

To draw a title for any hovered point I implemented showTitle() in CityMarker and EarthquakeMarker classes.

Implementation of mouseClicked was the first sophisticated assignment for me in this module. I realized two additional helper methods:  1)hideMarkers() that similar to unhideMarkers() but works in reverse mode. 2)unhideThreatMarkers() - this method makes hidden OFF showing earthquakes and cities in Threat Mode. And I was enforced to code for-loop operator twice: one for quakes and the second for cities, because the user can check two option: city or quake.

I tried to solve option task, but I'm not sure that it works correctly. I'v made a second constructor for OceanQuakeMarker for getting parameter UnfoldingMap and added a list of CityMarker as a field in the OceanQuakeMarker class. Also I modified some code in mouseClicked() method to set the city list in OceanQuakeMarker instance in Threat mode if user clicks on Ocean quake marker. Finally I added drawing lines in drawEarthquake() method in OceanQuakeMarker class. Before I check if the marker getClicked(). Also we need to clear and filling city list in mouseClicked() method because we have to draw lines only when the Ocean Quake has cities in threated zone.

Thank you very much for reading my bad English.

Их описание:
Here's how our code handled mouse clicks:

When the user clicks the mouse, the mouseClicked code in EarthquakeCityMap is called by Java. This method first checks the lastClicked variable. If it is null, meaning a city is already shown as "clicked", it sets lastClicked to null and unhides all the cities and earthquakes.

Otherwise, it relies on two helper methods: checkEarthquakesForClick and checkCitiesForClick.

checkEarthquakesForClick first checks lastClicked, and aborts if it is not null (just in case). Then it loops through all the earthquakes to see if one has been clicked on. If it finds one, it loops through all of the earthquake markers and sets all but the clicked earthquake to hidden. Then it loops through the city markers and sets all of the city markers outside of the clicked earthquake's threat circle to be hidden. It then returns so that it does not check anymore earthquakes.

checkCitiesForClick first checks lastClicked, and aborts if it is not null (which could mean an earthquake has already been found as clicked). Then it loops through all the cities to see if one has been clicked on. If it finds one, it loops through all of the city markers and sets all but the clicked city to hidden. Then it loops through the earthquake markers and sets all of the earthquake markers for which the city is outside of the threat circle to be hidden. It then returns so that it does not check anymore cities.




 */
  