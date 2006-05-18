/*-
* $Id: NonStorableBean.java,v 1.1 2005/11/17 09:00:33 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.beans;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.graph.MPort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class NonStorableBean extends AbstractBean {
	
	protected String		name;	

	@Override
	public final String getName() {
		return this.name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) throws ApplicationException {
		// nothing
		
	}
	
	@Override
	public void dispose() throws ApplicationException {
		// nothing ?		
	}
}

