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
// * Название: Интерфейс исполняемой команды                              * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 16 jul 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Command\Command.java                                  * //
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

public interface Command extends Cloneable
{
	public static int RESULT_UNSPECIFIED = 0;
	public static int RESULT_OK = 1;
	public static int RESULT_YES = 1;
	public static int RESULT_NO = 2;
	public static int RESULT_CANCEL = 3;

	public void execute();			// первое выполнение команды

	public void undo();				// обратное выполнение - восстановление
									// предыдущего состояния

	public void redo();				// повторное выполнение команды

	public void commit_execute();	// подтверждение окончательного выполнения
									// команды и освобождение ресурсов

	public void commit_undo();		// подтверждение окончательного обратного
									// выполнения команды и освобождение
									// ресурсов

	public Object getSource();		// получить источник команды

	public Object clone();			// получить копию команды. Используется для
									// получения точной копии команды

	public void setParameter(String field, Object value);
									// установить значение параметра field

	public int getResult();
}

