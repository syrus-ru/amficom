/*
 * 
 * Created on 19.03.2005 14:51:04
 *
 */
package com.syrus.AMFICOM.logic;


public class ServiceItem extends AbstractItem implements Populatable {

	private static int	serviceCount	= 0;
	private String		name;
	private Object		object;
	
	private ChildrenFactory childrenFactory;
	private boolean populated = false;

	public ServiceItem() {
		this.name = "service" + (serviceCount++);
	}
	
	public ServiceItem(String name) {
		this.name = name;
		serviceCount++;
	}

	public int getMaxChildrenCount() {
		return Integer.MAX_VALUE;
	}

	public boolean canHaveParent() {
		return true;
	}
	
	public boolean canHaveChildren() {
		return true;
	}

	public String getName() {
		return this.name;
	}

	public Object getObject() {
		return this.object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void removeChild(Item childItem) {
		if (this.children != null) {
			this.children.remove(childItem);
		}

	}	

	public boolean isService() {
		return true;
	}

	
	/**
	 * @param childrenFactory The childrenFactory to set.
	 */
	public void setChildrenFactory(ChildrenFactory childrenFactory) {
		this.childrenFactory = childrenFactory;
	}
	
	public void populate() {
		if (!this.populated) {
			if (this.childrenFactory != null) {
				this.childrenFactory.populate(this);
			}
			this.populated = true;
		}
	}

}
