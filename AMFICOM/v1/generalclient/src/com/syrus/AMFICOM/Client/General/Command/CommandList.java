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
// * ��������: ����������� ������ ������                                  * //
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

import java.util.Vector;

public class CommandList extends Object
{
	protected Vector commands;	// ������ ������

	private int maxlength = 10;	// ������������ ���������� ������ � ������

	private int curindex = 0;	// ������ ��������� ����������� ������� � ������
								// + 1 (��� ������ ������ ������� � ����������
								// �������)

	public CommandList()
	{
	}

	// ��������
	public CommandList(Vector commands)
	{
		this.commands = (Vector )commands.clone();
	}

	public void proceed(int c)	// ��������� ��������� c ������ � ������
	{
		int count = getCount();
		if(c + curindex < count)	// ���� ���� ��������� ������ ������, ���
									// ���� � ������, �� ��������� ���
			count = c + curindex;	// � ��������� ������ ��������� ������ C
		for(; curindex < count; curindex++)
		{
			Command command = (Command )commands.get(curindex);
			command.execute();
		}
	}

	public void proceed_undo(int c)	// ��������� �������� C ������
	{
		int index = 0;
		if(c < curindex + 1)			// ���� ��������� undo ��� ����������
										// ������ ��������, ��� ����, ��
										// ��������� undo ��� ����, � ���������
			index = curindex + 1 - c;	// ������ ������ ��� c ������
		for(; curindex >= index; curindex--)
		{
			Command command = (Command )commands.get(curindex);
			command.undo();
		}
	}

	public void proceed_redo(int c)	// �������� ��������� c ������
	{
		int count = getCount();
		if(c + curindex < count)	// ���� ���� ��������� ������ ������, ���
									// ���� � ������, �� ��������� ���
			count = c + curindex;	// � ��������� ������ ��������� ������ C
		for(; curindex < count; curindex++)
		{
			Command command = (Command )commands.get(curindex);
			command.redo();
		}
	}

	public void add(Command command)// �������� ������� � ����� ������
	{
		commands.add(command);
	}

	public void flush()				// ������� ���� ������
	{
		commands.clear();
		curindex = 0;
	}

	public void removeTop(int c)	// ������� c ������ � ����� ������
	{
		int i;
		int index = 0;
		if(c < getCount())
			index = getCount() - c;
		for(i = getCount() - 1; i >= index; i--)
		{
			Command command = (Command )commands.get(i);
			if(i > curindex - 1)		// ���� � ������� ��� �������� undo,
										// �� ���� ��� ��������� �� curindex
										// � ������, �� ����������� undo
				command.commit_undo();	//
			commands.remove(i);
		}
		if(curindex > getCount() - 1)	// ��������� ������ ������� �������
			curindex = getCount() - 1;	// ���� �� ������ �� �������
	}

	public void removeBottom(int c)	// ������� c ������ � ������ ������
	{
		int i;
		int count = getCount();
		if(c < count)
			count = c;
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(0);
			if(i < curindex)			// ���� ������� ��� ���� ���������,
										// �� ���� ��� ��������� ����� curindex
										// � ������, �� ����������� ����������
				command.commit_execute();	//
			commands.remove(0);
		}
		curindex -= count;
		if(curindex < 0)			// ��������� ������ ������� �������
			curindex = 0;			// ���� �� ������ �� �������
	}

	public void step()				// ��������� ���� ��������� �������
	{
		if(curindex == getCount())	// ���� � ����� ������ �� ��������� ������
			return;
		Command command = (Command )commands.get(curindex++);
									// ��������� ������� � ����������� ���������
									// ������ ����������� ������
		command.execute();
	}

	public void redo()				// ��������� ���� ��������� �������
	{
		if(curindex == getCount())	// ���� � ����� ������ �� ��������� ������
			return;
		Command command = (Command )commands.get(curindex++);
									// ��������� ������� � ����������� ���������
									// ������ ����������� ������
		command.redo();
	}

	public void undo()				// ������� ��������� ���� �������
	{
		if(curindex == 0)			// ���� � ������ ������ �� ��������� ������
			return;
		Command command = (Command )commands.get(curindex--);
									// ��������� undo ������� � �����������
									// ��������� ������ ����������� ������
		command.undo();
	}

	public int getCount()			// �������� ���������� ������ � ������
	{
		return commands.size();
	}
}

 