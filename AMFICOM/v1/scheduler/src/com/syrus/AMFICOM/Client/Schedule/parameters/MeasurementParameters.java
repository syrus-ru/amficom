/*-
 * $Id: MeasurementParameters.java,v 1.1.2.1 2006/04/18 09:18:59 saa Exp $
 * 
 * Copyright � 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.parameters;

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
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeCodename;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename;
import com.syrus.AMFICOM.reflectometry.MeasurementTimeEstimator;
import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementParameters;
import com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename;
import com.syrus.AMFICOM.reflectometry.ReflectometryUtil;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * ������������ �������� ���������� ��������� �
 * �� �������������� ����� ����� ���������������:
 * <ul>
 * <li> {@link ActionTemplate}/{@link ActionParameter},
 *      � �.�. ���������� {@link ReflectometryMeasurementParameters} �
 *      ����������� ������������ ��������� �����
 *      {@link MeasurementTimeEstimator}.
 * <li> ���������, ������� ��� GUI: ������ � ����� �� ������ ������ Property,
 *      ����������� boolean ����� �����, � int � double ��� String.
 *      ���������� ���� ����� ��������������, � ����� ������������� �
 *      ����������� �� ����������� ���� �������������� �����.
 * </ul>
 * 
 * @todo �������������: ���������� ���� ����� ������� �� ��������������.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/18 09:18:59 $
 * @module scheduler
 */
public class MeasurementParameters {

	/**
	 * ����������� ���������� � ���� "�������"
	 * (����� ������������� ������ ��� UI).
	 * <p>
	 * � ������� �� ����������,
	 * ������ �������� ����� ���� ������������� kind,
	 * ������� ���� � ��� �� �������� (��������, ����� ����������)
	 * ����� ���� ����������� ������� ����������.
	 * <p>
	 * ����� �� ������ ����������, ����� � ������ ������ �������
	 * ������ ��������� ��������������� �� ����� ������ ��������.
	 * <p>
	 * ����� ����, �� ����������, ��� ��� ������������ ����������.
	 * ����� ����� ������������ �� ��������� ���������� � ���������
	 * ���� {0,1} : 1, �.�. ������� ��������� ������������� ����� ����
	 * ��������, � ������� �������� ������������� ���� ��������
	 * ��� �� ������������� �� ������ (�������� �� ����������).
	 */
	public static enum Property {
		// ��� ����������� ������ ���������� �����������.
		// ��������� �������� ���������� ��������� �����.

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
		FLAG_LOWRES(ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES, false),

