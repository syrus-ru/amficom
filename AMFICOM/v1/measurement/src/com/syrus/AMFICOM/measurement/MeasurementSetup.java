/*-
 * $Id: MeasurementSetup.java,v 1.100.2.18 2006/04/13 12:48:30 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
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
 * ������ �������������� �������.
 * <p>
 * ������ ������� {@link com.syrus.AMFICOM.measurement.Test} ������ ����� ��
 * ������� ���� ���� ����� ������. ������ �������������� ������� ������� �� ����
 * ������: ������ ��������� {@link #measurementTemplateId} � ������ �������
 * {@link #analysisTemplateId}. ������ ������� ����� ���� �����
 * {@link com.syrus.AMFICOM.general.Identifier#VOID_IDENTIFIER} (���
 * ������������� ������� ��� �������), ������ ��������� - �������. ������
 * �������������� ������� ������ ���� �������� � ����� ��� ����� ����������
 * ����� {@link com.syrus.AMFICOM.measurement.MonitoredElement}; ����� �������,
 * ������ ���������� ����� ����� ���� ����� ��������, �� ������� �� ��� �����
 * ��������� ���������.
 * <p>
 * ������ �������������� ������� �������� � ���� �������������� �����
 * {@link com.syrus.AMFICOM.measurement.MeasurementPortType} (����
 * {@link #measurementPortTypeId}). ��� ���� ������ ��������������� �����������
 * ����� �������� ��������� � �������, �������� � ������ ������ ��������������
 * �������.
 * <p>
 * ��������� ������ �� �������� �������� - ������ ��������� � ������ ������� -
 * ��� �� ���� ��� �������� � ������-�� ������ ���������� �����, �� ��������� ��
 * �� ������ ������ �������������� ������� ����� ���� �������� ���� � ���
 * ������, � ������� �������� ������ �� ������������ ��� �������� ��������.
 * 
 * @version $Revision: 1.100.2.18 $, $Date: 2006/04/13 12:48:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementSetup extends StorableObject implements IdlTransferableObjectExt<IdlMeasurementSetup> {
	private static final long serialVersionUID = 7296011372140670277L;

	/**
	 * ������������� ���� �������������� �����.
	 */
	private Identifier measurementPortTypeId;

	/**
	 * ������������� ������� ���������.
	 */
	private Identifier measurementTemplateId;

	/**
	 * ������������� ������� �������.
	 */
	private Identifier analysisTemplateId;

	/**
	 * ��������.
	 */
	private String description;

	/**
	 * ����� ��������������� ���������� �����, � ������� �������� ������ ������.
	 */
	private Set<Identifier> monitoredElementIds;


	/**
	 * ��������������� ����. ������������ � {@link #isValid()}.
	 */
	private transient Set<Identifier> actionTemplateIds;

	MeasurementSetup(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier measurementPortTypeId,
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
		this.measurementPortTypeId = measurementPortTypeId;
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
	 * ������� ����� ���������.
	 * <p>
	 * ���� ����� ���������, ��� ���������� ��� �������������� �����
	 * <code>measurementPortTypeId</code> ������� ��� �������� ���������
	 * <code>measurementTemplateId</code> � �������
	 * <code>analysisTemplateId</code>. ������� �������, ��� �������
	 * <code>measurementTemplateId</code> � <code>analysisTemplateId</code>
	 * ������ ����� ��� �������������� ����� <code>measurementPortTypeId</code>.
	 * ���� ��� �� ���, �������� {@link IllegalArgumentException}.
	 * <p>
	 * ����� ����, ��� �������� �������� ����������� �� �����������
	 * <code>null</code>. ���� �������� �� ��������, ����� ��������
	 * {@link IllegalArgumentException}.
	 * 
	 * @param creatorId
	 * @param measurementPortTypeId
	 * @param measurementTemplateId
	 * @param analysisTemplateId
	 * @param description
	 * @param monitoredElementIds
	 * @return ����� ���������.
	 * @throws CreateObjectException
	 */
	public static MeasurementSetup createInstance(final Identifier creatorId,
			final Identifier measurementPortTypeId,
			final Identifier measurementTemplateId,
			final Identifier analysisTemplateId,
			final String description,
			final Set<Identifier> monitoredElementIds) throws CreateObjectException {
		if (creatorId == null
				|| measurementPortTypeId == null
				|| measurementTemplateId == null
				|| analysisTemplateId == null
				|| description == null
				|| monitoredElementIds == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}
		try {
			final ActionTemplate<Measurement> measurementTemplate = StorableObjectPool.getStorableObject(measurementTemplateId, true);
			final Identifier mTemplateMeasurementPortTypeId = measurementTemplate.getMeasurementPortTypeId();
			if (!mTemplateMeasurementPortTypeId.equals(measurementPortTypeId)) {
				throw new IllegalArgumentException("MeasurementPortType '"
						+ mTemplateMeasurementPortTypeId + "' of measurement ActionTemplate '" + measurementTemplateId
						+ "' differs from MeasurementPortType '" + measurementPortTypeId + "' of MeasurementSetup to create");
			}

			if (!analysisTemplateId.isVoid()) {
				final ActionTemplate<Analysis> analysisTemplate = StorableObjectPool.getStorableObject(analysisTemplateId, true);
				final Identifier aTemplateMeasurementPortTypeId = analysisTemplate.getMeasurementPortTypeId();
				if (!aTemplateMeasurementPortTypeId.equals(measurementPortTypeId)) {
					throw new IllegalArgumentException("MeasurementPortType '"
							+ aTemplateMeasurementPortTypeId + "' of analysis ActionTemplate '" + analysisTemplateId
							+ "' differs from MeasurementPortType '" + measurementPortTypeId + "' of MeasurementSetup to create");
				}
			}
		} catch (ApplicationException ae) {
			throw new CreateObjectException("Cannot validate MeasurementPortType", ae);
		}

		try {
			final MeasurementSetup measurementSetup = new MeasurementSetup(IdentifierPool.getGeneratedIdentifier(MEASUREMENTSETUP_CODE),
					creatorId,
					INITIAL_VERSION,
					measurementPortTypeId,
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
				this.measurementPortTypeId.getIdlTransferable(orb),
				this.measurementTemplateId.getIdlTransferable(orb),
				this.analysisTemplateId.getIdlTransferable(orb),
				this.description,
				Identifier.createTransferables(this.monitoredElementIds));
	}

	public synchronized void fromIdlTransferable(final IdlMeasurementSetup idlMeasurementSetup) throws IdlConversionException {
		super.fromIdlTransferable(idlMeasurementSetup);

		this.measurementPortTypeId = Identifier.valueOf(idlMeasurementSetup.measurementPortTypeId);
		this.measurementTemplateId = Identifier.valueOf(idlMeasurementSetup.measurementTemplateId);
		this.analysisTemplateId = Identifier.valueOf(idlMeasurementSetup.analysisTemplateId);
		this.description = idlMeasurementSetup.description;
		this.setMonitoredElementIds0(Identifier.fromTransferables(idlMeasurementSetup.monitoredElementIds));

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
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
	 * �������� ������������� ������� ���������.
	 * 
	 * @return ������������� ������� ���������
	 */
	public Identifier getMeasurementTemplateId() {
		return this.measurementTemplateId;
	}

	/**
	 * �������� ������ ���������. �£���� ���
	 * {@link #getMeasurementTemplateId()}.
	 * 
	 * @return ������ ���������
	 * @throws ApplicationException
	 */
	public ActionTemplate<Measurement> getMeasurementTemplate() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.measurementTemplateId, true);
	}

	/**
	 * ��������� ����� ������������� ������� ���������.
	 * 
	 * @param measurementTemplateId
	 */
	public void setMeasurementTemplateId(final Identifier measurementTemplateId) {
		this.measurementTemplateId = measurementTemplateId;
		super.markAsChanged();
	}

	/**
	 * �������� ������������� ������� �������.
	 * 
	 * @return ������������� ������� �������; ������� �� ����� <code>null</code>,
	 *         ����� ����
	 *         {@link com.syrus.AMFICOM.general.Identifier#VOID_IDENTIFIER}.
	 */
	public Identifier getAnalysisTemplateId() {
		return this.analysisTemplateId;
	}

	/**
	 * �������� ������ �������. �£���� ��� {@link #getAnalysisTemplateId()}.
	 * 
	 * @return ������ �������, ���� ����. ����� - <code>null</code>.
	 * @throws ApplicationException
	 */
	public ActionTemplate<Analysis> getAnalysisTemplate() throws ApplicationException {
		if (this.analysisTemplateId.isVoid()) {
			return null;
		}
		return StorableObjectPool.getStorableObject(this.analysisTemplateId, true);
	}

	/**
	 * ��������� ����� ������������� ������� �������.
	 * 
	 * @param analysisTemplateId
	 */
	public void setAnalysisTemplateId(final Identifier analysisTemplateId) {
		this.analysisTemplateId = analysisTemplateId;
		super.markAsChanged();
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
	 * ���������, �������� �� ������ ������ �������������� ������� � ������
	 * ���������� �����.
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
	 * ������ ������ �������������� �������.
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
			final Identifier measurementPortTypeId,
			final Identifier measurementTemplateId,
			final Identifier analysisTemplateId,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.measurementPortTypeId = measurementPortTypeId;
		this.measurementTemplateId = measurementTemplateId;
		this.analysisTemplateId = analysisTemplateId;
		this.description = description;
	}

	@Override
	protected boolean isValid() {
		final boolean valid = super.isValid()
				&& this.measurementPortTypeId != null
				&& this.measurementPortTypeId.getMajor() == MEASUREMENTPORT_TYPE_CODE
				&& this.measurementTemplateId != null
				&& this.measurementTemplateId.getMajor() == ACTIONTEMPLATE_CODE
				&& this.analysisTemplateId != null
				&& (this.analysisTemplateId.isVoid() || this.analysisTemplateId.getMajor() == ACTIONTEMPLATE_CODE)
				&& this.monitoredElementIds != null
				&& (this.monitoredElementIds.isEmpty() || StorableObject.getEntityCodeOfIdentifiables(this.monitoredElementIds) == MONITOREDELEMENT_CODE);
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
		dependencies.add(this.measurementPortTypeId);
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
	 * ��������� ����������������� ���������� � ��������� ������ ���������
	 * �� ����� �������.
	 * <p>
	 * XXX: ��� ������ ������ ������� ������ getMeasurementDuration(),
	 * ������� ��������� ����� ��������� � ����� �������.
	 * �� ����� ���� ����� ��������� � �������� ����� ���� ������������ �
	 * �������� �����������.
	 * 
	 * @return ����������������� ���������� � ��������� ������ ���������
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
