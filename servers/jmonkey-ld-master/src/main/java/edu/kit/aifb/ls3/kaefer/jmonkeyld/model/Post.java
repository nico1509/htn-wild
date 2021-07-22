package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

public class Post extends POI {

	String _content;
	String _creator;
	String _date;

	public Post(String id, float latitude, float longitude, String creator,
			String label, String content, String date, String image) {
		super(id, latitude, longitude, label, image);

		_creator = creator;
		_content = content;
		_date = date;
	}

	public String getCreator() {
		return _creator;
	}

	public String getContent() {
		return _content;
	}

	public String getDate() {
		return _date;
	}

	public String toString() {
		return "lat: " + _latitude + " long: " + _longitude + " label: "
				+ _label + " date " + _date + " creator " + _creator
				+ " content " + _content + " image: " + _image + " id: " + _id;
	}
}
