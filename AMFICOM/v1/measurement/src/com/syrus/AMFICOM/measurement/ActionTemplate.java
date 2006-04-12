/*-
 * $Id: ActionTemplate.java,v 1.1.2.15 2006/04/12 13:01:49 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
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
 * <p>
 * ������ ��������.
 * <p>
 * ������ ��������, �. �., ���������
 * {@link com.syrus.AMFICOM.measurement.Action}, ����� ���� ������. ������
 * �������� �������� � ���� ��������
 * {@link com.syrus.AMFICOM.measurement.ActionType} (���� {@link #actionTypeId})
 * � ���� �������������� �����
 * {@link com.syrus.AMFICOM.measurement.MeasurementPortType} (����
 * {@link #measurementPortTypeId}). ��� ���� ������ ��������������� �����������
 * ����� ������������� ������
 * {@link com.syrus.AMFICOM.measurement.ActionParameterTypeBinding} ��� �������
 * �� �������� � ������ ���������� {@link #actionParameterIds}.
 * <p>
 * ����� ����, ������ �������� ������ ���� �������� � ����� ��� ����� ����������
 * ����� {@link com.syrus.AMFICOM.measurement.MonitoredElement}. ����� �������,
 * ������ ���������� ����� ����� ���� ����� ��������, �� ������� �� ��� �����
 * ��������� ������ ��������.
 * 
 * @version $Revision: 1.1.2.15 $, $Date: 2006/04/12 13:01:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionTemplate<T extends Action> extends StorableObject implements IdlTransferableObjectExt<IdlActionTemplate> {
	private static final long serialVersionUID = -5951315596905172529L;

	/**
	 * ������������� ���� ��������.
	 */
	private Identifier actionTypeId;

	/**
	 * ������������� ���� �������������� �����.
	 */
	private Identifier measurementPortTypeId;

	/**
	 * ��������.
	 */
	private String description;

	/**
	 * ��������������� ����������������� �������� � ������ ��������.
	 */
	private long approximateActionDuration;

	/**
	 * ����� ��������������� ���������� ��������.
	 * XXX ��������, ���ģ��� �������� Set �� List.
	 */
	private Set<Identifier> actionParameterIds;

	/**
	 * ����� ��������������� ���������� �����, � ������� �������� ������ ������.
	 */
	private Set<Identifier> monitoredElementIds;

	ActionTemplate(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId,
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
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
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
	 * ������� ����� ���������.
	 * <p>
	 * ���� ����� ���������, ��� ���������� ��� ��������
	 * <code>actionTypeId</code> � ���������� ��� �������������� �����
	 * <code>measurementPortTypeId</code> ������� ��� ���������� ��������
	 * <code>actionParameterIds</code>. ������� �������, ������������� ������
	 * {@link ActionParameterTypeBinding} ��� ������� �� ����������
	 * <code>actionParameterIds</code>, ������ ��������� ��� ��������
	 * <code>actionTypeId</code> � ��� �������������� �����
	 * <code>measurementPortTypeId</code>. ���� ��� �� ���, ��������
	 * {@link IllegalArgumentException}.
	 * <p>
	 * ����� ����, ��� �������� �������� ����������� �� �����������
	 * <code>null</code>, � <code>actionParameterIds</code> ������ ����
	 * ��������. ���� ��� ������� �� �����������, ����� ��������
	 * {@link IllegalArgumentException}.
	 * 
	 * @param <TT>
	 * @param creatorId
	 * @param actionTypeId
	 * @param measurementPortTypeId
	 * @param description
	 * @param approximateActionDuration
	 * @param actionParameterIds
	 * @param monitoredElementIds
	 * @return ����� ���������.
	 * @throws CreateObjectException
	 */
	public static <TT extends Action> ActionTemplate<TT> createInstance(final Identifier creatorId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId,
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
		if (actionParameterIds.isEmpty()) {
			throw new IllegalArgumentException(NON_VOID_EXPECTED);
		}
		try {
			final Set<ActionParameter> actionParameters = StorableObjectPool.getStorableObjects(actionParameterIds, true);
			for (final ActionParameter actionParameter : actionParameters) {
				final Identifier parameterActionTypeId = actionParameter.getBinding().getActionTypeId();
				if (!parameterActionTypeId.equals(actionTypeId)) {
					throw new IllegalArgumentException("ActionType '"
							+ parameterActionTypeId + "' for ActionParameter '" + actionParameter.getId()
							+ "' differs from ActionType '" + actionTypeId + "' for ActionTemplate to create");
				}

				final Identifier parameterMeasurementPortTypeId = actionParameter.getBinding().getMeasurementPortTypeId();
				if (!parameterMeasurementPortTypeId.equals(measurementPortTypeId)) {
					throw new IllegalArgumentException("MeasurementPortType '"
							+ parameterMeasurementPortTypeId + "' for ActionParameter '" + actionParameter.getId()
							+ "' differs from MeasurementPortType '" + measurementPortTypeId + "' for ActionTemplate to create");
				}
			}
		} catch (ApplicationException ae) {
			throw new CreateObjectException("Cannot validate ActionType and MeasurementPortType", ae);
		}

		try {
			final ActionTemplate<TT> actionTemplate = new ActionTemplate<TT>(IdentifierPool.getGeneratedIdentifier(ACTIONTEMPLATE_CODE),
					creatorId,
					INITIAL_VERSION,
					actionTypeId,
					measurementPortTypeId,
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
				this.actionTypeId.getIdlTransferable(),
				this.measurementPortTypeId.getIdlTransferable(),
				this.description,
				this.approximateActionDuration,
				Identifier.createTransferables(this.actionParameterIds),
				Identifier.createTransferables(this.monitoredElementIds));
	}

	public synchronized void fromIdlTransferable(final IdlActionTemplate idlActionTemplate) throws IdlConversionException {
		super.fromIdlTransferable(idlActionTemplate);

		this.actionTypeId = Identifier.valueOf(idlActionTemplate.actionTypeId);
		this.measurementPortTypeId = Identifier.valueOf(idlActionTemplate.measurementPortTypeId);
		this.description = idlActionTemplate.description;
		this.approximateActionDuration = idlActionTemplate.approximateActionDuration;

		this.setActionParameterIds0(Identifier.fromTransferables(idlActionTemplate.actionParameterIds));
		this.setMonitoredElementIds0(Identifier.fromTransferables(idlActionTemplate.monitoredElementIds));

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * �������� ������������� ���� ��������.
	 * 
	 * @return ������������� ���� ��������.
	 */
	public Identifier getActionTypeId() {
		return this.actionTypeId;
	}

	/**
	 * �������� ��� ��������.
	 * 
	 * @return ��� ��������.
	 * @throws ApplicationException
	 */
	public ActionType getActionType() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.actionTypeId, true);
	}

	/**
	 * �������� ������������� ���� �������������� �����.
	 * 
	 * @return ������������� ���� �������������� �����.
	 */
	public Identifier getMeasurementPortTypeId() {
		return this.measurementPortTypeId;
	}

	/**
	 * �������� ��� �������������� �����.
	 * 
	 * @return ��� �������������� �����.
	 * @throws ApplicationException
	 */
	public MeasurementPortType getMeasurementPortType() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.measurementPortTypeId, true);
	}

	/**
	 * �������� ��������.
	 * 
	 * @return ��������
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * ��������� ��������.
	 * 
	 * @param description
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * �������� ��������������� ������������ ��������, ������������ �� �������
	 * �������.
	 * 
	 * @return ��������������� ������������ ��������
	 */
	public long getApproximateActionDuration() {
		return this.approximateActionDuration;
	}

	/**
	 * ��������� ��������������� ������������ ��������, ������������ �� �������
	 * �������.
	 * 
	 * @param approximateActionDuration
	 */
	public void setApproximateActionDuration(final long approximateActionDuration) {
		this.approximateActionDuration = approximateActionDuration;
		super.markAsChanged();
	}

	/**
	 * �������� ������ ��������������� ���������� ��������.
	 * 
	 * @return ������ ��������������� ���������� ��������
	 */
	public Set<Identifier> getActionParameterIds() {
		return Collections.unmodifiableSet(this.actionParameterIds);
	}

	/**
	 * �������� ������ ���������� ��������. �£���� ���
	 * {@link #getActionParameterIds()}.
	 * 
	 * @return ������ ���������� ��������
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
	 * ���������, �������� �� ������ ������ �������� � ������ ���������� �����.
	 * 
	 * @param monitoredElementId
	 * @return <code>true</code>, ���� ������ ������ �������� � �����
	 *         <code>monitoredElementId</code>.
	 */
	public boolean isAttachedToMonitoredElement(final Identifier monitoredElementId) {
		assert monitoredElementId.getMajor() == MONITOREDELEMENT_CODE : ILLEGAL_ENTITY_CODE;
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	/**
	 * �������� ����� ��������������� ���������� �����, � ������� ��������
	 * ������ ������ ��������.
	 * 
	 * @return ����� ���������������, � ������� �������� ������ ������
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
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId,
			final String description,
			final long approximateActionDuration) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
		this.description = description;
		this.approximateActionDuration = approximateActionDuration;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.actionTypeId != null
				&& (this.actionTypeId.getMajor() == MEASUREMENT_TYPE_CODE
						|| this.actionTypeId.getMajor() == ANALYSIS_TYPE_CODE
						|| this.actionTypeId.getMajor() == MODELING_TYPE_CODE)
				&& this.measurementPortTypeId != null
				&& this.measurementPortTypeId.getMajor() == MEASUREMENTPORT_TYPE_CODE
				&& this.actionParameterIds != null
				&& !this.actionParameterIds.isEmpty()
				&& StorableObject.getEntityCodeOfIdentifiables(this.actionParameterIds) == ACTIONPARAMETER_CODE
				&& this.monitoredElementIds != null
				&& (this.monitoredElementIds.isEmpty() || StorableObject.getEntityCodeOfIdentifiables(this.monitoredElementIds) == MONITOREDELEMENT_CODE);
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.actionTypeId);
		dependencies.add(this.measurementPortTypeId);
		dependencies.addAll(this.actionParameterIds);
		dependencies.addAll(this.monitoredElementIds);
		return dependencies;
	}

	@Override
	protected ActionTemplateWrapper getWrapper() {
		return ActionTemplateWrapper.getInstance();
	}

	/**
	 * ��� ���� ���������� ��������, �������� � ������ ������, ������� �����,
	 * ��� �� ����� - ������� ���, � �� �������� - �������� ����������������
	 * ���������. ���� ����� ������������, ��� ����� ���� ���������� ��������
	 * ������� ������� �� ���ģ��� �� ����� ���� � ����������� ������
	 * {@link com.syrus.AMFICOM.general.ParameterType}. ��. �����
	 * {@link Parameter#getTypeCodenameValueMap(Set)}.
	 * 
	 * @return ������������ ����� ���� <String parameterTypeCodename, byte[]
	 *         parameterValue>
	 * @throws ApplicationException
	 */
	public Map<String, byte[]> getParameterTypeCodenameValueMap() throws ApplicationException {
		final Set<ActionParameter> actionParameters = StorableObjectPool.getStorableObjects(this.actionParameterIds, true);
		return Parameter.getTypeCodenameValueMap(actionParameters);
	}
}
