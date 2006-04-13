/*-
 * $Id: MeasurementParameters.java,v 1.1.2.4 2006/04/13 12:48:26 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.client.resource.I18N;
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
 * Обеспечивает хранение параметров измерения и
 * их преобразование между тремя представлениями:
 * <ul>
 * <li> {@link ActionTemplate}/{@link ActionParameter}
 * <li> Параметры, удобные для GUI: доступ к полям по ключам набора Property,
 *      представляя boolean самим собой, а int и double как String.
 *      Конкретное поле может присутствовать, а может отсутствовать в
 *      зависимости от конкретного типа измерительного порта.
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
 * @version $Revision: 1.1.2.4 $, $Date: 2006/04/13 12:48:26 $
 * @module scheduler
 */
public class MeasurementParameters {

	/**
	 * Отображение параметров в виде "свойств"
	 * (такое представление удобно для UI).
	 * 
	 * В отличие от параметров,
	 * каждое свойство имеет свой фиксированный kind,
	 * поэтому один и тот же параметр (например, число усреднений)
	 * может быть представлен разными свойствами.
	 * 
	 * Здесь мы должны обеспечить, чтобы в каждый момент времени
	 * одному параметру соответствовало не более одного свойства.
	 * 
	 * Кроме того, мы полагаемся, что нет неопознанных параметров.
	 * Тогда можно рассчитывать на отношение параметров к свойствам
	 * вида {0,1} : 1, т.е. каждому параметру соответствует ровно одно
	 * свойство, а каждому свойству соответствует один параметр
	 * или не соответствует ни одного (свойство не определено).
	 */
	public static enum Property {
		// эти отображения только определяют зависимости.
		// Настоящие значения параметров уточнятся далее.

		// enumerated numericals
		E_WAVELENGTH(ReflectometryParameterTypeCodename.WAVE_LENGTH),
		E_TRACELENGTH(ReflectometryParameterTypeCodename.TRACE_LENGTH),
		E_REFRACTION_INDEX(ReflectometryParameterTypeCodename.INDEX_OF_REFRACTION),
		E_PULSE_WIDTH_M(ReflectometryParameterTypeCodename.PULSE_WIDTH_M),
		E_PULSE_WIDTH_NS(ReflectometryParameterTypeCodename.PULSE_WIDTH_NS),
		E_AVERAGES(ReflectometryParameterTypeCodename.AVERAGE_COUNT),
		E_RESOLUTION(ReflectometryParameterTypeCodename.RESOLUTION),

		// enumerated boolean
		FLAG_GAIN_SPLICE(ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON, true),
		FLAG_SMOOTH(ReflectometryParameterTypeCodename.FLAG_SMOOTH_FILTER, false),
		FLAG_LFD(ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT, true),
		FLAG_HIRES(ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES, true), // NB: HIRES = NOT(LOWRES); XXX: может, использовать само lowres, без трансляции?

		// continuous
		I_AVERAGES(ReflectometryParameterTypeCodename.AVERAGE_COUNT, 10); // NB: будет отфильтровано дополнительно

		public final ReflectometryParameterTypeCodename codename;
		private final byte[] defaultValue;
		Property(ReflectometryParameterTypeCodename codename) {
			this.codename = codename;
			this.defaultValue = null;
		}
		Property(ReflectometryParameterTypeCodename codename, boolean defaultValue) {
			this.codename = codename;
			this.defaultValue = ByteArray.toByteArray(defaultValue);
		}
		Property(ReflectometryParameterTypeCodename codename, int defaultValue) {
			this.codename = codename;
			this.defaultValue = ByteArray.toByteArray(defaultValue);
		}
		public byte[] getDefaultValueClone() {
			return this.defaultValue == null ? null : this.defaultValue.clone();
		}
	}

	/**
	 * Хранит текущее значения параметра в виде byte[]
	 * информацию, необходимую для его сохранения как ActionParameter,
	 * информацию о его истинном типе,
	 * а также множество допустимых значений параметра.
	 * 
	 * XXX: Создается в состоянии, когда текущее значение не определено,
	 * но перед использованием должен быть проинициализирован.
	 * 
	 * Имеет возможность доступа и изменения с помощью типизированных
	 * as...() и set...() методов: хотя определены все эти методы,
	 * допустимы только те, который соответствует реальному типу.
	 */
	private static class ParameterRecord {
		DataType dataType; // must correspond to binding.parameterType
		private ActionParameterTypeBinding binding; // please, read only access
		private List<byte[]> allowedValues; // null for continuous
		private byte[] data; // initialized as null, when used cheched to be not null

