package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

import java.net.URI;
import java.net.URISyntaxException;

import com.jme3.scene.shape.Quad;

public class OpenStreetMap extends WebMap {

	public OpenStreetMap(double latitude, double longitude, int mapZoomLevel, int mapDimensionInPixel, Quad q) {
		super(latitude, longitude, mapZoomLevel, mapDimensionInPixel, q);
	}

	public OpenStreetMap(Quad q) {
		super(q);
	}

	public URI getMapURI() {
		try {
			return new URI(
					"https://staticmap.openstreetmap.de/"
					+ "staticmap.php"
					+ "?center=" + _mapLat + "," + _mapLong + "&zoom="
					+ _mapZoomLevel + "&size=" + _mapDimensionInPixel + "x"
					+ _mapDimensionInPixel
					+ "&maptype=mapnik"
					);
		} catch (URISyntaxException e) {
			// should be safe.
			e.printStackTrace();
			return null;
		}
	}

	public String[] getMapURIinPiecesForJmonkeyAssetManager() {
		return new String[] {
				"https://staticmap.openstreetmap.de/",
				"staticmap.php"
						+"?center=" + _mapLat + "," + _mapLong + "&zoom="
						+ _mapZoomLevel + "&size=" + _mapDimensionInPixel + "x"
						+ _mapDimensionInPixel 
						+ "&maptype=mapnik"
						+ "#.png" };
	}

}
