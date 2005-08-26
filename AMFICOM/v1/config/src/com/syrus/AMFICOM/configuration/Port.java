/*-
 * $Id: Port.java,v 1.87 2005/08/26 11:06:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlPort;
import com.syrus.AMFICOM.configuration.corba.IdlPortHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.87 $, $Date: 2005/08/26 11:06:19 $
 * @author $Author: bass $
 * @module config
 */
public final class Port extends StorableObject implements Characterizable, TypedObject {
	private static final long serialVersionUID = -5139393638116159453L;

	private PortType type;
	private String description;
	private Identifier equipmentId;

	private transient CharacterizableDelegate characterizableDelegate;

	Port(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(PORT_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Port(final IdlPort pt) throws CreateObjectException {
		try {
			this.fromTransferable(pt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Port(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final PortType type,
			final String description,
			final Identifier equipmentId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param description
	 * @param equipmentId
	 * @throws CreateObjectException
	 */
	public static Port createInstance(final Identifier creatorId,
			final PortType type,
			final String description,
			final Identifier equipmentId) throws CreateObjectException {
		if (creatorId == null || type == null || description == null ||
				type == null || equipmentId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Port port = new Port(IdentifierPool.getGeneratedIdentifier(PORT_CODE),
						creatorId,
						StorableObjectVersion.createInitial(),
						type,
						description,
						equipmentId);

			assert port.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			port.markAsChanged();

			return port;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlPort pt = (IdlPort) transferable;
		super.fromTransferable(pt);

		this.type = (PortType) StorableObjectPool.getStorableObject(new Identifier(pt._typeId), true);

		this.description = pt.description;
		this.equipmentId = new Identifier(pt.equipmentId);
	}
	

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPort getTransferable(final ORB orb) {

		return IdlPortHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.type.getId().getTransferable(),
				this.description,
				this.equipmentId.getTransferable());
	}

	public Identifier getTypeId() {
		return this.getType().getId();
	}

	public PortType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final PortType type,
			final String description,
			final Identifier equipmentId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.type);
		dependencies.add(this.equipmentId);
		return dependencies;
	}	
	/**
	 * @param equipmentId The equipmentId to set.
	 */
	public void setEquipmentId(final Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.markAsChanged();
	}

	/**
	 * @param typeId
	 */
	public void setTypeId(final Identifier typeId) {
		assert typeId != null : NON_NULL_EXPECTED;
		assert !typeId.isVoid() : NON_VOID_EXPECTED;
		assert typeId.getMajor() == PORT_TYPE_CODE;

		if (typeId.equals(this.type.getId())) {
			return;
		}
		try {
			this.setType(StorableObjectPool.<PortType>getStorableObject(typeId, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
		}
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final PortType type) {
		assert type != null : NON_NULL_EXPECTED;

		if (this.type.equals(type)) {
			return;
		}
		this.type = type;
		super.markAsChanged();
	}
}
