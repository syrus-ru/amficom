/**
 * $Id: VoidCommand.java,v 1.9 2004/08/26 10:21:39 krupenn Exp $
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
 * @version $Revision: 1.9 $, $Date: 2004/08/26 10:21:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class VoidCommand implements Command, Cloneable
{
	protected int result = RESULT_UNSPECIFIED;

	/** ���� ��������� ������� */
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

	/** � ������ ������� �� ��������� ��� ��������� */ 
	public VoidCommand()
	{
		source = new String("NULL");
	}

	/** �������� */
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
	public void commit_execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "Void command execution commit() - ignored");
	}

	// ������ ������� �� ��������� ������� ��������
	public void commit_undo()
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

}
