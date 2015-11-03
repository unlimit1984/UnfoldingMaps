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
	List<CityMarker> threatCityMarkers = new ArrayList<>();
	
	public void setThreatCityMarkers(List<CityMarker> threatCityMarkers) {
		this.threatCityMarkers = threatCityMarkers;
	}

	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	
	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
		//pg.line()
		if(this.getClicked()){
			ScreenPosition sp = new ScreenPosition(x, y);// this.getScreenPosition(pg.get);
			//this.
		}
	}
	

	

}
