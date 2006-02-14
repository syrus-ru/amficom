/*-
 * $Id: EquipmentType.java,v 1.110.2.3 2006/02/14 01:26:42 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentTypeHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.110.2.3 $, $Date: 2006/02/14 01:26:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
*/
 /* 	REFLECTOMETER("reflectometer"),
	OPTICAL_SWITCH("optical_switch"),
	MUFF("muff"),
	CABLE_PANEL("cable_panel"),
	TRANSMITTER("transmitter"),
	RECEIVER("receiver"),
	MULTIPLEXOR("multiplexor"),
	CROSS("cross"),
	FILTER("filter"),
	OTHER("other"),
	UNKNOWN("unknown"),
	RACK("rack"),

	@Crutch136(notes = "Stub for SchemeElement without Equipment")
	BUG_136("bug136");

 */
public final class EquipmentType extends StorableObjectType<EquipmentType> {
	private static final long serialVersionUID = 361767579292639873L;

	EquipmentType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
	}

	public EquipmentType(final IdlEquipmentType idlEquipmentType) throws CreateObjectException {
		try {
			this.fromTransferable(idlEquipmentType);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	public IdlEquipmentType getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlEquipmentTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "");
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlEquipmentType idlEquipmentType = (IdlEquipmentType) transferable;
		super.fromTransferable(idlEquipmentType, idlEquipmentType.codename, idlEquipmentType.description);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public String getName() {
		return super.getDescription();
	}

	public void setName(final String name) {
		throw new UnsupportedOperationException(ErrorMessages.METHOD_NOT_NEEDED);
	}

	/**
	 * If add additional fields to class,
	 * remove Override annotation.
	 */
	@Override
	protected synchronized final void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}

	@Override
	public EquipmentTypeWrapper getWrapper() {
		return EquipmentTypeWrapper.getInstance();
	}
}
