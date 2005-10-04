/*-
 * $Id: IdlDataImpl.java,v 1.1 2005/10/04 11:02:35 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.report.DataStorableElement;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/10/04 11:02:35 $
 * @module report
 */

public class IdlDataImpl extends IdlData {
	
	private static final long	serialVersionUID	= -5954191740919180469L;

	IdlDataImpl() {
		//empty
	}
	
	IdlDataImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final int locationX,
			final int locationY,
			final int width,
			final int height,
			final IdlIdentifier idlReportTemplateId,
			final String reportName,
			final String modelClassName) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.locationX = locationX;
		this.locationY = locationY;
		this.width = width;
		this.height = height;
		this.idlReportTemplateId = idlReportTemplateId;
		this.reportName = reportName;
		this.modelClassName = modelClassName;
	}
	
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		return new DataStorableElement(this);
	}
}
