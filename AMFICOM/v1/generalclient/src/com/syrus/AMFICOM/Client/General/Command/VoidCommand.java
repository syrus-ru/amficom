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
// * ��������: �������� ��� ������� (������ �������)                      * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 16 jul 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\VoidCommand.java                              * //
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

public class VoidCommand implements Command
{
	// ���� ��������� �������
	private Object source;

	// � ������ ������� �� ��������� ��� ���������
	public VoidCommand()
	{
		source = new String("NULL");
	}

	// ��������
	public Object clone()
	{
		return new VoidCommand(source);
	}

	public VoidCommand(Object source)
	{
		if(source == null)
			source = new String("NULL");
		this.source = source;
	}

	// ������ ������� �� ��������� ������� ��������
	public void execute()
	{
		System.out.println("Void command executed for " + source.toString() + " - ignored");
	}
	
	public int getResult()
	{
		return RESULT_OK;
	}

	// ������ ������� �� ��������� ������� ��������
	public void undo()
	{
		System.out.println("Void command undo for " + source.toString() + " - ignored");
	}

	// ������ ������� �� ��������� ������� ��������
	public void redo()
	{
		System.out.println("Void command redo for " + source.toString() + " - ignored");
	}

	// ������ ������� �� ��������� ������� ��������
	public void commit_execute()
	{
		System.out.println("Void command execution commit for " + source.toString());
	}

	// ������ ������� �� ��������� ������� ��������
	public void commit_undo()
	{
		System.out.println("Void command undo commit for " + source.toString() + " - ignored");
	}

	// � ������ ������� ��� ���������
	public Object getSource()
	{
		System.out.println("Source for Void command is " + source.toString() + " - ignored");
		return null;
	}

	// ������ ������� �� ����� ����������
	public void setParameter(String field, Object value)
	{
		System.out.println("Set for Void command paramenter " + field +
				" to value " + value.toString() + " - ignored");
	}

}

 