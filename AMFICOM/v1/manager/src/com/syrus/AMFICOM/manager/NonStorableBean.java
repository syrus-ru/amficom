/*-
* $Id: NonStorableBean.java,v 1.5 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.ApplicationException;


/**
 * @version $Revision: 1.5 $, $Date: 2005/10/18 15:10:39 $
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

