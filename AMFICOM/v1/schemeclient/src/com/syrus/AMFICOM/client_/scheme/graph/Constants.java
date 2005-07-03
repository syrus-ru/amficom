/*
 * $Id: Constants.java,v 1.4 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.*;

import javax.swing.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/05/26 07:40:51 $
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
	public static final String MARQUEE = "marqueeTool"; //$NON-NLS-1$
//	public static final String rectangleTool = "rectangleTool"; //$NON-NLS-1$
//	public static final String deviceTool = "deviceTool"; //$NON-NLS-1$
//	public static final String linkTool = "edgeTool"; //$NON-NLS-1$
//	public static final String cableTool = "cableTool"; //$NON-NLS-1$
//	public static final String lineTool = "lineTool"; //$NON-NLS-1$
//	public static final String textTool = "textTool"; //$NON-NLS-1$
//	public static final String ellipseTool = "ellipseTool"; //$NON-NLS-1$
//	public static final String iconTool = "iconTool"; //$NON-NLS-1$
//	public static final String zoomTool = "zoomTool"; //$NON-NLS-1$
//	public static final String portTool = "portTool"; //$NON-NLS-1$
	
	public static final Icon ICON_MARQUEE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pointer.gif")); //$NON-NLS-1$
	public static final Icon ICON_DEVICE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")); //$NON-NLS-1$
	public static final Icon ICON_RECTANGLE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/rectangle.gif")); //$NON-NLS-1$
	public static final Icon ICON_ELLIPSE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ellipse.gif")); //$NON-NLS-1$
	public static final Icon ICON_TEXT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/text.gif")); //$NON-NLS-1$
	public static final Icon ICON_LINE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/line.gif")); //$NON-NLS-1$
	public static final Icon ICON_CABLE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/thick_edge.gif")); //$NON-NLS-1$
	public static final Icon ICON_LINK = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/edge.gif")); //$NON-NLS-1$
	public static final Icon ICON_ZOOM_IN = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_in.gif")); //$NON-NLS-1$
	public static final Icon ICON_ZOOM_OUT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_out.gif")); //$NON-NLS-1$
	public static final Icon ICON_ZOOM_NORMAL = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_actual.gif")); //$NON-NLS-1$
	public static final Icon ICON_PORT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/port.gif")); //$NON-NLS-1$
	public static final Icon ICON_CABLE_PORT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cableport.gif")); //$NON-NLS-1$
	public static final Icon ICON_GROUP = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/group.gif")); //$NON-NLS-1$
	public static final Icon ICON_UNGROUP = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ungroup.gif")); //$NON-NLS-1$
	public static final Icon ICON_UNDO = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/undo.gif")); //$NON-NLS-1$
	public static final Icon ICON_REDO = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/redo.gif")); //$NON-NLS-1$
	public static final Icon ICON_DELETE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif")); //$NON-NLS-1$
	public static final Icon ICON_CREATE_UGO = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/component_ugo.gif")); //$NON-NLS-1$
	public static final Icon ICON_HIERARCHY_PORT = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hierarchy_port.gif")); //$NON-NLS-1$
	public static final Icon ICON_SCHEME_SIZE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/sheme_size.gif")); //$NON-NLS-1$
	public static final Icon ICON_LINK_MODE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/linkmode.gif")); //$NON-NLS-1$
	public static final Icon ICON_PATH_MODE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif")); //$NON-NLS-1$
	public static final Icon ICON_TOP_LEVEL_MODE = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")); //$NON-NLS-1$
	
	public static final String RECTANGLE = "rectangle"; //$NON-NLS-1$
	public static final String ELLIPSE = "ellipse"; //$NON-NLS-1$
	public static final String TEXT = "text"; //$NON-NLS-1$
	public static final String ICON = "icon"; //$NON-NLS-1$
	public static final String LINE = "line"; //$NON-NLS-1$
	public static final String CABLE = "cable"; //$NON-NLS-1$
	public static final String LINK = "link";  //$NON-NLS-1$
	
	public static final String DEVICE = "device"; //$NON-NLS-1$
	
	public static final String ERROR = "error"; //$NON-NLS-1$
	public static final String ERROR_COMPONENT_NOT_FOUND = "error_component_not_found"; //$NON-NLS-1$
	public static final String ERROR_HIERARCHY_PORT_NOT_FOUND = "error_hierarchy_port_not_found"; //$NON-NLS-1$
	public static final String ERROR_PORTTYPE_NOT_FOUND = "error_porttype_not_found";//$NON-NLS-1$
	public static final String ERROR_GROUPED_DEVICE = "error_grouped_device";//$NON-NLS-1$
	
	// list of button keys
	public static final String DELETE = "delete"; //$NON-NLS-1$
	public static final String GROUP = "group"; //$NON-NLS-1$
	public static final String UNGROUP = "ungroup"; //$NON-NLS-1$

	public static final String CREATE_UGO = "create_ugo"; //$NON-NLS-1$

	public static final String PORT = "port"; //$NON-NLS-1$
	public static final String CABLE_PORT = "cable_port"; //$NON-NLS-1$
	public static final String BLOCK_PORT = "block_port"; //$NON-NLS-1$
	public static final String UNDO = "undo"; //$NON-NLS-1$
	public static final String REDO = "redo"; //$NON-NLS-1$
	public static final String ZOOM_IN = "zoom_in"; //$NON-NLS-1$
	public static final String ZOOM_OUT = "zoom_out"; //$NON-NLS-1$
	public static final String ZOOM_ACTUAL = "zoom_actual"; //$NON-NLS-1$
	public static final String SEPARATOR = "separator";  //$NON-NLS-1$
	public static final String BACKGROUND_SIZE = "background";  //$NON-NLS-1$

	public static final int CREATING_PATH_MODE = 3;
	public static final int NORMAL = 0;

	public static final String PATH_MODE = "path_mode"; //$NON-NLS-1$
	public static final String LINK_MODE = "link_mode"; //$NON-NLS-1$
	public static final String TOP_LEVEL_MODE = "top_level_mode"; //$NON-NLS-1$
}
