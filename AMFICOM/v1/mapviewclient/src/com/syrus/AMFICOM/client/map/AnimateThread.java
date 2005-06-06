/**
 * $Id: AnimateThread.java,v 1.6 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map;

/**
 * Thread который отвечает за изменение реЖима отображения графических 
 * элементов в состоянии alarmed с периодом timeInterval.
 * Thread меняет флаг отрисовки и выдает команду
 * логическому слою перерисовать свое содержимое.
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
	 * Остановить анимацию
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