		// continuous
		I_AVERAGES(ReflectometryParameterTypeCodename.AVERAGE_COUNT, 10); // NB: ����� ������������� �������������

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
	 * ������ ������� �������� ��������� � ���� byte[]
	 * ����������, ����������� ��� ��� ���������� ��� ActionParameter,
	 * ���������� � ��� �������� ����,
	 * � ����� ��������� ���������� �������� ���������.
	 * <p>
	 * NB: ��������� � ���������, ����� ������� �������� �� ����������;
	 * ����� �������������� ��� ������ ���� ������������������� �������
	 * {@link #setBAR(byte[])} ���� ��������������� ���� �������
	 * {@link #setBoolean()} / {@link #setDouble()} / {@link #setInteger()} /
	 * {@link #setStringValue()}.
	 * <p>
	 * ����� ����������� ������� � ��������� � ������� ��������������
	 * as...() � set...() �������: ���� ���������� ��� ��� ������,
	 * ��������� ������ ��, ������� ������������� ��������� ����.
	 */
	private static class ParameterRecord {
		DataType dataType; // must correspond to binding.parameterType
		private ActionParameterTypeBinding binding; // please, read only access
		private List<byte[]> allowedValues; // null for continuous
		private byte[] data; // ���������������� ��� null

		/* �������� ��������� ���������� (��-�� new ByteArray()),
		 * � ���� �� ��������� �������� ��� ������ ������
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
						// � ��������, ��������� ���������� boolean ����� �� ������������
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
			this.data = null;
			this.binding = binding;
			this.dataType = binding.getParameterType().getDataType();

			// ��������� allowedValues, ������ ����� safe copy
			if (allowedValues == null) {
				this.allowedValues = null;
			} else {
				//this.allowedValues = allowedValues;
				this.allowedValues = new ArrayList<byte[]>(allowedValues.size());
				for (byte[] bytes : allowedValues) {
					this.allowedValues.add(bytes.clone());
				}
				// � ����� ������� this.dataType ��� ���������,
				// � ��� ���������� ����� ��������
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
		 * ���������� byte[]-������������� �������� �������� ���������.
		 * ������������ �� ������ �������� ������������ ��������.
		 * � ������� ������ ������ �������� ��� ������ ���� �����������,
		 * ��� ����������� assert'��.
		 * 
		 * @return byte[]-�������������, not null.
		 */
		byte[] asBAR() {
			assert this.data != null;
			return this.data;
		}

		/**
		 * @return true, ���� �������� ��� ������������������
		 * (���� ��� ����������).
		 */
		public boolean isSet() {
			return this.data != null;
		}

		/**
		 * ������������� �������� byte[]-�������������.
		 * ������ safe copy.
		 * 
		 * @param data ��������������� ��������, not null
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

		/**
		 * ���������� ������� �������� ��������� ��� boolean-���������.
		 * � ������� ������ ������ �������� ��� ������ ���� �����������,
		 * ��� ����������� assert'��.
		 * 
		 * @return �������� boolean
		 */
		public boolean asBoolean() {
			assert this.data != null;
			return asBoolean(this.data);
		}

		/**
		 * ���������� ������� �������� ��������� ��� double-���������.
		 * � ������� ������ ������ �������� ��� ������ ���� �����������,
		 * ��� ����������� assert'��.
		 * 
		 * @return �������� int
		 */
		public int asInteger() {
			assert this.data != null;
			return asInteger(this.data);
		}

		/**
		 * ���������� ������� �������� ��������� ��� double-���������.
		 * � ������� ������ ������ �������� ��� ������ ���� �����������,
		 * ��� ����������� assert'��.
		 * 
		 * @return �������� double
		 */
		public double asDouble() {
			assert this.data != null;
			return asDouble(this.data);
		}

		/**
		 * ������������� �������� ��� boolean-���������
		 */
		public void setBoolean(boolean b) {
			if (this.dataType != DataType.BOOLEAN) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(b);
		}

		/**
		 * ������������� �������� ��� int-���������
		 */
		public void setInteger(int i) {
			if (this.dataType != DataType.INTEGER) {
				throw new IllegalStateException();
			}
			this.data = ByteArray.toByteArray(i);
		}

		/**
		 * ������������� �������� ��� double-���������
		 */
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
				return integerToString(asInteger(someData));
			case DOUBLE:
				return doubleToString(asDouble(someData));
			default:
				throw new IllegalStateException();
			}
		}

		/**
		 * ���������� ��������� ������������� ��� ��������� ����
		 * int ��� double.
		 * � ������� ������ ������ �������� ��� ������ ���� �����������,
		 * ��� ����������� assert'��.
		 * 
		 * @return ��������� �������������, not null
		 */
		public String getStringValue() {
			assert this.data != null;
			return getStringValue(this.data);
		}

		/**
		 * ������������� �������� int ��� double ���������
		 * �� ��� ��������� ���������� �������������.
		 * 
		 * @param ��������� ������������� �������� ���������������� ����,
		 *   not null
		 */
		public void setStringValue(String s) {
			switch(this.dataType) {
			case INTEGER:
				setInteger(stringToInteger(s));
				break;
			case DOUBLE:
				setDouble(stringToDouble(s));
				break;
			default:
				throw new IllegalStateException();
			}
		}

		/**
		 * ������������ ������������ �������������� �� ������ � int.
		 * ������� ����������� ���������� (@see {@link Integer#parseInt(String)})
		 * @param s ��������� �������������
		 * @return int-��������
		 */
		public static int stringToInteger(String s) {
			return Integer.parseInt(s);
		}

		/**
		 * ������������ ������������ �������������� �� ������ � double.
		 * ������� ����������� ���������� (@see {@link Double#parseDouble(String)})
		 * @param s ��������� �������������
		 * @return double-��������
		 */
		public static double stringToDouble(String s) {
			return Double.parseDouble(s);
		}

		/**
		 * ������������ ������������ �������������� �� int � ������
		 */
		public static String integerToString(int i) {
			return Integer.toString(i);
		}

		/**
		 * ������������ ������������ �������������� �� double � ������
		 */
		public static String doubleToString(double d) {
			return Double.toString(d);
		}

		/**
		 * ���������� ������(list) ��������� ������������� ����������
		 * �������� ���������.
		 * �������� ������ ���� ������������
		 * ({@link ParameterValueKind#ENUMERATED}) � ��������� ���������
		 * ������������� ({@link #getStringValue()}),
		 * �� ������ ������ ��� int � double.
		 * ������� �������� ��������� �� ������ ������ ����� ���� ��
		 * ���������������.
		 * 
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

	/* parameterType view */
	private Map<ParameterType, ParameterRecord> parameters;

	/* Property view */
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
	 * ������� ����� ����������, ������������ ��� ������ ����� ������������,
	 * ���������� ��������� ���������� ��������, ������������� ��������
	 * �� ���������.
	 * 
	 * @param monitoredElement ������ ����� ������������
	 * @throws ApplicationException ������ StorableObject Framework
	 */
	public MeasurementParameters(MonitoredElement monitoredElement)
	throws ApplicationException {
		this.me = monitoredElement;

		/*
		 * ��������� this.parameters.
		 * ��������: ��������� �������� (ParameterRecord.data)
		 * ���� �� ����������������. ��� ���� ����� ����������� �������
		 * ���� �����.
		 */
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

		/*
		 *  �������������� properties-������,
		 *  � ����� ��������� �������� ������� ��������������� �� ����������
		 *  (ParameterRecord.data).
		 */
		this.properties = new HashMap<Property,ParameterRecord>(
				Property.values().length);
		for (Property p : Property.values()) {
			// ���� �� ����� ��������?
			final ParameterRecord record = this.parameters.get(ParameterType.valueOf(p.codename));
			if (record == null) {
				continue; // ������ �������� �� ������ ���� ����� ���
			}
			// �������������� ���������� ��� ����� ����������
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
			// ��������� ��� ��������
			this.properties.put(p, record);

			// �������������� ��������������� ����� �������� ��������
			byte[] defaultValue = p.getDefaultValueClone();
			if (defaultValue != null) {
				record.setBAR(defaultValue);
			} else {
				assert record.getValueKind() == ParameterValueKind.ENUMERATED;
				assert record.getDataType() != DataType.BOOLEAN;
				// XXX: ����� ����� �������
				// XXX: ������������, ��� ����� �� ����� ������� �������� ��������
				final List<String> allowedValues = record.getAllowedStringValues();
				record.setStringValue(allowedValues.get(allowedValues.size() / 2));
			}
		}

		/*
		 * ���������, ��� �� �������� ����������������������� �������.
		 * ���� ������� ��� �� ��������, ������, ��� ���� ���������
		 * ���������, �� ������� �� ��������� �� ���� ��������.
		 */
		for (ParameterRecord record: this.parameters.values()) {
			assert record.isSet();
		}

//		// debug
//		for (Property p: Property.values()) {
//			System.err.println("Property " + p.toString() + ", "
//					+ " descr " + (hasProperty(p) ? getPropertyDescription(p) : "<no>"));
//		}
	}

	/**
	 * ������� ����� ����������, ������������ ��� ����� ������������,
	 * �� ������� ����� ������ ������ ���������,
	 * ���������� ��������� ���������� ��������, ������������� ��������
	 * ���������� �����, ����� ���� � ������ �������.
	 * <p>
	 * �� ������ ������,
	 * ���� �����-�� ���������� � ������� ���,
	 * �������� �������� �� ���������, �� ��� ��������� � ������� �����
	 * ���� ��������.
	 * <p>
	 * �� ������ ������, ������������ �������� �������������
	 * {@link #MeasurementParameters(MonitoredElement)}
	 * � ����������� �������� �������
	 * {@link #setTemplate(ActionTemplate)}
	 * 
	 * @param template ������ ������ ���������
	 * @throws ApplicationException ������ StorableObject Framework
	 */
	public MeasurementParameters(ActionTemplate<Measurement> template)
	throws ApplicationException {
		this((MonitoredElement) StorableObjectPool.getStorableObject(
				template.getMonitoredElementIds().iterator().next(),
				true));

		// ������������� ��������� ��������
		setFromTemplate(template);
	}

	/**
	 * �������� ��������� � ������������ � �������� ��������.
	 * ������ ������ ���������� � ��� �� ����� ������������,
	 * �� ������� ���������� ������ {@link MeasurementParameters}.
	 * @param template ����������� ������, �� ��� �� ����� ������������, not null
	 * @throws ApplicationException ������ StorableObject Framework
	 */
	public void setTemplate(ActionTemplate<Measurement> template)
	throws ApplicationException {
		if (!template.getMonitoredElementIds().iterator().next().equals(this.me)) {
			throw new IllegalStateException(
					"Monitored element should be change for MeasurementParameters");
		}
		setFromTemplate(template);
	}

	/**
	 * ���������� ReflectometryMeasurementParameters � �����������
	 * �������������, ����������� ��� ���������� ������ estimator'�
	 */
	private ReflectometryMeasurementParameters getRMP() {
		final boolean hasGainSplice = hasValue(ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON)
			&& getValue(ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON).asBoolean();
		final boolean hasHiRes = hasValue(ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES)
			&& !getValue(ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES).asBoolean();
		final boolean hasLFD = hasValue(ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT)
			&& getValue(ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT).asBoolean();
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
	 * @todo ����������� ��� ����-������ � measurement, ��� ��� ����� �����
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
		double durationSeconds =
			ReflectometryUtil.getUpperEstimatedAgentTestTime(
					getRMP(),
					getEstimator());
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
	 * @return true, ���� ������ �������� ���������� ��� ������� ���� �����
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
	 * @return ��� ������� ��������
	 * @throws IllegalArgumentException �������� �� ����������
	 */
	public ParameterValueKind getPropertyValueKind(Property property) {
		checkProperty(property);
		final ParameterRecord record = this.properties.get(property);
		return record.getValueKind();
	}

	/**
	 * ���������� ��������� ������������� ��������.
	 * �������������� ������ ��� �������� ��������.
	 * 
	 * @return ��������� ������������� ��������, not null
	 * @throws IllegalArgumentException �������� �� ����������
	 * @throws IllegalStateException ��� �������� �� ��������������
	 */
	public String getPropertyStringValue(Property property) {
		checkProperty(property);
		return this.properties.get(property).getStringValue();
	}

	/**
	 * ������������� �������� �� ��� ���������� �������������.
	 * �������������� ������ ��� �������� ��������.
	 * 
	 * @param property ��������������� ��������
	 * @param value ��������������� ��������, not null
	 * @throws IllegalArgumentException �������� �� ����������
	 * @throws IllegalStateException ��� �������� �� ��������������
	 */
	public void setPropertyStringValue(Property property, String value) {
		assert value != null;
		checkProperty(property);
		this.properties.get(property).setStringValue(value);
	}

	/**
	 * ���������� ��������� ��������� �������������
	 *   ���������� �������� ������������� ���������.
	 * �������������� ������ ��� �������� ��������.
	 * 
	 * @return ��� enumerated ����: ��������� �����,
	 *   ��� continuous ����: null
	 * @throws IllegalArgumentException �������� �� ����������
	 * @throws IllegalStateException ��� �������� �� ��������������
	 */
	public List<String> valuesPropertyStringValue(Property property) {
		checkProperty(property);
		final List<String> allowed = this.properties.get(property).getAllowedStringValues();
		return imposeConstraints(this.properties, property, allowed);
	}

	/**
	 * ��������� ����� ���������� �������� �������
	 * �������� � ������������ � {@link ParametersConstraints}.
	 * @param properties ������� ����� �������
	 * @param property ������ ��������
	 * @param allowedList ������� (�����������) ������ ���������� ��������.
	 *   �� ����� �������; � ����� ������� �����.
	 * @return ����� ������, ��������� �� ��� ��������� ��������
	 *   ������, ������� ������������� {@link ParametersConstraints}.
	 *   ������� ���������� ��������� �����������.
	 */
	private static List<String> imposeConstraints(
			Map<Property, ParameterRecord> properties,
			Property property,
			List<String> allowedList) {
		List<String> filtered = new ArrayList<String>(allowedList.size());
		for (String value : allowedList) {
			if (satisfiesConstraints(property, value, properties)) {
				filtered.add(value);
			}
		}
		return filtered;
	}

	/**
	 * @todo �������� javadoc
	 * @param property ����������� ��������. ������ ���� ����������
	 *  � ������� ������
	 * @param value ����������� ��������
	 * @param properties ������� �����
	 * @return true, ���� ����� �������� ��������� ������� �������
	 */
	private static boolean satisfiesConstraints(Property property,
			String value,
			Map<Property, ParameterRecord> properties) {
		try {
			return satisfiesConstraints0(property, value, properties);
		} catch (ApplicationException e) {
			/* XXX: ApplicationException handling */
			Log.errorMessage(e);
			throw new InternalError(e.getMessage());
		}
	}

	private static boolean satisfiesConstraints0(Property property,
			String value,
			Map<Property, ParameterRecord> properties)
	throws ApplicationException {
		ParameterRecord record = properties.get(property);
		assert record != null;
		final Identifier portTypeId = record.getBinding().getMeasurementPortTypeId();
		switch(property) {
		case E_RESOLUTION:
			final double tracelength = properties.get(Property.E_TRACELENGTH).asDouble();
			final double resolution = ParameterRecord.stringToDouble(value);
			return ParametersConstraints.getInstance().
				isCompatibleResolutionAndTracelength(
					portTypeId, resolution, tracelength);
		case E_PULSE_WIDTH_NS:
			/*
			 * � ����� �������������� ��� ������,
			 * ������� �������� E_PULSE_WIDTH_NS,
			 * ����� � �������� hires/lowres.
			 */
			final int pulseWidth = ParameterRecord.stringToInteger(value);
			final boolean lowRes = properties.get(Property.FLAG_LOWRES).asBoolean();
			return ParametersConstraints.getInstance().
			isCompatibleHiResAndPulsewidth(portTypeId, !lowRes, pulseWidth);
		default:
			/*
			 * ��������� ��������� �� ����� ����������� �������������
			 */
			return true;
		}
	}

	/**
	 * ���������� �������� boolean-��������.
	 * 
	 * @return �������� boolean ��������
	 * @throws IllegalArgumentException �������� �� ����������
	 * @throws IllegalStateException ��� �������� �� boolean
	 */
	public boolean getPropertyAsBoolean(Property property) {
		checkProperty(property);
		return this.properties.get(property).asBoolean();
	}

	/**
	 * ������������� �������� boolean-��������.
	 * 
	 * @param property ��������������� ��������
	 * @param value ��������������� �������� ��������
	 * @throws IllegalArgumentException �������� �� ����������
	 * @throws IllegalStateException ��� �������� �� boolean
	 */
	public void setPropertyAsBoolean(Property property, boolean value) {
		checkProperty(property);
		this.properties.get(property).setBoolean(value);
	}

	private String getUnit(final ParameterType parameterType) {
		String name = parameterType.getMeasurementUnit().getName();
		return name.trim().length() > 0 ? ", " + name : "";
	}

	public String getPropertyDescription(Property property) {
		checkProperty(property);
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