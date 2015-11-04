package module5;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	UnfoldingMap map = null;
	List<Marker> threatCities = new ArrayList<>();
	
	public List<Marker> getThreatCities() {
		return threatCities;
	}

	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	
	public OceanQuakeMarker(PointFeature feature, UnfoldingMap map) {
		this(feature);
		this.map = map;
	}

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
		if(this.getClicked()){
			for(Marker city: threatCities){
				float x2 = ((CityMarker)city).getScreenPosition(map).x;
				float y2 = ((CityMarker)city).getScreenPosition(map).y;
				pg.line(x, y, x2, y2);
			}
		}
	}
	

	

}
