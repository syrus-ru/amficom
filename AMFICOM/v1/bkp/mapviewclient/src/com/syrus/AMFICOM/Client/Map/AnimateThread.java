/**
 * $Id: AnimateThread.java,v 1.4 2005/01/21 16:19:56 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map;

import java.util.*;

/**
 * thread ������� �������� �� ��������� ������ ����������� ����������� 
 * ��������� � ��������� alarmed � �������� timeInterval
 * 
 * thread ������ ���� ��������� � ������ �������
 * ����������� ���� ������������ ���� ����������
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/01/21 16:19:56 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
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
		logicalNetLayer = logical;
	}

	/**
	 * ���������� ��������
	 */
	public void stopRunning()
	{
		isRunning = false;
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
