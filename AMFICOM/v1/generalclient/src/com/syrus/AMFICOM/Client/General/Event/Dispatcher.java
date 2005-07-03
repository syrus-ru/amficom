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

import com.syrus.AMFICOM.Client.General.Model.Environment;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Dispatcher implements OperationListener
{
	private List events; // список событий
	private Cmd tmp;

	// на каждое событие может подписываться произвольное число наблюдателей
	private class Cmd
	{
		private String command; // поле, определяющее тип события
		private LinkedList listeners; // наблюдатели
		private Cmd(String command)
		{
			this.command = command;
			listeners = new LinkedList();
		}

		private synchronized List cloneListeners()
		{
			return (LinkedList)listeners.clone();
		}
	}

	public Dispatcher()
	{
		events = new LinkedList();
	}

	// регистрация связывает подписчика с определенным событием
	public synchronized void register (OperationListener listener, String command)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "register(" + listener.getClass().getName() + ", " + command + ")");
		
		for (Iterator it = events.iterator(); it.hasNext();)
		{
			tmp = (Cmd)it.next();
			// если событие найдено в списке, просто добавляется новый наблюдатель
			if (tmp.command.equals(command))
			{
				tmp.listeners.add(listener);
				return;
			}
		}
		// если событие не найдено в списке, создается новый экземпляр и
		// добавляется новый наблюдатель
		tmp = new Cmd(command);
		tmp.listeners.add(listener);
		events.add(tmp);
	}

	// унрегистрация убирает связь подписчика с определенным событием
	public synchronized void unregister (OperationListener listener, String command)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "unregister(" + listener.getClass().getName() + ", " + command + ")");
		
		for (Iterator it = events.iterator(); it.hasNext();)
		{
			tmp = (Cmd)it.next();
			if (tmp.command.equals(command))
			{
				tmp.listeners.remove(listener);
				// в случае если не осталось ни одного подписчика,
				//  событие удаляется из списка
				if (tmp.listeners.isEmpty())
					events.remove(tmp);
				return;
			}
		}
	}

	public void operationPerformed(OperationEvent event)
	{
		notify(event);
	}

	public void notify (OperationEvent event)
	{
		String command = event.getActionCommand();
		List clone = null;
		synchronized(this)
		{
			clone = (List)((LinkedList)events).clone();
		}

		// ищем событие если список не пуст
		for (Iterator it = clone.iterator(); it.hasNext();)
		{
			tmp = (Cmd)it.next();
			if (tmp.command.equals(command))// если найдено событие с таким command
			{
				for (it = tmp.cloneListeners().iterator(); it.hasNext();)
				{
					OperationListener ol = (OperationListener )it.next();
					Environment.log(Environment.LOG_LEVEL_FINER, "event " + event.command + " sent to " + ol.getClass().getName(), getClass().getName(), "notify()");
					
					// у каждого наблюдателя вызываем метод actionPerformed(event)
					ol.operationPerformed(event);
				}
				return;
			}
		}
	}
}
