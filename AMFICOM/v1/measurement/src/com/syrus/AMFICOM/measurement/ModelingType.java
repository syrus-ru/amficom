/*
 * $Id: ModelingType.java,v 1.50 2005/08/19 15:51:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlParameterTypeEnum;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.AMFICOM.measurement.corba.IdlModelingTypeHelper;

/**
 * @version $Revision: 1.50 $, $Date: 2005/08/19 15:51:01 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class ModelingType extends ActionType {
/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3904963062178854712L;

	public static final String CODENAME_DADARA = "dadara";

	private enum ParameterMode {
		MODE_IN("IN"),
		MODE_OUT("OUT");

		private String stringValue;

		private ParameterMode(final String stringValue) {
			this.stringValue = stringValue;
		}

		String stringValue() {
			return this.stringValue;
		}
	}

	private Set<ParameterType> inParameterTypes;
	private Set<ParameterType> outParameterTypes;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ModelingType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new HashSet<ParameterType>();
		this.outParameterTypes = new HashSet<ParameterType>();

		try {
			DatabaseContext.getDatabase(ObjectEntities.MODELING_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ModelingType(final IdlModelingType mtt) throws CreateObjectException {
		try {
			this.fromTransferable(mtt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ModelingType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final Set<ParameterType> inParameterTypes,
			final Set<ParameterType> outParameterTypes) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypes = new HashSet<ParameterType>();
		this.setInParameterTypes0(inParameterTypes);

		this.outParameterTypes = new HashSet<ParameterType>();
		this.setOutParameterTypes0(outParameterTypes);
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param outParameterTypes
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static ModelingType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<ParameterType> inParameterTypes,
			final Set<ParameterType> outParameterTypes) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final ModelingType modelingType = new ModelingType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					inParameterTypes,
					outParameterTypes);

			assert modelingType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			modelingType.markAsChanged();

			return modelingType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlModelingType mtt = (IdlModelingType) transferable;
		super.fromTransferable(mtt, mtt.codename, mtt.description);

		this.inParameterTypes = ParameterType.fromTransferables(mtt.inParameterTypes);
		this.outParameterTypes = ParameterType.fromTransferables(mtt.outParameterTypes);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlModelingType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final IdlParameterTypeEnum[] inParTypes = ParameterType.createTransferables(this.inParameterTypes, orb);
		final IdlParameterTypeEnum[] outParTypes = ParameterType.createTransferables(this.outParameterTypes, orb);

		return IdlModelingTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				inParTypes,
				outParTypes);
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObjectType#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.inParameterTypes != null && this.inParameterTypes != Collections.EMPTY_SET && !this.inParameterTypes.contains(null)
				&& this.outParameterTypes != null && this.outParameterTypes != Collections.EMPTY_SET && !this.outParameterTypes.contains(null);
	}

	public Set<ParameterType> getInParameterTypes() {
		return Collections.unmodifiableSet(this.inParameterTypes);
	}

	public Set<ParameterType> getOutParameterTypes() {
		return Collections.unmodifiableSet(this.outParameterTypes);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void setParameterTypes(final Map<String, Set<ParameterType>> parameterTypesModeMap) {
		assert parameterTypesModeMap != null : ErrorMessages.NON_NULL_EXPECTED;
		this.setInParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_IN.stringValue()));
		this.setOutParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_OUT.stringValue()));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Map<String, Set<ParameterType>> getParameterTypesModeMap() {
		final Map<String, Set<ParameterType>> parameterTypeIdsModeMap = new HashMap<String, Set<ParameterType>>(2);
		parameterTypeIdsModeMap.put(ParameterMode.MODE_IN.stringValue(), this.inParameterTypes);
		parameterTypeIdsModeMap.put(ParameterMode.MODE_OUT.stringValue(), this.outParameterTypes);
		return parameterTypeIdsModeMap;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypes0(final Set<ParameterType> inParameterTypes) {
		this.inParameterTypes.clear();
		if (inParameterTypes != null) {
			this.inParameterTypes.addAll(inParameterTypes);
		}
	}

	/**
	 * client setter for inParameterTypeIds
	 *
	 * @param inParameterTypes
	 *            The inParameterTypeIds to set.
	 */
	public void setInParameterTypes(final Set<ParameterType> inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.markAsChanged();		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypes0(final Set<ParameterType> outParameterTypes) {
		this.outParameterTypes.clear();
		if (outParameterTypes != null) {
			this.outParameterTypes.addAll(outParameterTypes);
		}
	}

	/**
	 * client setter for outParameterTypeIds
	 *
	 * @param outParameterTypes
	 *            The outParameterTypeIds to set.
	 */
	public void setOutParameterTypes(final Set<ParameterType> outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
				
		return dependencies;
	}
}
