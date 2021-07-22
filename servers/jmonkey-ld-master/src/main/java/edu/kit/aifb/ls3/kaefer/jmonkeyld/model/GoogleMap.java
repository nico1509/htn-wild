package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

import java.net.URI;
import java.net.URISyntaxException;

import com.jme3.scene.shape.Quad;

public class GoogleMap extends WebMap {

	public GoogleMap(double latitude, double longitude, int mapZoomLevel, int mapDimensionInPixel, Quad q) {
		super(latitude, longitude, mapZoomLevel, mapDimensionInPixel, q);
	}

	public GoogleMap(Quad q) {
		super(q);
	}

	public URI getMapURI() {
		try {
			return new URI("http://maps.googleapis.com/maps/api/"
					+ "staticmap?center=" + _mapLat + "," + _mapLong + "&zoom="
					+ _mapZoomLevel + "&size=" + _mapDimensionInPixel + "x"
					+ _mapDimensionInPixel);
		} catch (URISyntaxException e) {
			// should be safe.
			e.printStackTrace();
			return null;
		}
	}

	public String[] getMapURIinPiecesForJmonkeyAssetManager() {
		return new String[] {
				"http://maps.googleapis.com/maps/api/",
				"staticmap?center=" + _mapLat + "," + _mapLong + "&zoom="
						+ _mapZoomLevel + "&size=" + _mapDimensionInPixel + "x"
						+ _mapDimensionInPixel + "#.png" };
	}

}
