/*-
 * $Id: IdlTableDataImpl.java,v 1.1 2005/10/04 11:02:35 max Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.report.TableDataStorableElement;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/10/04 11:02:35 $
 * @module report
 */

public class IdlTableDataImpl extends IdlTableData {
	
	private static final long	serialVersionUID	= -669826201911142744L;

	IdlTableDataImpl() {
		//empty
	}
	
	IdlTableDataImpl(final IdlIdentifier id,
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
			final String modelClassName,
			final int verticalDivisionCount) {
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
		this.verticalDivisionCount = verticalDivisionCount;
	}
	
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		return new TableDataStorableElement(this);
	}

}