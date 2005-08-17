/*-
* $Id: NonStorableBean.java,v 1.2 2005/08/17 15:59:40 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;


/**
 * @version $Revision: 1.2 $, $Date: 2005/08/17 15:59:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NonStorableBean extends AbstractBean {
	
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
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		// nothing
		
	}
}

