/*
 * ObjectResourceTableRow.java
 * Created on 02.08.2004 9:28:55
 * 
 */
package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;


/**
 * @author Vladimir Dolzhenko
 */
public abstract class ObjectResourceTableRow {

	private ObjectResource objectResource;
	
	
	public ObjectResourceTableRow(ObjectResource objectResource){
		this.setObjectResource(objectResource);
	}
	
	public Object get(int index) {
		return getData().get(index);
	}
	
	public abstract java.util.List getData();
	
	public void setObjectResource(ObjectResource objectResource){
		this.objectResource = objectResource;
	}
	
	public abstract void setValue(Object value, int columnIndex);
	
	public ObjectResource getObjectResource(){
		return this.objectResource;
	}
}
