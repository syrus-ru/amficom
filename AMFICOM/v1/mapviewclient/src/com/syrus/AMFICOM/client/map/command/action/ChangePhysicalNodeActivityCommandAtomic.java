/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

/**
 * ��������� ���������� ��������������� ���� - ��������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ChangePhysicalNodeActivityCommandAtomic extends MapActionCommand
{
	/** ���� */
	MapPhysicalNodeElement node;
	
	/**
	 * ����� ��������� ���������� ����
	 */
	boolean active;
	
	public ChangePhysicalNodeActivityCommandAtomic(
			MapPhysicalNodeElement mpne, 
			boolean active)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = mpne;
		this.active = active;
	}
	
	public MapPhysicalNodeElement getNode()
	{
		return node;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		node.setActive(active);
	}
	
	public void redo()
	{
		execute();
	}
	
	public void undo()
	{
		node.setActive(!active);
	}
}

