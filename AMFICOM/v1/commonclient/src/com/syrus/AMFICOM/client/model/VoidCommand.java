/**
 * $Id: VoidCommand.java,v 1.3 2005/09/08 14:25:57 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/09/08 14:25:57 $
 * @module commonclient
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (VoidCommand)super.clone();
	}

	public VoidCommand(Object source) {
		this.source = source;
	}

	/** ������ ������� �� ��������� ������� �������� */
	@Override
	public void execute() {
		try {
			throw new Exception("Void command executed for " + this.source + " - ignored");
		} catch (Exception e) {
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

	@Override
	public int getResult() {
		return RESULT_UNSPECIFIED;
	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void undo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command undo() - ignored");

	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void redo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command redo() - defaults to \'EXECUTE\'");
		execute();
	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void commitExecute() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command execution commit() - ignored");
	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void commitUndo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command undo commit() - ignored");
	}

	// � ������ ������� ��� ���������
	@Override
	public Object getSource() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call for Void command, ret val " + this.source
				+ " - ignored", getClass().getName(), "getSource()");

		return null;
	}

	// ������ ������� �� ����� ����������
	@Override
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
