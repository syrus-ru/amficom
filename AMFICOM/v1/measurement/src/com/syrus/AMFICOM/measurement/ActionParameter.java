/*-
 * $Id: ActionParameter.java,v 1.1.2.16 2006/03/27 15:00:44 arseniy Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind.ENUMERATED;

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
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameter;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterHelper;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;


/**
 * Параметр действия. Всегда имеет свою измерительную связку
 * {@link com.syrus.AMFICOM.measurement.ActionParameterTypeBinding},
 * идентификатор которой хранится в {@link #bindingId}.
 * 
 * @version $Revision: 1.1.2.16 $, $Date: 2006/03/27 15:00:44 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameter extends Parameter implements IdlTransferableObjectExt<IdlActionParameter> {
	private static final long serialVersionUID = -7695430559152990049L;

	/**
	 * Идентификатор измерительной связки.
	 */
	private Identifier bindingId;


	/**
	 * Вид набора значений данного параметра. Используется в
	 * {@link #getValueKind()}.
	 */
	private transient ParameterValueKind valueKind;

	/**
	 * Идентификатор типа данного параметра. Используется в {@link #getTypeId()}.
	 */
	private transient Identifier typeId;

	/**
	 * Кодовое имя типа данного параметра. Используется в
	 * {@link #getTypeCodename()}.
	 */
	private transient String typeCodename;

	ActionParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier bindingId) {
		super(id, creatorId, version, value);
		this.bindingId = bindingId;
	}

	public ActionParameter(final IdlActionParameter idlActionParameter) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlActionParameter);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * Создать новый экземпляр. Именно этот метод и должны использовать конечные
	 * пользователи. Он всегда проверяет вид набора значений данного параметра.
	 * Если этот вид перечисляемый
	 * {@link com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind#ENUMERATED},
	 * то возбуждается исключение {@link CreateObjectException}.
	 * 
	 * @param creatorId
	 * @param value
	 * @param bindingId
	 * @return Новый экземпляр
	 * @throws CreateObjectException
	 */
	public static ActionParameter createInstance(final Identifier creatorId, final byte[] value, final Identifier bindingId)
			throws CreateObjectException {
		return createInstance(creatorId, value, bindingId, true);
	}

	/**
	 * Создать новый экземпляр. Этот метод не должен использоваться конечными
	 * пользователями, поскольку оставляет возможность создавать параметры для
	 * перечисляемых наборов значений. Такие параметры должны создаваться только
	 * на этапе установки системы.
	 * 
	 * @param creatorId
	 * @param value
	 * @param bindingId
	 * @param checkValueKind
	 * @return Новый экземпляр
	 * @throws CreateObjectException
	 */
	public static ActionParameter createInstance(final Identifier creatorId,
			final byte[] value,
			final Identifier bindingId,
			final boolean checkValueKind) throws CreateObjectException {
		if (creatorId == null || value == null || bindingId == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		if (checkValueKind) {
			final ActionParameterTypeBinding binding;
			try {
				binding = StorableObjectPool.getStorableObject(bindingId, true);
			} catch (ApplicationException ae) {
				throw new CreateObjectException(ae);
			}
			final ParameterValueKind valueKind = binding.getParameterValueKind();
			if (valueKind == ENUMERATED) {
				throw new CreateObjectException("Cannot create new ENUMERATED action parameter; binding :'" + bindingId + "'");
			}
		}

		try {
			final ActionParameter actionParameter = new ActionParameter(IdentifierPool.getGeneratedIdentifier(ACTIONPARAMETER_CODE),
					creatorId,
					INITIAL_VERSION,
					value,
					bindingId);

			assert actionParameter.isValid() : OBJECT_STATE_ILLEGAL;

			actionParameter.markAsChanged();

			return actionParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlActionParameter getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlActionParameterHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.getValue(),
				this.bindingId.getIdlTransferable());
	}

	public synchronized void fromIdlTransferable(final IdlActionParameter idlActionParameter) throws IdlConversionException {
		super.fromIdlTransferable(idlActionParameter);

		this.bindingId = Identifier.valueOf(idlActionParameter.bindingId);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier bindingId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, value	);
		this.bindingId = bindingId;
	}

	public Identifier getBindingId() {
		return this.bindingId;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Identifier getTypeId() throws ApplicationException {
		if (this.typeId == null) {
			final ActionParameterTypeBinding actionParameterTypeBinding = StorableObjectPool.getStorableObject(this.bindingId, true);
			this.typeId = actionParameterTypeBinding.getParameterTypeId();
		}
		return this.typeId;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getTypeCodename() throws ApplicationException {
		if (this.typeCodename == null) {
			final ActionParameterTypeBinding actionParameterTypeBinding = StorableObjectPool.getStorableObject(this.bindingId, true);
			final ParameterType parameterType = StorableObjectPool.getStorableObject(actionParameterTypeBinding.getParameterTypeId(), true);
			this.typeCodename = parameterType.getCodename();
		}
		return this.typeCodename;
	}

	/**
	 * Получить вид набора значений данного параметра.
	 * 
	 * @return Вид набора значений данного параметра
	 * @throws ApplicationException
	 */
	public ParameterValueKind getValueKind() throws ApplicationException {
		if (this.valueKind == null) {
			final ActionParameterTypeBinding actionParameterTypeBinding = StorableObjectPool.getStorableObject(this.bindingId, true);
			this.valueKind = actionParameterTypeBinding.getParameterValueKind();
		}
		return this.valueKind;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.bindingId != null && this.bindingId.getMajor() == ACTIONPARAMETERTYPEBINDING_CODE;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.bindingId);
		return dependencies;
	}

	@Override
	protected ActionParameterWrapper getWrapper() {
		return ActionParameterWrapper.getInstance();
	}

	/**
	 * Найти все параметры действия для данной измерительной связки. См. также
	 * {@link ActionParameterTypeBinding#getActionParameters()}
	 * 
	 * @param actionParameterTypeBinding
	 * @return Все параметры действия для данной измерительной связки
	 * @throws ApplicationException
	 */
	public static Set<ActionParameter> getValues(final ActionParameterTypeBinding actionParameterTypeBinding) throws ApplicationException {
		assert actionParameterTypeBinding != null : NON_NULL_EXPECTED;

		return actionParameterTypeBinding.getActionParameters();
	}

	/**
	 * Найти все параметры действия для данной измерительной связки. См. также
	 * {@link ActionParameterTypeBinding#getActionParameters()}
	 * 
	 * @param actionParameterTypeBindingId
	 * @return Все параметры действия для данной измерительной связки
	 * @throws ApplicationException
	 */
	public static Set<ActionParameter> getValues(final Identifier actionParameterTypeBindingId) throws ApplicationException {
		assert actionParameterTypeBindingId != null : NON_NULL_EXPECTED;
		assert actionParameterTypeBindingId.getMajor() == ACTIONPARAMETERTYPEBINDING_CODE : ILLEGAL_ENTITY_CODE;

		final ActionParameterTypeBinding actionParameterTypeBinding = StorableObjectPool.getStorableObject(actionParameterTypeBindingId, true);
		if (actionParameterTypeBinding == null) {
			throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": '" + actionParameterTypeBindingId + "'");
		}
		return getValues(actionParameterTypeBinding);
	}
}
