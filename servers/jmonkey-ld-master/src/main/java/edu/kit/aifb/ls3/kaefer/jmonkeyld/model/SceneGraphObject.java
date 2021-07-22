package edu.kit.aifb.ls3.kaefer.jmonkeyld.model;

public abstract class SceneGraphObject {

	public SceneGraphObject() {
		_state = State.toBeDisplayed;
	}

	public enum FreshnessInDisplay {
		toBeRefreshed, refreshed
	}

	public enum State {
		toBeDisplayed, displayed, toBeRemoved
	}

	State _state;

	public State getState() {
		return _state;
	}

	public void setState(State state) {
		_state = state;
	}

	public FreshnessInDisplay getFreshnessInDisplay() {
		if (getState() == State.displayed)
			return getFreshnessInDisplayInternal();
		else
			throw new IllegalStateException("Must be in state "
					+ State.displayed);
	}

	protected abstract FreshnessInDisplay getFreshnessInDisplayInternal();
}
