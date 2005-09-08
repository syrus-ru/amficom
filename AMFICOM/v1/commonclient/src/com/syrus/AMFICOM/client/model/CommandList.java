/**
 * $Id: CommandList.java,v 1.4 2005/09/08 14:25:57 bob Exp $
 * Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.client.model;

/**
 * @version $Revision: 1.4 $
 * @author $Author: bob $
 * @module commonclient
 */
public class CommandList extends AbstractCommand {
	/** ������ ������ ������ */
	private CommandHolder top = null;

	/** ����� ������ ������ */
	private CommandHolder bottom = null;
	
	/** ���������� ������ */
	private int count = 0;
	
	/**
	 * ��������� ����������� ������� � ������ + 1
	 * (��� ������ ������ ������� � ���������� �������)
	 */
	private CommandHolder current = null;

	/** ������������ ���������� ������ � ������ */
	private int maxlength = 50;

	public CommandList(int maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * ��������� ��������� c ������ � ������
	 */
	public void proceed(int c) {
		for(int i = 0; i < c; i++) {
			CommandHolder commandHolder = this.current.getPrevious();
			if(commandHolder != null) {
				this.current = commandHolder;
				this.current.getCommand().execute();
			} else
				return;
		}
	}

	/**
	 * ��������� �������� C ������
	 */
	public void proceedUndo(int c) {
		for(int i = 0; i < c; i++) {
			if(this.current == null)
				return;
			this.current.getCommand().undo();
			this.current = this.current.getNext();
		}
	}

	/**
	 * �������� ��������� c ������
	 */
	public void proceedRedo(int c) {
		for(int i = 0; i < c; i++) {
			CommandHolder commandHolder = getPrevious();
			if(commandHolder != null) {
				this.current = commandHolder;
				this.current.getCommand().redo();
			} else
				return;
		}
	}
	
	/**
	 * �������� ������� � ����� ������
	 */
	public void add(Command command) {
		if(command == null)
			return;

		CommandHolder commandHolder = new CommandHolder(command);
		// not executed commands are lost
		commandHolder.setNext(this.current);
		if(this.current != null)
			this.current.setPrevious(commandHolder);
		commandHolder.setPrevious(null);
		this.top = commandHolder;
		if(this.bottom == null)
			this.bottom = this.top;

		this.count++;

		if(getCount() > this.maxlength)
			removeBottom(1);
	}

	/**
	 * ������� ���� ������
	 */
	public void flush() {
		this.top = null;
		this.bottom = null;
		this.current = null;
		this.count = 0;
	}

	/**
	 * ������� c ������ � ����� ������
	 */
	public void removeTop(int c) {
		for(int i = 0; i < c; i++) {
			if(this.top != null) {
				this.top.getCommand().commitUndo();
				if(this.current == this.top)
					this.current = this.top.getNext();
				this.top = this.top.getNext();
				this.count--;
			} else
				break;
		}
		if(this.top != null)
			this.top.setPrevious(null);
	}

	/**
	 * ������� c ������ � ������ ������
	 */
	public void removeBottom(int c) {
		for(int i = 0; i < c; i++) {
			if(this.bottom != null) {
				this.bottom.getCommand().commitExecute();
				if(this.current == this.bottom)
					this.current = null;
				this.bottom = this.bottom.getPrevious();
				this.count--;
			} else
				break;
		}

		if(this.bottom != null)
			this.bottom.setNext(null);
	}
	
	public void executeAll() {
		while(this.current != this.top) {
			// ��������� ������� � ����������� ���������
			// ������ ����������� ������
			this.current = getPrevious();
			if(this.current != null)
				this.current.getCommand().execute();
		}
	}

	/**
	 * ��������� ���� ��������� �������
	 */
	@Override
	public void execute() {
		if(this.current == this.top)
			return;

		// ��������� ������� � ����������� ���������
		// ������ ����������� ������
		this.current = getPrevious();
		if(this.current != null)
			this.current.getCommand().execute();
	}

	/**
	 * ��������� ���� ��������� �������
	 */
	@Override
	public void redo() {
		if(this.current == this.top)
			return;

		// ��������� ������� � ����������� ���������
		// ������ ����������� ������
		this.current = getPrevious();
		this.current.getCommand().redo();
	}

	/**
	 * ������� ��������� ���� �������
	 */
	@Override
	public void undo() {
		if(this.current == null)
			return;// ���� � ������ ������ �� ��������� ������

		// ��������� undo ������� � �����������
		// ��������� ������ ����������� ������
		this.current.getCommand().undo();
		this.current = this.current.getNext();
	}
	
	CommandHolder getPrevious() {
		if(this.current == null)
			return getBottom();
		return this.current.getPrevious();
	}

	CommandHolder getBottom() {
		return this.bottom;
	}

	CommandHolder getTop() {
		return this.top;
	}

	/**
	 * �������� ���������� ������ � ������
	 */
	public int getCount() {
		return this.count;
	}

	class CommandHolder {
		CommandHolder next = null;

		CommandHolder previous = null;

		Command command;
		
		public CommandHolder(Command command) {
			this.command = command;
		}

		public CommandHolder getNext() {
			return this.next;
		}

		public void setNext(CommandHolder next) {
			this.next = next;
		}

		public CommandHolder getPrevious() {
			return this.previous;
		}

		public void setPrevious(CommandHolder previous) {
			this.previous = previous;
		}

		public Command getCommand() {
			return this.command;
		}

		public void setCommand(Command command) {
			this.command = command;
		}
	}
}
