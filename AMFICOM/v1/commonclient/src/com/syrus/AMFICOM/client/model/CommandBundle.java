/**
 * $Id: CommandBundle.java,v 1.1 2005/06/06 14:51:21 bob Exp $
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 * Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.model;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Название: команда, включающая в себя несколько команд
 * 
 * @version $Revision: 1.1 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
public class CommandBundle extends AbstractCommand {
	/** список команд */
	protected List commands = new ArrayList();

	/**
	 * добавить в список команду
	 */
	public void add(Command command) {
		this.commands.add(command);
	}

	/**
	 * удалить команду из списка
	 */
	public void remove(Command command) {
		this.commands.remove(command);
	}

	/**
	 * удалить команду из списка
	 */
	public void remove(int index) {
		this.commands.remove(index);
	}

	/**
	 * выполнить - все команды в списке
	 */
	public void execute() {
		for(Iterator it = this.commands.iterator(); it.hasNext();) {
			Command command = (Command)it.next();
			command.execute();
		}
	}

	/**
	 * обратно выполнить - все команды в списке в обратном порядке
	 */
	public void undo() {
		for(ListIterator it = this.commands.listIterator(this.commands.size()); it
				.hasPrevious();) {
			Command command = (Command)it.previous();
			command.undo();
		}
	}

	/**
	 * выполнить - команды в списке повторно
	 */
	public void redo() {
		for(ListIterator it = this.commands.listIterator(); it.hasNext();) {
			Command command = (Command)it.next();
			command.redo();
		}
	}

	/**
	 * подтверждение выполнения - подтвердить для всех команд
	 */
	public void commitExecute() {
		for(ListIterator it = this.commands.listIterator(); it.hasNext();) {
			Command command = (Command)it.next();
			command.commitExecute();
		}
	}

	/**
	 * подтверждение обратного выполнения - подтвердить для всех команд
	 */
	public void commitUndo() {
		for(ListIterator it = this.commands.listIterator(this.commands.size()); it
				.hasPrevious();) {
			Command command = (Command)it.previous();
			command.commitUndo();
		}
	}

	/**
	 * @inheritDoc
	 */
	public Object getSource() {
		return null;
	}

	/**
	 * получить количество команд в данной команде
	 */
	public int getCount() {
		return this.commands.size();
	}

	/**
	 * @inheritDoc
	 */
	public void setParameter(String field, Object value) {
		for(Iterator iter = this.commands.iterator(); iter.hasNext();) {
			Command command = (Command)iter.next();
			command.setParameter(field, value);
		}
	}
}
