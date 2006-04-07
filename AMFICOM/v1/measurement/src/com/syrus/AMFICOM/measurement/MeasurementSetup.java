/*-
 * $Id: MeasurementSetup.java,v 1.100.2.15 2006/04/07 07:23:03 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementSetup;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementSetupHelper;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;


/**
 * <p>
 * Шаблон измерительного задания.
 * <p>
 * Каждое задание {@link com.syrus.AMFICOM.measurement.Test} должно иметь по
 * меньшей мере один такой шаблон. Шаблон измерительного задания состоит из двух
 * частей: шаблон измерения {@link #measurementTemplateId} и шаблон анализа
 * {@link #analysisTemplateId}. Шаблон анализа может быть равен
 * {@link com.syrus.AMFICOM.general.Identifier#VOID_IDENTIFIER} (что
 * соответствует заданию без анализа), шаблон измерения - никогда. Шаблон
 * измерительного задания должен быть привязан к одной или более измеряемых
 * линий {@link com.syrus.AMFICOM.measurement.MonitoredElement}; таким образом,
 * каждая измеряемая линия имеет свой набор шаблонов, по которым на ней можно
 * проводить измерения.
 * <p>
 * Поскольку каждый из шаблонов действия - шаблон измерения и шаблон анализа -
 * сам по себе уже привязан к какому-то набору измеряемых линий, то созданный на
 * их основе шаблон измерительного задания может быть привязан лишь к тем
 * линиям, к которым привязан каждый из составляющих его шаблонов действия.
 * 
 * @version $Revision: 1.100.2.15 $, $Date: 2006/04/07 07:23:03 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementSetup extends StorableObject implements IdlTransferableObjectExt<IdlMeasurementSetup> {
	private static final long serialVersionUID = -2848488077315796037L;

	/**
	 * Идентификатор шаблона измерения.
	 */
	private Identifier measurementTemplateId;

	/**
	 * Идентификатор шаблона анализа.
	 */
	private Identifier analysisTemplateId;

	/**
	 * Описание.
	 */
	private String description;

	/**
	 * Набор идентификаторов измеряемых линий, к которым привязан данный шаблон.
	 */
	private Set<Identifier> monitoredElementIds;


	/**
	 * Вспомогательное поле. используется в {@link #isValid()}.
	 */
	private transient Set<Identifier> actionTemplateIds;

	MeasurementSetup(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier measurementTemplateId,
			final Identifier analysisTemplateId,
			final String description,
			final Set<Identifier> monitoredElementIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.measurementTemplateId = measurementTemplateId;
		this.analysisTemplateId = analysisTemplateId;
		this.description = description;
		this.monitoredElementIds = new HashSet<Identifier>();
		this.setMonitoredElementIds0(monitoredElementIds);
	}

	public MeasurementSetup(final IdlMeasurementSetup idlMeasurementSetup) throws CreateObjectException {
		this.monitoredElementIds = new HashSet<Identifier>();
		try {
			this.fromIdlTransferable(idlMeasurementSetup);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * Создать новый экземпляр.
	 * 
	 * @param creatorId
	 * @param measurementTemplateId
	 * @param analysisTemplateId
	 * @param description
	 * @param monitoredElementIds
	 * @return Новый экземпляр.
	 * @throws CreateObjectException
	 */
	public static MeasurementSetup createInstance(final Identifier creatorId,
			final Identifier measurementTemplateId,
			final Identifier analysisTemplateId,
			final String description,
			final Set<Identifier> monitoredElementIds) throws CreateObjectException {
		if (creatorId == null
				|| measurementTemplateId == null
				|| analysisTemplateId == null
				|| description == null
				|| monitoredElementIds == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final MeasurementSetup measurementSetup = new MeasurementSetup(IdentifierPool.getGeneratedIdentifier(MEASUREMENTSETUP_CODE),
					creatorId,
					INITIAL_VERSION,
					measurementTemplateId,
					analysisTemplateId,
					description,
					monitoredElementIds);

			assert measurementSetup.isValid() : OBJECT_STATE_ILLEGAL;

			measurementSetup.markAsChanged();

			return measurementSetup;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlMeasurementSetup getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlMeasurementSetupHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(orb),
				super.modifierId.getIdlTransferable(orb),
				super.version.longValue(),
				this.measurementTemplateId.getIdlTransferable(orb),
				this.analysisTemplateId.getIdlTransferable(orb),
				this.description,
				Identifier.createTransferables(this.monitoredElementIds));
	}

	public synchronized void fromIdlTransferable(final IdlMeasurementSetup idlMeasurementSetup) throws IdlConversionException {
		super.fromIdlTransferable(idlMeasurementSetup);

		this.measurementTemplateId = Identifier.valueOf(idlMeasurementSetup.measurementTemplateId);
		this.analysisTemplateId = Identifier.valueOf(idlMeasurementSetup.analysisTemplateId);
		this.description = idlMeasurementSetup.description;
		this.setMonitoredElementIds0(Identifier.fromTransferables(idlMeasurementSetup.monitoredElementIds));

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * Получить идентификатор шаблона измерения.
	 * 
	 * @return Идентификатор шаблона измерения
	 */
	public Identifier getMeasurementTemplateId() {
		return this.measurementTemplateId;
	}

	/**
	 * Получить шаблон измерения. Обёртка над
	 * {@link #getMeasurementTemplateId()}.
	 * 
	 * @return Шаблон измерения
	 * @throws ApplicationException
	 */
	public ActionTemplate<Measurement> getMeasurementTemplate() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.measurementTemplateId, true);
	}

	/**
	 * Выставить новый идентификатор шаблона измерения.
	 * 
	 * @param measurementTemplateId
	 */
	public void setMeasurementTemplateId(final Identifier measurementTemplateId) {
		this.measurementTemplateId = measurementTemplateId;
		super.markAsChanged();
	}

	/**
	 * Получить идентификатор шаблона анализа.
	 * 
	 * @return Идентификатор шаблона анализа; никогда не равен <code>null</code>,
	 *         может быть
	 *         {@link com.syrus.AMFICOM.general.Identifier#VOID_IDENTIFIER}.
	 */
	public Identifier getAnalysisTemplateId() {
		return this.analysisTemplateId;
	}

	/**
	 * Получить шаблон анализа. Обёртка над {@link #getAnalysisTemplateId()}.
	 * 
	 * @return Шаблон анализа, если есть. Иначе - <code>null</code>.
	 * @throws ApplicationException
	 */
	public ActionTemplate<Analysis> getAnalysisTemplate() throws ApplicationException {
		if (this.analysisTemplateId.isVoid()) {
			return null;
		}
		return StorableObjectPool.getStorableObject(this.analysisTemplateId, true);
	}

	/**
	 * Выставить новый идентификатор шаблона анализа.
	 * 
	 * @param analysisTemplateId
	 */
	public void setAnalysisTemplateId(final Identifier analysisTemplateId) {
		this.analysisTemplateId = analysisTemplateId;
		super.markAsChanged();
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
	 * Проверить, привязан ли данный шаблон измерительного задания к данной
	 * измеряемой линии.
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
	 * данный шаблон измерительного задания.
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
			final Identifier measurementTemplateId,
			final Identifier analysisTemplateId,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.measurementTemplateId = measurementTemplateId;
		this.analysisTemplateId = analysisTemplateId;
		this.description = description;
	}

	@Override
	protected boolean isValid() {
		final boolean valid = super.isValid()
				&& this.measurementTemplateId != null && this.measurementTemplateId.getMajor() == ACTIONTEMPLATE_CODE
				&& this.analysisTemplateId != null && (this.analysisTemplateId.isVoid() || this.analysisTemplateId.getMajor() == ACTIONTEMPLATE_CODE);
		if (!valid) {
			return false;
		}

		if (this.actionTemplateIds == null) {
			this.actionTemplateIds = new HashSet<Identifier>();
			this.actionTemplateIds.add(this.measurementTemplateId);
			if (!this.analysisTemplateId.isVoid()) {
				this.actionTemplateIds.add(this.analysisTemplateId);
			}
		}
		try {
			final Set<ActionTemplate<? extends Action>> actionTemplates = StorableObjectPool.getStorableObjects(this.actionTemplateIds, true);
			if (actionTemplates.size() < this.actionTemplateIds.size()) {
				Log.errorMessage("Cannot load all action templates for measurement setup '" + this.id + "'; loaded: " + actionTemplates);
				return false;
			}
			for (final ActionTemplate<? extends Action> actionTemplate : actionTemplates) {
				final Set<Identifier> actionTemplateMEIds = actionTemplate.getMonitoredElementIds();
				if (!actionTemplateMEIds.containsAll(this.monitoredElementIds)) {
					Log.errorMessage("Action template '"
							+ actionTemplate.getId() + "' attached to monitored elements: " + actionTemplateMEIds
							+ "; uncompatible with monitored elements of measurement setup '" + this.id + "': " + this.monitoredElementIds);
					return false;
				}
			}
			return true;
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return true;
		}
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.measurementTemplateId);
		dependencies.add(this.analysisTemplateId);
		dependencies.addAll(this.monitoredElementIds);
		return dependencies;
	}

	@Override
	protected MeasurementSetupWrapper getWrapper() {
		return MeasurementSetupWrapper.getInstance();
	}	

	/**
	 * Вычисляет продолжительность выполнения и обработки одного измерения
	 * по этому шаблону.
	 * <p>
	 * XXX: это просто замена старого метода getMeasurementDuration(),
	 * которая суммирует время измерения и время анализа.
	 * На самом деле новое измерение в принципе может идти одновременно с
	 * анализом предыдущего.
	 * 
	 * @return продолжительность выполнения и обработки одного измерения
	 * @throws ApplicationException
	 */
	public long calcTotalDuration() throws ApplicationException {
		long ret = this.getMeasurementTemplate().getApproximateActionDuration();
		if (!this.analysisTemplateId.isVoid()) {
			ret += this.getAnalysisTemplate().getApproximateActionDuration();
		}
		return ret;
	}
}
