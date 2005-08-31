/*
 * $Id: StorableElement.java,v 1.1 2005/08/31 10:32:55 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

public abstract class StorableElement{

	IntPoint location;
	IntDimension size;
	/**
	 * Время последнего изменения объекта
	 */
	private long modified = 0L;

	public long getModified() {
		return this.modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}
	
	public IntPoint getLocation()
	{
		return this.location;
	}

	public void setLocation(IntPoint newLocation)
	{
		this.location = newLocation;
	}

	public IntDimension getSize()
	{
		return this.size;
	}

	public void setSize(IntDimension newSize)
	{
		this.size = newSize;
	}
	
	public StorableElement(IntPoint location, IntDimension size)
	{
		this.location = location;
		this.size = size;
	}

	public int getX(){
		return this.location.x;
	}

	public int getY(){
		return this.location.y;
	}

	public int getWidth(){
		return this.size.getWidth();
	}

	public int getHeight(){
		return this.size.getHeight();
	}
	
	public void setLocation (int x, int y){
		this.location.x = x;
		this.location.y = y;		
	}

	public void setSize (int width, int height){
		this.size.setWidth(width);
		this.size.setHeight(height);		
	}
	
	public boolean hasPoint(int x, int y){
		if (	this.location.x < x
			&&	x < this.location.x + this.size.getWidth()
			&&	this.location.y < y
			&&	y < this.location.y + this.size.getHeight())
			return true;
		
		return false;
	}
	
	public StorableElement(){
	}
	
}
