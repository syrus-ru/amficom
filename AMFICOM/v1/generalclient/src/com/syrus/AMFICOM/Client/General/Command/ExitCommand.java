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
// * ��������: ������� �������� ���� ������                               * //
// *                                                                      * //
// * ���: Java 1.4.1_03                                                   * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 16 jul 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\ExitCommand.java                              * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 9.0.4.12.80                      * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.1_03)* //
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

import com.syrus.AMFICOM.Client.General.Model.Environment;

import java.awt.Window;

public class ExitCommand extends VoidCommand implements Command
{
	Window window;	// ����, �� �������� ������� �������

	public ExitCommand()
	{
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("window"))
			setWindow((Window )value);
	}

	public void setWindow(Window window)
	{
		this.window = window;
	}

	public ExitCommand(Window window)
	{
		this.window = window;
	}

	public void execute()
	{
		System.out.println("exit window " + window.getName());
		Environment.disposeWindow(window);	// ������� �������� ���� ����������
											// ������ ����� ��������� Environment
	}

	public Object clone()
	{
		return new ExitCommand(window);
	}

}
