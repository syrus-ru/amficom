/*-
 * $Id: ReflectogramMismatch.java,v 1.8 2005/10/09 15:04:33 bass Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.reflectometry.corba.IdlAlarmType;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;
import com.syrus.util.TransferableObject;

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
 * �� ������ (�� ��� ���������) ��� ��������� � ���.
 * <p> ���� ����� ���������� �� �������� ������� (������, ���������),
 * ��� ������� �������� � �����, �� anchor1Id � anchor2Id ���������,
 * � �����. ��������� �������.
 * <p> ���� {@link #hasAnchors()} ���������� false, �� ��������
 * ��� � ����� ������������ ������ ������ ���������� � ������� ���������.
 * </ol>
 * XXX: ��������, ����� ����� ��������� ������ �������� ��������� � ��� ����
 * ���������� �� ���� ������� �� ������. ���� ������ ������������� ��,
 * ���� ��� �� �������� ������ ���� �����.
 * 
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/10/09 15:04:33 $
 * @module reflectometry
 */
public interface ReflectogramMismatch {
	/**
	 * ������� ������� (���, �����, �������������� ��������).
	 * Must be comparable.
	 * � ��������� ������ ����������� ��������� ���������� ������� ������� ��
	 * �������: ����� �� ������ �����, ��� �� ����� ��������������.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.8 $, $Date: 2005/10/09 15:04:33 $
	 * @module reflectometry
	 */
	enum Severity implements TransferableObject<IdlSeverity> {
		SEVERITY_NONE, // just a convenience level, not a real alarm
		SEVERITY_SOFT, // soft alarm ('warning')
		SEVERITY_HARD;  // hard alarm ('alarm')

		private static Severity[] values = values();

		/**
		 * @param orb
		 * @see TransferableObject#getTransferable(ORB)
		 */
		public IdlSeverity getTransferable(final ORB orb) {
			return IdlSeverity.from_int(this.ordinal());
		}

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static Severity valueOf(final int i) {
			return values[i];
		}

		/**
		 * @param severity
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static Severity valueOf(final IdlSeverity severity) {
			return valueOf(severity.value());
		}
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.8 $, $Date: 2005/10/09 15:04:33 $
	 * @module reflectometry
	 */
	enum AlarmType implements TransferableObject<IdlAlarmType> {
		TYPE_UNDEFINED,
		TYPE_LINEBREAK, // ����� �����
		TYPE_OUTOFMASK, // ����� �� �����
		TYPE_EVENTLISTCHANGED; // �����/���������� ������� � �������� �����

		private static AlarmType[] values = values();

		/**
		 * @param orb
		 * @see TransferableObject#getTransferable(ORB)
		 */
		public IdlAlarmType getTransferable(final ORB orb) {
			return IdlAlarmType.from_int(this.ordinal());
		}

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static AlarmType valueOf(final int i) {
			return values[i];
		}

		public static AlarmType valueOf(final IdlAlarmType alarmType) {
			return valueOf(alarmType.value());
		}
	}

	/**
	 * @return �������������� ��������, see {@link Severity}.
	 */
	Severity getSeverity();

	/**
	 * @return ��� ��������������, see {@link AlarmType}.
	 */
	AlarmType getAlarmType();

	/**
	 * @return ���������� ������ (�����).
	 */
	int getCoord();

	/**
	 * @return �������� ���������� ��������� ������� ������ (�����).
	 */
	int getEndCoord();

	/**
	 * @return ����������, �����/����.
	 */
	double getDeltaX();

	/**
	 * ���������, �����.
	 * XXX: ��-��������, ���� ����� ������ ���� ��������� ��� final � ����������� ������.
	 * @return _������_ ����������
	 *   {@link #getDeltaX()} * {@link #getCoord()}
	 */
	double getDistance();

	/**
	 * @return true, ���� ������� ���������� �������. ������ ����������.
	 */
	boolean hasMismatch();

	/**
	 * @return ������ ������ ������� ���������� �������. ������,
	 *   ���� ������ ������� ���������� �������. ������ ����������.
	 * �������������, ��� {@link #getMinMismatch()} &lt;= {@link #getMaxMismatch()}
	 * @throws IllegalStateException ������� ���������� �� ����������,
	 *  ({@link #hasMismatch()} == false)
	 */
	double getMinMismatch();

	/**
	 * @return ������� ������ ������� ���������� �������. ������,
	 *   ���� ������ ������� ���������� �������. ������ ����������.
	 * �������������, ��� {@link #getMinMismatch()} &lt;= {@link #getMaxMismatch()}
	 * @throws IllegalStateException ������� ���������� �� ����������
	 *  ({@link #hasMismatch()} == false)
	 */
	double getMaxMismatch();

	/**
	 * @return true, ���� �������� ������������� �������� �� ���� ������
	 * {@link #getAnchor1Id} {@link #getAnchor1Coord}
	 * {@link #getAnchor2Id} {@link #getAnchor2Coord}
	 */
	boolean hasAnchors();

	/**
	 * @return ID ����� 1. not null.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	SOAnchor getAnchor1Id();

	/**
	 * @return ID ����� 2. not null.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	SOAnchor getAnchor2Id();

	/**
	 * @return ���������(�����) ����� 1.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	int getAnchor1Coord();

	/**
	 * @return ���������(�����) ����� 2.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	int getAnchor2Coord();
}