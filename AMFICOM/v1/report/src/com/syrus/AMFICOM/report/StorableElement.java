/*
 * $Id: StorableElement.java,v 1.15 2006/03/15 14:47:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.report.corba.IdlAbstractReportElement;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2006/03/15 14:47:29 $
 * @module report
 */
public abstract class StorableElement
		extends StorableObject implements Cloneable {

	protected IntPoint location;
	protected IntDimension size;
	protected Identifier reportTemplateId;
	/**
	 * Время последнего изменения объекта
	 */
	public IntPoint getLocation()
	{
		return this.location;
	}

	public void setLocation(IntPoint newLocation) {
		this.location = newLocation;
		super.markAsChanged();
	}

	public IntDimension getSize() {
		return this.size;
	}

	public void setSize(IntDimension newSize) {
		this.size = newSize;
		super.markAsChanged();
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
		super.markAsChanged();
	}

	public void setSize (int width, int height){
		this.size.setWidth(width);
		this.size.setHeight(height);
		super.markAsChanged();
	}
	
	public boolean hasPoint(int x, int y){
		if (	this.location.x < x
			&&	x < this.location.x + this.size.getWidth()
			&&	this.location.y < y
			&&	y < this.location.y + this.size.getHeight())
			return true;
		
		return false;
	}
	
	StorableElement() {
		//Empty constructor for transferable
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	StorableElement(final Identifier id, 
			final Date created, 
			final Date modified,
			final Identifier creatorId, 
			final Identifier modifierId, 
			final StorableObjectVersion version,
			final IntPoint location,
			final IntDimension size,
			final Identifier reportTemplateId) {
		super(id, created, modified, creatorId, modifierId, version);
		this.location = location;
		this.size = size;
		this.reportTemplateId = reportTemplateId;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 *
	 * @param iae
	 */
	final void fromIdlTransferable(final IdlAbstractReportElement iae) {
		super.fromIdlTransferable(iae);
		this.location = new IntPoint(iae.locationX, iae.locationY);
		this.size = new IntDimension(iae.width, iae.height);
		this.reportTemplateId  = new Identifier(iae.idlReportTemplateId);
	}
	
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final int locationX,
			final int locationY,
			final int width,
			final int height,
			final Identifier reportTemplateId) {
		
			super.setAttributes(created, modified, creatorId, modifierId, version);
			this.location = new IntPoint(locationX, locationY);
			this.size = new IntDimension(width, height);
			this.reportTemplateId = reportTemplateId;
	}

	Identifier getReportTemplateId() {
		return this.reportTemplateId;
	}

	void setReportTemplateId(Identifier reportTemplateId) {
		this.reportTemplateId = reportTemplateId;
		super.markAsChanged();
	}
	
	@Override
	protected StorableElement clone() throws CloneNotSupportedException {
		StorableElement clone = (StorableElement) super.clone();
		clone.location = new IntPoint(this.location.x, this.location.y);
		clone.size = new IntDimension(this.size.getWidth(), this.size.getHeight());
		clone.reportTemplateId = Identifier.VOID_IDENTIFIER;
		return clone;
	}
}
