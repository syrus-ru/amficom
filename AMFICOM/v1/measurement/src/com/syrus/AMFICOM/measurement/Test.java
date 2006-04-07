/*-
 * $Id: Test.java,v 1.183.2.22 2006/04/07 14:15:36 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CRONTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.INTERVALSTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERIODICALTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.measurement.corba.IdlTestHelper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestStops;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTemporalType;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStamps;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObject;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.183.2.22 $, $Date: 2006/04/07 14:15:36 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Test extends StorableObject implements IdlTransferableObjectExt<IdlTest>, Describable {
	private static final long serialVersionUID = 3614619269477477987L;

	private String description;
	private Identifier groupTestId;
	private Identifier monitoredElementId;
	private TestStatus status;
	private TestTemporalType temporalType;
	private TestTimeStamps timeStamps;
	private Set<Identifier> measurementSetupIds;
	private Identifier measurementTypeId;
	private int numberOfMeasurements;
	private Identifier analysisTypeId;

	private SortedMap<Date, String> stopMap;  

	private transient Identifier measurementPortId;
	private transient Identifier kisId;
	private transient Identifier mcmId;
	private transient Identifier currentMeasurementSetupId;

	Test(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final TestStatus status,
			final TestTemporalType temporalType,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId,
			final int numberOfMeasurements,
			final Identifier analysisTypeId,
			final SortedMap<Date, String> stopMap) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.description = description;
		this.groupTestId = groupTestId;
		this.monitoredElementId = monitoredElementId;
		this.status = status;
		this.temporalType = temporalType;

		/**
		 * Значение null соответствует случаю, когда конструктор
		 * {@link Test#Test(Identifier, Identifier, StorableObjectVersion, String, Identifier, Identifier, TestStatus, TestTemporalType, Date, Date, Identifier, Set, Identifier, int, Identifier, SortedMap)}
		 * вызывается из
		 * {@link TestDatabase#updateEntityFromResultSet(Test, ResultSet)}. В
		 * этом случае объект должен быть заполнен с помощью метода
		 * {@link Test#setAttributes(Date, Date, Identifier, Identifier, StorableObjectVersion, String, Identifier, Identifier, TestStatus, TestTemporalType, Date, Date, Identifier, Identifier, int, Identifier)}.
		 */
		if (this.temporalType != null) {
			this.timeStamps = new TestTimeStamps(startTime, endTime, temporalPatternId);
		}

		this.measurementSetupIds = new HashSet<Identifier>();
		this.setMeasurementSetupIds0(measurementSetupIds);
		this.measurementTypeId = measurementTypeId;
		this.numberOfMeasurements = numberOfMeasurements;
		this.analysisTypeId = analysisTypeId;
		this.stopMap = new TreeMap<Date, String>();
		this.setStopMap0(stopMap);
	}

	public Test(final IdlTest idlTest) throws CreateObjectException {
		this.measurementSetupIds = new HashSet<Identifier>();
		try {
			this.fromIdlTransferable(idlTest);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * Создать новый экземпляр одноразового задания.
	 * 
	 * @param creatorId
	 *        Идентификатор пользователя, создающего объект.
	 * @param description
	 *        Строка описания.
	 * @param groupTestId
	 *        Идентификатор старшего в группе задания. Для одиночного задания
	 *        (т. е. - задание вне какой-либо группы) должен быть
	 *        {@link Identifier#VOID_IDENTIFIER}. Не <code>null</code>.
	 * @param monitoredElementId
	 *        Идентификатор измеряемой линии. Не <code>null</code>.
	 * @param startTime
	 *        Время начала выполнения. Не <code>null</code>.
	 * @param measurementSetupIds
	 *        Идентификаторы шаблонов. Должно быть непустое множество.
	 * @param measurementTypeId
	 *        Идентификатор типа измерения. Не
	 *        {@link Identifier#VOID_IDENTIFIER}, не <code>null</code>.
	 * @param analysisTypeId
	 *        Идентификатор типа анализа. Может быть
	 *        {@link Identifier#VOID_IDENTIFIER}, не <code>null</code>.
	 * @return Новый экземпляр одноразового задания.
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final Date startTime,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId,
			final Identifier analysisTypeId) throws CreateObjectException {
		try {
			final Test test = new Test(IdentifierPool.getGeneratedIdentifier(TEST_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					groupTestId,
					monitoredElementId,
					TestStatus.TEST_STATUS_NEW,
					TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
					startTime,
					startTime,
					VOID_IDENTIFIER,
					measurementSetupIds,
					measurementTypeId,
					0,
					analysisTypeId,
					null);
			test.normalizeEndTime();

			assert test.isValid() : OBJECT_STATE_ILLEGAL;

			test.markAsChanged();

			return test;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * Создать новый экземпляр периодического задания.
	 * 
	 * @param creatorId
	 *        Идентификатор пользователя, создающего объект.
	 * @param description
	 *        Строка описания.
	 * @param groupTestId
	 *        Идентификатор старшего в группе задания. Для одиночного задания
	 *        (т. е. - задание вне какой-либо группы) должен быть
	 *        {@link Identifier#VOID_IDENTIFIER}. Не <code>null</code>.
	 * @param monitoredElementId
	 *        Идентификатор измеряемой линии. Не <code>null</code>.
	 * @param startTime
	 *        Время начала выполнения. Не <code>null</code>.
	 * @param endTime
	 *        Время окончания выполнения. Не <code>null</code>. Должно быть
	 *        позднее <code>startTime</code>.
	 * @param temporalPatternId
	 *        Идентификатор временного шаблона. Не
	 *        {@link Identifier#VOID_IDENTIFIER}, не <code>null</code>.
	 * @param measurementSetupIds
	 *        Идентификаторы шаблонов. Должно быть непустое множество.
	 * @param measurementTypeId
	 *        Идентификатор типа измерения. Не
	 *        {@link Identifier#VOID_IDENTIFIER}, не <code>null</code>.
	 * @param analysisTypeId
	 *        Идентификатор типа анализа. Может быть
	 *        {@link Identifier#VOID_IDENTIFIER}, не <code>null</code>.
	 * @return Новый экземпляр периодического задания.
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId,
			final Identifier analysisTypeId) throws CreateObjectException {
		try {
			final Test test = new Test(IdentifierPool.getGeneratedIdentifier(TEST_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					groupTestId,
					monitoredElementId,
					TestStatus.TEST_STATUS_NEW,
					TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL,
					startTime,
					endTime,
					temporalPatternId,
					measurementSetupIds,
					measurementTypeId,
					0,
					analysisTypeId,
					null);
			test.normalizeEndTime();

			assert test.isValid() : OBJECT_STATE_ILLEGAL;

			test.markAsChanged();

			return test;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTest getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final IdlTestStops[] idlTestStops = new IdlTestStops[this.stopMap.size()];
		int i = 0;
		for (final Date stopDate : this.stopMap.keySet()) {
			idlTestStops[i++] = new IdlTestStops(stopDate.getTime(), this.stopMap.get(stopDate));
		}

		return IdlTestHelper.init(orb,
				this.id.getIdlTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(orb),
				this.modifierId.getIdlTransferable(orb),
				this.version.longValue(),
				this.description,
				this.groupTestId.getIdlTransferable(orb),
				this.monitoredElementId.getIdlTransferable(orb),
				this.status.getIdlTransferable(),
				this.temporalType.getIdlTransferable(),
				this.timeStamps.getIdlTransferable(orb),
				Identifier.createTransferables(this.measurementSetupIds),
				this.measurementTypeId.getIdlTransferable(orb),
				this.numberOfMeasurements,
				this.analysisTypeId.getIdlTransferable(orb),
				idlTestStops);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void fromIdlTransferable(final IdlTest idlTest) throws IdlConversionException {
		super.fromIdlTransferable(idlTest);

		this.description = idlTest.description;
		this.groupTestId = Identifier.valueOf(idlTest.groupTestId);
		this.monitoredElementId = Identifier.valueOf(idlTest.monitoredElementId);
		this.status = TestStatus.valueOf(idlTest.status);
		this.temporalType = TestTemporalType.valueOf(idlTest.temporalType);
		this.timeStamps = new TestTimeStamps(idlTest.timeStamps);
		this.setMeasurementSetupIds0(Identifier.fromTransferables(idlTest.measurementSetupIds));
		this.measurementTypeId = Identifier.valueOf(idlTest.measurementTypeId);
		this.numberOfMeasurements = idlTest.numberOfMeasurements;
		this.analysisTypeId = Identifier.valueOf(idlTest.analysisTypeId);
		this.stopMap = new TreeMap<Date, String>();
		for (int i = 0; i < idlTest.stops.length; i++) {
			final IdlTestStops idlTestStops = idlTest.stops[i];
			this.stopMap.put(new Date(idlTestStops.time), idlTestStops.reason);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 */
	public void setDescription(final String description) {
		assert description != null : NON_NULL_EXPECTED;
		this.description = description;
		super.markAsChanged();
	}

	public String getName() {
		return this.description;
	}

	/**
	 * <p>
	 * Метод не поддерживается!
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void setName(final String name) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	public Identifier getGroupTestId() {
		return this.groupTestId;
	}

	public void setGroupTestId(final Identifier groupTestId) {
		assert groupTestId != null : NON_NULL_EXPECTED;
		assert (groupTestId.isVoid() || groupTestId.getMajor() == TEST_CODE) : ILLEGAL_ENTITY_CODE;
		this.groupTestId = groupTestId;
		this.markAsChanged();
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	public MonitoredElement getMonitoredElement() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.monitoredElementId, true);
	}

	/**
	 * @param monitoredElement
	 */
	public void setMonitoredElementId(final Identifier monitoredElementId) {
		assert monitoredElementId != null : NON_NULL_EXPECTED;
		assert monitoredElementId.getMajor() == MONITOREDELEMENT_CODE : ILLEGAL_ENTITY_CODE;
		this.monitoredElementId = monitoredElementId;
		super.markAsChanged();
	}

	public TestStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status
	 */
	public void setStatus(final TestStatus status) {
		assert status != null : NON_NULL_EXPECTED;
		this.status = status;
		super.markAsChanged();
	}

	public TestTemporalType getTemporalType() {
		return this.temporalType;
	}

	public Date getStartTime() {
		return this.timeStamps.getStartTime();
	}

	/**
	 * Выставить новое время начала.
	 * 
	 * @param startTime
	 *        Не <code>null</code>.
	 */
	public void setStartTime(final Date startTime) {
		assert startTime != null : NON_NULL_EXPECTED;
		this.timeStamps.setStartTime(startTime);
		super.markAsChanged();
	}

	public Date getEndTime() {
		return this.timeStamps.getEndTime();
	}

	/**
	 * Выставить новое время завершения.
	 * 
	 * @param endTime
	 *        Не <code>null</code>.
	 */
	public void setEndTime(final Date endTime) {
		assert endTime != null : NON_NULL_EXPECTED;
		this.timeStamps.setEndTime(endTime);
		super.markAsChanged();
	}

	public Identifier getTemporalPatternId() {
		return this.timeStamps.getTemporalPatternId();
	}

	/**
	 * <p>
	 * Выставить новый временной шаблон.
	 * <p>
	 * Если данное задание имеет периодический временной тип ({@link TestTemporalType#TEST_TEMPORAL_TYPE_PERIODICAL}),
	 * то выставляется новый идентификатор временного шаблона. В противном
	 * случае бросается {@link IllegalStateException}.
	 * 
	 * @param temporalPatternId
	 *        Идентификатор нового временного шаблона. Должен быть не пустой и
	 *        не <code>null</code>.
	 */
	public void setTemporalPatternId(final Identifier temporalPatternId) {
		if (this.temporalType == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL) {
			assert temporalPatternId != null : NON_NULL_EXPECTED;
			assert !temporalPatternId.isVoid() : NON_VOID_EXPECTED;
			final short temporalPatternIdMajor = temporalPatternId.getMajor();
			assert temporalPatternId.isVoid()
					|| temporalPatternIdMajor == CRONTEMPORALPATTERN_CODE
					|| temporalPatternIdMajor == INTERVALSTEMPORALPATTERN_CODE
					|| temporalPatternIdMajor == PERIODICALTEMPORALPATTERN_CODE
				: ILLEGAL_ENTITY_CODE;
			this.timeStamps.setTemporalPatternId(temporalPatternId);
			super.markAsChanged();
		}
		throw new IllegalStateException("Cannot set temporalPatternId to non-periodical test");
	}

	public Set<Identifier> getMeasurementSetupIds() {
		return Collections.unmodifiableSet(this.measurementSetupIds);
	}

	public void setMeasurementSetupIds(final Set<Identifier> measurementSetupIds) {
		assert measurementSetupIds != null : NON_NULL_EXPECTED;
		assert measurementSetupIds.size() > 0 : NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(measurementSetupIds) == MEASUREMENTSETUP_CODE : ILLEGAL_ENTITY_CODE;
		this.setMeasurementSetupIds0(measurementSetupIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	synchronized void setMeasurementSetupIds0(final Set<Identifier> measurementSetupIds) {
		this.measurementSetupIds.clear();
		if (measurementSetupIds != null) {
			this.measurementSetupIds.addAll(measurementSetupIds);
		}
	}

	public Identifier getMeasurementTypeId() {
		return this.measurementTypeId;
	}

	public MeasurementType getMeasurementType() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.measurementTypeId, true);
	}

	/**
	 * @param measurementType
	 */
	public void setMeasurementType(final Identifier measurementTypeId) {
		assert measurementTypeId != null : NON_NULL_EXPECTED;
		assert measurementTypeId.getMajor() == MEASUREMENT_TYPE_CODE : ILLEGAL_ENTITY_CODE;
		this.measurementTypeId = measurementTypeId;
		super.markAsChanged();
	}

	public int getNumberOfMeasurements() {
		return this.numberOfMeasurements;
	}

	/**
	 * @return Идентификатор типа анализа, или
	 *         {@link Identifier#VOID_IDENTIFIER}, если это задание без
	 *         анализа.
	 */
	public Identifier getAnalysisTypeId() {
		return this.analysisTypeId;
	}

	/**
	 * @return Тип анализа, или <code>null</code>, если это задание без
	 *         анализа.
	 * @throws ApplicationException
	 */
	public AnalysisType getAnalysisType() throws ApplicationException {
		if (!this.analysisTypeId.isVoid()) {
			return StorableObjectPool.getStorableObject(this.analysisTypeId, true);
		}
		return null;
	}

	/**
	 * @param analysisType
	 */
	public void setAnalysisTypeId(final Identifier analysisTypeId) {
		assert analysisTypeId != null : NON_NULL_EXPECTED;
		assert (analysisTypeId.isVoid() || analysisTypeId.getMajor() == ANALYSIS_TYPE_CODE) : ILLEGAL_ENTITY_CODE;
		this.analysisTypeId = analysisTypeId;
		super.markAsChanged();
	}

	/**
	 * @return Карту вида <дата остановки, причина остановки>
	 */
	public SortedMap<Date, String> getStopMap() {
		return Collections.unmodifiableSortedMap(this.stopMap);
	}

	/**
	 * Остановить задание сейчас.
	 * 
	 * @param reason
	 * @see #addStop(Date, String)
	 */
	public void addStop(final String reason) {
		this.addStop(new Date(), reason);
	}

	/**
	 * Остановить задание в указанное время.
	 * 
	 * @param stopTime
	 * @param reason
	 */
	public void addStop(final Date stopTime, final String reason) {
		assert stopTime != null : NON_NULL_EXPECTED;
		assert reason != null : NON_NULL_EXPECTED;
		this.stopMap.put(new Date(stopTime.getTime()), reason);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	synchronized void setStopMap0(final SortedMap<Date, String> stopMap) {
		this.stopMap.clear();
		if (stopMap != null) {
			this.stopMap.putAll(stopMap);
		}
	}

	public Identifier getMeasurementPortId() throws ApplicationException {
		if (this.measurementPortId == null) {
			this.measurementPortId = this.getMonitoredElement().getMeasurementPortId();
		}
		return this.measurementPortId;
	}

	public MeasurementPort getMeasurementPort() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getMeasurementPortId(), true);
	}

	public Identifier getKISId() throws ApplicationException {
		if (this.kisId == null) {
			this.kisId = this.getMeasurementPort().getKISId();
		}
		return this.kisId;
	}

	public KIS getKIS() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getKISId(), true);
	}

	public Identifier getMCMId() throws ApplicationException {
		if (this.mcmId == null) {
			final KIS kis = StorableObjectPool.getStorableObject(this.getKISId(), true);
			this.mcmId = kis.getMCMId();
		}
		return this.mcmId;
	}

	public MCM getMCM() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getMCMId(), true);
	}

	public Identifier getCurrentMeasurementSetupId() {
		this.ensureCurrentMeasurementSetupIsSet();
		return this.currentMeasurementSetupId;
	}

	/**
	 * Получить текущий шаблон данного задания. В случае невозможности
	 * подгрузить шаблон кидает исключение.
	 * 
	 * @return Текущий шаблон задания. Никогда не возвращает <code>null</code>.
	 * @throws ApplicationException
	 */
	public MeasurementSetup getCurrentMeasurementSetup() throws ApplicationException {
		final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(this.getCurrentMeasurementSetupId(), true);
		if (measurementSetup != null) {
			return measurementSetup;
		}
		throw new ObjectNotFoundException("Cannot find measurement setup '" + this.currentMeasurementSetupId + "' for test '" + this.id + "'");
	}

	/**
	 * @todo Implement more sophysticated method to determine current MeasurementSetup
	 * @todo заменить обратно s/CurrentMeasurementSetup/MainMeasurementSetup/gi; т.к. основной шаблон не меняется
	 */
	private void ensureCurrentMeasurementSetupIsSet() {
		if (this.currentMeasurementSetupId == null) {
			this.currentMeasurementSetupId = this.measurementSetupIds.iterator().next();
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		final boolean valid = super.isValid()
				&& this.description != null
				&& this.groupTestId != null
					&& (this.groupTestId.isVoid() || this.groupTestId.getMajor() == TEST_CODE)
				&& this.monitoredElementId != null
					&& this.monitoredElementId.getMajor() == MONITOREDELEMENT_CODE
				&& this.status != null
				&& this.temporalType != null
				&& this.timeStamps.isValid()
				&& this.measurementSetupIds != null
					&& this.measurementSetupIds.size() > 0
					&& StorableObject.getEntityCodeOfIdentifiables(this.measurementSetupIds) == MEASUREMENTSETUP_CODE
				&& this.measurementTypeId != null
					&& this.measurementTypeId.getMajor() == MEASUREMENT_TYPE_CODE
				&& this.numberOfMeasurements >= 0
				&& this.analysisTypeId != null
					&& (this.analysisTypeId.isVoid() || this.analysisTypeId.getMajor() == ANALYSIS_TYPE_CODE)
				&& this.stopMap != null;
		if (!valid) {
			return false;
		}

		try {
			final Set<MeasurementSetup> measurementSetups = StorableObjectPool.getStorableObjects(this.measurementSetupIds, true);
			for (final MeasurementSetup actionTemplate : measurementSetups) {
				if (!actionTemplate.isAttachedToMonitoredElement(this.monitoredElementId.getId())) {
					Log.errorMessage("Test '" + this.id + "': "
							+ "MeasurementSetup: '" + actionTemplate.getId()
							+ "' is not attached to MonitoredElement: '" + this.monitoredElementId.getId() + "'");
					return false;
				}
			}
			return true;
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return true;
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	public Measurement createMeasurement(final Identifier measurementCreatorId, final Date startTime) throws CreateObjectException {
		if (this.status != TestStatus.TEST_STATUS_PROCESSING) {
			throw new CreateObjectException("Status of test '" + this.id + "' is " + this.status + ", not PROCESSING");
		}

		MonitoredElement monitoredElement = null;
		ActionTemplate<Measurement> measurementTemplate = null;
		try {
			monitoredElement = StorableObjectPool.getStorableObject(this.monitoredElementId, true);
			final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(this.getCurrentMeasurementSetupId(), true);
			measurementTemplate = measurementSetup.getMeasurementTemplate();
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		if (monitoredElement == null) {
			throw new CreateObjectException("Cannot find monitored element '" + this.monitoredElementId + "'");
		}
		if (measurementTemplate == null) {
			throw new CreateObjectException("Cannot find measurement template");
		}
		final Measurement measurement = Measurement.createInstance(measurementCreatorId,
				this.measurementTypeId,
				this.monitoredElementId,
				measurementTemplate.getId(),
				LangModelMeasurement.getString("RR")
						+ "-" + monitoredElement.getName()
						+ "-" + EasyDateFormatter.formatDate(startTime),
				startTime,
				measurementTemplate.getApproximateActionDuration(),
				this.id);
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = measurementCreatorId;
		this.numberOfMeasurements++;

		super.markAsChanged();

		return measurement;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final TestStatus status,
			final TestTemporalType temporalType,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Identifier measurementTypeId,
			final int numberOfMeasurements,
			final Identifier analysisTypeId) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.description = description;
		this.groupTestId = groupTestId;
		this.monitoredElementId = monitoredElementId;
		this.status = status;
		this.temporalType = temporalType;
		this.timeStamps = new TestTimeStamps(startTime, endTime, temporalPatternId);
		this.measurementTypeId = measurementTypeId;
		this.numberOfMeasurements = numberOfMeasurements;
		this.analysisTypeId = analysisTypeId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();

		if (!this.groupTestId.isVoid() && !this.id.equals(this.groupTestId)) {
			dependencies.add(this.groupTestId);
		}

		dependencies.add(this.monitoredElementId);

		final Identifier temporalPatternId = this.timeStamps.getTemporalPatternId();
		if (this.temporalType == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL && !temporalPatternId.isVoid()) {
			dependencies.add(temporalPatternId);
		}

		dependencies.add(this.measurementTypeId);
		dependencies.addAll(this.measurementSetupIds);
		if (!this.analysisTypeId.isVoid()) {
			dependencies.add(this.analysisTypeId);
		}

		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected TestWrapper getWrapper() {
		return TestWrapper.getInstance();
	}

	public static IdlTest[] createTransferables(final Set<Test> tests, final ORB orb) {
		assert tests != null : NON_NULL_EXPECTED;

		final IdlTest[] transferables = new IdlTest[tests.size()];
		int i = 0;
		synchronized (tests) {
			for (final Test test : tests) {
				transferables[i++] = test.getIdlTransferable(orb);
			}
		}
		return transferables;
	}

	/**
	 * Normalize end date to finishing of last test's measurement
	 * 
	 * @throws ApplicationException
	 * @deprecated Объект сам должен себя нормализовать.
	 */
	@Deprecated
	public final void normalize() throws ApplicationException {
		this.normalizeEndTime();
	}

	/**
	 * Нормализовать время завершения.
	 * 
	 * @throws ApplicationException
	 */
	private void normalizeEndTime() throws ApplicationException {
		if (this.timeStamps.normalize()) {
			this.markAsChanged();
		}
	}

	/**
	 * Нормализует конечную дату. На данный момент поддерживает только
	 * одноразовые тесты и тесты с периодическим временным шаблоном.
	 * 
	 * @param startTime
	 *        начальная дата, не <code>null</code>
	 * @param endTime
	 *        Действующий контракт: ненормализованная конечная дата, не раньше
	 *        startTime, не <code>null</code>
	 *        <p>
	 * 		@todo <b>Новый (тестовый) контракт</b>: ненормализованная конечная дата,
	 *       <b>позднее startTime</b>, не <code>null</code>.
	 * @param temporalPattern
	 *        временной шаблон либо <code>null</code> для одноразового теста
	 * @param measurementSetup
	 *        параметры измерения (для определения продолжительности измерения)
	 * @return нормализованная конечная дата. Если параметр endTime уже
	 *         нормализован, то может возвращать тот же объект endTime.
	 * @throws ApplicationException
	 */
	public static Date normalizeEndDate(final Date startTime,
			final Date endTime,
			final AbstractTemporalPattern temporalPattern,
			final MeasurementSetup measurementSetup) throws ApplicationException {
		assert startTime != null;
		assert endTime != null;
		assert measurementSetup != null;
		assert !endTime.before(startTime);
		/*Новый контракт - так правильнее.
		assert endTime.after(startTime);*/

		final long duration = measurementSetup.calcTotalDuration();

		final long start = startTime.getTime();
		final long end = endTime.getTime();

		final long normalizedEnd;
		if (temporalPattern == null) {
			normalizedEnd = start + duration;
		} else {
			assert (temporalPattern instanceof PeriodicalTemporalPattern) :
				"Normalization of end time for " + temporalPattern.getClass().getName() + " is not supported";
			final long period = ((PeriodicalTemporalPattern) temporalPattern).getPeriod();
			normalizedEnd = start + period * ((end - start) / period) + duration;
		}

		if (end == normalizedEnd) {
			return endTime;
		}
		return new Date(normalizedEnd);
	}



	public static enum TestStatus {
		TEST_STATUS_NEW,		//Сохранён в БД на сервере
		TEST_STATUS_SCHEDULED,	//Передан на МУИ и сохранён в БД
		TEST_STATUS_PROCESSING,	//Извлечён из очереди; первое измерение начинает выполняться
		TEST_STATUS_COMPLETED,	//Последнее измерение завершено
		TEST_STATUS_STOPPING,	//Остановливается по запросу
		TEST_STATUS_STOPPED,	//Остановлен по запросу
		TEST_STATUS_ABORTED;	//Прерван

		private static final TestStatus[] VALUES = values();

		IdlTestStatus getIdlTransferable() {
			return IdlTestStatus.from_int(this.ordinal());
		}

		static TestStatus valueOf(final int code) {
			return VALUES[code];
		}

		static TestStatus valueOf(final IdlTestStatus idlTestStatus) {
			return valueOf(idlTestStatus.value());
		}

		@Override
		public String toString() {
			return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
		}
	}

	public static enum TestTemporalType {
		TEST_TEMPORAL_TYPE_ONETIME,
		TEST_TEMPORAL_TYPE_PERIODICAL;

		private static final TestTemporalType[] VALUES = values();

		IdlTestTemporalType getIdlTransferable() {
			return IdlTestTemporalType.from_int(this.ordinal());
		}

		static TestTemporalType valueOf(final int code) {
			return VALUES[code];
		}

		static TestTemporalType valueOf(final IdlTestTemporalType idlTestTemporalType) {
			return valueOf(idlTestTemporalType.value());
		}

		@Override
		public String toString() {
			return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
		}
	}

	private final class TestTimeStamps implements IdlTransferableObject<IdlTestTimeStamps>, Serializable {
		private static final long serialVersionUID = -5285003774876473339L;

		private Date startTime;
		private Date endTime;
		private Identifier temporalPatternId;

		TestTimeStamps(final Date startTime, final Date endTime, final Identifier temporalPatternId) {
			this.startTime = startTime;
			this.endTime = endTime;
			final TestTemporalType testTemporalType = this.getTestTemporalType();
			switch (testTemporalType) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					this.temporalPatternId = VOID_IDENTIFIER;
					break;
				case TEST_TEMPORAL_TYPE_PERIODICAL:
					this.temporalPatternId = temporalPatternId;
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal temporal type: " + testTemporalType + " of test");
			}

			assert this.isValid() : OBJECT_STATE_ILLEGAL;
		}

		TestTimeStamps(final IdlTestTimeStamps idlTestTimeStamps) {
			this.startTime = new Date(idlTestTimeStamps.startTime);
			this.endTime = new Date(idlTestTimeStamps.endTime);
			this.temporalPatternId = Identifier.valueOf(idlTestTimeStamps.temporalPatternId);

			assert this.isValid() : OBJECT_STATE_ILLEGAL;
		}

		/**
		 * @param orb
		 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
		 */
		public IdlTestTimeStamps getIdlTransferable(final ORB orb) {
			assert this.isValid() : OBJECT_STATE_ILLEGAL;

			return new IdlTestTimeStamps(this.startTime.getTime(),
					this.endTime.getTime(),
					this.temporalPatternId.getIdlTransferable());
		}

		/**
		 * Действующий контракт: конечная дата <u>не раньше</u> начальной.
		 * 
		 * @todo Новый (тестовый) контракт: конечная дата <u>позднее</u>
		 *       начальной. См. также
		 *       {@link Test#normalizeEndDate(Date, Date, AbstractTemporalPattern, MeasurementSetup)}.
		 */
		boolean isValid() {
			if (this.startTime == null || this.endTime == null || this.temporalPatternId == null) {
				return false;
			}
			if (this.endTime.before(this.startTime)) {
				return false;
			}
			/*Новый контракт
			if (!this.endTime.after(this.startTime)) {
				return false;
			}*/
			switch (this.getTestTemporalType()) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					return this.temporalPatternId.isVoid();
				case TEST_TEMPORAL_TYPE_PERIODICAL:
					if (this.temporalPatternId.isVoid()) {
						return false;
					}
					final short temporalPatternIdMajor = this.temporalPatternId.getMajor();
					return (temporalPatternIdMajor == CRONTEMPORALPATTERN_CODE
									|| temporalPatternIdMajor == INTERVALSTEMPORALPATTERN_CODE
									|| temporalPatternIdMajor == PERIODICALTEMPORALPATTERN_CODE)
							&& this.startTime.before(this.endTime);
				default:
					return false;
			}
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 37 * result + this.startTime.hashCode();
			result = 37 * result + this.endTime.hashCode();
			result = 37 * result + this.temporalPatternId.hashCode();
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof TestTimeStamps) {
				final TestTimeStamps that = (TestTimeStamps) obj;
				if (that.getTestTemporalType() == this.getTestTemporalType()
						&& (this.startTime == that.startTime || 
								this.startTime != null && this.startTime.equals(that.startTime))
						&& (this.endTime == that.endTime || 
							this.endTime != null && this.endTime.equals(that.endTime))
						&& (this.temporalPatternId == that.temporalPatternId || 
								this.temporalPatternId != null && this.temporalPatternId.equals(that.temporalPatternId))) {
					return true;
				}
			}
			return false;
		}

		private TestTemporalType getTestTemporalType() {
			return Test.this.getTemporalType();
		}

		Date getStartTime() {
			return this.startTime;
		}

		Date getEndTime() {
			return this.endTime;
		}

		Identifier getTemporalPatternId() {
			return this.temporalPatternId;
		}

		void setStartTime(final Date startTime) {
			this.startTime = startTime;
		}

		void setEndTime(final Date endTime) {
			this.endTime = endTime;
		}

		void setTemporalPatternId(final Identifier temporalPatternId) {
			this.temporalPatternId = temporalPatternId;
		}

		boolean normalize() throws ApplicationException {
			final AbstractTemporalPattern temporalPattern;
			switch (this.getTestTemporalType()) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					temporalPattern = null;
					break;
				default:
					temporalPattern = StorableObjectPool.getStorableObject(this.temporalPatternId, true);
					assert (temporalPattern != null) : NON_NULL_EXPECTED;
			}
			final Date normalizedEnd = Test.normalizeEndDate(this.startTime,
					this.endTime,
					temporalPattern,
					Test.this.getCurrentMeasurementSetup());

			if (normalizedEnd.equals(this.endTime)) {
				return false;
			}
			this.endTime = normalizedEnd;
			return true;
		}
	}
}
