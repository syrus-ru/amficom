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

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class CommandBundle extends VoidCommand implements Command
{
	protected List commands = new ArrayList();			// ������ ������

	public CommandBundle()
	{
	}

	public Object clone()				// �������� ����� �������
	{
		Command c;
		CommandBundle cb = new CommandBundle();
		cb.commands = new ArrayList();
		for(ListIterator e = commands.listIterator(); e.hasNext();)
		{
			c = (Command )e.next();
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
		for(ListIterator it = commands.listIterator();it.hasNext();)
		{
			Command command = (Command )it.next();
			command.execute();
		}
	}

	public void undo()					// ������� ��������� - ��� ������� 
										// � ������ � �������� �������
	{
		for(ListIterator it = commands.listIterator(commands.size());it.hasPrevious();)
		{
			Command command = (Command )it.previous();
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
		int i;
		int count = getCount();
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(i);
			command.setParameter(field, value);
		}
	}
}
