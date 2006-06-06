/*-
* $Id: IdentifiableBeanFactory.java,v 1.2 2006/06/06 11:34:18 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.beans;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;


/**
 * @version $Revision: 1.2 $, $Date: 2006/06/06 11:34:18 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class IdentifiableBeanFactory<T extends AbstractBean> extends AbstractBeanFactory<T> {

	protected IdentifiableBeanFactory(final String nameKey, 
	                              final String shortNameKey) {
		super(nameKey, shortNameKey);
	}
	
	@Override
	public T createBean(final String codename) throws ApplicationException {
		return this.createBean(Identifier.valueOf(codename));
	}
	
	protected abstract T createBean(final Identifier id) throws ApplicationException;

}

