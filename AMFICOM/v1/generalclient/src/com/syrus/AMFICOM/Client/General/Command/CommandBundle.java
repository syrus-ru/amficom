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

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class CommandBundle extends VoidCommand implements Command
{
	protected List commands = new ArrayList();			// список команд

	public CommandBundle()
	{
	}

	public Object clone()				// получить копию команды
	{
		Command c;
		CommandBundle cb = new CommandBundle();
		cb.commands = new ArrayList();
		for(ListIterator e = commands.listIterator(); e.hasNext();)
		{
			c = (Command )e.next();
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
		for(ListIterator it = commands.listIterator();it.hasNext();)
		{
			Command command = (Command )it.next();
			command.execute();
		}
	}

	public void undo()					// обратно выполнить - все команды 
										// в списке в обратном порядке
	{
		for(ListIterator it = commands.listIterator(commands.size());it.hasPrevious();)
		{
			Command command = (Command )it.previous();
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
		int i;
		int count = getCount();
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(i);
			command.setParameter(field, value);
		}
	}
}
