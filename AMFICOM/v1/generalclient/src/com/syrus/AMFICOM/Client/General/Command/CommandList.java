/*
 * $Id: CommandList.java,v 1.7 2004/10/07 11:52:09 krupenn Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.Client.General.Command;

import java.util.LinkedList;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2004/10/07 11:52:09 $
 * @module generalclient_v1
 */
public class CommandList extends VoidCommand
{
	/** ������ ������ */
	private Command top = null;
	private Command bottom = null;
	
	private int count = 0;
	
	/**
	 * ��������� ����������� ������� � ������ + 1
	 * (��� ������ ������ ������� � ���������� �������)
	 */
	private Command current = null;

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
			Command com = current.getPrevious();
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
	public void proceedUndo(int c)
	{
		for(int i = 0; i < c; i++)
		{
			if(current == null)
				return;
			current.undo();
			current = current.getNext();
		}
	}

	public void proceedRedo(int c)	// �������� ��������� c ������
	{
		for(int i = 0; i < c; i++)
		{
			Command com = current.getPrevious();
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
	
	/**
	 * �������� ������� � ����� ������
	 */
	public void add(Command command)
	{
		if(command == null)
			return;

		// not executed commands are lost
		command.setNext(current);
		if(current != null)	
			current.setPrevious(command);
		command.setPrevious(null);
		top = command;
		if(bottom == null)
			bottom = top;

		count++;

		if(getCount() > maxlength)
			removeBottom(1);
	}

	/**
	 * ������� ���� ������
	 */
	public void flush()
	{
		top = null;
		bottom = null;
		current = null;
		count = 0;
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
				top.commitUndo();
				if(current == top)
					current = top.getNext();
				top = top.getNext();
				count--;
			}
			else
				break;
		}
		if(top != null)
			top.setPrevious(null);
	}

	/**
	 * ������� c ������ � ������ ������
	 */
	public void removeBottom(int c)
	{
		for (int i = 0; i < c; i++) 
		{
			if(bottom != null)
			{
				bottom.commitExecute();
				if(current == bottom)
					current = null;
				bottom = bottom.getPrevious();
				count--;
			}
			else
				break;
		}

		if(bottom != null)
			bottom.setNext(null);
	}
	
	public void executeAll()
	{
		while(current != top)
		{
			// ��������� ������� � ����������� ���������
			// ������ ����������� ������
			current = current.getPrevious();
			if(current != null)
				current.execute();
		}
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
		current = current.getPrevious();
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
		current = current.getPrevious();
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
		return bottom;
	 }

	 public Command getTop()
	 {
		return top;
	 }

	/**
	 * �������� ���������� ������ � ������
	 */
	public int getCount()
	{
		return count;
	}
}
