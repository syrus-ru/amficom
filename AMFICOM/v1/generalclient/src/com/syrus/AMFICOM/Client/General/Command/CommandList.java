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

import java.util.Vector;

public class CommandList extends Object
{
	protected Vector commands;	// список команд

	private int maxlength = 10;	// максимальное количество команд в списке

	private int curindex = 0;	// индекс последней выполненной команды в списке
								// + 1 (или индекс первой готовой к выполнению
								// команды)

	public CommandList()
	{
	}

	// создание
	public CommandList(Vector commands)
	{
		this.commands = (Vector )commands.clone();
	}

	public void proceed(int c)	// выполнить следующие c команд в списке
	{
		int count = getCount();
		if(c + curindex < count)	// если надо выполнить команд больше, чем
									// есть в списке, то выполнить все
			count = c + curindex;	// в противном случае выполнять только C
		for(; curindex < count; curindex++)
		{
			Command command = (Command )commands.get(curindex);
			command.execute();
		}
	}

	public void proceed_undo(int c)	// выполнить обратоно C команд
	{
		int index = 0;
		if(c < curindex + 1)			// если выполнить undo для количества
										// команд большего, чем есть, то
										// выполнить undo для всех, в противном
			index = curindex + 1 - c;	// случае только для c команд
		for(; curindex >= index; curindex--)
		{
			Command command = (Command )commands.get(curindex);
			command.undo();
		}
	}

	public void proceed_redo(int c)	// повторно выполнить c команд
	{
		int count = getCount();
		if(c + curindex < count)	// если надо выполнить команд больше, чем
									// есть в списке, то выполнить все
			count = c + curindex;	// в противном случае выполнять только C
		for(; curindex < count; curindex++)
		{
			Command command = (Command )commands.get(curindex);
			command.redo();
		}
	}

	public void add(Command command)// добавить команду в конец списка
	{
		commands.add(command);
	}

	public void flush()				// удалить весь список
	{
		commands.clear();
		curindex = 0;
	}

	public void removeTop(int c)	// удалить c команд в конце списка
	{
		int i;
		int index = 0;
		if(c < getCount())
			index = getCount() - c;
		for(i = getCount() - 1; i >= index; i--)
		{
			Command command = (Command )commands.get(i);
			if(i > curindex - 1)		// если у команды был выполнен undo,
										// то есть она находится за curindex
										// в списке, то подтвердить undo
				command.commit_undo();	//
			commands.remove(i);
		}
		if(curindex > getCount() - 1)	// поправить индекс текущей команды
			curindex = getCount() - 1;	// если он выехал за границы
	}

	public void removeBottom(int c)	// удалить c команд в начале списка
	{
		int i;
		int count = getCount();
		if(c < count)
			count = c;
		for(i = 0; i < count; i++)
		{
			Command command = (Command )commands.get(0);
			if(i < curindex)			// если команда уже была выполнена,
										// то есть она находится перед curindex
										// в списке, то подтвердить выполнение
				command.commit_execute();	//
			commands.remove(0);
		}
		curindex -= count;
		if(curindex < 0)			// поправить индекс текущей команды
			curindex = 0;			// если он выехал за границы
	}

	public void step()				// выполнить одну следующую команду
	{
		if(curindex == getCount())	// если в конце списка то выполнять нечего
			return;
		Command command = (Command )commands.get(curindex++);
									// выполнить команду и переместить указатель
									// списка выполненных команд
		command.execute();
	}

	public void redo()				// выполнить одну следующую команду
	{
		if(curindex == getCount())	// если в конце списка то выполнять нечего
			return;
		Command command = (Command )commands.get(curindex++);
									// выполнить команду и переместить указатель
									// списка выполненных команд
		command.redo();
	}

	public void undo()				// обратно выполнить одну команду
	{
		if(curindex == 0)			// если в начале списка то выполнять нечего
			return;
		Command command = (Command )commands.get(curindex--);
									// выполнить undo команды и переместить
									// указатель списка выполненных команд
		command.undo();
	}

	public int getCount()			// получить количество команд в списке
	{
		return commands.size();
	}
}

 