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
// * Название: Команда закрытия окна модуля                               * //
// *                                                                      * //
// * Тип: Java 1.4.1_03                                                   * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 16 jul 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\ExitCommand.java                              * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 9.0.4.12.80                      * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.1_03)* //
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

import com.syrus.AMFICOM.Client.General.Model.Environment;

import java.awt.Window;

public class ExitCommand extends VoidCommand implements Command
{
	Window window;	// Окно, из которого вызвана команда

	public ExitCommand()
	{
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("window"))
			setWindow((Window )value);
	}

	public void setWindow(Window window)
	{
		this.window = window;
	}

	public ExitCommand(Window window)
	{
		this.window = window;
	}

	public void execute()
	{
		System.out.println("exit window " + window.getName());
		Environment.disposeWindow(window);	// Реально удаление окна производит
											// только класс окружения Environment
	}

	public Object clone()
	{
		return new ExitCommand(window);
	}

}
