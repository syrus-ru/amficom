/*
 * $Id: AbstractSchemePort.java,v 1.3 2005/03/11 17:26:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemePort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import java.util.Date;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemePort}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/11 17:26:58 $
 * @module scheme_v1
 */
public abstract class AbstractSchemePort extends CloneableStorableObject implements
		Namable, Describable, Characterizable,
		ComSyrusAmficomConfigurationAbstractSchemePort {

	/**
	 * Depending on implementation, may reference either {@link SchemeLink}
	 * or {@link SchemeCableLink}.
	 */
	protected Identifier abstractSchemeLinkId = null;

	protected Identifier characteristicIds[] = null;

	protected Identifier measurementPortId = null;

	protected Identifier measurementPortTypeId = null;

	/**
	 * Depending on implementation, may reference either
	 * {@link com.syrus.AMFICOM.configuration.Port port}or
	 * {@link com.syrus.AMFICOM.configuration.Port cable port}.
	 */
	protected Identifier portId = null;

	/**
	 * Depending on implementation, may reference either
	 * {@link com.syrus.AMFICOM.configuration.PortType port type}or
	 * {@link com.syrus.AMFICOM.configuration.PortType cable port type}.
	 */
	protected Identifier portTypeId = null;

	protected Identifier schemeDeviceId = null;

	protected String thisDescription = null;

	protected DirectionType thisDirectionType = null;

	protected String thisName = null;

	/**
	 * @param id
	 */
	protected AbstractSchemePort(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected AbstractSchemePort(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @see #abstractSchemeLinkId
	 */
	public abstract AbstractSchemeLink abstractSchemeLink();

	/**
	 * @see #abstractSchemeLinkId
	 */
	public abstract void abstractSchemeLink(
			AbstractSchemeLink newAbstractSchemeLink);

	public abstract DirectionType directionType();

	public abstract void directionType(DirectionType newDirectionType);

	public abstract SchemeDevice schemeDevice();

	public abstract void schemeDevice(SchemeDevice newSchemeDevice);
}
