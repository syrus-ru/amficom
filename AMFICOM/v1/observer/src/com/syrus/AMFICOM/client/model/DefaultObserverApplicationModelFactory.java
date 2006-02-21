package com.syrus.AMFICOM.client.model;


public class DefaultObserverApplicationModelFactory extends
		ObserverApplicationModelFactory {
	public DefaultObserverApplicationModelFactory() {
		// empty
	}

	@Override
	public ApplicationModel create(ApplicationContext aContext) {
		ApplicationModel aModel = super.create(aContext);
		
		aModel.setVisible(ObserverApplicationModel.MENU_OPEN, false);
		aModel.setVisible(ObserverApplicationModel.MENU_OPEN_SCHEME, false);
		
		return aModel;
	}
}
