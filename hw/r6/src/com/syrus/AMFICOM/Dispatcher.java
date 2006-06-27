package com.syrus.AMFICOM;

import java.util.Vector;
import java.util.Enumeration;

public class Dispatcher implements OperationListener {
	private Vector commands; // ������ �������

	// �� ������ ������� ����� ������������� ������������ ����� ������������
	class Command	{
		private String commandString; // ����, ������������ ��� �������
		private Vector listeners; // �����������

		public Command(String commandString) {
			this.commandString = commandString;
			this.listeners = new Vector();
		}
	}

	public Dispatcher()	{
		this.commands = new Vector();
	}

	// ����������� ��������� ���������� � ������������ ��������
	public void register(OperationListener listener, String commandString)	{
    Command cmd;
		for (Enumeration enum = this.commands.elements(); enum.hasMoreElements();)	{
			cmd = (Command)enum.nextElement();
			// ���� ������� ������� � ������, ������ ����������� ����� �����������
			if (cmd.commandString.equals(commandString)) {
				cmd.listeners.add(listener);
				return;
			}
		}
		// ���� ������� �� ������� � ������, ��������� ����� ��������� �
		// ����������� ����� �����������
		cmd = new Command(commandString);
		cmd.listeners.add(listener);
		this.commands.add(cmd);
	}

	// ������������� ������� ����� ���������� � ������������ ��������
	public void unregister(OperationListener listener, String commandString)	{
    Command cmd;
		for (Enumeration enum = this.commands.elements(); enum.hasMoreElements();) {
			cmd = (Command)enum.nextElement();
			if (cmd.commandString.equals(commandString)) {
				cmd.listeners.remove(listener);
				// � ������ ���� �� �������� �� ������ ����������,
				//  ������� ��������� �� ������
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
		// ���� ������� ���� ������ �� ����
		for (Enumeration enum = this.commands.elements(); enum.hasMoreElements();)	{
			cmd = (Command)enum.nextElement();
			if (cmd.commandString.equals(event.getActionCommand())) {// ���� ������� ������� � ����� command
				for (enum = cmd.listeners.elements(); enum.hasMoreElements();)
					// � ������� ����������� �������� ����� actionPerformed(event)
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
