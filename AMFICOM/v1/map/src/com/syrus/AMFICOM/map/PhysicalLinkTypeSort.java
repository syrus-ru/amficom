/*-
 * $Id: PhysicalLinkTypeSort.java,v 1.6 2005/08/05 09:05:58 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;


/**
 * ��� ����� �������������� �����. ���������� ��������� �����������������
 * ����� �����, ������� ������������ ����� {@link #codename}, ���������������
 * ������-���� �������� {@link #TUNNEL}, {@link #COLLECTOR}, {@link #INDOOR},
 * {@link #SUBMARINE}, {@link #OVERHEAD}, {@link #UNBOUND}
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/05 09:05:58 $
 * @module map_v1
 * @deprecated use com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort
 */

@Deprecated
public class PhysicalLinkTypeSort {

	/** ������� */
	public static final String _TUNNEL = "tunnel";
	/** ������� ���������� */
	public static final String _COLLECTOR = "collector";
	/** ���������� �������� */
	public static final String _INDOOR = "indoor";
	/** ��������� ����� */
	public static final String _SUBMARINE = "submarine";
	/** �������� ����� */
	public static final String _OVERHEAD = "overhead";
	/** ������������� ������ */
	public static final String _UNBOUND = "cable";

	public static final PhysicalLinkTypeSort TUNNEL = new PhysicalLinkTypeSort(_TUNNEL);
	/** ������� ���������� */
	public static final PhysicalLinkTypeSort COLLECTOR = new PhysicalLinkTypeSort(_COLLECTOR);
	/** ���������� �������� */
	public static final PhysicalLinkTypeSort INDOOR = new PhysicalLinkTypeSort(_INDOOR);
	/** ��������� ����� */
	public static final PhysicalLinkTypeSort SUBMARINE = new PhysicalLinkTypeSort(_SUBMARINE);
	/** �������� ����� */
	public static final PhysicalLinkTypeSort OVERHEAD = new PhysicalLinkTypeSort(_OVERHEAD);
	/** ������������� ������ */
	public static final PhysicalLinkTypeSort UNBOUND = new PhysicalLinkTypeSort(_UNBOUND);
	private String codename;

	PhysicalLinkTypeSort(final String codename) {
		this.codename = codename;
	}

	public String value() {
		return this.codename;
	}

	public static PhysicalLinkTypeSort fromString(final String codename) {
		if (codename.equals(_TUNNEL))
			return TUNNEL;
		if (codename.equals(_COLLECTOR))
			return COLLECTOR;
		if (codename.equals(_INDOOR))
			return INDOOR;
		if (codename.equals(_SUBMARINE))
			return SUBMARINE;
		if (codename.equals(_OVERHEAD))
			return OVERHEAD;
		if (codename.equals(_UNBOUND))
			return UNBOUND;
		return null;
	}
}
