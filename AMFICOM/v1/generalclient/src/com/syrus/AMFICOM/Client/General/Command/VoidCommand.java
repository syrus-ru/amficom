/**
 * $Id: VoidCommand.java,v 1.8 2004/08/23 12:11:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Command;

import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 *  Заглушка для команды (пустая команда)
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/08/23 12:11:45 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class VoidCommand implements Command, Cloneable
{
	protected int result = RESULT_UNSPECIFIED;

	/** поле источника команды */
	private Object source;

	Command next = null;
	
	public Command getNext()
	{
		return next;
	}

	public void setNext(Command next)
	{
		this.next = next;
	}

	/** у пустой команды по умолчанию нет источника */ 
	public VoidCommand()
	{
		source = new String("NULL");
	}

	/** получить */
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

	/** пустая команда не выполняет никаких действий */ 
	public void execute()
	{
		System.out.println("Void command executed for " + source.toString() + " - ignored");
		try
		{
			throw new Exception("dummy");
		}
		catch(Exception e)
		{
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}
	
	public int getResult()
	{
		return result;
	}
	
	protected void setResult(int res)
	{
		this.result = res;
	}

	// пустая команда не выполняет никаких действий
	public void undo()
	{
		System.out.println("Void command undo for " + source.toString() + " - ignored");
		try
		{
			throw new Exception("dummy");
		}
		catch(Exception e)
		{
			System.out.println("current execution point with call stack: " + this);
			e.printStackTrace();
		}
	}

	// пустая команда не выполняет никаких действий
	public void redo()
	{
		System.out.println("Void command redo for " + source.toString() + " - defaults to \'EXECUTE\'");
		try
		{
			throw new Exception("dummy");
		}
		catch(Exception e)
		{
			System.out.println("current execution point with call stack: " + this);
			e.printStackTrace();
		}
		execute();
	}

	// пустая команда не выполняет никаких действий
	public void commit_execute()
	{
		System.out.println("Void command execution commit for " + source.toString());
	}

	// пустая команда не выполняет никаких действий
	public void commit_undo()
	{
		System.out.println("Void command undo commit for " + source.toString() + " - ignored");
	}

	// у пустой команды нет источника
	public Object getSource()
	{
		System.out.println("Source for Void command is " + source.toString() + " - ignored");
		return null;
	}

	// пустая команда не имеет параметров
	public void setParameter(String field, Object value)
	{
//		System.out.println("Set for Void command paramenter " + field +
//				" to value " + value.toString() + " - ignored");
		try
		{
			throw new Exception("dummy");
		}
		catch(Exception e)
		{
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

}
