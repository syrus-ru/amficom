/**
 * $Id: CommandBundle.java,v 1.3 2005/09/07 02:37:31 arseniy Exp $
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 * Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Название: команда, включающая в себя несколько команд
 * 
 * @version $Revision: 1.3 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class CommandBundle extends AbstractCommand {
	/** список команд */
	protected List<Command> commands = new ArrayList<Command>();

	/**
	 * добавить в список команду
	 */
	public void add(final Command command) {
		this.commands.add(command);
	}

	/**
	 * удалить команду из списка
	 */
	public void remove(final Command command) {
		this.commands.remove(command);
	}

	/**
	 * удалить команду из списка
	 */
	public void remove(final int index) {
		this.commands.remove(index);
	}

	/**
	 * выполнить - все команды в списке
	 */
	@Override
	public void execute() {
		for (final Command command : this.commands) {
			command.execute();
		}
	}

	/**
	 * обратно выполнить - все команды в списке в обратном порядке
	 */
	@Override
	public void undo() {
		for (final ListIterator<Command> it = this.commands.listIterator(this.commands.size()); it.hasPrevious();) {
			final Command command = it.previous();
			command.undo();
		}
	}

	/**
	 * выполнить - команды в списке повторно
	 */
	@Override
	public void redo() {
		for(final ListIterator<Command> it = this.commands.listIterator(); it.hasNext();) {
			final Command command = it.next();
			command.redo();
		}
	}

	/**
	 * подтверждение выполнения - подтвердить для всех команд
	 */
	@Override
	public void commitExecute() {
		for(final ListIterator<Command> it = this.commands.listIterator(); it.hasNext();) {
			final Command command = it.next();
			command.commitExecute();
		}
	}

	/**
	 * подтверждение обратного выполнения - подтвердить для всех команд
	 */
	@Override
	public void commitUndo() {
		for (final ListIterator<Command> it = this.commands.listIterator(this.commands.size()); it.hasPrevious();) {
			final Command command = it.previous();
			command.commitUndo();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
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
	@Override
	public void setParameter(final String field, final Object value) {
		for (final Command command : this.commands) {
			command.setParameter(field, value);
		}
	}
}
