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
// * Название: Заглушка для команды (пустая команда)                      * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 16 jul 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\VoidCommand.java                              * //
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

public class VoidCommand implements Command
{
	// поле источника команды
	private Object source;

	// у пустой команды по умолчанию нет источника
	public VoidCommand()
	{
		source = new String("NULL");
	}

	// получить
	public Object clone()
	{
		return new VoidCommand(source);
	}

	public VoidCommand(Object source)
	{
		if(source == null)
			source = new String("NULL");
		this.source = source;
	}

	// пустая команда не выполняет никаких действий
	public void execute()
	{
		System.out.println("Void command executed for " + source.toString() + " - ignored");
	}
	
	public int getResult()
	{
		return RESULT_OK;
	}

	// пустая команда не выполняет никаких действий
	public void undo()
	{
		System.out.println("Void command undo for " + source.toString() + " - ignored");
	}

	// пустая команда не выполняет никаких действий
	public void redo()
	{
		System.out.println("Void command redo for " + source.toString() + " - ignored");
	}

	// пустая команда не выполняет никаких действий
	public void commit_execute()
	{
		System.out.println("Void command execution commit for " + source.toString());
	}

	// пустая команда не выполняет никаких действий
	public void commit_undo()
	{
		System.out.println("Void command undo commit for " + source.toString() + " - ignored");
	}

	// у пустой команды нет источника
	public Object getSource()
	{
		System.out.println("Source for Void command is " + source.toString() + " - ignored");
		return null;
	}

	// пустая команда не имеет параметров
	public void setParameter(String field, Object value)
	{
		System.out.println("Set for Void command paramenter " + field +
				" to value " + value.toString() + " - ignored");
	}

}

 