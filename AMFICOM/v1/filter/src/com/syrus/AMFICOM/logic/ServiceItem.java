/*-
 * $Id: ServiceItem.java,v 1.10 2005/09/02 14:20:49 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.logic;


/**
 * Service item
 *
 * @version $Revision: 1.10 $, $Date: 2005/09/02 14:20:49 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public class ServiceItem extends PopulatableItem {

	private static int	serviceCount	= 0;

	public ServiceItem() {
		this("service" + serviceCount);
	}
	
	public ServiceItem(final String name) {
		this.name = name;
		serviceCount++;
		this.setMaxChildrenCount(Integer.MAX_VALUE);
		this.setCanHaveParent(true);
		this.setCanHaveChildren(true);
		this.setService(true);
	}


	public void removeChild(Item childItem) {
		if (this.children != null) {
			this.children.remove(childItem);
		}
	}	

	@Override
	public void setCanHaveChildren(boolean canHaveChildren) {
		throw new UnsupportedOperationException("ServiceItem.setCanHaveChildren() is unsupported");
	}
	
	@Override
	public void setCanHaveParent(boolean canHaveParent) {
		throw new UnsupportedOperationException("ServiceItem.setCanHaveParent() is unsupported");
	}
	
	@Override
	public void setMaxChildrenCount(int maxChildrenCount) {
		throw new UnsupportedOperationException("ServiceItem.setMaxChildrenCount() is unsupported");
	}

	@Override
	public void setService(boolean service) {
		throw new UnsupportedOperationException("ServiceItem.setService() is unsupported");
	}
}
