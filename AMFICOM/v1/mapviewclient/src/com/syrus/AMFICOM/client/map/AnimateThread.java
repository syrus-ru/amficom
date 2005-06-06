/**
 * $Id: AnimateThread.java,v 1.6 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map;

/**
 * Thread ������� �������� �� ��������� ������ ����������� ����������� 
 * ��������� � ��������� alarmed � �������� timeInterval.
 * Thread ������ ���� ��������� � ������ �������
 * ����������� ���� ������������ ���� ����������.
 * @version $Revision: 1.6 $, $Date: 2005/06/06 12:20:29 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class AnimateThread 
		extends Thread
		implements Runnable
{
	public static final int DEFAULT_TIME_INTERVAL = 1000;

	boolean alarmState = false;

	int timeInterval = DEFAULT_TIME_INTERVAL;

	LogicalNetLayer logicalNetLayer;

	private volatile boolean isRunning = true;

	public AnimateThread(LogicalNetLayer logical)
	{
		this.logicalNetLayer = logical;
	}

	/**
	 * ���������� ��������
	 */
	public void stopRunning()
	{
		this.isRunning = false;
	}
	
	public void run()
	{
//		while (is_running)
//		{
//			try
//			{
//				sleep( timeInterval );
//				MapPropertiesManager.setShowAlarmState(!MapPropertiesManager.isShowAlarmState());
//				logicalNetLayer.repaint();
//			}
//			catch (Exception e)
//			{
//				System.out.println("AnimateThread found: " + e);
//			}
//		}
	}
}
