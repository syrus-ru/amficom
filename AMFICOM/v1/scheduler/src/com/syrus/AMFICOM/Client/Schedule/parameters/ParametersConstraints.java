/*-
 * $Id: ParametersConstraints.java,v 1.1.2.1 2006/04/18 09:18:59 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.parameters;

import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.reflectometry.ReflectometryCharacteristicTypeCodename;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/18 09:18:59 $
 * @module
 */
public class ParametersConstraints {
	private static ParametersConstraints INSTANCE = null;

	private final CharacteristicType pointsMaxNumberCType;
	private final CharacteristicType hiResMaxPulseWidthNsCType;
	private final CharacteristicType lowResMinPulseWidthNsCType;

	private final Map<Identifier, Integer> pointsMaxNumberCache =
		new HashMap<Identifier, Integer>();
	private final Map<Identifier, Integer> hiResMaxPulseWidthNsCache =
		new HashMap<Identifier, Integer>();
	private final Map<Identifier, Integer> lowResMinPulseWidthNsCache =
		new HashMap<Identifier, Integer>();

	private ParametersConstraints() throws ApplicationException {
		this.pointsMaxNumberCType = CharacteristicType.valueOf(
				ReflectometryCharacteristicTypeCodename.POINTS_MAX_NUMBER);
		this.hiResMaxPulseWidthNsCType = CharacteristicType.valueOf(
				ReflectometryCharacteristicTypeCodename.HIGH_RES_MAX_PULSE_WIDTH_NS);
		this.lowResMinPulseWidthNsCType = CharacteristicType.valueOf(
				ReflectometryCharacteristicTypeCodename.LOW_RES_MIN_PULSE_WIDTH_NS);
	}

	public static ParametersConstraints getInstance()
	throws ApplicationException {
		if (INSTANCE == null) {
			synchronized (ParametersConstraints.class) {
				if (INSTANCE == null) {
					INSTANCE = new ParametersConstraints();
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * Определяет, совместима ли пара "разрешение - длина линии"
	 * @param portTypeId Id типа измерительного порта
	 * @param resolution разрешение (м)
	 * @param tracelength длина линии (км)
	 * @return true тогда и только тогда, когда пара совместима
	 * @throws ApplicationException ошибки SOF
	 */
	public boolean isCompatibleResolutionAndTracelength(Identifier portTypeId,
			double resolution,
			double tracelength) throws ApplicationException {
		final int maxPoints = getPointsMaxNumber(portTypeId);
//		System.err.println("comparison Resolution vs Tracelength:" 
//				+ " res " + resolution
//				+ " tracelength " + tracelength
//				+ " maxPoints " + maxPoints);
		return tracelength * 1000.0 <= resolution * maxPoints;
	}

	/**
	 * Определяет, совместима ли пара "режим высокого разрешения - длина импульса"
	 * @param portTypeId Id типа измерительного порта
	 * @param hiRes true в режиме высокого разрешения
	 * @param pulseWidthNs длина импульса в нс
	 * @return true тогда и только тогда, когда пара совместима
	 * @throws ApplicationException ошибки SOF
	 */
	public boolean isCompatibleHiResAndPulsewidth(Identifier portTypeId,
			boolean hiRes,
			int pulseWidthNs) throws ApplicationException {
		if (hiRes) {
//			System.err.println("comparison hiRes vs pulsewidth:"
//					+ " hiResMaxPW " + getHiResMaxPulseWidthNs(portTypeId)
//					+ " pulsewidth " + pulseWidthNs);
			return pulseWidthNs <= getHiResMaxPulseWidthNs(portTypeId);
		} else {
//			System.err.println("comparison hiRes vs pulsewidth:"
//					+ " lowResMinPW " + getLowResMinPulseWidthNs(portTypeId)
//					+ " pulsewidth " + pulseWidthNs);
			return pulseWidthNs >= getLowResMinPulseWidthNs(portTypeId);
		}
	}

	/**
	 * Определяет максимальное число точек в рефлектограмме на данном
	 * измерительном порту.
	 * @param measurementPortTypeId id данного измерительного порта
	 * @return максимальное число точек (ненулевое)
	 * @throws ApplicationException ошибки SOF
	 */
	private int getPointsMaxNumber(final Identifier measurementPortTypeId)
	throws ApplicationException {
		final int value = getValueThroughCache(measurementPortTypeId,
				this.pointsMaxNumberCache,
				this.pointsMaxNumberCType,
				0);
		if (value == 0) {
			throw new InternalError("POINTS_MAX_NUMBER undefined");
		}
		return value;
	}

	/**
	 * Определяет максимальную длину импульса (ns), поддерживаемую
	 * на данном измерительном порту в режиме высокого разрешения.
	 * @param measurementPortTypeId id данного измерительного порта
	 * @return максимальная длина импульса (ns) в режиме высокого разрешения
	 *   либо Integer.MAX_VALUE, если нет ограничения, нет самого режима высокого разрешения
	 *   или нет параметра "длина импульса (ns)".
	 * @throws ApplicationException ошибки SOF
	 */
	private int getHiResMaxPulseWidthNs(final Identifier measurementPortTypeId)
	throws ApplicationException {
		final int value = getValueThroughCache(measurementPortTypeId,
				this.hiResMaxPulseWidthNsCache,
				this.hiResMaxPulseWidthNsCType,
				Integer.MAX_VALUE);
		return value;
	}

	/**
	 * Определяет минимальную длину импульса (ns), поддерживаемую
	 * на данном измерительном порту в режиме низкого разрешения.
	 * @param measurementPortTypeId id данного измерительного порта
	 * @return минимальная длина импульса (ns) в режиме низкого разрешения
	 *   либо 0, если нет ограничения, нет самого режима низкого разрешения
	 *   или нет параметра "длина импульса (ns)".
	 * @throws ApplicationException ошибки SOF
	 */
	private int getLowResMinPulseWidthNs(final Identifier measurementPortTypeId)
	throws ApplicationException {
		final int value = getValueThroughCache(measurementPortTypeId,
				this.lowResMinPulseWidthNsCache,
				this.lowResMinPulseWidthNsCType,
				0);
		return value;
	}

	private static int getValueThroughCache(final Identifier measurementPortTypeId,
			Map<Identifier, Integer> cache,
			CharacteristicType cType,
			int defaultValue)
	throws ApplicationException {
		if (cache.containsKey(measurementPortTypeId)) {
			return cache.get(measurementPortTypeId).intValue();
		}
		Characteristic characteristic = loadCharacteristic(
				measurementPortTypeId,
				cType.getId());
		final int value = characteristic == null
				? defaultValue
				: Integer.parseInt(characteristic.getValue());
		cache.put(measurementPortTypeId, Integer.valueOf(value));
		return value;
	}

	private static Characteristic loadCharacteristic(
			Identifier measurementPortTypeId,
			Identifier characteristicTypeId)
	throws ApplicationException {
		final CompoundCondition characteristicCondition = new CompoundCondition(
				new LinkedIdsCondition(measurementPortTypeId, CHARACTERISTIC_CODE),
				AND,
				new LinkedIdsCondition(characteristicTypeId, CHARACTERISTIC_CODE));
		final Set<Characteristic> characteristics =
			StorableObjectPool.getStorableObjectsByCondition(
					characteristicCondition, true);
		if (characteristics.isEmpty()) {
			return null;
		}
		assert characteristics.size() == 1 : ErrorMessages.ONLY_ONE_EXPECTED;
		return characteristics.iterator().next();
	}
}
