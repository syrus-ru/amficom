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
// * ��������: �������, ���������� � ���� ��������� ������                * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 16 jul 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\CommandBundle.java                            * //
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

import java.util.Enumeration;
import java.util.Vector;

public class CommandBundle implements Command
{
	protected Vector commands;			// ������ ������

	public CommandBundle()
	{
	}

	public Object clone()				// �������� ����� �������
	{
		Command c;
		CommandBundle cb = new CommandBundle();
		cb.commands = new Vector();
		for(Enumeration e = commands.elements(); e.hasMoreElements();)
		{
			c = (Command )e.nextElement();
			cb.commands.add(c.clone());
		}
		return cb;
	}

	public void add(Command command)	// �������� � ������ �������
	{
		commands.add(command);
	}

	public void remove(Command command)	// ������� ������� �� ������
	{
		commands.remove(command);
	}

	public void remove(int index)		// ������� ������� �� ������
	{
		commands.remove(index);
	}

	public void execute()				// ��������� - ��� ������� � ������
	{
		int i;
		int count = getCount();
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(i);
			command.execute();
		}
	}

	public int getResult()
	{
		return RESULT_OK;
	}

	public void undo()					// ������� ��������� - ��� ������� 
										// � ������ � �������� �������
	{
		int i;
		int count = getCount();
		for(i = count - 1; i >= 0; i--)
		{
			Command command = (Command )commands.get(i);
			command.undo();
		}
	}

	public void redo()		// ��������� - ������� � ������ �������� - ����� ��
							// ���������� �� ������� ���������� ������ ������
	{
		execute();
	}

	public void commit_execute()		// ������������� ���������� - 
										// ����������� ��� ���� ������
	{
		int i;
		int count = getCount();
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(i);
			command.commit_execute();
		}
	}

	public void commit_undo()			// ������������� ��������� ���������� -
										// ����������� ��� ���� ������
	{
		int i;
		int count = getCount();
		for(i = count - 1; i >= 0; i--)
		{
			Command command = (Command )commands.get(i);
			command.commit_undo();
		}
	}

	public Object getSource()
	{
		return null;
	}

	public int getCount()		// �������� ���������� ������ � ������ �������
	{
		return commands.size();
	}

	public void setParameter(String field, Object value)
	{
	}
}
