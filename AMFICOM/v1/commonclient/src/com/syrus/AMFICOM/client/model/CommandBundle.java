/**
 * $Id: CommandBundle.java,v 1.3 2005/09/07 02:37:31 arseniy Exp $
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 * ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * ��������: �������, ���������� � ���� ��������� ������
 * 
 * @version $Revision: 1.3 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class CommandBundle extends AbstractCommand {
	/** ������ ������ */
	protected List<Command> commands = new ArrayList<Command>();

	/**
	 * �������� � ������ �������
	 */
	public void add(final Command command) {
		this.commands.add(command);
	}

	/**
	 * ������� ������� �� ������
	 */
	public void remove(final Command command) {
		this.commands.remove(command);
	}

	/**
	 * ������� ������� �� ������
	 */
	public void remove(final int index) {
		this.commands.remove(index);
	}

	/**
	 * ��������� - ��� ������� � ������
	 */
	@Override
	public void execute() {
		for (final Command command : this.commands) {
			command.execute();
		}
	}

	/**
	 * ������� ��������� - ��� ������� � ������ � �������� �������
	 */
	@Override
	public void undo() {
		for (final ListIterator<Command> it = this.commands.listIterator(this.commands.size()); it.hasPrevious();) {
			final Command command = it.previous();
			command.undo();
		}
	}

	/**
	 * ��������� - ������� � ������ ��������
	 */
	@Override
	public void redo() {
		for(final ListIterator<Command> it = this.commands.listIterator(); it.hasNext();) {
			final Command command = it.next();
			command.redo();
		}
	}

	/**
	 * ������������� ���������� - ����������� ��� ���� ������
	 */
	@Override
	public void commitExecute() {
		for(final ListIterator<Command> it = this.commands.listIterator(); it.hasNext();) {
			final Command command = it.next();
			command.commitExecute();
		}
	}

	/**
	 * ������������� ��������� ���������� - ����������� ��� ���� ������
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
	 * �������� ���������� ������ � ������ �������
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
