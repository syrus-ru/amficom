/**
 * $Id: VoidCommand.java,v 1.5 2006/06/02 10:45:50 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * �������� ��� ������� (������ �������)
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2006/06/02 10:45:50 $
 * @module commonclient
 * @author $Author: arseniy $
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
		return super.clone();
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
			Log.debugMessage(e, Level.FINE);
		}
	}

	@Override
	public int getResult() {
		return RESULT_UNSPECIFIED;
	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void undo() {
		Log.debugMessage("method call", Level.FINER);

	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void redo() {
		Log.debugMessage("method call", Level.FINER);
		execute();
	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void commitExecute() {
		Log.debugMessage("method call", Level.FINER);
	}

	// ������ ������� �� ��������� ������� ��������
	@Override
	public void commitUndo() {
		Log.debugMessage("method call", Level.FINER);
	}

	// � ������ ������� ��� ���������
	@Override
	public Object getSource() {
		Log.debugMessage("method call for Void command, ret val " + this.source
		+ " - ignored", Level.FINER);

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
			Log.debugMessage(e, Level.FINE);
		}
	}

}
