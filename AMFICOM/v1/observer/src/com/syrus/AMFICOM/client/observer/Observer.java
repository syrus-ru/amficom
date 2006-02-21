/*-
 * $Id: Observer.java,v 1.3 2006/02/21 14:07:43 stas Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer;

import java.awt.Toolkit;

import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.DefaultObserverApplicationModelFactory;

/**
 * @author krupenn
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2006/02/21 14:07:43 $
 * @module observer
 */
final class Observer extends AbstractApplication {
	public static final String APPLICATION_NAME = "observer";
	
	Observer() {
		super(APPLICATION_NAME);
	}		

	@Override
	protected void init() {
		super.aContext.setApplicationModel(new DefaultObserverApplicationModelFactory().create(super.aContext));
		super.startMainFrame(new ObserverMainFrame(this.aContext), Toolkit.getDefaultToolkit().getImage("images/main/observe_mini.gif"));
	}

	public static void main(String[] args) {
		new Observer();
	}
}
