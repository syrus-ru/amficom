/**
 * $Id: MapModeCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

/**
 * ������� ������������ ������ ������ � ������ - ������ ���������, �����, ���� 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapModeCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;

	String modeString;
	int mode;
	
	public MapModeCommand(LogicalNetLayer logicalNetLayer, String modeString, int mode)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.modeString = modeString;
		this.mode = mode;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public Object clone()
	{
		return new MapModeCommand(logicalNetLayer, modeString, mode);
	}

	public void execute()
	{
		if ( aModel.isEnabled(modeString))
		{
			if(!aModel.isSelected(modeString))
			{
				aModel.setSelected("mapModeNodeLink", false);
				aModel.setSelected("mapModeLink", false);
				aModel.setSelected("mapModeCablePath", false);
				aModel.setSelected("mapModePath", false);
	
				aModel.setSelected(modeString, true);
	
				aModel.fireModelChanged("");
	
				logicalNetLayer.getMapState().setShowMode(mode);
				logicalNetLayer.repaint();
			}
		}
	}
}
