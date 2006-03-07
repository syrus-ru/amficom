/*
 * $Id: KIS.java,v 1.14.2.3 2006/03/07 10:42:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlKIS;
import com.syrus.AMFICOM.measurement.corba.IdlKISHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14.2.3 $, $Date: 2006/03/07 10:42:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class KIS extends DomainMember<KIS> implements Namable {
	private static final long serialVersionUID = -7396074492931314603L;

	private String name;
	private String description;
	private String hostname;
	private short tcpPort;
	private Identifier equipmentId;
	private Identifier mcmId;
	private boolean onService;

	public KIS(final IdlKIS kt) throws CreateObjectException {
		try {
			this.fromTransferable(kt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	KIS(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final short tcpPort,
			final Identifier equipmentId,
			final Identifier mcmId,
			final boolean onSevice) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
		this.onService = onSevice;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param mcmId
	 * @throws CreateObjectException
	 */
	public static KIS createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final short tcpPort,
			final Identifier equipmentId,
			final Identifier mcmId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null ||
				description == null || hostname == null || equipmentId == null || mcmId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final KIS kis = new KIS(IdentifierPool.getGeneratedIdentifier(KIS_CODE),
					creatorId,
					INITIAL_VERSION,
					domainId,
					name,
					description,
					hostname,
					tcpPort,
					equipmentId,
					mcmId,
					false);

			assert kis.isValid() : OBJECT_STATE_ILLEGAL;

			kis.markAsChanged();

			return kis;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlKIS idlKIS = (IdlKIS) transferable;
		super.fromTransferable(idlKIS, Identifier.valueOf(idlKIS.domainId));

		this.name = idlKIS.name;
		this.description = idlKIS.description;
		this.hostname = idlKIS.hostname;
		this.tcpPort = idlKIS.tcpPort;
		this.equipmentId = Identifier.valueOf(idlKIS.equipmentId);
		this.mcmId = Identifier.valueOf(idlKIS.mcmId);
		this.onService = idlKIS.onSevice;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlKIS getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlKISHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.getDomainId().getIdlTransferable(),
				this.name,
				this.description,
				this.hostname,
				this.tcpPort,
				this.equipmentId.getIdlTransferable(),
				this.mcmId.getIdlTransferable(),
				this.onService);
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public String getHostName() {
		return this.hostname;
	}

	/**
	 * @param hostname The hostname to set.
	 */
	public void setHostName(final String hostname) {
		this.hostname = hostname;
		super.markAsChanged();
	}

	public short getTCPPort() {
		return this.tcpPort;
	}

	/**
	 * @param tcpPort The tcpPort to set.
	 */
	public void setTCPPort(final short tcpPort) {
		this.tcpPort = tcpPort;
		super.markAsChanged();
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	/**
	 * @param equipmentId The equipmentId to set.
	 */
	public void setEquipmentId(final Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.markAsChanged();
	}

	public Identifier getMCMId() {
		return this.mcmId;
	}

	/**
	 * @param mcmId
	 */
	public void setMCMId(final Identifier mcmId) {
		this.mcmId = mcmId;
		super.markAsChanged();
	}

	public boolean isOnService() {
		return this.onService;
	}

	public void setOnService(final boolean onService) {
		this.onService = onService;
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final short tcpPort,
			final Identifier equipmentId,
			final Identifier mcmId,
			final boolean onService) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
		this.onService = onService;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.equipmentId);
		dependencies.add(this.mcmId);
		return dependencies;
	}

	/**
	 * @return <code>Set&lt;MeasurementPort&gt;</code>
	 */
	public Set<MeasurementPort> getMeasurementPorts(final boolean breakOnLoadError) {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id,
					MEASUREMENTPORT_CODE), true, breakOnLoadError);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, Level.SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected KISWrapper getWrapper() {
		return KISWrapper.getInstance();
	}
}
