/**
 * $Id: CommandBundle.java,v 1.8 2004/10/07 11:52:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Command;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *  Название: команда, включающая в себя несколько команд 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/10/07 11:52:09 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CommandBundle extends VoidCommand implements Command
{
	/** список команд */
	protected List commands = new ArrayList();

	public CommandBundle()
	{
	}

	/** 
	 * получить копию команды 
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
	 * добавить в список команду 
	 */
	public void add(Command command)
	{
		commands.add(command);
	}

	/** 
	 * удалить команду из списка 
	 */
	public void remove(Command command)
	{
		commands.remove(command);
	}

	/** 
	 * удалить команду из списка 
	 */
	public void remove(int index)
	{
		commands.remove(index);
	}

	/** 
	 * выполнить - все команды в списке 
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
	 * обратно выполнить - все команды в списке в обратном порядке 
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
	 * выполнить - команды в списке повторно 
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
	 * подтверждение выполнения - подтвердить для всех команд 
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
	 * подтверждение обратного выполнения - подтвердить для всех команд 
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
	 * получить количество команд в данной команде
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
