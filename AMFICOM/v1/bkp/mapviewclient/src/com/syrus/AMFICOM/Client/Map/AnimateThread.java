/**
 * $Id: AnimateThread.java,v 1.4 2005/01/21 16:19:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map;

import java.util.*;

/**
 * thread который отвечает за изменение реЖима отображения графических 
 * элементов в состоянии alarmed с периодом timeInterval
 * 
 * thread меняет флаг отрисовки и выдает команду
 * логическому слою перерисовать свое содержимое
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
	 * Остановить анимацию
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
