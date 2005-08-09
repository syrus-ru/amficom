/*
 * $Id: Domain.java,v 1.52 2005/08/09 09:07:13 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.administration.corba.IdlDomainHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;

/**
 * @version $Revision: 1.52 $, $Date: 2005/08/09 09:07:13 $
 * @author $Author: bob $
 * @module administration
 */

public final class Domain extends DomainMember implements Characterizable {
	private static final long serialVersionUID = 6401785674412391641L;

	private String name;
	private String description;

	private transient CharacterizableDelegate characterizableDelegate;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Domain(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.DOMAIN_CODE).retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Domain(final IdlDomain dt) throws CreateObjectException {
		try {
			this.fromTransferable(dt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Domain(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.name = name;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlDomain dt = (IdlDomain)transferable;
		super.fromTransferable(dt, new Identifier(dt.domainId));
		this.name = dt.name;
		this.description = dt.description;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlDomain getTransferable(final ORB orb) {
		assert this.isValid(): ErrorMessages.OBJECT_STATE_ILLEGAL;
		return IdlDomainHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.domainId.getTransferable(),
				this.name,
				this.description);
	}	

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null
				&& this.description != null;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}
	
	public final PermissionAttributes getPermissionAttributes(final Identifier userId) 
	throws ApplicationException {
		PermissionAttributes permissionAttributes = null;
		LinkedIdsCondition domainCondition = 
			new LinkedIdsCondition(this.id, ObjectEntities.PERMATTR_CODE) {

			@Override
			public boolean isNeedMore(Set< ? extends StorableObject> storableObjects) {
				return false;
			}
		};
		
		LinkedIdsCondition userCondition = 
			new LinkedIdsCondition(userId, ObjectEntities.PERMATTR_CODE) {

				@Override
				public boolean isNeedMore(Set< ? extends StorableObject> storableObjects) {
					return false;
				}
			};

		CompoundCondition compoundCondition 
			= new CompoundCondition(
				domainCondition, 
				CompoundConditionSort.AND,
				userCondition);
		
		Set<PermissionAttributes> storableObjectsByCondition = 
			StorableObjectPool.getStorableObjectsByCondition(
			compoundCondition, 
			true);
		
		if (!storableObjectsByCondition.isEmpty()) {
			permissionAttributes = storableObjectsByCondition.iterator().next();
		}

		return permissionAttributes;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @throws CreateObjectException
	 */
	public static Domain createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description) throws CreateObjectException {
		try {
			final Domain domain = new Domain(IdentifierPool.getGeneratedIdentifier(ObjectEntities.DOMAIN_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description);

			assert domain.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			domain.markAsChanged();

			return domain;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
	}

	/**
	 *
	 * @param domain
	 * @return true if this is child of domain, false otherwise
	 */
	public boolean isChild(final Domain domain) {
		/**
		 * calculate parent tree
		 */
		return this.id.equals(domain.getId());
	}	

	public Identifier getParentDomainId() {
		return super.domainId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(1);
		if (!super.domainId.isVoid()) {
			dependencies.add(super.domainId);
		}
		return dependencies;
	}
}
