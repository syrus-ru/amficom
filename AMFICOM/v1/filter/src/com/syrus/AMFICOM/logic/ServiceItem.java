/*
 * 
 * Created on 19.03.2005 14:51:04
 *
 */
package com.syrus.AMFICOM.logic;


public class ServiceItem extends AbstractItem {

	private static int	serviceCount	= 0;
	private String		name;
	private Object		object;

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

	public boolean allowsParents() {
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

}
