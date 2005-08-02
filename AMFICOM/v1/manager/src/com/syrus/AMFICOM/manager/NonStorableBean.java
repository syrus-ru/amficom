/*-
* $Id: NonStorableBean.java,v 1.1 2005/08/02 14:41:10 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/02 14:41:10 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NonStorableBean extends AbstractBean {
	
	protected String		name;
	
	public final String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}

