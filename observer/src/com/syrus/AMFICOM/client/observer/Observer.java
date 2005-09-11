package com.syrus.AMFICOM.client.observer;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.DefaultObserverApplicationModelFactory;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;

public class Observer extends AbstractApplication {
	
	public static final String APPLICATION_NAME = "observer";
	
	public Observer() {
		super(APPLICATION_NAME);
	}		

	@Override
	protected void init() {
		super.init();		
		super.aContext.setApplicationModel(new DefaultObserverApplicationModelFactory().create());
		super.startMainFrame(new ObserverMainFrame(this.aContext), Toolkit.getDefaultToolkit().getImage("images/main/observe_mini.gif"));
	}
	
	public static void main(String[] args) {
		new Observer();
	}
}
