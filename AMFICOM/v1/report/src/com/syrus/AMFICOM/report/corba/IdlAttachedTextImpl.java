/*-
 * $Id: IdlAttachedTextImpl.java,v 1.2 2006/03/14 10:47:56 bass Exp $
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
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.corba.IdlAttachedTextPackage.IdlFont;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/03/14 10:47:56 $
 * @module report
 */

public class IdlAttachedTextImpl extends IdlAttachedText {
	private static final long	serialVersionUID	= 5399303303728435271L;
	
	IdlAttachedTextImpl() {
		//empty
	}
	
	IdlAttachedTextImpl(final IdlIdentifier id,
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
			final String text,
			final IdlIdentifier verticalAttacherId,
			final IdlIdentifier horizontalAttacherId,
			final int distanceX,
			final int distanceY,
			final IdlFont font,
			final int verticalAttachType,
			final int horizontalAttachType) {
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
		this.text = text;
		this.verticalAttacherId = verticalAttacherId;
		this.horizontalAttacherId = horizontalAttacherId;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		this.font = font;
		this.verticalAttachType = verticalAttachType;
		this.horizontalAttachType = horizontalAttachType;
	}
	
	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new AttachedTextStorableElement(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