		/* довольно медленная реализация (из-за new ByteArray()),
		 * к тому же создается отдельно для каждой записи
		 */
		private Comparator<byte[]> valueComparator = new Comparator<byte[]>() {
			public int compare(byte[] o1, byte[] o2) {
				final ByteArray bar1 = new ByteArray(o1);
				final ByteArray bar2 = new ByteArray(o2);
				try {
					switch(ParameterRecord.this.dataType) {
					case DOUBLE:
						double delta = bar1.toDouble() - bar2.toDouble();
						return delta <= 0 ? delta < 0 ? -1 : 0 : 1;
					case INTEGER:
						return bar1.toInt() - bar2.toInt();
					case BOOLEAN:
						// в принципе, результат сортировки boolean нигде не отображается
						return bar1.toBoolean()
							? bar2.toBoolean() ? 0 : 1
							: bar2.toBoolean() ? -1 : 0;
					default:
						throw new IllegalStateException("Comparator not supported for " + ParameterRecord.this.dataType);
					}
				} catch(IOException e) {
					/* XXX: use DataFormatException? */
					throw new InternalError();
				}
			}};

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
			this.data = null; // XXX: инициализация
			this.binding = binding;
			this.dataType = binding.getParameterType().getDataType();

			// сортируем allowedValues, заодно делая safe copy
			if (allowedValues == null) {
				this.allowedValues = null;
			} else {
				//this.allowedValues = allowedValues;
				this.allowedValues = new ArrayList<byte[]>(allowedValues.size());
				for (byte[] bytes : allowedValues) {
					this.allowedValues.add(bytes.clone());
				}
				// к этому моменту this.dataType уже определен,
				// и наш компаратор будет работать
				Collections.sort(this.allowedValues, this.valueComparator);
			}
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

		/**
		 * please, do not modify returned array
		 */
		public byte[] asBAR() {
			assert this.data != null;
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
			assert this.data != null;
			return asBoolean(this.data);
		}

		public int asInteger() {
			assert this.data != null;
			return asInteger(this.data);
		}

		public double asDouble() {
			assert this.data != null;
			return asDouble(this.data);
		}

		public void setBoolean(boolean b) {
			if (this.dataType != DataType.BOOLEAN) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(b);
		}

		public void setInteger(int i) {
			if (this.dataType != DataType.INTEGER) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(i);
		}

