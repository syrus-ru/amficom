/*-
 * $Id: ReflectogramMismatch.java,v 1.3 2005/10/07 08:15:12 bass Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * �������� �������������� �������������� �������.
 * (��� ��� �� �����!)
 * ����� ���������� � ����, ������ � ��������� ������� � �������� ���������.
 * <p>
 * ��������� � �������� ������� ������� ���������.
 * �������� {@link #getCoord} �������� ��������� ���������� ��������� (� ������)
 * ������� ��� �������, � ������� ������������� ����������.
 * �������� {@link #getDistance()} ������������ �� �� ��������� ����������
 * ��������� � ������
 * ��� �������� ��������� ���������� ���������
 * � ������� ���������� ��������� ���� ��� �������:
 * <ol>
 * <li> ���������������� ����������� ��������� ���������� ���������
 * {@link #getDistance()} � �������� ������� ���������� ���������.
 * ��� ���� ��������� �����������, ������������
 * �������� ������� ���������� ��������� �� �������� ���������� ���������
 * (��-�� ����������� ����������� ������ ������� ��������), �
 * ����� �������� ��������� ���������� ��������� �� ��������.
 * ���� ��� ����������� ����� ����������� ������ ��� ������� ����� ����
 * ���������������, ��� ������ ��������� ������ �����������, ��������� ��
 * ������� � ������� ��������� ������� (����� � ��.) ��� � �������� � ���
 * ������.
 * <li> � ������, ���� {@link #hasAnchors()} ���������� true, ��������
 * ������������� ��������
 *   {@link #getAnchor1Id}/{@link #getAnchor1Coord},
 *   {@link #getAnchor2Id}/{@link #getAnchor2Coord}.
 * ��� ���������� ��������� ��������� ���������� ��������� (���� � ������)
 * ��� 2 ��������, ����������� �� ������ ������� �� ������.
 * ��� ��������� ������ ���������� ����� ������. � ������, ���� � ������
 * ������� ������� ���� ������������� �������� ��� ������� �������,
 * �������� �������� �������, ���������������� ������, ���������� ����������,
 * � ����������� ��������� ������ � �������� �������� ��� ���������
 * ���������� � ���������� ���������.
 * <p> ��� �������������� ������� ���������� ��� ���������� ���������,
 * ���� ������������ ���������
 * 
 * <pre> (coord - anchor1Coord) : (anchor2Coord - coord) = L1 : L2 </pre>
 * 
 * ��� L1 � L2 - ������� (���������� ��� ����������) ���������� �� ������
 * �� ������������� �������� �����.
 * <p> ���� ������� �������� ����������, �� ��� ����� �� ������ �������
 * �� ������ (�� ��� ���������).
 * <p> ���� ����� ���������� �� �������� ������� (������, ���������),
 * ��� ������� �������� � �����, �� anchor1Id � anchor2Id ���������,
 * � �����. ��������� �������.
 * <p> ���� {@link #hasAnchors()} ���������� false, �� ��������
 * ��� � ����� ������������ ������ ������ ���������� � ������� ���������.
 * </ol>
 * 
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/07 08:15:12 $
 * @module reflectometry
 */
public interface ReflectogramMismatch {
	/**
	 * Alarm levels. Must be comparable with >; >=
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.3 $, $Date: 2005/10/07 08:15:12 $
	 * @module reflectometry
	 */
	enum Severity {
		SEVERITY_NONE, // just a convenience level, not a real alarm
		SEVERITY_SOFT, // soft alarm ('warning')
		SEVERITY_HARD;  // hard alarm ('alarm')

		private static Severity[] values = values();

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static Severity valueOf(final int i) {
			return values[i];
		}
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.3 $, $Date: 2005/10/07 08:15:12 $
	 * @module reflectometry
	 */
	enum AlarmType {
		TYPE_UNDEFINED,
		TYPE_LINEBREAK, // ����� �����
		TYPE_OUTOFMASK, // ����� �� �����
		TYPE_EVENTLISTCHANGED; // �����/���������� ������� � �������� �����

		private static AlarmType[] values = values();

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static AlarmType valueOf(final int i) {
			return values[i];
		}
	}

	/**
	 * @return true, ���� ������� ���������� �������. ������ ����������
	 */
	boolean hasMismatch();

	/**
	 * @return ������ ������ ������� ���������� ������������������ ������,
	 *   ���� ������� ���������� ����������
	 *   ({@link #hasMismatch() ���������� true})
	 * @throws IllegalArgumentException, ���� ������� ���������� �� ����������
	 */
	double getMinMismatch();

	/**
	 * @return ������� ������ ������� ���������� ������������������ ������,
	 *   ���� ������� ���������� ����������
	 *   ({@link #hasMismatch() ���������� true})
	 * @throws IllegalArgumentException, ���� ������� ���������� �� ����������
	 */
	double getMaxMismatch();

	/**
	 * @return �������������� ��������, ������������ ����������� SEVERITY_*
	 */
	Severity getSeverity();

	/**
	 * XXX: ��-��������, ���� ����� ������ ���� ��������� ��� final � ����������� ������
	 * @return _������_ ����������
	 *   {@link #getDeltaX()} * {@link #getCoord()}
	 */
	double getDistance();

	/**
	 * @return true, ���� �������� ������������� ��������
	 * {@link #getAnchor1Id} {@link #getAnchor1Coord}
	 * {@link #getAnchor2Id} {@link #getAnchor2Coord}
	 */
	boolean hasAnchors();

	SOAnchor getAnchor1Id();

	SOAnchor getAnchor2Id();

	int getAnchor1Coord();

	int getAnchor2Coord();

	/**
	 * @return ���������� ������ (�����)
	 */
	int getCoord();

	/**
	 * @return �������� �������� ���������� ������ (�����)
	 */
	int getEndCoord();

	/**
	 * @return ��� ��������������, ��. ���� TYPE_*
	 */
	AlarmType getAlarmType();

	/**
	 * @return ����������, �����/����
	 */
	double getDeltaX();

}