/*-
 * $Id: AbstractDataStorableElement.java,v 1.3 2006/03/14 10:47:56 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.report;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.report.corba.IdlAbstractData;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/03/14 10:47:56 $
 * @module report
 */
public abstract class AbstractDataStorableElement 
		extends StorableElement {
	protected String reportName;

	/**
	 * Полное имя класса модели, которая "знает" как строить этот отчёт.
	 */
	protected String modelClassName;
	
	protected Identifier reportObjectId = Identifier.VOID_IDENTIFIER;
	
	AbstractDataStorableElement() {
		//empty constructor for transferables
	}
	
	AbstractDataStorableElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IntPoint location,
			final IntDimension size,
			final Identifier reportTemplateId,
			final String reportName,
			final String modelClassName) {
		super(id, created, modified, creatorId, modifierId, version, location, size, reportTemplateId);
		this.reportName = reportName;
		this.modelClassName = modelClassName;
	}
	
	@Override
	protected synchronized void fromIdlTransferable(IdlStorableObject transferable)
	throws IdlConversionException {
		IdlAbstractData idlAbstractData = (IdlAbstractData) transferable;
		super.fromIdlTransferable(idlAbstractData);
		this.reportName = idlAbstractData.reportName;
		this.modelClassName = idlAbstractData.modelClassName;		
	}
	
	synchronized void setAttributes(Date created, 
			Date modified, 
			Identifier creatorId, 
			Identifier modifierId, 
			StorableObjectVersion version,
			final int locationX, 
			final int locationY, 
			final int width, 
			final int height,
			final Identifier reportTemplateId,
			final String reportName,
			final String moduleClassName) {
		super.setAttributes(created, modified, creatorId, modifierId, version,
				locationX, locationY, width, height, reportTemplateId);
		this.reportName = reportName;
		this.modelClassName = moduleClassName;
	}
	
	public String getReportName() {
		return this.reportName;
	}

	public String getModelClassName() {
		return this.modelClassName;
	}

	@Override
	protected AbstractDataStorableElement clone() throws CloneNotSupportedException {
		AbstractDataStorableElement clone = (AbstractDataStorableElement) super.clone();
		
		clone.reportName = this.reportName;
		clone.modelClassName = this.modelClassName;
		clone.reportObjectId = Identifier.VOID_IDENTIFIER;
		return clone;
	}
	
	public Identifier getReportObjectId() {
		return this.reportObjectId;
	}
	
	public void setReportObjectId(Identifier reportObjectId) {
		this.reportObjectId = reportObjectId;
		super.markAsChanged();
	}
}
