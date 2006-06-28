/**
 * $Id: VoidCommand.java,v 1.5 2006/06/02 10:45:50 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * Заглушка для команды (пустая команда)
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2006/06/02 10:45:50 $
 * @module commonclient
 * @author $Author: arseniy $
 */
public final class VoidCommand extends AbstractCommand {

	public static final VoidCommand VOID_COMMAND = new VoidCommand();
	
	/** поле источника команды */
	private Object	source;

	/** у пустой команды по умолчанию нет источника */
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

	/** пустая команда не выполняет никаких действий */
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

	// пустая команда не выполняет никаких действий
	@Override
	public void undo() {
		Log.debugMessage("method call", Level.FINER);

	}

	// пустая команда не выполняет никаких действий
	@Override
	public void redo() {
		Log.debugMessage("method call", Level.FINER);
		execute();
	}

	// пустая команда не выполняет никаких действий
	@Override
	public void commitExecute() {
		Log.debugMessage("method call", Level.FINER);
	}

	// пустая команда не выполняет никаких действий
	@Override
	public void commitUndo() {
		Log.debugMessage("method call", Level.FINER);
	}

	// у пустой команды нет источника
	@Override
	public Object getSource() {
		Log.debugMessage("method call for Void command, ret val " + this.source
		+ " - ignored", Level.FINER);

		return null;
	}

	// пустая команда не имеет параметров
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
