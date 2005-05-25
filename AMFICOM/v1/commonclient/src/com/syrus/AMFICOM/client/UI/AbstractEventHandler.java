/*-
 * $Id: AbstractEventHandler.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module commonclient_v1
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
