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

import java.util.LinkedList;
import java.util.Vector;

public class CommandList extends VoidCommand implements Command
{
	/** ������ ������ */
	protected Command top = null;
	
	/**
	 * ��������� ����������� ������� � ������ + 1
	 * (��� ������ ������ ������� � ���������� �������)
	 */
	protected Command current = null;

	/** ������������ ���������� ������ � ������ */
	private int maxlength = 50;

	public CommandList()
	{
	}

	public CommandList(int maxlength)
	{
		this.maxlength = maxlength;
	}

	/**
	 * ��������� ��������� c ������ � ������
	 */
	public void proceed(int c)
	{
		for(int i = 0; i < c; i++)
		{
			Command com = getPrevious(current);
			if( com != null)
			{
				current = com;
				current.execute();
			}
			else
			{
				return;
			}
		}
	}

	/**
	 * ��������� �������� C ������
	 */
	public void proceed_undo(int c)
	{
		for(int i = 0; i < c; i++)
		{
			if(current == null)
				return;
			current.undo();
			current = current.getNext();
		}
	}

	public void proceed_redo(int c)	// �������� ��������� c ������
	{
		for(int i = 0; i < c; i++)
		{
			Command com = getPrevious(current);
			if( com != null)
			{
				current = com;
				current.redo();
			}
			else
			{
				return;
			}
		}
	}
	
	public Command getPrevious(Command c)
	{
		Command com;
		
		if(c == null)
			return getBottom(); //bottom of list

		if(c == top)
			return null;// has no previous

		for(com = top; com != null; com = com.getNext())
			if(com.getNext() == c)
				return com;

		return null;// not on list
	}

	/**
	 * �������� ������� � ����� ������
	 */
	public void add(Command command)
	{
		if(command == null)
			return;

		// not executed commands are lost
		command.setNext(current);
		top = command;

		if(getCount() > maxlength)
			removeBottom(1);
	}

	/**
	 * ������� ���� ������
	 */
	public void flush()
	{
		top = null;
		current = null;
	}

	/**
	 * ������� c ������ � ����� ������
	 */
	public void removeTop(int c)
	{
		for(int i = 0; i < c; i++)
		{
			if(top != null)
			{
				top.commit_undo();
				top = top.getNext();
			}
			else
				return;
		}
	}

	/**
	 * ������� c ������ � ������ ������
	 */
	public void removeBottom(int c)
	{
		Command com;
		LinkedList commands = new LinkedList();
		
		for(com = top; com != null; com = com.getNext())
			commands.add(com);

		if(c >= commands.size())
		{
			flush();
			return;
		}
		if(c < 0)
			return;
			
		com = (Command )commands.get(commands.size() - c - 1);

		for(Command c2 = com; c2 != null; c2 = c2.getNext())
		{
			c2.commit_execute();
		}
		
		com.setNext(null);// �������� ��� ����� ������
	}
	
	/**
	 * ��������� ���� ��������� �������
	 */
	public void execute()
	{
		if(current == top)
			return;

		// ��������� ������� � ����������� ���������
		// ������ ����������� ������
		current = getPrevious(current);
		if(current != null)
			current.execute();
	}

	/**
	 * ��������� ���� ��������� �������
	 */
	public void redo()
	{
		if(current == top)
			return;

		// ��������� ������� � ����������� ���������
		// ������ ����������� ������
		current = getPrevious(current);
		current.redo();
	}

	/**
	 * ������� ��������� ���� �������
	 */
	public void undo()
	{
		if(current == null)
			return;// ���� � ������ ������ �� ��������� ������

		// ��������� undo ������� � �����������
		// ��������� ������ ����������� ������
		current.undo();
		current = current.getNext();
	}
	
	/**
	 * 
	 */
	 public Command getBottom()
	 {
		Command com;
		
		if(top == null)
			return null;
		LinkedList commands = new LinkedList();
		
		for(com = top; com.getNext() != null; com = com.getNext())
			;
		return com;
	 }

	/**
	 * �������� ���������� ������ � ������
	 */
	public int getCount()
	{
		Command c;
		int count = 0;
		for(c = top; c != null; )
		{
			count++;
			c = c.getNext();
		}
		return count;
	}
}
