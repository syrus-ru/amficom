/**
 * $Id: VoidCommand.java,v 1.3 2005/09/08 14:25:57 bob Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

/**
 * Заглушка для команды (пустая команда)
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/09/08 14:25:57 $
 * @module commonclient
 * @author $Author: bob $
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
		return (VoidCommand)super.clone();
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
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

	@Override
	public int getResult() {
		return RESULT_UNSPECIFIED;
	}

	// пустая команда не выполняет никаких действий
	@Override
	public void undo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command undo() - ignored");

	}

	// пустая команда не выполняет никаких действий
	@Override
	public void redo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command redo() - defaults to \'EXECUTE\'");
		execute();
	}

	// пустая команда не выполняет никаких действий
	@Override
	public void commitExecute() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command execution commit() - ignored");
	}

	// пустая команда не выполняет никаких действий
	@Override
	public void commitUndo() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(),
			"Void command undo commit() - ignored");
	}

	// у пустой команды нет источника
	@Override
	public Object getSource() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call for Void command, ret val " + this.source
				+ " - ignored", getClass().getName(), "getSource()");

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
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

}
