/*-
 * $Id: ReflectogramMismatch.java,v 1.16 2005/11/25 08:23:48 saa Exp $
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
 * ��������� � ������.
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
 * @author $Author: saa $
 * @version $Revision: 1.16 $, $Date: 2005/11/25 08:23:48 $
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
	 * @author $Author: saa $
	 * @version $Revision: 1.16 $, $Date: 2005/11/25 08:23:48 $
	 * @module reflectometry
	 */
	enum Severity implements TransferableObject<IdlSeverity> {
		/**
		 * just a convenience level, not a real alarm
		 */
		SEVERITY_NONE("ReflectogramMismatch.Severity.None"), 
		/**
		 * soft alarm ('warning')
		 */
		SEVERITY_SOFT("ReflectogramMismatch.Severity.Soft"), 
		/**
		 * hard alarm ('alarm')
		 */
		SEVERITY_HARD("ReflectogramMismatch.Severity.Hard"); 

		private static Severity[] values = values();

		private final String key;

		private Severity(final String key) {
			this.key = key;
		}

		/**
		 * @param orb
		 * @see TransferableObject#getTransferable(ORB)
		 */
		public IdlSeverity getTransferable(final ORB orb) {
			return IdlSeverity.from_int(this.ordinal());
		}

		public String getLocalizedName() {
			return I18N.getString(this.key);
		}

		public String getLocalizedDescription() {
			return I18N.getString("ReflectogramMismatch.Severity") + ":\t" + this.getLocalizedName();
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
	 * ��� ����������.
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: saa $
	 * @version $Revision: 1.16 $, $Date: 2005/11/25 08:23:48 $
	 * @module reflectometry
	 */
	enum AlarmType implements TransferableObject<IdlAlarmType> {
		/**
		 * ��� �� ���������
		 * XXX: ������������ ������ ������ � SEVERITY_NONE
		 */
		TYPE_UNDEFINED("ReflectogramMismatch.AlarmType.Undefined"),
		/**
		 * ����� �����
		 */
		TYPE_LINEBREAK("ReflectogramMismatch.AlarmType.LineBreak"),
		/**
		 * ���������� (����� �� �����)
		 */
		TYPE_OUTOFMASK("ReflectogramMismatch.AlarmType.OutOfMask"),
		/**
		 * �����/���������� ������� � �������� �����
		 */
		TYPE_EVENTLISTCHANGED("ReflectogramMismatch.AlarmType.EventListChanged");

		private static AlarmType[] values = values();

		private final String key;

		private AlarmType(final String key) {
			this.key = key;
		}

		/**
		 * @param orb
		 * @see TransferableObject#getTransferable(ORB)
		 */
		public IdlAlarmType getTransferable(final ORB orb) {
			return IdlAlarmType.from_int(this.ordinal());
		}

		public String getLocalizedName() {
			return I18N.getString(this.key);
		}

		public String getLocalizedDescription() {
			return I18N.getString("ReflectogramMismatch.AlarmType") + ":\t" + this.getLocalizedName();
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
	 * ���������� �������������� ��������, see {@link Severity}.
	 * @return �������������� ��������, see {@link Severity}.
	 */
	Severity getSeverity();

	/**
	 * ���������� ��� ��������������, see {@link AlarmType}.
	 * @return ��� ��������������, see {@link AlarmType}.
	 */
	AlarmType getAlarmType();

	/**
	 * ���������� ���������� ������ (�����).
	 * @return ���������� ������ (�����).
	 */
	int getCoord();

	/**
	 * ���������� �������� ���������� ��������� ������� ������ (�����).
	 * �������� ���������� ������ ���� "�� �����" ���������.
	 * @return �������� ���������� ��������� ������� ������ (�����).
	 */
	int getEndCoord();

	/**
	 * ���������� ����������, �����/����.
	 * @return ����������, �����/����.
	 */
	double getDeltaX();

	/**
	 * ���������� ���������, �����.
	 * XXX: ��-��������, ���� ����� ������ ���� ��������� ��� final � ����������� ������.
	 * @return _������_ ����������
	 *   {@link #getDeltaX()} * {@link #getCoord()}
	 */
	double getDistance();

	/**
	 * ����������, ���������� �� ������� ���������� ������.
	 * @return true, ���� ������� ���������� ������ ����������.
	 */
	boolean hasMismatch();

	/**
	 * ���� ������� ���������� ������ ����������,
	 * �� ���������� ������ ������ ������� ���������� ������. 
	 * @return ������ ������ ������� ���������� ������,
	 *   ���� ������ ������� ���������� ������ ����������.
	 * �������������, ��� {@link #getMinMismatch()} &lt;= {@link #getMaxMismatch()}
	 * @throws IllegalStateException ������� ���������� �� ����������,
	 *  ({@link #hasMismatch()} == false)
	 */
	double getMinMismatch();

	/**
	 * ���� ������� ���������� ������ ����������,
	 * �� ���������� ������� ������ ������� ���������� ������. 
	 * @return ������� ������ ������� ���������� ������,
	 *   ���� ������ ������� ���������� ������ ����������.
	 * �������������, ��� {@link #getMinMismatch()} &lt;= {@link #getMaxMismatch()}
	 * @throws IllegalStateException ������� ���������� �� ����������
	 *  ({@link #hasMismatch()} == false)
	 */
	double getMaxMismatch();

	/**
	 * ����������, �������� �� ������������� �������� �� ���� ������.
	 * @return true, ���� �������� ������������� �������� �� ���� ������
	 * {@link #getAnchor1Id} {@link #getAnchor1Coord}
	 * {@link #getAnchor2Id} {@link #getAnchor2Coord}
	 */
	boolean hasAnchors();

	/**
	 * ���������� ID ����� 1.
	 * @return ID ����� 1. not null.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	SOAnchor getAnchor1Id();

	/**
	 * ���������� ID ����� 2.
	 * @return ID ����� 2. not null.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	SOAnchor getAnchor2Id();

	/**
	 * ���������� ���������(�����) ����� 1.
	 * @return ���������(�����) ����� 1.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	int getAnchor1Coord();

	/**
	 * ���������� ���������(�����) ����� 2.
	 * @return ���������(�����) ����� 2.
	 * @throws IllegalStateException ���� {@link #hasAnchors()} is false
	 */
	int getAnchor2Coord();
}