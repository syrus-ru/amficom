package com.syrus.AMFICOM.client.model;


public class DefaultObserverApplicationModelFactory extends
		ObserverApplicationModelFactory {
	public DefaultObserverApplicationModelFactory() {
		// empty
	}

	public ApplicationModel create() {
		ApplicationModel aModel = super.create();
		
		aModel.setVisible(ObserverApplicationModel.MENU_OPEN, false);
		aModel.setVisible(ObserverApplicationModel.MENU_OPEN_SCHEME, false);
		
		return aModel;
	}
}
