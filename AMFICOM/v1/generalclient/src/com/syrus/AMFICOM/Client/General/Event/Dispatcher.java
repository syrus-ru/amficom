//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: �������� ��������������� ������� �����������                 * //
// *                                                                      * //
// * ��������: ��������� - �����, �������������� �������� ������� �����   * //
// *    ������������ � �������������. ������� ����������� ������������� ��* //
// *           ������������ ������� ����������� ����� command, ����� ���  * //
// *           ������������� ������� �������� ��������� ���� �������������* //
// *           �� ���� ��� �������. ��������� ������ ��������� ���������  * //
// *           ActionListener. ��������� ������� ������ �������� �����    * //
// *           ����������  notify (ActionEvent event).                    * //
// * ���: Java 1.3.0                                                      * //
// *                                                                      * //
// * �����: ������� �.�.                                                  * //
// *                                                                      * //
// * ������: 0.2                                                          * //
// * ��: 22 jul 2002                                                      * //
// * ������������: com\syrus\AMFICOM\Client\General\Event\Dispatcher.java * //
// *                                                                      * //
// * ����������: Borland JBuilder 6.0                                     * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
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
	private List events; // ������ �������
	private Cmd tmp;

	// �� ������ ������� ����� ������������� ������������ ����� ������������
	private class Cmd
	{
		private String command; // ����, ������������ ��� �������
		private LinkedList listeners; // �����������
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

	// ����������� ��������� ���������� � ������������ ��������
	public synchronized void register (OperationListener listener, String command)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "register(" + listener.getClass().getName() + ", " + command + ")");
		
		for (Iterator it = events.iterator(); it.hasNext();)
		{
			tmp = (Cmd)it.next();
			// ���� ������� ������� � ������, ������ ����������� ����� �����������
			if (tmp.command.equals(command))
			{
				tmp.listeners.add(listener);
				return;
			}
		}
		// ���� ������� �� ������� � ������, ��������� ����� ��������� �
		// ����������� ����� �����������
		tmp = new Cmd(command);
		tmp.listeners.add(listener);
		events.add(tmp);
	}

	// ������������� ������� ����� ���������� � ������������ ��������
	public synchronized void unregister (OperationListener listener, String command)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "unregister(" + listener.getClass().getName() + ", " + command + ")");
		
		for (Iterator it = events.iterator(); it.hasNext();)
		{
			tmp = (Cmd)it.next();
			if (tmp.command.equals(command))
			{
				tmp.listeners.remove(listener);
				// � ������ ���� �� �������� �� ������ ����������,
				//  ������� ��������� �� ������
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

		// ���� ������� ���� ������ �� ����
		for (Iterator it = clone.iterator(); it.hasNext();)
		{
			tmp = (Cmd)it.next();
			if (tmp.command.equals(command))// ���� ������� ������� � ����� command
			{
				for (it = tmp.cloneListeners().iterator(); it.hasNext();)
				{
					OperationListener ol = (OperationListener )it.next();
					Environment.log(Environment.LOG_LEVEL_FINER, "event " + event.command + " sent to " + ol.getClass().getName(), getClass().getName(), "notify()");
					
					// � ������� ����������� �������� ����� actionPerformed(event)
					ol.operationPerformed(event);
				}
				return;
			}
		}
	}
}
