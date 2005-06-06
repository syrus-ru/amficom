/**
 * $Id: CommandList.java,v 1.1 2005/06/06 14:51:21 bob Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.model;

/**
 * @version $Revision: 1.1 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
public class CommandList extends AbstractCommand {
	/** голова списка команд */
	private CommandHolder top = null;

	/** хвост списка команд */
	private CommandHolder bottom = null;
	
	/** количество команд */
	private int count = 0;
	
	/**
	 * последней выполненной команды в списке + 1
	 * (или индекс первой готовой к выполнению команды)
	 */
	private CommandHolder current = null;

	/** максимальное количество команд в списке */
	private int maxlength = 50;

	public CommandList(int maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * выполнить следующие c команд в списке
	 */
	public void proceed(int c) {
		for(int i = 0; i < c; i++) {
			CommandHolder commandHolder = this.current.getPrevious();
			if(commandHolder != null) {
				this.current = commandHolder;
				this.current.getCommand().execute();
			}
			else
				return;
		}
	}

	/**
	 * выполнить обратоно C команд
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
	 * повторно выполнить c команд
	 */
	public void proceedRedo(int c) {
		for(int i = 0; i < c; i++) {
			CommandHolder commandHolder = getPrevious();
			if(commandHolder != null) {
				this.current = commandHolder;
				this.current.getCommand().redo();
			}
			else
				return;
		}
	}
	
	/**
	 * добавить команду в конец списка
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
	 * удалить весь список
	 */
	public void flush() {
		this.top = null;
		this.bottom = null;
		this.current = null;
		this.count = 0;
	}

	/**
	 * удалить c команд в конце списка
	 */
	public void removeTop(int c) {
		for(int i = 0; i < c; i++) {
			if(this.top != null) {
				this.top.getCommand().commitUndo();
				if(this.current == this.top)
					this.current = this.top.getNext();
				this.top = this.top.getNext();
				this.count--;
			}
			else
				break;
		}
		if(this.top != null)
			this.top.setPrevious(null);
	}

	/**
	 * удалить c команд в начале списка
	 */
	public void removeBottom(int c) {
		for(int i = 0; i < c; i++) {
			if(this.bottom != null) {
				this.bottom.getCommand().commitExecute();
				if(this.current == this.bottom)
					this.current = null;
				this.bottom = this.bottom.getPrevious();
				this.count--;
			}
			else
				break;
		}

		if(this.bottom != null)
			this.bottom.setNext(null);
	}
	
	public void executeAll() {
		while(this.current != this.top) {
			// выполнить команду и переместить указатель
			// списка выполненных команд
			this.current = getPrevious();
			if(this.current != null)
				this.current.getCommand().execute();
		}
	}

	/**
	 * выполнить одну следующую команду
	 */
	public void execute() {
		if(this.current == this.top)
			return;

		// выполнить команду и переместить указатель
		// списка выполненных команд
		this.current = getPrevious();
		if(this.current != null)
			this.current.getCommand().execute();
	}

	/**
	 * выполнить одну следующую команду
	 */
	public void redo() {
		if(this.current == this.top)
			return;

		// выполнить команду и переместить указатель
		// списка выполненных команд
		this.current = getPrevious();
		this.current.getCommand().redo();
	}

	/**
	 * обратно выполнить одну команду
	 */
	public void undo() {
		if(this.current == null)
			return;// если в начале списка то выполнять нечего

		// выполнить undo команды и переместить
		// указатель списка выполненных команд
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
	 * получить количество команд в списке
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
