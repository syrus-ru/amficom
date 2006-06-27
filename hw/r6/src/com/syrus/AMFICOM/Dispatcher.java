package com.syrus.AMFICOM;

import java.util.Vector;
import java.util.Enumeration;

public class Dispatcher implements OperationListener {
	private Vector commands; // список событий

	// на каждое событие может подписываться произвольное число наблюдателей
	class Command	{
		private String commandString; // поле, определяющее тип события
		private Vector listeners; // наблюдатели

		public Command(String commandString) {
			this.commandString = commandString;
			this.listeners = new Vector();
		}
	}

	public Dispatcher()	{
		this.commands = new Vector();
	}

	// регистрация связывает подписчика с определенным событием
	public void register(OperationListener listener, String commandString)	{
    Command cmd;
		for (Enumeration enum = this.commands.elements(); enum.hasMoreElements();)	{
			cmd = (Command)enum.nextElement();
			// если событие найдено в списке, просто добавляется новый наблюдатель
			if (cmd.commandString.equals(commandString)) {
				cmd.listeners.add(listener);
				return;
			}
		}
		// если событие не найдено в списке, создается новый экземпляр и
		// добавляется новый наблюдатель
		cmd = new Command(commandString);
		cmd.listeners.add(listener);
		this.commands.add(cmd);
	}

	// унрегистрация убирает связь подписчика с определенным событием
	public void unregister(OperationListener listener, String commandString)	{
    Command cmd;
		for (Enumeration enum = this.commands.elements(); enum.hasMoreElements();) {
			cmd = (Command)enum.nextElement();
			if (cmd.commandString.equals(commandString)) {
				cmd.listeners.remove(listener);
				// в случае если не осталось ни одного подписчика,
				//  событие удаляется из списка
				if (cmd.listeners.isEmpty())
					this.commands.remove(cmd);
				return;
			}
		}
	}

	public void operationPerformed(OperationEvent event) {
		this.notify(event);
	}

	public void notify(OperationEvent event)	{
    Command cmd;
		// ищем событие если список не пуст
		for (Enumeration enum = this.commands.elements(); enum.hasMoreElements();)	{
			cmd = (Command)enum.nextElement();
			if (cmd.commandString.equals(event.getActionCommand())) {// если найдено событие с таким command
				for (enum = cmd.listeners.elements(); enum.hasMoreElements();)
					// у каждого наблюдателя вызываем метод actionPerformed(event)
					((OperationListener)(enum.nextElement())).operationPerformed(event);
				return;
			}
		}
	}

  public void printCommands() {
    System.out.println("DISPATCHER:");
    for (int i = 0; i < this.commands.size(); i++)
      System.out.println("  Command: " + ((Command)this.commands.get(i)).commandString);
  }
}
