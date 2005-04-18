/*-
 * $Id: SchemeCablePort.java,v 1.16 2005/04/18 13:19:01 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort_Transferable;

/**
 * #09 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/04/18 13:19:01 $
 * @module scheme_v1
 */
public final class SchemeCablePort extends AbstractSchemePort {
	private static final long serialVersionUID = 4050767078690534455L;

	private SchemeCablePortDatabase schemeCablePortDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCablePort(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.schemeCablePortDatabase = SchemeDatabaseContext.getSchemeCablePortDatabase();
		try {
			this.schemeCablePortDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	SchemeCablePort(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeCablePort(final SchemeCablePort_Transferable transferable) throws CreateObjectException {
		try {
			this.schemeCablePortDatabase = SchemeDatabaseContext.getSchemeCablePortDatabase();
			fromTransferable(transferable);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public static SchemeCablePort createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeCablePort schemeCablePort = new SchemeCablePort(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeCablePort.changed = true;
			return schemeCablePort;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeCablePort.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
		}
	}

	public Object clone() {
		final SchemeCablePort schemeCablePort = (SchemeCablePort) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCablePort;
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	public AbstractSchemeLink getAbstractSchemeLink() {
		return getSchemeCableLink();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLEPORT;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemePort#getPort()
	 */
	public Port getPort() {
		final Port port = super.getPort();
		assert port == null || port.getSort() == PortSort._PORT_SORT_CABLE_PORT: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return port;
	}

	public SchemeCableLink getSchemeCableLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param port
	 * @see AbstractSchemePort#setPort(Port)
	 */
	public void setPort(final Port port) {
		assert port == null || port.getSort() == PortSort._PORT_SORT_CABLE_PORT: ErrorMessages.NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param transferable
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
