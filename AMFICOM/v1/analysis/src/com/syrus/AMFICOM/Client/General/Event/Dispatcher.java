//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: прототип Интегрированной Системы Мониторинга                 * //
// *                                                                      * //
// * Название: Диспетчер - класс, обеспечивающий передачу событий между   * //
// *    генераторами и наблюдателями. Сначала наблюдатель подписывается на* //
// *           определенные события описываемые полем command, затем при  * //
// *           возникновении события дипетчер оповещает всех подписавшихся* //
// *           на этот вид событий. Подписчик должен расширять интерфейс  * //
// *           ActionListener. Генератор событий должен вызывать метод    * //
// *           диспетчера  notify (ActionEvent event).                    * //
// * Тип: Java 1.3.0                                                      * //
// *                                                                      * //
// * Автор: Хольшин С.И.                                                  * //
// *                                                                      * //
// * Версия: 0.2                                                          * //
// * От: 22 jul 2002                                                      * //
// * Расположение: com\syrus\AMFICOM\Client\General\Event\Dispatcher.java * //
// *                                                                      * //
// * Компилятор: Borland JBuilder 6.0                                     * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Event;

import java.util.*;

public class Dispatcher implements OperationListener
{
	private HashMap events; // список событий

	// на каждое событие может подписываться произвольное число наблюдателей
	private class Cmd
	{
		private String command; // поле, определяющее тип события
		protected LinkedList listeners; // наблюдатели
		private Cmd(String command)
		{
			this.command = command;
			listeners = new LinkedList();
		}

		protected synchronized LinkedList cloneListeners()
		{
			return (LinkedList)listeners.clone();
		}
	}

	public Dispatcher()
	{
		events = new HashMap();
	}

	// регистрация связывает подписчика с определенным событием
	public synchronized void register (OperationListener listener, String command)
	{
		Cmd cmd = (Cmd)events.get(command);
		// если событие не найдено в списке, создается новый экземпляр и
		// добавляется новый наблюдатель
		if (cmd == null)
		{
			cmd = new Cmd(command);
			events.put(command, cmd);
		}
		// если событие найдено в списке, просто добавляется новый наблюдатель
		cmd.listeners.add(listener);
	}

	// унрегистрация убирает связь подписчика с определенным событием
	public synchronized void unregister (OperationListener listener, String command)
	{
		Cmd cmd = (Cmd)events.get(command);
		if (cmd == null)
			return;

		cmd.listeners.remove(listener);
		// в случае если не осталось ни одного подписчика,
		//  событие удаляется из списка
		if (cmd.listeners.isEmpty())
			events.remove(cmd);
	}

	public void operationPerformed(OperationEvent event)
	{
		notify(event);
	}

	public void notify (OperationEvent event)
	{
		String command = event.getActionCommand();
		Cmd cmd = (Cmd)events.get(command);
		if (cmd == null)
			return;

		LinkedList listeners = cmd.cloneListeners();
		for (Iterator it = listeners.iterator(); it.hasNext();)
		{
			// у каждого наблюдателя вызываем метод actionPerformed(event)
			((OperationListener)(it.next())).operationPerformed(event);
		}
	}
}