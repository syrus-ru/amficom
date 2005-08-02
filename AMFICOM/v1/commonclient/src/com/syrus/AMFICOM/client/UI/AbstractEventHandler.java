/*-
 * $Id: AbstractEventHandler.java,v 1.2 2005/08/02 13:03:21 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:21 $
 * @module commonclient
 */

public abstract class AbstractEventHandler implements PropertyChangeListener {
	protected ApplicationContext aContext;
	protected AbstractPropertiesFrame frame;
	
	public AbstractEventHandler(AbstractPropertiesFrame frame) {
		this.frame = frame;
		frame.setEventhandler(this);
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
}
