package module6;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;
import processing.core.PImage;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	public OceanQuakeMarker(PointFeature quake, PImage img) {
		super(quake, img);
		isOnLand = false;
	}
	

	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		//IMPLEMENT: drawing centered square for Ocean earthquakes
		// DO NOT set the fill color.  That will be set in the EarthquakeMarker
		// class to indicate the depth of the earthquake.
		// Simply draw a centered square.
		// HINT: Notice the radius variable in the EarthquakeMarker class
		// and how it is set in the EarthquakeMarker constructor
		if(img!=null){
			pg.image(img, x-img.width/4,y-img.height/4,img.width/2,img.height/2);
		}
		else{
			pg.rect(x-radius, y-radius, 2*radius, 2*radius);
		}

	}
	

	

}
