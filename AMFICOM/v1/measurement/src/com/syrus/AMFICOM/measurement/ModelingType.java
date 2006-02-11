/*-
 * $Id: ModelingType.java,v 1.65.2.2 2006/02/11 18:40:46 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.AMFICOM.measurement.corba.IdlModelingTypeHelper;

/**
 * @version $Revision: 1.65.2.2 $, $Date: 2006/02/11 18:40:46 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingType extends ActionType<ModelingType> {

	ModelingType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, creatorId, version, codename, description);
	}

	public ModelingType(final IdlModelingType idlModelingType) throws CreateObjectException {
		super(idlModelingType);
	}

	public static ModelingType createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws ApplicationException {
		if (creatorId == null || codename == null || description == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ModelingType modelingType = new ModelingType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_TYPE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					description);

			assert modelingType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			modelingType.markAsChanged();

			return modelingType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlModelingType getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlModelingTypeHelper.init(orb,
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
		final IdlModelingType idlModelingType = (IdlModelingType) transferable;
		super.fromTransferable(idlModelingType, idlModelingType.codename, idlModelingType.description);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	@Override
	public ModelingTypeWrapper getWrapper() {
		return ModelingTypeWrapper.getInstance();
	}
}
