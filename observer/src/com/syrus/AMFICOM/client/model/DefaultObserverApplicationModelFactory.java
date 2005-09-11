package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class DefaultObserverApplicationModelFactory extends
		ObserverApplicationModelFactory {
	public DefaultObserverApplicationModelFactory() {
		// empty
	}

	public ApplicationModel create() {
		ApplicationModel aModel = super.create();
		return aModel;
	}
}
