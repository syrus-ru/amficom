/**
 * $Id: VoidCommand.java,v 1.1 2005/05/19 14:06:42 bob Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

/**
 * �������� ��� ������� (������ �������)
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:42 $
 * @module commonclient_v1
 * @author $Author: bob $
 */
public final class VoidCommand extends AbstractCommand {

	public static final VoidCommand VOID_COMMAND = new VoidCommand();
	
	/** ���� ��������� ������� */
	private Object	source;

	/** � ������ ������� �� ��������� ��� ��������� */
	public VoidCommand() {
		this.source = null;
	}

	public Object clone() throws CloneNotSupportedException {
		VoidCommand voidCommand = (VoidCommand)super.clone();
		voidCommand.source = this.source;
		return voidCommand;
	}

	public VoidCommand(Object source) {
		this.source = source;
	}

	/** ������ ������� �� ��������� ������� �������� */
	public void execute() {
		try {
			throw new Exception("Void command executed for " + this.source + " - ignored");
		} catch (Exception e) {
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

	public int getResult() {
		return RESULT_UNSPECIFIED;
	}

	// ������ ������� �� ��������� ������� ��������
	public void undo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command undo() - ignored");

	}

	// ������ ������� �� ��������� ������� ��������
	public void redo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command redo() - defaults to \'EXECUTE\'");
		execute();
	}

	// ������ ������� �� ��������� ������� ��������
	public void commitExecute() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command execution commit() - ignored");
	}

	// ������ ������� �� ��������� ������� ��������
	public void commitUndo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command undo commit() - ignored");
	}

	// � ������ ������� ��� ���������
	public Object getSource() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call for Void command, ret val " + this.source
				+ " - ignored", getClass().getName(), "getSource()");

		return null;
	}

	// ������ ������� �� ����� ����������
	public void setParameter(	String field,
								Object value) {
		try {
			throw new Exception("Set for Void command paramenter " + field + " to value " + value.toString()
					+ " - ignored");
		} catch (Exception e) {
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

}
