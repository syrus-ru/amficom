//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: команда, включающая в себя несколько команд                * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 16 jul 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\CommandBundle.java                            * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Command;

import java.util.Enumeration;
import java.util.Vector;

public class CommandBundle implements Command
{
	protected Vector commands;			// список команд

	public CommandBundle()
	{
	}

	public Object clone()				// получить копию команды
	{
		Command c;
		CommandBundle cb = new CommandBundle();
		cb.commands = new Vector();
		for(Enumeration e = commands.elements(); e.hasMoreElements();)
		{
			c = (Command )e.nextElement();
			cb.commands.add(c.clone());
		}
		return cb;
	}

	public void add(Command command)	// добавить в список команду
	{
		commands.add(command);
	}

	public void remove(Command command)	// удалить команду из списка
	{
		commands.remove(command);
	}

	public void remove(int index)		// удалить команду из списка
	{
		commands.remove(index);
	}

	public void execute()				// выполнить - все команды в списке
	{
		int i;
		int count = getCount();
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(i);
			command.execute();
		}
	}

	public int getResult()
	{
		return RESULT_OK;
	}

	public void undo()					// обратно выполнить - все команды 
										// в списке в обратном порядке
	{
		int i;
		int count = getCount();
		for(i = count - 1; i >= 0; i--)
		{
			Command command = (Command )commands.get(i);
			command.undo();
		}
	}

	public void redo()		// выполнить - команды в списке повторно - ничем не
							// отличается от первого выполнения списка команд
	{
		execute();
	}

	public void commit_execute()		// подтверждение выполнения - 
										// подтвердить для всех команд
	{
		int i;
		int count = getCount();
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(i);
			command.commit_execute();
		}
	}

	public void commit_undo()			// подтверждение обратного выполнения -
										// подтвердить для всех команд
	{
		int i;
		int count = getCount();
		for(i = count - 1; i >= 0; i--)
		{
			Command command = (Command )commands.get(i);
			command.commit_undo();
		}
	}

	public Object getSource()
	{
		return null;
	}

	public int getCount()		// получить количество команд в данной команде
	{
		return commands.size();
	}

	public void setParameter(String field, Object value)
	{
	}
}
