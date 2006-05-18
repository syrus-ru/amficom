/*-
 * $Id: IdlDataImpl.java,v 1.2 2006/03/14 10:47:56 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report.corba;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.report.DataStorableElement;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/03/14 10:47:56 $
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
		try {
			return new DataStorableElement(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
