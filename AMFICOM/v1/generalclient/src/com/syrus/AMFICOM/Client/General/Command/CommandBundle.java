/**
 * $Id: CommandBundle.java,v 1.9 2005/05/18 14:01:20 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Command;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *  ��������: �������, ���������� � ���� ��������� ������
 *
 *
 *
 * @version $Revision: 1.9 $, $Date: 2005/05/18 14:01:20 $
 * @module
 * @author $Author: bass $
 * @see
 */
public class CommandBundle extends VoidCommand implements Command
{
	/** ������ ������ */
	protected List commands = new ArrayList();

	public CommandBundle()
	{
	}

	/**
	 * �������� ����� �������
	 */
	public Object clone()
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

	/**
	 * �������� � ������ �������
	 */
	public void add(Command command)
	{
		commands.add(command);
	}

	/**
	 * ������� ������� �� ������
	 */
	public void remove(Command command)
	{
		commands.remove(command);
	}

	/**
	 * ������� ������� �� ������
	 */
	public void remove(int index)
	{
		commands.remove(index);
	}

	/**
	 * ��������� - ��� ������� � ������
	 */
	public void execute()
	{
		for(ListIterator it = commands.listIterator();it.hasNext();)
		{
			Command command = (Command )it.next();
			command.execute();
		}
	}

	/**
	 * ������� ��������� - ��� ������� � ������ � �������� �������
	 */
	public void undo()
	{
		for(ListIterator it = commands.listIterator(commands.size());it.hasPrevious();)
		{
			Command command = (Command )it.previous();
			command.undo();
		}
	}

	/**
	 * ��������� - ������� � ������ ��������
	 */
	public void redo()
	{
		for(ListIterator it = commands.listIterator();it.hasNext();)
		{
			Command command = (Command )it.next();
			command.redo();
		}
	}

	/**
	 * ������������� ���������� - ����������� ��� ���� ������
	 */
	public void commitExecute()
	{
		for(ListIterator it = commands.listIterator();it.hasNext();)
		{
			Command command = (Command )it.next();
			command.commitExecute();
		}
	}

	/**
	 * ������������� ��������� ���������� - ����������� ��� ���� ������
	 */
	public void commitUndo()
	{
		for(ListIterator it = commands.listIterator(commands.size());it.hasPrevious();)
		{
			Command command = (Command )it.previous();
			command.commitUndo();
		}
	}

	public Object getSource()
	{
		return null;
	}

	/**
	 * �������� ���������� ������ � ������ �������
	 */
	public int getCount()
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
