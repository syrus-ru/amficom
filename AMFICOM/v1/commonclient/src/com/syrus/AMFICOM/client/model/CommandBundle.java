/**
 * $Id: CommandBundle.java,v 1.2 2005/08/02 13:03:22 arseniy Exp $
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 * ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.model;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * ��������: �������, ���������� � ���� ��������� ������
 * 
 * @version $Revision: 1.2 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class CommandBundle extends AbstractCommand {
	/** ������ ������ */
	protected List commands = new ArrayList();

	/**
	 * �������� � ������ �������
	 */
	public void add(Command command) {
		this.commands.add(command);
	}

	/**
	 * ������� ������� �� ������
	 */
	public void remove(Command command) {
		this.commands.remove(command);
	}

	/**
	 * ������� ������� �� ������
	 */
	public void remove(int index) {
		this.commands.remove(index);
	}

	/**
	 * ��������� - ��� ������� � ������
	 */
	public void execute() {
		for(Iterator it = this.commands.iterator(); it.hasNext();) {
			Command command = (Command)it.next();
			command.execute();
		}
	}

	/**
	 * ������� ��������� - ��� ������� � ������ � �������� �������
	 */
	public void undo() {
		for(ListIterator it = this.commands.listIterator(this.commands.size()); it
				.hasPrevious();) {
			Command command = (Command)it.previous();
			command.undo();
		}
	}

	/**
	 * ��������� - ������� � ������ ��������
	 */
	public void redo() {
		for(ListIterator it = this.commands.listIterator(); it.hasNext();) {
			Command command = (Command)it.next();
			command.redo();
		}
	}

	/**
	 * ������������� ���������� - ����������� ��� ���� ������
	 */
	public void commitExecute() {
		for(ListIterator it = this.commands.listIterator(); it.hasNext();) {
			Command command = (Command)it.next();
			command.commitExecute();
		}
	}

	/**
	 * ������������� ��������� ���������� - ����������� ��� ���� ������
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
	 * �������� ���������� ������ � ������ �������
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
