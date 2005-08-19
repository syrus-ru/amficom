/*-
 * $Id: AbstractEventHandler.java,v 1.3 2005/08/19 12:45:55 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/08/19 12:45:55 $
 * @module commonclient
 */

public abstract class AbstractEventHandler implements PropertyChangeListener, ChangeListener {
	protected ApplicationContext aContext;
	protected AbstractPropertiesFrame frame;
	
	public AbstractEventHandler(AbstractPropertiesFrame frame) {
		this.frame = frame;
		frame.addEventhandler(this);
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
}
