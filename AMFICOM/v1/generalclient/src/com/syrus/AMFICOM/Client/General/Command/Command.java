//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ��������� ����������� �������                              * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 16 jul 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\Command.java                                  * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Command;

public interface Command extends Cloneable
{
	public static int RESULT_UNSPECIFIED = 0;
	public static int RESULT_OK = 1;
	public static int RESULT_YES = 1;
	public static int RESULT_NO = 2;
	public static int RESULT_CANCEL = 3;

	public void execute();			// ������ ���������� �������

	public void undo();				// �������� ���������� - ��������������
									// ����������� ���������

	public void redo();				// ��������� ���������� �������

	public void commit_execute();	// ������������� �������������� ����������
									// ������� � ������������ ��������

	public void commit_undo();		// ������������� �������������� ���������
									// ���������� ������� � ������������
									// ��������

	public Object getSource();		// �������� �������� �������

	public Object clone();			// �������� ����� �������. ������������ ���
									// ��������� ������ ����� �������

	public void setParameter(String field, Object value);
									// ���������� �������� ��������� field

	public int getResult();
}

