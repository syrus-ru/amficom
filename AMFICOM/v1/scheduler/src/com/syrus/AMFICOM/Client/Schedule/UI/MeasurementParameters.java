/*-
 * $Id: MeasurementParameters.java,v 1.1.2.1 2006/04/13 09:39:03 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.ActionParameter;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeCodename;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.AMFICOM.reflectometry.MeasurementTimeEstimator;
import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementParameters;
import com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename;
import com.syrus.AMFICOM.reflectometry.ReflectometryUtil;
import com.syrus.util.ByteArray;

/**
 * @todo избавиться от состояния "не определено"
 * 
 * Обеспечивает хранение параметров измерения и
 * их преобразование между тремя представлениями:
 * <ul>
 * <li> {@link ActionTemplate}/{@link ActionParameter}
 * <li> Параметры, удобные для GUI:
 *   FIXME: здесь javadoc устарел. Теперь используется Properties
 *   <ul>
 *   <li> String waveLength - перечислимый, определен всегда
 *   <li> String traceLength - перечислимый, определен всегда
 *   <li> ... надо добавить еще
 *   </ul>
 * <li> (только экспорт с потерей данных)
 *   {@link ReflectometryMeasurementParameters}/{@link MeasurementTimeEstimator}
 *   - для определения продолжительности измерения (фактически, это необходимо
 *   для представления в виде {@link ActionTemplate}.
 * </ul>
 * 
 * FIXME: переименовать: Фактически этот класс завязан на рефлектометрию.<p>
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/13 09:39:03 $
 * @module scheduler
 */
public class MeasurementParameters {

	public static enum Property {
		// эти отображения только определяют зависимости.
		// Настоящие значения параметров уточнятся далее.
		E_WAVELENGTH(ReflectometryParameterTypeCodename.WAVE_LENGTH),
		E_TRACELENGTH(ReflectometryParameterTypeCodename.TRACE_LENGTH),
		E_REFRACTION_INDEX(ReflectometryParameterTypeCodename.INDEX_OF_REFRACTION),
		FLAG_HIRES(ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES), // NB: будет преобразовано дополнительно
		E_PULSE_WIDTH_M(ReflectometryParameterTypeCodename.PULSE_WIDTH_M),
		E_PULSE_WIDTH_NS(ReflectometryParameterTypeCodename.PULSE_WIDTH_NS),
		E_AVERAGES(ReflectometryParameterTypeCodename.AVERAGE_COUNT), // NB: будет отфильтровано дополнительно
		I_AVERAGES(ReflectometryParameterTypeCodename.AVERAGE_COUNT), // NB: будет отфильтровано дополнительно
		E_RESOLUTION(ReflectometryParameterTypeCodename.RESOLUTION),
		FLAG_GAIN_SPLICE(ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON),
		FLAG_SMOOTH(ReflectometryParameterTypeCodename.FLAG_SMOOTH_FILTER),
		FLAG_LFD(ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT);

		public final ReflectometryParameterTypeCodename codename;
		Property(ReflectometryParameterTypeCodename codename) {
			this.codename = codename;
		}
	}

	/**
	 * Хранит текущее значения параметра (в виде byte[]) либо null,
	 * информацию, необходимую для его сохранения как ActionParameter,
	 * информацию о его истинном типе,
	 * а также множество допустимых значений параметра.
	 * 
	 * Имеет возможность доступа и изменения с помощью типизированных
	 * as...() и set...() методов: хотя определены все эти методы,
	 * допустимы только те, который соответствует реальному типу.
	 * Для работы со значениями типа "не определено", предоставлены
	 * методы isUnset() и unset(). Если значение не определено,
	 * (isUnset() == true), то вызов метода as...() недопустим.
	 */
	private static class ParameterRecord {
		private DataType dataType; // must correspond to binding.parameterType
		private ActionParameterTypeBinding binding; // please, read only access
		private Set<byte[]> allowedValues; // null for continuous
		public byte[] data; // may be null

		public ParameterRecord(ActionParameterTypeBinding binding,
				Set<byte[]> allowedValues) throws ApplicationException {
			switch(binding.getParameterValueKind()) {
			case CONTINUOUS:
				assert allowedValues == null;
				break;
			case ENUMERATED:
				assert allowedValues != null;
				break;
			default:
				assert false;
			}
			this.binding = binding;
			this.dataType = binding.getParameterType().getDataType();
			this.allowedValues = allowedValues;
			this.data = null;
		}

		public ActionParameterTypeBinding getBinding() {
			return this.binding;
		}

		public ParameterValueKind getValueKind() {
			return this.binding.getParameterValueKind();
		}

