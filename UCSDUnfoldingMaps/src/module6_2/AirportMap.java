package module6_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

public class AirportMap extends PApplet {
	private static final long serialVersionUID = 1L;
	
	private String cityFile = "city-data.json";
	
	private UnfoldingMap map;

	private List<Marker> cityMarkers;
	
	private CommonMarker lastSelected;
	
	public void setup() {
		
		size(800,600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500,new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
		
		map.addMarkers(cityMarkers);
	}
	
	public void draw() {
		background(0);
		map.draw();
		
	}
	
	@Override
	public void mouseMoved() {
		if(lastSelected==null){//сделать всплывающее окно
			selectMarkerIfHover(cityMarkers);	
		}
		else{//убрать всплывающее окно
			lastSelected.setSelected(false);
			lastSelected = null;
		}
//		if (lastSelected != null) {
//			lastSelected.setSelected(false);
//			lastSelected = null;
//		}
//		selectMarkerIfHover(cityMarkers);
		
	}

	private void selectMarkerIfHover(List<Marker> markers) {
		if (lastSelected != null) {
			return;
		}
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

}
