package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

public class HUDmessage extends SceneGraphObject {

	String _message;
	FreshnessInDisplay _fid;

	public HUDmessage(String message) {
		_message = message;
		_fid = FreshnessInDisplay.refreshed;
	}

	@Override
	protected FreshnessInDisplay getFreshnessInDisplayInternal() {
		return _fid;
	}
	
	public void setFreshnessInDisplay(FreshnessInDisplay fid) {
		_fid = fid;
	}

	public void setMessage(String message) {
		_message = message;
		_fid = FreshnessInDisplay.toBeRefreshed;
	}

	public String getMessage() {
		return _message;
	}
	
}