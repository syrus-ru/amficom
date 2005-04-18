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

import java.util.*;

public class Dispatcher implements OperationListener
{
	private HashMap events; // ������ �������

	// �� ������ ������� ����� ������������� ������������ ����� ������������
	private class Cmd
	{
		protected LinkedList listeners; // �����������
		private Cmd()
		{
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

	// ����������� ��������� ���������� � ������������ ��������
	public synchronized void register (OperationListener listener, String command)
	{
		Cmd cmd = (Cmd)events.get(command);
		// ���� ������� �� ������� � ������, ��������� ����� ��������� �
		// ����������� ����� �����������
		if (cmd == null)
		{
			cmd = new Cmd();
			events.put(command, cmd);
		}
		// ���� ������� ������� � ������, ������ ����������� ����� �����������
		cmd.listeners.add(listener);
	}

	// ������������� ������� ����� ���������� � ������������ ��������
	public synchronized void unregister (OperationListener listener, String command)
	{
		Cmd cmd = (Cmd)events.get(command);
		if (cmd == null)
			return;

		cmd.listeners.remove(listener);
		// � ������ ���� �� �������� �� ������ ����������,
		//  ������� ��������� �� ������
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
			// � ������� ����������� �������� ����� actionPerformed(event)
			((OperationListener)(it.next())).operationPerformed(event);
		}
	}
}