		public DataType getDataType() {
			return this.dataType;
		}

		public boolean isUnset() {
			return this.data == null;
		}

		/**
		 * please, do not modify returned array
		 */
		public byte[] asBAR() {
			return this.data;
		}

		/**
		 * makes a safe copy
		 */
		public void setBAR(byte[] data) {
			this.data = data.clone();
		}

		private boolean asBoolean(byte[] someData) {
			if (this.dataType != DataType.BOOLEAN) {
				throw new IllegalStateException();
			}
			try {
				return new ByteArray(someData).toBoolean();
			} catch (IOException e) {
				/* XXX: use DataFormatException? */
				throw new InternalError(e.getMessage());
			}
		}

		private int asInteger(byte[] someData) {
			if (this.dataType != DataType.INTEGER) {
				throw new IllegalStateException();
			}
			try {
				return new ByteArray(someData).toInt();
			} catch (IOException e) {
				/* XXX: use DataFormatException? */
				throw new InternalError(e.getMessage());
			}
		}

		private double asDouble(byte[] someData) {
			if (this.dataType != DataType.DOUBLE) {
				throw new IllegalStateException();
			}
			try {
				return new ByteArray(someData).toDouble();
			} catch (IOException e) {
				/* XXX: use DataFormatException? */
				throw new InternalError(e.getMessage());
			}
		}

		public boolean asBoolean() {
			return asBoolean(this.data);
		}

		public int asInteger() {
			return asInteger(this.data);
		}

		public double asDouble() {
			return asDouble(this.data);
		}

		public void setBoolean(boolean b) {
			if (this.dataType != DataType.BOOLEAN) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(b);
		}

		public void setInteger(int i) {
			if (this.dataType != DataType.BOOLEAN) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(i);
		}

		public void setDouble(double d) {
			if (this.dataType != DataType.BOOLEAN) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(d);
		}

