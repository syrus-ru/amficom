/*-
* $Id: AbstractBeanFactory.java,v 1.1 2005/11/17 09:00:32 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.beans;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractBeanFactory<T extends AbstractBean> {

	protected String nameKey;
	
	protected String shortNameKey;
	
	protected int count = 0;
	
	protected ManagerMainFrame graphText;
	
	protected AbstractBeanFactory(final String nameKey, 
	                              final String shortNameKey) {
		this.nameKey = nameKey;
		this.shortNameKey = shortNameKey;
	}
	
	public abstract T createBean(final Perspective perspective) 
	throws ApplicationException;	

	public abstract T createBean(final String codename) 
	throws ApplicationException;	


	public String getName() {
		return I18N.getString(this.nameKey);
	}
	
	public String getShortName() {
		return I18N.getString(this.shortNameKey);
	}	
	
	public final ManagerMainFrame getGraphText() {
		return this.graphText;
	}
	
	public final void setGraphText(ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	public abstract String getCodename();
	
	public final int getCount() {
		return this.count;
	}
}

