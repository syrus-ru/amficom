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
// * Название: исполняемый список команд                                  * //
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

import java.util.LinkedList;
import java.util.Vector;

public class CommandList extends VoidCommand implements Command
{
	/** список команд */
	protected Command top = null;
	
	/**
	 * последней выполненной команды в списке + 1
	 * (или индекс первой готовой к выполнению команды)
	 */
	protected Command current = null;

	/** максимальное количество команд в списке */
	private int maxlength = 50;

	public CommandList()
	{
	}

	public CommandList(int maxlength)
	{
		this.maxlength = maxlength;
	}

	/**
	 * выполнить следующие c команд в списке
	 */
	public void proceed(int c)
	{
		for(int i = 0; i < c; i++)
		{
			Command com = getPrevious(current);
			if( com != null)
			{
				current = com;
				current.execute();
			}
			else
			{
				return;
			}
		}
	}

	/**
	 * выполнить обратоно C команд
	 */
	public void proceed_undo(int c)
	{
		for(int i = 0; i < c; i++)
		{
			if(current == null)
				return;
			current.undo();
			current = current.getNext();
		}
	}

	public void proceed_redo(int c)	// повторно выполнить c команд
	{
		for(int i = 0; i < c; i++)
		{
			Command com = getPrevious(current);
			if( com != null)
			{
				current = com;
				current.redo();
			}
			else
			{
				return;
			}
		}
	}
	
	public Command getPrevious(Command c)
	{
		Command com;
		
		if(c == null)
			return getBottom(); //bottom of list

		if(c == top)
			return null;// has no previous

		for(com = top; com != null; com = com.getNext())
			if(com.getNext() == c)
				return com;

		return null;// not on list
	}

	/**
	 * добавить команду в конец списка
	 */
	public void add(Command command)
	{
		if(command == null)
			return;

		// not executed commands are lost
		command.setNext(current);
		top = command;

		if(getCount() > maxlength)
			removeBottom(1);
	}

	/**
	 * удалить весь список
	 */
	public void flush()
	{
		top = null;
		current = null;
	}

	/**
	 * удалить c команд в конце списка
	 */
	public void removeTop(int c)
	{
		for(int i = 0; i < c; i++)
		{
			if(top != null)
			{
				top.commit_undo();
				top = top.getNext();
			}
			else
				return;
		}
	}

	/**
	 * удалить c команд в начале списка
	 */
	public void removeBottom(int c)
	{
		Command com;
		LinkedList commands = new LinkedList();
		
		for(com = top; com != null; com = com.getNext())
			commands.add(com);

		if(c >= commands.size())
		{
			flush();
			return;
		}
		if(c < 0)
			return;
			
		com = (Command )commands.get(commands.size() - c - 1);

		for(Command c2 = com; c2 != null; c2 = c2.getNext())
		{
			c2.commit_execute();
		}
		
		com.setNext(null);// Отметить как конец списка
	}
	
	/**
	 * выполнить одну следующую команду
	 */
	public void execute()
	{
		if(current == top)
			return;

		// выполнить команду и переместить указатель
		// списка выполненных команд
		current = getPrevious(current);
		if(current != null)
			current.execute();
	}

	/**
	 * выполнить одну следующую команду
	 */
	public void redo()
	{
		if(current == top)
			return;

		// выполнить команду и переместить указатель
		// списка выполненных команд
		current = getPrevious(current);
		current.redo();
	}

	/**
	 * обратно выполнить одну команду
	 */
	public void undo()
	{
		if(current == null)
			return;// если в начале списка то выполнять нечего

		// выполнить undo команды и переместить
		// указатель списка выполненных команд
		current.undo();
		current = current.getNext();
	}
	
	/**
	 * 
	 */
	 public Command getBottom()
	 {
		Command com;
		
		if(top == null)
			return null;
		LinkedList commands = new LinkedList();
		
		for(com = top; com.getNext() != null; com = com.getNext())
			;
		return com;
	 }

	/**
	 * получить количество команд в списке
	 */
	public int getCount()
	{
		Command c;
		int count = 0;
		for(c = top; c != null; )
		{
			count++;
			c = c.getNext();
		}
		return count;
	}
}