		public void setDouble(double d) {
			if (this.dataType != DataType.DOUBLE) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(d);
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
		 * @return String, not null
		 */
		public String getStringValue() {
			assert this.data != null;
			return getStringValue(this.data);
		}

		/**
		 * Sets from string value.
		 *  Supports Integer and Double value types only.
		 * @param value string value, not null
		 */
		public void setStringValue(String s) {
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
		 * Gets list of allowed string representations.
		 *   Supports Integer and Double value types only.
		 * @return list of allowed string representations or null for continuous
		 */
		public List<String> getAllowedStringValues() {
			if (this.allowedValues == null) {
				return null;
			}
			List<String> ret = new ArrayList<String>(this.allowedValues.size());
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

		// загружаем this.parameters
		// Внимание: значения ParameterRecord.data остаются null,
		// надо будет проинициализировать!
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

		// инициализируем properties-ссылки,
		// а также начальные значения свойств соответствующих им параметров
		this.properties = new HashMap<Property,ParameterRecord>(
				Property.values().length);
		for (Property p : Property.values()) {
			// есть ли такое свойство?
			final ParameterRecord record = this.parameters.get(ParameterType.valueOf(p.codename));
			if (record == null) {
				continue; // такого свойства на данном типе порта нет
			}
			// дополнительная фильтрация для числа усреднений
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
			// добавляем это свойство
			this.properties.put(p, record);

			// инициализируем соответствующий этому свойству параметр
			byte[] defaultValue = p.getDefaultValueClone();
			if (defaultValue != null) {
				record.setBAR(defaultValue);
			} else {
				assert record.getValueKind() == ParameterValueKind.ENUMERATED;
				assert record.getDataType() != DataType.BOOLEAN;
				// XXX: берем первый попавшийся
				record.setStringValue(
						record.getAllowedStringValues().iterator().next());
			}
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
					record.asBAR(),
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

	public MonitoredElement getMe() {
		return this.me;
	}

	// GUI API

	/**
	 * @return true, если данное свойство определено для данного типа порта
	 */
	public boolean hasProperty(Property property) {
		return this.properties.containsKey(property);
	}

	private void checkProperty(Property property) {
		if (!hasProperty(property)) {
			throw new IllegalArgumentException("Property not present: " + property);
		}
	}

	/**
	 * @return тип данного свойства
	 * @throws IllegalArgumentException свойство не определено
	 */
	public ParameterValueKind getPropertyValueKind(Property property) {
		checkProperty(property);
		final ParameterRecord record = this.properties.get(property);
		return record.getValueKind();
	}

	/**
	 * Определяет строковое представление значения.
	 * Поддерживается только для числовых значений.
	 * 
	 * @return строковое представление значения, not null
	 * @throws IllegalArgumentException свойство не определено
	 * @throws IllegalStateException тип свойства не поддерживается
	 */
	public String getPropertyStringValue(Property property) {
		checkProperty(property);
		return this.properties.get(property).getStringValue();
	}

	/**
	 * Устанавливает свойство по его строковому представлению.
	 * Поддерживается только для числовых значений.
	 * 
	 * @param property устанавливаемое свойство
	 * @param value устанавливаемое значение, not null
	 * @throws IllegalArgumentException свойство не определено
	 * @throws IllegalStateException тип свойства не поддерживается
	 */
	public void setPropertyStringValue(Property property, String value) {
		assert value != null;
		checkProperty(property);
		this.properties.get(property).setStringValue(value);
	}

	/**
	 * Определяет множество строковых представлений
	 *   допустимых значений перечислимого параметра.
	 * Поддерживается только для числовых значений.
	 * 
	 * @return для enumerated типа: множество строк,
	 *   для continuous типа: null
	 * @throws IllegalArgumentException свойство не определено
	 * @throws IllegalStateException тип свойства не поддерживается
	 */
	public List<String> valuesPropertyStringValue(Property property) {
		checkProperty(property);
		return this.properties.get(property).getAllowedStringValues();
	}

	/**
	 * Определяет значение boolean-свойства.
	 * 
	 * @return значение boolean свойства
	 * @throws IllegalArgumentException свойство не определено
	 * @throws IllegalStateException тип свойства не boolean
	 */
	public boolean getPropertyAsBoolean(Property property) {
		checkProperty(property);
		if (property == Property.FLAG_HIRES) {
			return !this.properties.get(property).asBoolean();
		}
		return this.properties.get(property).asBoolean();
	}

	/**
	 * Устанавливает значение boolean-свойства.
	 * 
	 * @param property устанавливаемое свойство
	 * @param value устанавливаемое значение свойства
	 * @throws IllegalArgumentException свойство не определено
	 * @throws IllegalStateException тип свойства не boolean
	 */
	public void setPropertyAsBoolean(Property property, boolean value) {
		checkProperty(property);
		if (property == Property.FLAG_HIRES) {
			this.properties.get(property).setBoolean(!value);
		} else {
			this.properties.get(property).setBoolean(value);
		}
	}

	private String getUnit(final ParameterType parameterType) {
		String name = parameterType.getMeasurementUnit().getName();
		return name.trim().length() > 0 ? ", " + name : "";
	}

	public String getPropertyDescription(Property property) {
		checkProperty(property);
		if (property == Property.FLAG_HIRES) {
			return I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.HighResolution");
		}
		try {
			final ParameterType parameterType =
				this.properties.get(property).getBinding().getParameterType();
			return parameterType.getDescription() + getUnit(parameterType);
		} catch (ApplicationException e) {
			/* XXX: ApplicationException handling */
			throw new InternalError(e.getMessage());
		}
	}
}
