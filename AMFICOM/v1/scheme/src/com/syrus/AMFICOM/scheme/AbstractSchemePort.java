/*-
 * $Id: AbstractSchemePort.java,v 1.6 2005/03/28 12:01:28 bass Exp $
 * 
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import java.util.*;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemePort}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/03/28 12:01:28 $
 * @module scheme_v1
 */
public abstract class AbstractSchemePort extends
		AbstractCloneableStorableObject implements Describable,
		Characterizable {
	private static final long serialVersionUID = 6943625949984422779L;

	private AbstractSchemePortDirectionType abstractSchemePortDirectionType;

	private Collection characteristics;

	private String description;

	private Identifier measurementPortId;

	private Identifier measurementPortTypeId;

	private String name;

	private Identifier parentSchemeDeviceId;

	/**
	 * Depending on implementation, may reference either {@link Port Port}
	 * or {@link Port CablePort}.
	 */
	private Identifier portId;

	/**
	 * Depending on implementation, may reference either
	 * {@link PortType PortType} or {@link PortType CablePortType}.
	 */
	private Identifier portTypeId;

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

	public final void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public abstract AbstractSchemeLink getAbstractSchemeLink();

	public final AbstractSchemePortDirectionType getAbstractSchemePortDirectionType() {
		throw new UnsupportedOperationException();
	}

	public final Collection getCharacteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public final MeasurementPort getMeasurementPort() {
		throw new UnsupportedOperationException();
	}

	public final MeasurementPortType getMeasurementPortType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public final SchemeDevice getParentSchemeDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 */
	public Port getPort() {
		throw new UnsupportedOperationException();
	}

	public final PortType getPortType() {
		throw new UnsupportedOperationException();
	}

	public final void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Use one of:
	 *             <ul><li>{@link AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)};</li>
	 *             <li>{@link AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)};</li>
	 *             <li>{@link SchemeLink#setSourceSchemePort(SchemePort)};</li>
	 *             <li>{@link SchemeLink#setTargetSchemePort(SchemePort)};</li>
	 *             <li>{@link SchemeCableLink#setSourceSchemeCablePort(SchemeCablePort)};</li>
	 *             <li>{@link SchemeCableLink#setTargetSchemeCablePort(SchemeCablePort)}</li></ul>
	 *             -- instead.
	 */
	public abstract void setAbstractSchemeLink(final AbstractSchemeLink abstractSchemeLink);

	public final void setAbstractSchemePortDirectionType(final AbstractSchemePortDirectionType abstractSchemePortDirectionType) {
		throw new UnsupportedOperationException();
	}

	public final void setCharacteristics(final Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	public final void setCharacteristics0(final Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
			return;
		this.description = description;
		this.changed = true;
	}

	public final void setMeasurementPort(final MeasurementPort measurementPort) {
		throw new UnsupportedOperationException();
	}

	public final void setMeasurementPortType(final MeasurementPortType measurementPortType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
			return;
		this.name = name;
		this.changed = true;
	}

	public final void setParentSchemeDevice(final SchemeDevice parentSchemeDevice) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param port
	 */
	public void setPort(final Port port) {
		throw new UnsupportedOperationException();
	}

	public final void setPortType(final PortType portType) {
		throw new UnsupportedOperationException();
	}
}
