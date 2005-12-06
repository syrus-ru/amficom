/*-
 * $Id: EquipmentType.java,v 1.109 2005/12/06 09:41:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.bugs.Crutch136;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment.XmlEquipmentType;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.util.Codeable;
import com.syrus.util.IdlTransferableObject;

/**
 * @version $Revision: 1.109 $, $Date: 2005/12/06 09:41:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public enum EquipmentType implements Namable, Codeable,
		IdlTransferableObject<IdlEquipmentType> {
	REFLECTOMETER("reflectometer"),
	OPTICAL_SWITCH("optical_switch"),
	MUFF("muff"),
	CABLE_PANEL("cable_panel"),
	TRANSMITTER("transmitter"),
	RECEIVER("receiver"),
	MULTIPLEXOR("multiplexor"),
	CROSS("cross"),
	FILTER("filter"),
	OTHER("other"),
	UNKNOWN("unknown"),
	RACK("rack"),

	@Crutch136(notes = "Stub for SchemeElement without Equipment")
	BUG_136("bug136");

	private static final String KEY_ROOT = "EquipmentType.Description.";

	private String codename;
	private String description;

	private EquipmentType(final String codename) {
		this.codename = codename;
		this.description = LangModelConfiguration.getString(KEY_ROOT + this.codename);
	}

	public static EquipmentType valueOf(final int code) {
		switch (code) {
			case IdlEquipmentType._REFLECTOMETER:
				return REFLECTOMETER;
			case IdlEquipmentType._OPTICAL_SWITCH:
				return OPTICAL_SWITCH;
			case IdlEquipmentType._MUFF:
				return MUFF;
			case IdlEquipmentType._CABLE_PANEL:
				return CABLE_PANEL;
			case IdlEquipmentType._TRANSMITTER:
				return TRANSMITTER;
			case IdlEquipmentType._RECEIVER:
				return RECEIVER;
			case IdlEquipmentType._MULTIPLEXOR:
				return MULTIPLEXOR;
			case IdlEquipmentType._CROSS:
				return CROSS;
			case IdlEquipmentType._FILTER:
				return FILTER;
			case IdlEquipmentType._OTHER:
				return OTHER;
			case IdlEquipmentType._UNKNOWN:
				return UNKNOWN;
			case IdlEquipmentType._RACK:
				return RACK;

			case IdlEquipmentType._BUG_136:
				return BUG_136;
			default:
				throw new IllegalArgumentException("Illegal code: " + code);
		}
	}

	public static EquipmentType fromTransferable(final IdlEquipmentType idlEquipmentType) {
		return valueOf(idlEquipmentType.value());
	}

	public static EquipmentType fromXmlTransferable(final XmlEquipmentType.Enum xmlEquipmentType) {
		return valueOf(xmlEquipmentType.intValue() - 1);
	}

	public int getCode() {
		return this.ordinal();
	}

	public String getCodename() {
		return this.codename;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.description;
	}

	public void setName(final String name) {
		throw new UnsupportedOperationException(ErrorMessages.METHOD_NOT_NEEDED);
	}

	public IdlEquipmentType getIdlTransferable(final ORB orb) {
		try {
			return IdlEquipmentType.from_int(this.getCode());
		} catch (org.omg.CORBA.BAD_PARAM bp) {
			throw new IllegalArgumentException("Illegal code: " + this.getCode());
		}
	}
	
	public XmlEquipmentType.Enum getXmlTransferable() {
		return XmlEquipmentType.Enum.forInt(this.getCode() + 1);
	}

	public static IdlEquipmentType[] createTransferables(final EnumSet<EquipmentType> equipmentTypes, final ORB orb) {
		assert equipmentTypes != null : ErrorMessages.NON_NULL_EXPECTED;

		final IdlEquipmentType[] idlEquipmentTypes = new IdlEquipmentType[equipmentTypes.size()];
		int i = 0;
		synchronized (equipmentTypes) {
			for (final EquipmentType equipmentType : equipmentTypes) {
				idlEquipmentTypes[i++] = equipmentType.getIdlTransferable(orb);
			}
		}
		return idlEquipmentTypes;
	}

	public static EnumSet<EquipmentType> fromTransferables(final IdlEquipmentType[] idlEquipmentTypes) {
		assert idlEquipmentTypes != null: NON_NULL_EXPECTED;

		final Collection<EquipmentType> equipmentTypes = new HashSet<EquipmentType>(idlEquipmentTypes.length);
		for (final IdlEquipmentType idlEquipmentType : idlEquipmentTypes) {
			equipmentTypes.add(EquipmentType.fromTransferable(idlEquipmentType));
		}
		return EnumSet.copyOf(equipmentTypes);
	}

	@Override
	public String toString() {
		return this.getCodename() + " " + this.getCode();
	}
}
