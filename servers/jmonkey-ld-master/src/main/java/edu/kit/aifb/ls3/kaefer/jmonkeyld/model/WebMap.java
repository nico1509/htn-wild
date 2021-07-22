package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

import java.net.URI;

import com.jme3.math.Vector2f;
import com.jme3.scene.shape.Quad;

public abstract class WebMap extends SceneGraphObject {

	Quad _q;

	public static final int DEFAULT_MAP_DIMENSION_IN_PIXEL = 512;
	public static final int DEFAULT_ZOOM_LEVEL = 14;

	protected int _mapZoomLevel = DEFAULT_ZOOM_LEVEL;
	protected int _mapDimensionInPixel = DEFAULT_MAP_DIMENSION_IN_PIXEL;

	protected double _mapLong;

	protected double _mapLat;

	private FreshnessInDisplay _state;

	public WebMap(Quad q) {
		_q = q;
		setMapCenterCoordinates(WellKnownCoordinates.London);
	}

	public WebMap(double latitude, double longitude, int mapZoomLevel,
			int mapDimensionInPixel, Quad q) {
		_mapZoomLevel = mapZoomLevel;
		_mapDimensionInPixel = mapDimensionInPixel;
		_mapLat = latitude;
		_mapLong = longitude;
		_q = q;
		_state = FreshnessInDisplay.refreshed;
	}

	public void setMapCenterCoordinates(double latitude, double longitude) {
		_mapLat = latitude;
		_mapLong = longitude;
		_state = FreshnessInDisplay.toBeRefreshed;
	}

	public double getLatitude() {
		return _mapLat;
	}

	public double getLongitude() {
		return _mapLong;
	}

	public FreshnessInDisplay getFreshnessInDisplayInternal() {
		return _state;
	}

	public void setFreshnessInDisplay(FreshnessInDisplay state) {
		_state = state;
	}

	public State getState() {
		return State.displayed;
	}

	public void setState(State state) {
		throw new IllegalStateException("State of the map cannot get changed. "
				+ "It is always displayed.");
	}

	public void setMapCenterCoordinates(Coordinates coordinates) {
		setMapCenterCoordinates(coordinates.getLatitude(),
				coordinates.getLongitude());
	}

	public void setMapZoomLevel(int mapZoomLevel) {
		_mapZoomLevel = mapZoomLevel;
	}

	public void setMapDimensionInPixel(int mapDimensionInPixel) {
		_mapDimensionInPixel = mapDimensionInPixel;
	}

	public abstract URI getMapURI();

	public abstract String[] getMapURIinPiecesForJmonkeyAssetManager();

	public static interface Coordinates {
		public double getLatitude();

		public double getLongitude();
	}

	static enum WellKnownCoordinates implements Coordinates {

		Karlsruhe(49.009, 8.409), Munich(48.138074, 11.576511), London(
				51.5143725, -0.1198652);

		private double _long;
		private double _lat;

		private WellKnownCoordinates(double latitude, double longitude) {
			_lat = latitude;
			_long = longitude;
		}

		@Override
		public double getLatitude() {
			return _lat;
		}

		@Override
		public double getLongitude() {
			return _long;
		}
	}

	public Vector2f getMapCoordsForPOI(POI poi) {
		return new Vector2f(lonToX(poi.getLongitude(), _mapZoomLevel), latToY(
				poi.getLatitude(), _mapZoomLevel)).mult(
				(float) _q.getWidth() / (float) _mapDimensionInPixel).subtract(
				new Vector2f(lonToX(_mapLong, _mapZoomLevel), latToY(_mapLat,
						_mapZoomLevel)).mult((float) _q.getWidth()
						/ (float) _mapDimensionInPixel));
	}

	public static int lonToX(double lon, int zoom) {
		// https://trac.openstreetmap.org/browser/applications/mobile/FreemapMobile/src/GProjection.java?rev=8216
		return (int) (0.5 + Math
				.floor((Math.pow(2, zoom + 8) * (lon + 180)) / 360));
	}

	public static int latToY(double lat, int zoom) {
		// https://trac.openstreetmap.org/browser/applications/mobile/FreemapMobile/src/GProjection.java?rev=8216
		double f = Math.sin((Math.PI / 180) * lat);
		int y = (int) (0.5 + Math.floor(Math.pow(2, zoom + 7) + 0.5
				* Math.log((1 + f) / (1 - f))
				* (-Math.pow(2, zoom + 8) / (2 * Math.PI))));
		return y;
	}

}
