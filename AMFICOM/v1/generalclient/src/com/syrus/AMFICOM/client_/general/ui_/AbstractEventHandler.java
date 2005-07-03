/*-
 * $Id: AbstractEventHandler.java,v 1.1 2005/04/28 11:53:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public abstract class AbstractEventHandler implements OperationListener {
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