		public void unset() {
			this.data = null;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof ParameterRecord)) {
				return false;
			}
			ParameterRecord that = (ParameterRecord) obj;
			return this.binding.equals(that.binding)
				&& this.dataType == that.dataType
				&& Arrays.equals(this.data, that.data);
		}

		@Override
		public int hashCode() {
			int rc = 17;
			rc += this.binding.hashCode();
			rc += this.dataType.hashCode();
			rc += Arrays.hashCode(this.data);
			return rc;
		}

		private String getStringValue(byte[] someData) {
			switch(this.dataType) {
			case INTEGER:
				return Integer.toString(asInteger(someData));
			case DOUBLE:
				return Double.toString(asDouble(someData));
			default:
				throw new IllegalStateException();
			}
		}

		/**
		 * Makes string representation. Supports Integer and Double value types only.
		 * returns null if values is unset.
		 */
		public String getStringValue() {
			return this.data == null ? null : getStringValue(this.data);
		}

		/**
		 * Sets from string value.
		 *  Supports Integer and Double value types only.
		 * @param value string value or null to unset
		 */
		public void setStringValue(String s) {
			// XXX: if s is null, does not checks if the type is allowed. That's not desired but suitable.
			if (s == null) {
				unset();
				return;
			}
			switch(this.dataType) {
			case INTEGER:
				setInteger(Integer.parseInt(s));
				break;
			case DOUBLE:
				setDouble(Double.parseDouble(s));
				break;
			default:
				throw new IllegalStateException();
			}
		}

		/**
		 * returns set of allowed string representations or null of continuous.
		 *   Supports Integer and Double value types only.
		 * @return set of allowed string representations or null of continuous
		 */
		public Set<String> getAllowedStringValues() {
			if (this.allowedValues == null) {
				return null;
			}
			Set<String> ret = new HashSet<String>(this.allowedValues.size());
			for (byte[] bar : this.allowedValues) {
				ret.add(getStringValue(bar));
			}
			return ret;
		}
	}

	// paramterType view
	private Map<ParameterType, ParameterRecord> parameters;

	// Property view
	private Map<Property, ParameterRecord> properties;

	private MonitoredElement me;

	private boolean hasValue(ReflectometryParameterTypeCodename codename) {
		try {
			return this.parameters.containsKey(ParameterType.valueOf(codename));
		} catch (ApplicationException e) {
			/* XXX: ApplicationException handling */
			throw new InternalError(e.getMessage());
		}
	}

	private ParameterRecord getValue(ReflectometryParameterTypeCodename codename) {
		try {
			return this.parameters.get(ParameterType.valueOf(codename));
		} catch (ApplicationException e) {
			/* XXX: ApplicationException handling */
			throw new InternalError(e.getMessage());
		}
	}

	/**
	 * @todo javadoc required
	 * @param me
	 * @throws ApplicationException
	 */
	public MeasurementParameters(MonitoredElement me) throws ApplicationException {
		this.me = me;

		// надо загрузить начальные значения values
		final MeasurementPortType mpType = getMeasurementPortType();
		final Set<ActionParameterTypeBinding> bindings =
			ActionParameterTypeBinding.getValues(
				MeasurementType.valueOf(MeasurementTypeCodename.REFLECTOMETRY),
				mpType);

		this.parameters = new HashMap<ParameterType, ParameterRecord>();
		for (ActionParameterTypeBinding binding : bindings) {
			switch(binding.getParameterValueKind()) {
			case ENUMERATED:
				final Set<ActionParameter> pValues = binding.getActionParameters();
				final Set<byte[]> bValues = new HashSet<byte[]>(pValues.size());
				for (ActionParameter pValue : pValues) {
					bValues.add(pValue.getValue());
				}
				this.parameters.put(binding.getParameterType(),
						new ParameterRecord(binding,
								bValues));
				break;
			case CONTINUOUS:
				this.parameters.put(binding.getParameterType(),
						new ParameterRecord(binding,
								null));
				break;
			default:
				assert false;
			}
		}

		// инициализируем properties-ссылки
		this.properties = new HashMap<Property,ParameterRecord>(
				Property.values().length);
		for (Property p : Property.values()) {
			// есть ли такое свойство?
			final ParameterRecord record = this.parameters.get(ParameterType.valueOf(p.codename));
			if (record == null) {
				continue;
			}
			switch(p) {
			case E_AVERAGES:
				if (record.getValueKind() != ParameterValueKind.ENUMERATED) {
					continue;
				}
				break;
			case I_AVERAGES:
				if (record.getValueKind() != ParameterValueKind.CONTINUOUS) {
					continue;
				}
				break;
			default:
				// common processing
			}
			this.properties.put(p, record);
		}
	}

	/**
	 * @todo javadoc required
	 * @param template
	 * @throws ApplicationException 
	 */
	public MeasurementParameters(ActionTemplate<Measurement> template)
	throws ApplicationException {
		this((MonitoredElement) StorableObjectPool.getStorableObject(
				template.getMonitoredElementIds().iterator().next(),
				true));

		// устанавливаем начальные значения
		setFromTemplate(template);
	}

	public ReflectometryMeasurementParameters getRMP() {
		// FIXME: may throw NullPointers if there is not enough data
		final boolean hasGainSplice = getValue(ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON).asBoolean();
		final boolean hasHiRes = false; // FIXME: implement this for both QP and PK reflectometers
		final boolean hasLFD = getValue(ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT).asBoolean();
		final int averages = getValue(ReflectometryParameterTypeCodename.AVERAGE_COUNT).asInteger();
		final double refIndex = getValue(ReflectometryParameterTypeCodename.INDEX_OF_REFRACTION).asDouble();
		final int pulseWidthNs;
		if (hasValue(ReflectometryParameterTypeCodename.PULSE_WIDTH_NS)) {
			pulseWidthNs = getValue(ReflectometryParameterTypeCodename.PULSE_WIDTH_NS).asInteger();
		} else {
			pulseWidthNs = (int) Math.round(
					ReflectometryUtil.pulseWidthPKMetersToNanoseconds(
							getValue(ReflectometryParameterTypeCodename.PULSE_WIDTH_M).asInteger(),
							refIndex));
		}
		final double resolution = getValue(ReflectometryParameterTypeCodename.RESOLUTION).asDouble();
		final double traceLength = getValue(ReflectometryParameterTypeCodename.TRACE_LENGTH).asDouble();
		final int wavelength = getValue(ReflectometryParameterTypeCodename.WAVE_LENGTH).asInteger();

		return new ReflectometryMeasurementParameters() {

			public boolean hasGainSplice() {
				return hasGainSplice;
			}

			public boolean hasHighResolution() {
				return hasHiRes;
			}

			public boolean hasLiveFiberDetection() {
				return hasLFD;
			}

			public int getNumberOfAverages() {
				return averages;
			}

			public int getPulseWidthNs() {
				return pulseWidthNs;
			}

			public double getRefractionIndex() {
				return refIndex;
			}

			public double getResolution() {
				return resolution;
			}

			public double getTraceLength() {
				return traceLength;
			}

			public int getWavelength() {
				return wavelength;
			}
			
		};
	}

	/**
	 * @todo переместить его куда-нибудь в measurement, где ему будет место
	 */
	public static MeasurementTimeEstimator getTimeEstimatorByMeasurementPortType(
			MeasurementPortType type) {
		if (type.getCodename().equals(MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A.stringValue())) {
			return ReflectometryUtil.getQP1640AEstimator();
		}
		if (type.getCodename().equals(MeasurementPortTypeCodename.REFLECTOMETRY_QP1643A.stringValue())) {
			return ReflectometryUtil.getQP1643AEstimator();
		}
		if (type.getCodename().equals(MeasurementPortTypeCodename.REFLECTOMETRY_PK7600.stringValue())) {
			return ReflectometryUtil.getQP1640MREstimator();
		}
		throw new InternalError("Unsupported type of reflectometer: " + type);
	}

	private MeasurementPortType getMeasurementPortType()
	throws ApplicationException {
		return this.me.getMeasurementPort().getType();
	}

	private MeasurementTimeEstimator getEstimator()
	throws ApplicationException {
		return getTimeEstimatorByMeasurementPortType(getMeasurementPortType());
	}

	public ActionTemplate<Measurement> createMeasurementTemplate()
	throws ApplicationException {
		double durationSeconds = getEstimator().getEstimatedMeasurementTime(
				getRMP(), true);
		long durationMillis = (long)Math.ceil(durationSeconds * 1000.0);

		Set<Identifier> parameterIds = new HashSet<Identifier>();

		for (ParameterType key: this.parameters.keySet()) {
			final ParameterRecord record = this.parameters.get(key);
			final ActionParameter parameter =
				ActionParameter.valueOf(LoginManager.getUserId(),
					record.data,
					record.getBinding());
			parameterIds.add(parameter.getId());
		}

		return ActionTemplate.createInstance(LoginManager.getUserId(),
				MeasurementType.valueOf(MeasurementTypeCodename.REFLECTOMETRY).getId(),
				getMeasurementPortType().getId(),
				"", // FIXME: no description,
				durationMillis,
				parameterIds,
				Collections.singleton(this.me.getId()));
	}

	private void setFromTemplate(ActionTemplate<Measurement> template)
	throws ApplicationException {
		final Identifier meId = template.getMonitoredElementIds().iterator().next();
		if (!this.me.getId().equals(meId)) {
			throw new IllegalArgumentException("ME should be the same");
		}
		for (ActionParameter par: template.getActionParameters()) {
			final ParameterType type = par.getType();
			final ParameterRecord record = this.parameters.get(type);
			record.setBAR(par.getValue());
		}
	}

	// GUI API

	/**
	 * @todo javadoc required
	 */
	public boolean hasProperty(Property property) {
		return this.properties.containsKey(property);
	}

	/**
	 * @todo javadoc required
	 */
	public ParameterValueKind getPropertyValueKind(Property property) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		final ParameterRecord record = this.properties.get(property);
		return record.getValueKind();
	}

	/**
	 * @todo javadoc required
	 * Supported only for numerical values
	 * @return string value or null if not set yet
	 */
	public String getPropertyStringValue(Property property) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		return this.properties.get(property).getStringValue();
	}

	/**
	 * @todo javadoc required
	 * Supported only for numerical values
	 * @return string value set or null if not set yet
	 */
	public Set<String> valuesPropertyStringValue(Property property) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		return this.properties.get(property).getAllowedStringValues();
	}

	/**
	 * @todo javadoc required
	 * @return true if value is not set yet
	 */
	public boolean isPropertyUnset(Property property) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		return this.properties.get(property).isUnset();
	}

	/**
	 * @todo javadoc required
	 */
	public void unsetProperty(Property property) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		this.properties.get(property).unset();
	}

	/**
	 * @todo javadoc required
	 * Supported only for boolean values.
	 * Will crush if property is not set yet (please check
	 * {@link #isPropertyUnset} first).
	 * @return string value or null if not set yet
	 */
	public boolean getPropertyAsBoolean(Property property) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		if (property == Property.FLAG_HIRES) {
			return !this.properties.get(property).asBoolean();
		}
		return this.properties.get(property).asBoolean();
	}

	/**
	 * @todo javadoc required
	 * Supported only for boolean values.
	 */
	public void setPropertyAsBoolean(Property property, boolean value) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		if (property == Property.FLAG_HIRES) {
			this.properties.get(property).setBoolean(!value);
		} else {
			this.properties.get(property).setBoolean(value);
		}
	}

	public void setPropertyStringValue(Property property, String value) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
		this.properties.get(property).setStringValue(value);
	}

	public MonitoredElement getMe() {
		return this.me;
	}
}
