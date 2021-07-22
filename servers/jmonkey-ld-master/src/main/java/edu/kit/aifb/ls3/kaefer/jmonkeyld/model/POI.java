package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public abstract class POI extends SceneGraphObject {
	float _latitude;
	float _longitude;

	String _label;
	String _id;
	String _image;
	
	Node _node = null;

	public enum Colour {
		toBeHighlighted, highlighted, toBeNormalised, normal
	}

	Colour _colour;

	public POI(String id, float latitude, float longitude, String label,
			String image) {
		_id = id;
		_latitude = latitude;
		_longitude = longitude;
		_label = label;
		_image = image;

		_state = State.toBeDisplayed;
		_colour = Colour.normal;

	}

	public float getLatitude() {
		return _latitude;
	}

	public float getLongitude() {
		return _longitude;
	}

	public String getLabel() {
		return _label;
	}

	public String getImage() {
		return _image;
	}

	public void setState(State state) {
		if (_state == State.toBeDisplayed && state == State.displayed
				&& _node == null)
			throw new IllegalStateException(
					"Please set node before changing state to."
							+ State.displayed.toString());
		else
			super.setState(state);
	}

	public Node getNode() {
		return _node;
	}

	public void setNode(Node n) {
		_node = n;
	}

	public Vector3f getWorldCoordinates() {
		return _node.getWorldTranslation();
	}

	public Colour getColour() {
		return _colour;
	}

	public void setColour(Colour colour) {
		_colour = colour;
	}

	public String getId() {
		return _id;
	}
	
	public FreshnessInDisplay getFreshnessInDisplayInternal() {
		// once displayed, a POI does not change.
		return FreshnessInDisplay.refreshed;
	}

}
