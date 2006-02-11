/*-
 * $Id: ActionTemplate.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlActionTemplate;
import com.syrus.AMFICOM.measurement.corba.IdlActionTemplateHelper;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionTemplate extends StorableObject<ActionTemplate> {
	private String description;
	private Set<Identifier> actionParameterIds;

	ActionTemplate(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String description,
			final Set<Identifier> actionParameterIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.description = description;
		this.actionParameterIds = new HashSet<Identifier>();
		this.setActionParameterIds0(actionParameterIds);
	}

	public ActionTemplate(final IdlActionTemplate idlActionTemplate) throws CreateObjectException {
		try {
			this.fromTransferable(idlActionTemplate);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public static ActionTemplate createInstance(final Identifier creatorId,
			final String description,
			final Set<Identifier> actionParameterIds) throws CreateObjectException {
		if (creatorId == null
				|| description == null
				|| actionParameterIds == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ActionTemplate actionTemplate = new ActionTemplate(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ACTIONTEMPLATE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					description,
					actionParameterIds);

			assert actionTemplate.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			actionTemplate.markAsChanged();

			return actionTemplate;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlActionTemplate getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlActionTemplateHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.description,
				Identifier.createTransferables(this.actionParameterIds));
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlActionTemplate idlActionTemplate = (IdlActionTemplate) transferable;
		super.fromTransferable(idlActionTemplate);
		this.description = idlActionTemplate.description;
		this.setActionParameterIds0(Identifier.fromTransferables(idlActionTemplate.actionParameterIds));

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Set<Identifier> getActionParameterIds() {
		return Collections.unmodifiableSet(this.actionParameterIds);
	}

	protected synchronized void setActionParameterIds0(final Set<Identifier> actionParameterIds) {
		this.actionParameterIds.clear();
		if (actionParameterIds != null) {
			this.actionParameterIds.addAll(actionParameterIds);
		}
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.description = description;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.actionParameterIds != null
				&& StorableObject.getEntityCodeOfIdentifiables(this.actionParameterIds) == ObjectEntities.ACTIONPARAMETER_CODE;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.actionParameterIds);
		return dependencies;
	}

	@Override
	protected ActionTemplateWrapper getWrapper() {
		return ActionTemplateWrapper.getInstance();
	}

}
