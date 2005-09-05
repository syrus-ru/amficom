/*-
 * $Id: ServiceItem.java,v 1.11 2005/09/05 08:57:50 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.logic;


/**
 * Service item
 *
 * @version $Revision: 1.11 $, $Date: 2005/09/05 08:57:50 $
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
		super.maxChildrenCount = Integer.MAX_VALUE;
		super.canHaveChildren = true;
		super.canHaveParent = true;
		super.service = true;
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
