/*
 * $Id: Constants.java,v 1.2 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.*;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.ImageIcon;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public interface Constants {
	// colors
	public static final Color COLOR_BORDER = Color.BLACK;
	
//panel dimensions
	public static final Dimension A0 = new Dimension (3360, 4760);
	public static final Dimension A1 = new Dimension (3360, 2380);
	public static final Dimension A2 = new Dimension (1680, 2380);
	public static final Dimension A3 = new Dimension (1680, 1190);
	public static final Dimension A4 = new Dimension (840, 1190);

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	// list of tools
	public static final String marqueeTool = "marqueeTool";
	public static final String rectangleTool = "rectangleTool";
	public static final String deviceTool = "deviceTool";
	public static final String linkTool = "edgeTool";
	public static final String cableTool = "cableTool";
	public static final String lineTool = "lineTool";
	public static final String textTool = "textTool";
	public static final String ellipseTool = "ellipseTool";
	public static final String iconTool = "iconTool";
	public static final String zoomTool = "zoomTool";
	public static final String portTool = "portTool";
	
	public static final Icon ICON_MARQUEE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pointer.gif"));
	public static final Icon ICON_DEVICE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif"));
	public static final Icon ICON_RECTANGLE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/rectangle.gif"));
	public static final Icon ICON_ELLIPSE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ellipse.gif"));
	public static final Icon ICON_TEXT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/text.gif"));
	public static final Icon ICON_LINE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/line.gif"));
	public static final Icon ICON_CABLE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/thick_edge.gif"));
	public static final Icon ICON_LINK = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/edge.gif"));
	public static final Icon ICON_ZOOM_IN = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_in.gif"));
	public static final Icon ICON_ZOOM_OUT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_out.gif"));
	public static final Icon ICON_ZOOM_NORMAL = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_actual.gif"));
	public static final Icon ICON_PORT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/port.gif"));
	public static final Icon ICON_CABLE_PORT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cableport.gif"));
	public static final Icon ICON_GROUP = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/group.gif"));
	public static final Icon ICON_UNGROUP = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ungroup.gif"));
	public static final Icon ICON_UNDO = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/undo.gif"));
	public static final Icon ICON_REDO = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/redo.gif"));
	public static final Icon ICON_DELETE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif"));
	public static final Icon ICON_CREATE_UGO = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/component_ugo.gif"));
	public static final Icon ICON_HIERARCHY_PORT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hierarchy_port.gif"));
	
	public static final String TEXT_DEVICE = "Device";
	public static final String TEXT_RECTANGLE = "Rectangle";
	public static final String TEXT_ELLIPSE = "Ellipse";
	public static final String TEXT_TEXT = "Text";
	public static final String TEXT_ICON = "Icon";
	public static final String TEXT_LINE = "Line";
	public static final String TEXT_CABLE = "Cable";
	public static final String TEXT_LINK = "Link";
	public static final String TEXT_ZOOM = "Zoom";
	public static final String TEXT_ZOOM_IN = "Zoom In";
	public static final String TEXT_ZOOM_OUT = "Zoom Out";
	public static final String TEXT_ZOOM_NORMAL = "Zoom Normal";
	public static final String TEXT_PORT = "Port";
	public static final String TEXT_CABLE_PORT = "Cable port";
	public static final String TEXT_GROUP = "Create component";
	public static final String TEXT_UNGROUP = "Strip component";
	public static final String TEXT_UNDO = "Undo";
	public static final String TEXT_REDO = "Redo";
	public static final String TEXT_DELETE = "Delete";
	public static final String TEXT_CREATE_UGO = "Create symbolic notation";
	public static final String TEXT_HIERARCHY_PORT = "Hierarchy port";
		
	public static final String COMPONENT = "Component";
	public static final String DEVICE = "device";
	public static final String ERROR = "Error";
	public static final String ERROR_COMPONENT_NOT_FOUND = "Не найдено ни одного компонента";
	public static final String ERROR_HIERARCHY_PORT_NOT_FOUND = "Не найдено ни одного иерархического порта";
	
	// list of button keys
	public static final String deleteKey = "deleteKey";
	public static final String groupKey = "groupKey";
	public static final String groupSEKey = "groupSEKey";
	public static final String ungroupKey = "ungroupKey";
	public static final String hierarchyUpKey = "hierarchyUpKey";

	public static final String createTopLevelElementKey = "createTopLevelElementKey";
	public static final String createTopLevelSchemeKey = "createTopLevelSchemeKey";
	public static final String openLibraryKey = "openLibraryKey";
	public static final String saveLibraryKey = "saveLibraryKey";
	public static final String renameLibraryKey = "renameLibraryKey";
	public static final String closeLibraryKey = "closeLibraryKey";
	public static final String saveKey = "saveKey";
	public static final String saveAsKey = "saveAsKey";

	public static final String portOutKey = "portTool1";
	public static final String portInKey = "portTool2";
	public static final String blockPortKey = "blockPortKey";
	public static final String undoKey = "undo";
	public static final String redoKey = "redo";
	public static final String zoomInKey = "zoomin";
	public static final String zoomOutKey = "zoomout";
	public static final String zoomActualKey = "zoomactual";
	public static final String separator = "separator";
	public static final String backgroundSize = "backgroundSize";

	public static final int CREATING_PATH_MODE = 3;
	public static final int NORMAL = 0;

	public static final String PATH_MODE = "pathmode";
	public static final String LINK_MODE = "linkmode";
	public static final String TOP_LEVEL_SCHEME_MODE = "toplevelmode";
}
