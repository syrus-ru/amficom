/**
 * $Id: VoidCommand.java,v 1.9 2004/08/26 10:21:39 krupenn Exp $
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
 * @version $Revision: 1.9 $, $Date: 2004/08/26 10:21:39 $
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
		try
		{
			throw new Exception("Void command executed for " + source.toString() + " - ignored");
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command undo() - ignored");
		
	}

	// пустая команда не выполняет никаких действий
	public void redo()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command redo() - defaults to \'EXECUTE\'");
		execute();
	}

	// пустая команда не выполняет никаких действий
	public void commit_execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command execution commit() - ignored");
	}

	// пустая команда не выполняет никаких действий
	public void commit_undo()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command undo commit() - ignored");
	}

	// у пустой команды нет источника
	public Object getSource()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call for Void command, ret val " + source.toString() + " - ignored", 
				getClass().getName(), 
				"getSource()");
		
		return null;
	}

	// пустая команда не имеет параметров
	public void setParameter(String field, Object value)
	{
		try
		{
			throw new Exception("Set for Void command paramenter " + field +
				" to value " + value.toString() + " - ignored");
		}
		catch(Exception e)
		{
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

}
