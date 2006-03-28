/*-
 * $Id: ActionTemplate.java,v 1.1.2.11 2006/03/28 08:48:16 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlActionTemplate;
import com.syrus.AMFICOM.measurement.corba.IdlActionTemplateHelper;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * Шаблон действия. Каждое действие, т. е., наследник
 * {@link com.syrus.AMFICOM.measurement.Action}, имеет свой шаблон. Шаблон
 * действия должен быть привязан к одной или более измеряемых линий
 * {@link com.syrus.AMFICOM.measurement.MonitoredElement}. Таким образом,
 * каждая измеряемая линия имеет свой набор шаблонов, по которым на ней можно
 * проводить данное действие.
 * 
 * @version $Revision: 1.1.2.11 $, $Date: 2006/03/28 08:48:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionTemplate extends StorableObject implements IdlTransferableObjectExt<IdlActionTemplate> {
	private static final long serialVersionUID = 725853453958152504L;

	/**
	 * Описание.
	 */
	private String description;

	/**
	 * Приблизительная продолжительность действия с данным шаблоном.
	 */
	private long approximateActionDuration;

	/**
	 * Набор идентификаторов параметров действия.
	 * XXX Возможно, придётся заменить Set на List.
	 */
	private Set<Identifier> actionParameterIds;

	/**
	 * Набор идентификаторов измеряемых линий, к которым привязан данный шаблон.
	 */
	private Set<Identifier> monitoredElementIds;

	ActionTemplate(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String description,
			final long approximateActionDuration,
			final Set<Identifier> actionParameterIds,
			final Set<Identifier> monitoredElementIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.description = description;
		this.approximateActionDuration = approximateActionDuration;
		this.actionParameterIds = new HashSet<Identifier>();
		this.setActionParameterIds0(actionParameterIds);
		this.monitoredElementIds = new HashSet<Identifier>();
		this.setMonitoredElementIds0(monitoredElementIds);
	}

	public ActionTemplate(final IdlActionTemplate idlActionTemplate) throws CreateObjectException {
		this.actionParameterIds = new HashSet<Identifier>();
		this.monitoredElementIds = new HashSet<Identifier>();
		try {
			this.fromIdlTransferable(idlActionTemplate);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * Создать новый экземпляр.
	 * 
	 * @param creatorId
	 * @param description
	 * @param approximateActionDuration
	 * @param actionParameterIds
	 * @param monitoredElementIds
	 * @return Новый экземпляр.
	 * @throws CreateObjectException
	 */
	public static ActionTemplate createInstance(final Identifier creatorId,
			final String description,
			final long approximateActionDuration,
			final Set<Identifier> actionParameterIds,
			final Set<Identifier> monitoredElementIds) throws CreateObjectException {
		if (creatorId == null
				|| description == null
				|| actionParameterIds == null
				|| monitoredElementIds == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ActionTemplate actionTemplate = new ActionTemplate(IdentifierPool.getGeneratedIdentifier(ACTIONTEMPLATE_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					approximateActionDuration,
					actionParameterIds,
					monitoredElementIds);

			assert actionTemplate.isValid() : OBJECT_STATE_ILLEGAL;

			actionTemplate.markAsChanged();

			return actionTemplate;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlActionTemplate getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlActionTemplateHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(orb),
				super.modifierId.getIdlTransferable(orb),
				super.version.longValue(),
				this.description,
				this.approximateActionDuration,
				Identifier.createTransferables(this.actionParameterIds),
				Identifier.createTransferables(this.monitoredElementIds));
	}

	public synchronized void fromIdlTransferable(final IdlActionTemplate idlActionTemplate) throws IdlConversionException {
		super.fromIdlTransferable(idlActionTemplate);

		this.description = idlActionTemplate.description;
		this.approximateActionDuration = idlActionTemplate.approximateActionDuration;
		this.setActionParameterIds0(Identifier.fromTransferables(idlActionTemplate.actionParameterIds));
		this.setMonitoredElementIds0(Identifier.fromTransferables(idlActionTemplate.monitoredElementIds));

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * Получить описание.
	 * 
	 * @return Описание
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Выставить описание.
	 * 
	 * @param description
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * Получить приблизительную длительность действия, выполняемого по данному
	 * шаблону.
	 * 
	 * @return Приблизительная длительность действия
	 */
	public long getApproximateActionDuration() {
		return this.approximateActionDuration;
	}

	/**
	 * Выставить приблизительную длительность действия, выполняемого по данному
	 * шаблону.
	 * 
	 * @param approximateActionDuration
	 */
	public void setApproximateActionDuration(final long approximateActionDuration) {
		this.approximateActionDuration = approximateActionDuration;
		super.markAsChanged();
	}

	/**
	 * Получить список идентификаторов параметров действия.
	 * 
	 * @return Список идентификаторов параметров действия
	 */
	public Set<Identifier> getActionParameterIds() {
		return Collections.unmodifiableSet(this.actionParameterIds);
	}

	/**
	 * Получить список параметров действия. Обёртка над
	 * {@link #getActionParameterIds()}.
	 * 
	 * @return Список параметров действия
	 * @throws ApplicationException
	 */
	public Set<ActionParameter> getActionParameters() throws ApplicationException {
		return StorableObjectPool.getStorableObjects(this.actionParameterIds, true);
	}

	protected synchronized void setActionParameterIds0(final Set<Identifier> actionParameterIds) {
		this.actionParameterIds.clear();
		if (actionParameterIds != null) {
			this.actionParameterIds.addAll(actionParameterIds);
		}
	}

	/**
	 * Проверить, привязан ли данный шаблон действия к данной измеряемой линии.
	 * 
	 * @param monitoredElementId
	 * @return <code>true</code>, если данный шаблон привязан к линии
	 *         <code>monitoredElementId</code>.
	 */
	public boolean isAttachedToMonitoredElement(final Identifier monitoredElementId) {
		assert monitoredElementId.getMajor() == MONITOREDELEMENT_CODE : ILLEGAL_ENTITY_CODE;
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	/**
	 * Получить набор идентификаторов измеряемых линий, к которым привязан
	 * данный шаблон действия.
	 * 
	 * @return Набор идентификаторов, к которым привязан данный шаблон
	 */
	public Set<Identifier> getMonitoredElementIds() {
		return Collections.unmodifiableSet(this.monitoredElementIds);
	}

	protected synchronized void setMonitoredElementIds0(final Set<Identifier> monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null) {
			this.monitoredElementIds.addAll(monitoredElementIds);
		}
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String description,
			final long approximateActionDuration) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.description = description;
		this.approximateActionDuration = approximateActionDuration;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.actionParameterIds != null
				&& StorableObject.getEntityCodeOfIdentifiables(this.actionParameterIds) == ACTIONPARAMETER_CODE
				&& this.monitoredElementIds != null
				&& StorableObject.getEntityCodeOfIdentifiables(this.monitoredElementIds) == MONITOREDELEMENT_CODE;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.actionParameterIds);
		dependencies.addAll(this.monitoredElementIds);
		return dependencies;
	}

	@Override
	protected ActionTemplateWrapper getWrapper() {
		return ActionTemplateWrapper.getInstance();
	}

	/**
	 * Для всех параметров действия, входящих в данный шаблон, создать карту,
	 * где по ключу - кодовое имя, а по величине - величина соответствующего
	 * параметра. Этот метод предполагает, что среди всех параметров действия
	 * данного шаблона не найдётся ни одной пары с одинаковыми типами
	 * {@link com.syrus.AMFICOM.general.ParameterType}. См. также
	 * {@link Parameter#getTypeCodenameValueMap(Set)}.
	 * 
	 * @return Неизменяемая карта вида <String parameterTypeCodename, byte[]
	 *         parameterValue>
	 * @throws ApplicationException
	 */
	public Map<String, byte[]> getParameterTypeCodenameValueMap() throws ApplicationException {
		final Set<ActionParameter> actionParameters = StorableObjectPool.getStorableObjects(this.actionParameterIds, true);
		return Parameter.getTypeCodenameValueMap(actionParameters);
	}
}
