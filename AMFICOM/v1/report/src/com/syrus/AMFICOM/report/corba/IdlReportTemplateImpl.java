/*-
 * $Id: IdlReportTemplateImpl.java,v 1.2 2006/03/14 10:47:56 bass Exp $
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
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.corba.IdlReportTemplatePackage.IdlOrientation;
import com.syrus.AMFICOM.report.corba.IdlReportTemplatePackage.IdlSheetSize;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/03/14 10:47:56 $
 * @module report
 */

public class IdlReportTemplateImpl extends IdlReportTemplate {
	
	private static final long	serialVersionUID	= -8554829809590670249L;

	IdlReportTemplateImpl() {
		//empty
	}
	
	IdlReportTemplateImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final IdlSheetSize sheetSize,
			final IdlOrientation orientation,
			final int marginSize,
			final String destinationModule) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.sheetSize = sheetSize;
		this.idlOrientation = orientation;
		this.marginSize = marginSize;
		this.destinationModule = destinationModule;
	}
	
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new ReportTemplate(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
