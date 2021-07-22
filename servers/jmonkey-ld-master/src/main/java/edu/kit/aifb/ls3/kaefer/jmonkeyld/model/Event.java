package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

public class Event extends POI {
	String _venue;
	String _date;

	public Event(String id, float latitude, float longitude, String label,
			String date, String venue, String image) {
		super(id, latitude, longitude, label, image);

		_venue = venue;
		_date = date;

	}

	public String getVenue() {
		return _venue;
	}

	public String getDate() {
		return _date;
	}

	public String toString() {
		return "lat: " + _latitude + " long: " + _longitude + " label: "
				+ _label + " date " + _date + " image: " + _image + " id: "
				+ _id;
	}
}
