/*-
 * $Id: ActionParameterTypeBinding.java,v 1.1.2.17 2006/04/06 10:23:04 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_FOUND;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterTypeBindingHelper;
import com.syrus.AMFICOM.measurement.corba.IdlParameterValueKind;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;


/**
 * <p>
 * Измерительная связка. Связка трёх сущностей: типа параметра
 * {@link com.syrus.AMFICOM.general.ParameterType}, типа измерительного порта
 * {@link com.syrus.AMFICOM.measurement.MeasurementPortType} и типа действия
 * {@link com.syrus.AMFICOM.measurement.ActionType}. Для каждой такой связки
 * существует свой набор допустимых значений параметра. Измерительные связки
 * создаются на этапе первоначальной установки системы, либо на этапе её
 * настройки. Конечный пользователь не должен их создавать, а должен подгружать
 * с помощью методов {@link #valueOf(Identifier, Identifier, Identifier)},
 * {@link #valueOf(ParameterType, ActionType, MeasurementPortType)}, а также
 * {@link #getValues(ActionType, MeasurementPortType)},
 * {@link #getValues(Identifier, Identifier)}.
 * <p>
 * Набор значений может быть двух видов: перечисляемый
 * {@link com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind#ENUMERATED}
 * и непрерывный
 * {@link com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind#CONTINUOUS};
 * вид набора значений хранится в {@link #parameterValueKind}. Если он -
 * перечисляемый, то все допустимые параметры для данной связки (т. е. - данного
 * типа параметра {@link #parameterTypeId} для данного типа действия
 * {@link #actionTypeId} и на данном типе измерительного порта
 * {@link #measurementPortTypeId}) уже должны быть предопределены при установке
 * системы. Пользователь не должен создавать новые экземпляры параметров
 * {@link com.syrus.AMFICOM.measurement.ActionParameter} для измерительных
 * связок {@link ActionParameterTypeBinding} с перечисляемым набором значений
 * параметра. Если же набор значений - непрерывный, то пользователь может
 * создавать новые параметры
 * {@link com.syrus.AMFICOM.measurement.ActionParameter} с ещё не существующими
 * значениями.
 * <p>
 * В любом из этих двух случаев можно использовать метод
 * {@link #getActionParameters()} для получения всех параметров, существующих
 * для данной измерительной связки.
 * 
 * @version $Revision: 1.1.2.17 $, $Date: 2006/04/06 10:23:04 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterTypeBinding extends StorableObject implements IdlTransferableObjectExt<IdlActionParameterTypeBinding> {
	private static final long serialVersionUID = 8851510439449075891L;

	/**
	 * Виды набора значений параметра - перечисляемый и непрерывный.
	 */
	static enum ParameterValueKind {
		ENUMERATED,
		CONTINUOUS;

		private static final ParameterValueKind VALUES[] = values();

		IdlParameterValueKind getIdlTransferable() {
			return IdlParameterValueKind.from_int(this.ordinal());
		}

		static ParameterValueKind valueOf(final int code) {
			return VALUES[code];
		}

		static ParameterValueKind valueOf(final IdlParameterValueKind idlParameterValueKind) {
			return valueOf(idlParameterValueKind.value());
		}

		@Override
		public String toString() {
			return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
		}
	}

	/**
	 * Вид набора значений параметра.
	 */
	private ParameterValueKind parameterValueKind;

	/**
	 * Тип параметра.
	 */
	private Identifier parameterTypeId;

	/**
	 * Тип действия.
	 */
	private Identifier actionTypeId;

	/**
	 * Тип измерительного порта.
	 */
	private Identifier measurementPortTypeId;


	/**
	 * Условие для подгрузки параметров, существующих для данной измерительной
	 * связки. См. {@link #getActionParameters()}.
	 */
	private transient LinkedIdsCondition actionParameterCondition;

	/**
	 * Условие для подгрузки измерительной связки по типу параметра.
	 * Используется в {@link #valueOf(Identifier, Identifier, Identifier)}.
	 */
	private static LinkedIdsCondition parameterTypeIdCondition;

	/**
	 * Условие для подгрузки измерительной связки по типу действия. Используется
	 * в {@link #getValues(Identifier, Identifier)} и в
	 * {@link #valueOf(Identifier, Identifier, Identifier)}.
	 */
	private static LinkedIdsCondition actionTypeIdCondition;

	/**
	 * Условие для подгрузки измерительной связки по типу измерительного порта.
	 * Используется в {@link #getValues(Identifier, Identifier)} и в
	 * {@link #valueOf(Identifier, Identifier, Identifier)}.
	 */
	private static LinkedIdsCondition measurementPortTypeIdCondition;

	ActionParameterTypeBinding(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final ParameterValueKind parameterValueKind,
			final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.parameterValueKind = parameterValueKind;
		this.parameterTypeId = parameterTypeId;
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
	}

	public ActionParameterTypeBinding(final IdlActionParameterTypeBinding idlActionParameterTypeBinding) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlActionParameterTypeBinding);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * Создать новый экземпляр.
	 * 
	 * @param creatorId
	 * @param parameterValueKind
	 * @param parameterTypeId
	 * @param actionTypeId
	 * @param measurementPortTypeId
	 * @return Новый экземпляр измерительной связки.
	 * @throws CreateObjectException
	 */
	public static ActionParameterTypeBinding createInstance(final Identifier creatorId,
			final ParameterValueKind parameterValueKind,
			final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) throws CreateObjectException {
		if (creatorId == null
				|| parameterValueKind == null
				|| parameterTypeId == null
				|| actionTypeId == null
				|| measurementPortTypeId == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ActionParameterTypeBinding actionParameterTypeBinding = new ActionParameterTypeBinding(IdentifierPool.getGeneratedIdentifier(ACTIONPARAMETERTYPEBINDING_CODE),
					creatorId,
					INITIAL_VERSION,
					parameterValueKind,
					parameterTypeId,
					actionTypeId,
					measurementPortTypeId);

			assert actionParameterTypeBinding.isValid() : OBJECT_STATE_ILLEGAL;

			actionParameterTypeBinding.markAsChanged();

			return actionParameterTypeBinding;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlActionParameterTypeBinding getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlActionParameterTypeBindingHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.parameterValueKind.getIdlTransferable(),
				this.parameterTypeId.getIdlTransferable(orb),
				this.actionTypeId.getIdlTransferable(orb),
				this.measurementPortTypeId.getIdlTransferable(orb));
	}

	public synchronized void fromIdlTransferable(final IdlActionParameterTypeBinding idlActionParameterTypeBinding) throws IdlConversionException {
		super.fromIdlTransferable(idlActionParameterTypeBinding);

		this.parameterValueKind = ParameterValueKind.valueOf(idlActionParameterTypeBinding.parameterValueKind);
		this.parameterTypeId = Identifier.valueOf(idlActionParameterTypeBinding.parameterTypeId);
		this.actionTypeId = Identifier.valueOf(idlActionParameterTypeBinding.actionTypeId);
		this.measurementPortTypeId = Identifier.valueOf(idlActionParameterTypeBinding.measurementPortTypeId);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * Получить вид набора значений параметра для данной измерительной связки.
	 * 
	 * @return Вид набора значений параметра.
	 */
	public ParameterValueKind getParameterValueKind() {
		return this.parameterValueKind;
	}

	/**
	 * Получить идентификатор типа параметра.
	 * 
	 * @return Идентификатор типа параметра.
	 */
	public Identifier getParameterTypeId() {
		return this.parameterTypeId;
	}

	/**
	 * Получить тип параметра. Обёртка для {@link #getParameterTypeId()}.
	 * 
	 * @return Тип параметра.
	 * @throws ApplicationException
	 */
	public ParameterType getParameterType() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.parameterTypeId, true);
	}

	/**
	 * Получить идентификатор типа действия.
	 * 
	 * @return Идентификатор типа действия.
	 */
	public Identifier getActionTypeId() {
		return this.actionTypeId;
	}

	/**
	 * Получить тип действия. Обёртка для {@link #getActionTypeId()}.
	 * 
	 * @return Тип действия.
	 * @throws ApplicationException
	 */
	public ActionType getActionType() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.actionTypeId, true);
	}

	/**
	 * Получить идентификатор типа измерительного порта.
	 * 
	 * @return Идентификатор типа измерительного порта.
	 */
	public Identifier getMeasurementPortTypeId() {
		return this.measurementPortTypeId;
	}

	/**
	 * Получить тип измерительного порта. Обёртка для
	 * {@link #getMeasurementPortTypeId()}.
	 * 
	 * @return Тип измерительного порта.
	 * @throws ApplicationException
	 */
	public MeasurementPortType getMeasurementPortType() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.measurementPortTypeId, true);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final ParameterValueKind parameterValueKind,
			final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.parameterValueKind = parameterValueKind;
		this.parameterTypeId = parameterTypeId;
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
	}

	@Override
	protected boolean isValid() {
		if (this.actionTypeId == null) {
			return false;
		}

		final short actionTypeIdMajor = this.actionTypeId.getMajor();
		return super.isValid()
				&& this.parameterValueKind != null
				&& this.parameterTypeId != null && this.parameterTypeId.getMajor() == PARAMETER_TYPE_CODE
				&& (actionTypeIdMajor == MEASUREMENT_TYPE_CODE
						|| actionTypeIdMajor == ANALYSIS_TYPE_CODE
						|| actionTypeIdMajor == MODELING_TYPE_CODE)
				&& this.measurementPortTypeId != null && this.measurementPortTypeId.getMajor() == MEASUREMENTPORT_TYPE_CODE;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parameterTypeId);
		dependencies.add(this.actionTypeId);
		dependencies.add(this.measurementPortTypeId);
		return dependencies;
	}

	@Override
	protected ActionParameterTypeBindingWrapper getWrapper() {
		return ActionParameterTypeBindingWrapper.getInstance();
	}

	/**
	 * Получить все существующие параметры действия для данной измерительной
	 * связки.
	 * 
	 * @return Набор параметров для данной измерительной связки.
	 * @throws ApplicationException
	 */
	public Set<ActionParameter> getActionParameters() throws ApplicationException {
		if (this.actionParameterCondition == null) {
			this.actionParameterCondition = new LinkedIdsCondition(this, ACTIONPARAMETER_CODE);
		}

		return StorableObjectPool.getStorableObjectsByCondition(this.actionParameterCondition, true);
	}

	/**
	 * Найти все измерительные связки для данного типа действия и данного типа
	 * измерительного порта.
	 * 
	 * @param actionType
	 * @param measurementPortType
	 * @return Набор измерительных связок для данного типа действия и данного
	 *         типа измерительного порта.
	 * @throws ApplicationException
	 */
	public static Set<ActionParameterTypeBinding> getValues(final ActionType actionType,
			final MeasurementPortType measurementPortType) throws ApplicationException {
		assert actionType != null : NON_NULL_EXPECTED;
		assert measurementPortType != null : NON_NULL_EXPECTED;

		return getValues(actionType.getId(), measurementPortType.getId());
	}

	/**
	 * Найти все измерительные связки для данного типа действия и данного типа
	 * измерительного порта.
	 * 
	 * @param actionTypeId
	 * @param measurementPortTypeId
	 * @return Набор измерительных связок для данного типа действия и данного
	 *         типа измерительного порта.
	 * @throws ApplicationException
	 */
	public static Set<ActionParameterTypeBinding> getValues(final Identifier actionTypeId,
			final Identifier measurementPortTypeId) throws ApplicationException {
		assert actionTypeId != null : NON_NULL_EXPECTED;
		assert measurementPortTypeId != null : NON_NULL_EXPECTED;

		assert actionTypeId.getMajor() == MEASUREMENT_TYPE_CODE
				|| actionTypeId.getMajor() == ANALYSIS_TYPE_CODE
				|| actionTypeId.getMajor() == MODELING_TYPE_CODE : ILLEGAL_ENTITY_CODE;
		assert measurementPortTypeId.getMajor() == MEASUREMENTPORT_TYPE_CODE : ILLEGAL_ENTITY_CODE;

		if (actionTypeIdCondition == null) {
			actionTypeIdCondition = new LinkedIdsCondition(actionTypeId, ACTIONPARAMETERTYPEBINDING_CODE);
		} else {
			actionTypeIdCondition.setLinkedIdentifiable(actionTypeId);
		}

		if (measurementPortTypeIdCondition == null) {
			measurementPortTypeIdCondition = new LinkedIdsCondition(measurementPortTypeId, ACTIONPARAMETERTYPEBINDING_CODE);
		} else {
			measurementPortTypeIdCondition.setLinkedIdentifiable(measurementPortTypeId);
		}

		final CompoundCondition condition = new CompoundCondition(actionTypeIdCondition, AND, measurementPortTypeIdCondition);
		return StorableObjectPool.getStorableObjectsByCondition(condition, true);
	}

	/**
	 * Найти измерительную связку по заданным типу параметра, типу действия и
	 * типу измерительного порта.
	 * 
	 * @param parameterType
	 * @param actionType
	 * @param measurementPortType
	 * @return Измерительную связку для заданных типа параметра, типа действия и
	 *         типа измерительного порта.
	 * @throws ApplicationException
	 *         {@link CreateObjectException}, если такой объект не найден;
	 *         {@link ApplicationException} в случае ошибки поиска.
	 */
	public static ActionParameterTypeBinding valueOf(final ParameterType parameterType,
			final ActionType actionType,
			final MeasurementPortType measurementPortType) throws ApplicationException {
		assert parameterType != null : NON_NULL_EXPECTED;
		assert actionType != null : NON_NULL_EXPECTED;
		assert measurementPortType != null : NON_NULL_EXPECTED;

		return valueOf(parameterType.getId(), actionType.getId(), measurementPortType.getId());
	}

	/**
	 * Найти измерительную связку по заданным типу параметра, типу действия и
	 * типу измерительного порта.
	 * 
	 * @param parameterTypeId
	 * @param actionTypeId
	 * @param measurementPortTypeId
	 * @return Измерительную связку для заданных типа параметра, типа действия и
	 *         типа измерительного порта.
	 * @throws ApplicationException
	 *         {@link CreateObjectException}, если такой объект не найден;
	 *         {@link ApplicationException} в случае ошибки поиска.
	 */
	public static ActionParameterTypeBinding valueOf(final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) throws ApplicationException {
		assert parameterTypeId != null : NON_NULL_EXPECTED;
		assert actionTypeId != null : NON_NULL_EXPECTED;
		assert measurementPortTypeId != null : NON_NULL_EXPECTED;

		assert parameterTypeId.getMajor() == PARAMETER_TYPE_CODE : ILLEGAL_ENTITY_CODE;
		assert actionTypeId.getMajor() == MEASUREMENT_TYPE_CODE
				|| actionTypeId.getMajor() == ANALYSIS_TYPE_CODE
				|| actionTypeId.getMajor() == MODELING_TYPE_CODE : ILLEGAL_ENTITY_CODE;
		assert measurementPortTypeId.getMajor() == MEASUREMENTPORT_TYPE_CODE : ILLEGAL_ENTITY_CODE;

		if (parameterTypeIdCondition == null) {
			parameterTypeIdCondition = new LinkedIdsCondition(parameterTypeId, ACTIONPARAMETERTYPEBINDING_CODE);
		} else {
			parameterTypeIdCondition.setLinkedIdentifiable(parameterTypeId);
		}

		if (actionTypeIdCondition == null) {
			actionTypeIdCondition = new LinkedIdsCondition(actionTypeId, ACTIONPARAMETERTYPEBINDING_CODE);
		} else {
			actionTypeIdCondition.setLinkedIdentifiable(actionTypeId);
		}

		if (measurementPortTypeIdCondition == null) {
			measurementPortTypeIdCondition = new LinkedIdsCondition(measurementPortTypeId, ACTIONPARAMETERTYPEBINDING_CODE);
		} else {
			measurementPortTypeIdCondition.setLinkedIdentifiable(measurementPortTypeId);
		}

		final CompoundCondition condition = new CompoundCondition(AND,
				parameterTypeIdCondition,
				actionTypeIdCondition,
				measurementPortTypeIdCondition);
		final Set<ActionParameterTypeBinding> actionParameterTypeBindings = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		if (actionParameterTypeBindings.isEmpty()) {
			throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": for '" + parameterTypeId + "', '" + actionTypeId + "', '" + measurementPortTypeId + "'");
		}
		assert actionParameterTypeBindings.size() == 1 : ONLY_ONE_EXPECTED;
		return actionParameterTypeBindings.iterator().next();
	}
}
