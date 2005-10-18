/*-
* $Id: IdentifiableBeanFactory.java,v 1.1 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class IdentifiableBeanFactory<T extends AbstractBean> extends AbstractBeanFactory<T> {

	protected Validator validator;	

	protected IdentifiableBeanFactory(final String nameKey, 
	                              final String shortNameKey) {
		super(nameKey, shortNameKey);
	}
	
	@Override
	public T createBean(final String codename) throws ApplicationException {
		return this.createBean(new Identifier(codename));
	}
	
	protected abstract T createBean(final Identifier id) throws ApplicationException;

}

