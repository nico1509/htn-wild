package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

import java.net.URI;
import java.net.URISyntaxException;

import com.jme3.scene.shape.Quad;

public class WikimediaMap extends WebMap {

	public WikimediaMap(double latitude, double longitude, int mapZoomLevel, int mapDimensionInPixel, Quad q) {
		super(latitude, longitude, mapZoomLevel, mapDimensionInPixel, q);
	}

	public WikimediaMap(Quad q) {
		super(q);
	}

	public URI getMapURI() {
		try {
			return new URI(
					"https://maps.wikimedia.org/img/"
					+ "osm-intl,"
					+ _mapZoomLevel
					+ ","
					+ _mapLat
					+ ","
					+ _mapLong
					+ ","
					+ _mapDimensionInPixel
					+ "x"
					+ _mapDimensionInPixel
					+ ".png"
					);
		} catch (URISyntaxException e) {
			// should be safe.
			e.printStackTrace();
			return null;
		}
	}

	public String[] getMapURIinPiecesForJmonkeyAssetManager() {
		return new String[] {
				"https://maps.wikimedia.org/img/",
						"osm-intl,"
						+ _mapZoomLevel
						+ ","
						+ _mapLat
						+ ","
						+ _mapLong
						+ ","
						+ _mapDimensionInPixel
						+ "x"
						+ _mapDimensionInPixel
						+ ".png"
		};
	}

}
