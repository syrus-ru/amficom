/**
 * $Id: VoidCommand.java,v 1.13 2005/05/18 14:01:20 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Command;

import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 *  �������� ��� ������� (������ �������)
 *
 *
 *
 * @version $Revision: 1.13 $, $Date: 2005/05/18 14:01:20 $
 * @module
 * @author $Author: bass $
 * @see
 */
public class VoidCommand implements Command, Cloneable
{
	protected int result = RESULT_UNSPECIFIED;

	/** ���� ��������� ������� */
	private Object source;

	Command next = null;
	Command previous = null;
	
	public Command getNext()
	{
		return next;
	}

	public void setNext(Command next)
	{
		this.next = next;
	}

	/** � ������ ������� �� ��������� ��� ��������� */
	public VoidCommand()
	{
		source = "NULL";
	}

	/** �������� */
	public Object clone()
	{
		return new VoidCommand(source);		
	}

	public VoidCommand(Object source)
	{
		if(source == null)
			source = "NULL";
		this.source = source;
	}

	/** ������ ������� �� ��������� ������� �������� */
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

	// ������ ������� �� ��������� ������� ��������
	public void undo()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command undo() - ignored");
		
	}

	// ������ ������� �� ��������� ������� ��������
	public void redo()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command redo() - defaults to \'EXECUTE\'");
		execute();
	}

	// ������ ������� �� ��������� ������� ��������
	public void commitExecute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command execution commit() - ignored");
	}

	// ������ ������� �� ��������� ������� ��������
	public void commitUndo()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command undo commit() - ignored");
	}

	// � ������ ������� ��� ���������
	public Object getSource()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER,
				"method call for Void command, ret val " + source.toString() + " - ignored",
				getClass().getName(),
				"getSource()");
		
		return null;
	}

	// ������ ������� �� ����� ����������
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

	public Command getPrevious()
	{
		return previous;
	}

	public void setPrevious(Command previous)
	{
		this.previous = previous;
	}

}
