package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import java.beans.*;

public class JObjectResourceListBeanInfo extends SimpleBeanInfo {
	
    // Bean descriptor //GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( JObjectResourceList.class , null );//GEN-HEADEREND:BeanDescriptor
		
		// Here you can add code for customizing the BeanDescriptor.
		
        return beanDescriptor;         }//GEN-LAST:BeanDescriptor
	
	
    // Property identifiers //GEN-FIRST:Properties
    private static final int PROPERTY_accessibleContext = 0;
    private static final int PROPERTY_actionMap = 1;
    private static final int PROPERTY_alignmentX = 2;
    private static final int PROPERTY_alignmentY = 3;
    private static final int PROPERTY_ancestorListeners = 4;
    private static final int PROPERTY_anchorSelectionIndex = 5;
    private static final int PROPERTY_autoscrolls = 6;
    private static final int PROPERTY_background = 7;
    private static final int PROPERTY_backgroundSet = 8;
    private static final int PROPERTY_border = 9;
    private static final int PROPERTY_bounds = 10;
    private static final int PROPERTY_cellRenderer = 11;
    private static final int PROPERTY_colorModel = 12;
    private static final int PROPERTY_component = 13;
    private static final int PROPERTY_componentCount = 14;
    private static final int PROPERTY_componentListeners = 15;
    private static final int PROPERTY_componentOrientation = 16;
    private static final int PROPERTY_components = 17;
    private static final int PROPERTY_containerListeners = 18;
    private static final int PROPERTY_cursor = 19;
    private static final int PROPERTY_cursorSet = 20;
    private static final int PROPERTY_debugGraphicsOptions = 21;
    private static final int PROPERTY_displayable = 22;
    private static final int PROPERTY_doubleBuffered = 23;
    private static final int PROPERTY_dragEnabled = 24;
    private static final int PROPERTY_dropTarget = 25;
    private static final int PROPERTY_enabled = 26;
    private static final int PROPERTY_firstVisibleIndex = 27;
    private static final int PROPERTY_fixedCellHeight = 28;
    private static final int PROPERTY_fixedCellWidth = 29;
    private static final int PROPERTY_focusable = 30;
    private static final int PROPERTY_focusCycleRoot = 31;
    private static final int PROPERTY_focusCycleRootAncestor = 32;
    private static final int PROPERTY_focusListeners = 33;
    private static final int PROPERTY_focusOwner = 34;
    private static final int PROPERTY_focusTraversable = 35;
    private static final int PROPERTY_focusTraversalKeys = 36;
    private static final int PROPERTY_focusTraversalKeysEnabled = 37;
    private static final int PROPERTY_focusTraversalPolicy = 38;
    private static final int PROPERTY_focusTraversalPolicySet = 39;
    private static final int PROPERTY_font = 40;
    private static final int PROPERTY_fontSet = 41;
    private static final int PROPERTY_foreground = 42;
    private static final int PROPERTY_foregroundSet = 43;
    private static final int PROPERTY_graphics = 44;
    private static final int PROPERTY_graphicsConfiguration = 45;
    private static final int PROPERTY_height = 46;
    private static final int PROPERTY_hierarchyBoundsListeners = 47;
    private static final int PROPERTY_hierarchyListeners = 48;
    private static final int PROPERTY_ignoreRepaint = 49;
    private static final int PROPERTY_inputContext = 50;
    private static final int PROPERTY_inputMethodListeners = 51;
    private static final int PROPERTY_inputMethodRequests = 52;
    private static final int PROPERTY_inputVerifier = 53;
    private static final int PROPERTY_insets = 54;
    private static final int PROPERTY_keyListeners = 55;
    private static final int PROPERTY_lastVisibleIndex = 56;
    private static final int PROPERTY_layout = 57;
    private static final int PROPERTY_layoutOrientation = 58;
    private static final int PROPERTY_leadSelectionIndex = 59;
    private static final int PROPERTY_lightweight = 60;
    private static final int PROPERTY_listData = 61;
    private static final int PROPERTY_listSelectionListeners = 62;
    private static final int PROPERTY_locale = 63;
    private static final int PROPERTY_locationOnScreen = 64;
    private static final int PROPERTY_managingFocus = 65;
    private static final int PROPERTY_maximumSize = 66;
    private static final int PROPERTY_maximumSizeSet = 67;
    private static final int PROPERTY_maxSelectionIndex = 68;
    private static final int PROPERTY_minimumSize = 69;
    private static final int PROPERTY_minimumSizeSet = 70;
    private static final int PROPERTY_minSelectionIndex = 71;
    private static final int PROPERTY_model = 72;
    private static final int PROPERTY_mouseListeners = 73;
    private static final int PROPERTY_mouseMotionListeners = 74;
    private static final int PROPERTY_mouseWheelListeners = 75;
    private static final int PROPERTY_name = 76;
    private static final int PROPERTY_nextFocusableComponent = 77;
    private static final int PROPERTY_opaque = 78;
    private static final int PROPERTY_optimizedDrawingEnabled = 79;
    private static final int PROPERTY_paintingTile = 80;
    private static final int PROPERTY_parent = 81;
    private static final int PROPERTY_peer = 82;
    private static final int PROPERTY_preferredScrollableViewportSize = 83;
    private static final int PROPERTY_preferredSize = 84;
    private static final int PROPERTY_preferredSizeSet = 85;
    private static final int PROPERTY_propertyChangeListeners = 86;
    private static final int PROPERTY_prototypeCellValue = 87;
    private static final int PROPERTY_registeredKeyStrokes = 88;
    private static final int PROPERTY_requestFocusEnabled = 89;
    private static final int PROPERTY_rootPane = 90;
    private static final int PROPERTY_scrollableTracksViewportHeight = 91;
    private static final int PROPERTY_scrollableTracksViewportWidth = 92;
    private static final int PROPERTY_selectedIndex = 93;
    private static final int PROPERTY_selectedIndices = 94;
    private static final int PROPERTY_selectedValue = 95;
    private static final int PROPERTY_selectedValues = 96;
    private static final int PROPERTY_selectionBackground = 97;
    private static final int PROPERTY_selectionEmpty = 98;
    private static final int PROPERTY_selectionForeground = 99;
    private static final int PROPERTY_selectionInterval = 100;
    private static final int PROPERTY_selectionMode = 101;
    private static final int PROPERTY_selectionModel = 102;
    private static final int PROPERTY_showing = 103;
    private static final int PROPERTY_toolkit = 104;
    private static final int PROPERTY_toolTipText = 105;
    private static final int PROPERTY_topLevelAncestor = 106;
    private static final int PROPERTY_transferHandler = 107;
    private static final int PROPERTY_treeLock = 108;
    private static final int PROPERTY_UI = 109;
    private static final int PROPERTY_UIClassID = 110;
    private static final int PROPERTY_valid = 111;
    private static final int PROPERTY_validateRoot = 112;
    private static final int PROPERTY_valueIsAdjusting = 113;
    private static final int PROPERTY_verifyInputWhenFocusTarget = 114;
    private static final int PROPERTY_vetoableChangeListeners = 115;
    private static final int PROPERTY_visible = 116;
    private static final int PROPERTY_visibleRect = 117;
    private static final int PROPERTY_visibleRowCount = 118;
    private static final int PROPERTY_width = 119;
    private static final int PROPERTY_x = 120;
    private static final int PROPERTY_y = 121;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[122];
    
        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor ( "accessibleContext", JObjectResourceList.class, "getAccessibleContext", null );
            properties[PROPERTY_actionMap] = new PropertyDescriptor ( "actionMap", JObjectResourceList.class, "getActionMap", "setActionMap" );
            properties[PROPERTY_alignmentX] = new PropertyDescriptor ( "alignmentX", JObjectResourceList.class, "getAlignmentX", "setAlignmentX" );
            properties[PROPERTY_alignmentY] = new PropertyDescriptor ( "alignmentY", JObjectResourceList.class, "getAlignmentY", "setAlignmentY" );
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor ( "ancestorListeners", JObjectResourceList.class, "getAncestorListeners", null );
            properties[PROPERTY_anchorSelectionIndex] = new PropertyDescriptor ( "anchorSelectionIndex", JObjectResourceList.class, "getAnchorSelectionIndex", null );
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor ( "autoscrolls", JObjectResourceList.class, "getAutoscrolls", "setAutoscrolls" );
            properties[PROPERTY_background] = new PropertyDescriptor ( "background", JObjectResourceList.class, "getBackground", "setBackground" );
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor ( "backgroundSet", JObjectResourceList.class, "isBackgroundSet", null );
            properties[PROPERTY_border] = new PropertyDescriptor ( "border", JObjectResourceList.class, "getBorder", "setBorder" );
            properties[PROPERTY_bounds] = new PropertyDescriptor ( "bounds", JObjectResourceList.class, "getBounds", "setBounds" );
            properties[PROPERTY_cellRenderer] = new PropertyDescriptor ( "cellRenderer", JObjectResourceList.class, "getCellRenderer", "setCellRenderer" );
            properties[PROPERTY_colorModel] = new PropertyDescriptor ( "colorModel", JObjectResourceList.class, "getColorModel", null );
            properties[PROPERTY_component] = new IndexedPropertyDescriptor ( "component", JObjectResourceList.class, null, null, "getComponent", null );
            properties[PROPERTY_componentCount] = new PropertyDescriptor ( "componentCount", JObjectResourceList.class, "getComponentCount", null );
            properties[PROPERTY_componentListeners] = new PropertyDescriptor ( "componentListeners", JObjectResourceList.class, "getComponentListeners", null );
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor ( "componentOrientation", JObjectResourceList.class, "getComponentOrientation", "setComponentOrientation" );
            properties[PROPERTY_components] = new PropertyDescriptor ( "components", JObjectResourceList.class, "getComponents", null );
            properties[PROPERTY_containerListeners] = new PropertyDescriptor ( "containerListeners", JObjectResourceList.class, "getContainerListeners", null );
            properties[PROPERTY_cursor] = new PropertyDescriptor ( "cursor", JObjectResourceList.class, "getCursor", "setCursor" );
            properties[PROPERTY_cursorSet] = new PropertyDescriptor ( "cursorSet", JObjectResourceList.class, "isCursorSet", null );
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor ( "debugGraphicsOptions", JObjectResourceList.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions" );
            properties[PROPERTY_displayable] = new PropertyDescriptor ( "displayable", JObjectResourceList.class, "isDisplayable", null );
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor ( "doubleBuffered", JObjectResourceList.class, "isDoubleBuffered", "setDoubleBuffered" );
            properties[PROPERTY_dragEnabled] = new PropertyDescriptor ( "dragEnabled", JObjectResourceList.class, "getDragEnabled", "setDragEnabled" );
            properties[PROPERTY_dropTarget] = new PropertyDescriptor ( "dropTarget", JObjectResourceList.class, "getDropTarget", "setDropTarget" );
            properties[PROPERTY_enabled] = new PropertyDescriptor ( "enabled", JObjectResourceList.class, "isEnabled", "setEnabled" );
            properties[PROPERTY_firstVisibleIndex] = new PropertyDescriptor ( "firstVisibleIndex", JObjectResourceList.class, "getFirstVisibleIndex", null );
            properties[PROPERTY_fixedCellHeight] = new PropertyDescriptor ( "fixedCellHeight", JObjectResourceList.class, "getFixedCellHeight", "setFixedCellHeight" );
            properties[PROPERTY_fixedCellWidth] = new PropertyDescriptor ( "fixedCellWidth", JObjectResourceList.class, "getFixedCellWidth", "setFixedCellWidth" );
            properties[PROPERTY_focusable] = new PropertyDescriptor ( "focusable", JObjectResourceList.class, "isFocusable", "setFocusable" );
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor ( "focusCycleRoot", JObjectResourceList.class, "isFocusCycleRoot", "setFocusCycleRoot" );
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor ( "focusCycleRootAncestor", JObjectResourceList.class, "getFocusCycleRootAncestor", null );
            properties[PROPERTY_focusListeners] = new PropertyDescriptor ( "focusListeners", JObjectResourceList.class, "getFocusListeners", null );
            properties[PROPERTY_focusOwner] = new PropertyDescriptor ( "focusOwner", JObjectResourceList.class, "isFocusOwner", null );
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor ( "focusTraversable", JObjectResourceList.class, "isFocusTraversable", null );
            properties[PROPERTY_focusTraversalKeys] = new IndexedPropertyDescriptor ( "focusTraversalKeys", JObjectResourceList.class, null, null, "getFocusTraversalKeys", "setFocusTraversalKeys" );
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor ( "focusTraversalKeysEnabled", JObjectResourceList.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled" );
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor ( "focusTraversalPolicy", JObjectResourceList.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy" );
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor ( "focusTraversalPolicySet", JObjectResourceList.class, "isFocusTraversalPolicySet", null );
            properties[PROPERTY_font] = new PropertyDescriptor ( "font", JObjectResourceList.class, "getFont", "setFont" );
            properties[PROPERTY_fontSet] = new PropertyDescriptor ( "fontSet", JObjectResourceList.class, "isFontSet", null );
            properties[PROPERTY_foreground] = new PropertyDescriptor ( "foreground", JObjectResourceList.class, "getForeground", "setForeground" );
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor ( "foregroundSet", JObjectResourceList.class, "isForegroundSet", null );
            properties[PROPERTY_graphics] = new PropertyDescriptor ( "graphics", JObjectResourceList.class, "getGraphics", null );
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor ( "graphicsConfiguration", JObjectResourceList.class, "getGraphicsConfiguration", null );
            properties[PROPERTY_height] = new PropertyDescriptor ( "height", JObjectResourceList.class, "getHeight", null );
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor ( "hierarchyBoundsListeners", JObjectResourceList.class, "getHierarchyBoundsListeners", null );
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor ( "hierarchyListeners", JObjectResourceList.class, "getHierarchyListeners", null );
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor ( "ignoreRepaint", JObjectResourceList.class, "getIgnoreRepaint", "setIgnoreRepaint" );
            properties[PROPERTY_inputContext] = new PropertyDescriptor ( "inputContext", JObjectResourceList.class, "getInputContext", null );
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor ( "inputMethodListeners", JObjectResourceList.class, "getInputMethodListeners", null );
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor ( "inputMethodRequests", JObjectResourceList.class, "getInputMethodRequests", null );
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor ( "inputVerifier", JObjectResourceList.class, "getInputVerifier", "setInputVerifier" );
            properties[PROPERTY_insets] = new PropertyDescriptor ( "insets", JObjectResourceList.class, "getInsets", null );
            properties[PROPERTY_keyListeners] = new PropertyDescriptor ( "keyListeners", JObjectResourceList.class, "getKeyListeners", null );
            properties[PROPERTY_lastVisibleIndex] = new PropertyDescriptor ( "lastVisibleIndex", JObjectResourceList.class, "getLastVisibleIndex", null );
            properties[PROPERTY_layout] = new PropertyDescriptor ( "layout", JObjectResourceList.class, "getLayout", "setLayout" );
            properties[PROPERTY_layoutOrientation] = new PropertyDescriptor ( "layoutOrientation", JObjectResourceList.class, "getLayoutOrientation", "setLayoutOrientation" );
            properties[PROPERTY_leadSelectionIndex] = new PropertyDescriptor ( "leadSelectionIndex", JObjectResourceList.class, "getLeadSelectionIndex", null );
            properties[PROPERTY_lightweight] = new PropertyDescriptor ( "lightweight", JObjectResourceList.class, "isLightweight", null );
            properties[PROPERTY_listData] = new PropertyDescriptor ( "listData", JObjectResourceList.class, null, "setListData" );
            properties[PROPERTY_listSelectionListeners] = new PropertyDescriptor ( "listSelectionListeners", JObjectResourceList.class, "getListSelectionListeners", null );
            properties[PROPERTY_locale] = new PropertyDescriptor ( "locale", JObjectResourceList.class, "getLocale", "setLocale" );
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor ( "locationOnScreen", JObjectResourceList.class, "getLocationOnScreen", null );
            properties[PROPERTY_managingFocus] = new PropertyDescriptor ( "managingFocus", JObjectResourceList.class, "isManagingFocus", null );
            properties[PROPERTY_maximumSize] = new PropertyDescriptor ( "maximumSize", JObjectResourceList.class, "getMaximumSize", "setMaximumSize" );
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor ( "maximumSizeSet", JObjectResourceList.class, "isMaximumSizeSet", null );
            properties[PROPERTY_maxSelectionIndex] = new PropertyDescriptor ( "maxSelectionIndex", JObjectResourceList.class, "getMaxSelectionIndex", null );
            properties[PROPERTY_minimumSize] = new PropertyDescriptor ( "minimumSize", JObjectResourceList.class, "getMinimumSize", "setMinimumSize" );
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor ( "minimumSizeSet", JObjectResourceList.class, "isMinimumSizeSet", null );
            properties[PROPERTY_minSelectionIndex] = new PropertyDescriptor ( "minSelectionIndex", JObjectResourceList.class, "getMinSelectionIndex", null );
            properties[PROPERTY_model] = new PropertyDescriptor ( "model", JObjectResourceList.class, "getModel", "setModel" );
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor ( "mouseListeners", JObjectResourceList.class, "getMouseListeners", null );
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor ( "mouseMotionListeners", JObjectResourceList.class, "getMouseMotionListeners", null );
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor ( "mouseWheelListeners", JObjectResourceList.class, "getMouseWheelListeners", null );
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", JObjectResourceList.class, "getName", "setName" );
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor ( "nextFocusableComponent", JObjectResourceList.class, "getNextFocusableComponent", "setNextFocusableComponent" );
            properties[PROPERTY_opaque] = new PropertyDescriptor ( "opaque", JObjectResourceList.class, "isOpaque", "setOpaque" );
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor ( "optimizedDrawingEnabled", JObjectResourceList.class, "isOptimizedDrawingEnabled", null );
            properties[PROPERTY_paintingTile] = new PropertyDescriptor ( "paintingTile", JObjectResourceList.class, "isPaintingTile", null );
            properties[PROPERTY_parent] = new PropertyDescriptor ( "parent", JObjectResourceList.class, "getParent", null );
            properties[PROPERTY_peer] = new PropertyDescriptor ( "peer", JObjectResourceList.class, "getPeer", null );
            properties[PROPERTY_preferredScrollableViewportSize] = new PropertyDescriptor ( "preferredScrollableViewportSize", JObjectResourceList.class, "getPreferredScrollableViewportSize", null );
            properties[PROPERTY_preferredSize] = new PropertyDescriptor ( "preferredSize", JObjectResourceList.class, "getPreferredSize", "setPreferredSize" );
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor ( "preferredSizeSet", JObjectResourceList.class, "isPreferredSizeSet", null );
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor ( "propertyChangeListeners", JObjectResourceList.class, "getPropertyChangeListeners", null );
            properties[PROPERTY_prototypeCellValue] = new PropertyDescriptor ( "prototypeCellValue", JObjectResourceList.class, "getPrototypeCellValue", "setPrototypeCellValue" );
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor ( "registeredKeyStrokes", JObjectResourceList.class, "getRegisteredKeyStrokes", null );
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor ( "requestFocusEnabled", JObjectResourceList.class, "isRequestFocusEnabled", "setRequestFocusEnabled" );
            properties[PROPERTY_rootPane] = new PropertyDescriptor ( "rootPane", JObjectResourceList.class, "getRootPane", null );
            properties[PROPERTY_scrollableTracksViewportHeight] = new PropertyDescriptor ( "scrollableTracksViewportHeight", JObjectResourceList.class, "getScrollableTracksViewportHeight", null );
            properties[PROPERTY_scrollableTracksViewportWidth] = new PropertyDescriptor ( "scrollableTracksViewportWidth", JObjectResourceList.class, "getScrollableTracksViewportWidth", null );
            properties[PROPERTY_selectedIndex] = new PropertyDescriptor ( "selectedIndex", JObjectResourceList.class, "getSelectedIndex", "setSelectedIndex" );
            properties[PROPERTY_selectedIndices] = new PropertyDescriptor ( "selectedIndices", JObjectResourceList.class, "getSelectedIndices", "setSelectedIndices" );
            properties[PROPERTY_selectedValue] = new PropertyDescriptor ( "selectedValue", JObjectResourceList.class, "getSelectedValue", null );
            properties[PROPERTY_selectedValues] = new PropertyDescriptor ( "selectedValues", JObjectResourceList.class, "getSelectedValues", null );
            properties[PROPERTY_selectionBackground] = new PropertyDescriptor ( "selectionBackground", JObjectResourceList.class, "getSelectionBackground", "setSelectionBackground" );
            properties[PROPERTY_selectionEmpty] = new PropertyDescriptor ( "selectionEmpty", JObjectResourceList.class, "isSelectionEmpty", null );
            properties[PROPERTY_selectionForeground] = new PropertyDescriptor ( "selectionForeground", JObjectResourceList.class, "getSelectionForeground", "setSelectionForeground" );
            properties[PROPERTY_selectionInterval] = new IndexedPropertyDescriptor ( "selectionInterval", JObjectResourceList.class, null, null, null, "setSelectionInterval" );
            properties[PROPERTY_selectionMode] = new PropertyDescriptor ( "selectionMode", JObjectResourceList.class, "getSelectionMode", "setSelectionMode" );
            properties[PROPERTY_selectionModel] = new PropertyDescriptor ( "selectionModel", JObjectResourceList.class, "getSelectionModel", "setSelectionModel" );
            properties[PROPERTY_showing] = new PropertyDescriptor ( "showing", JObjectResourceList.class, "isShowing", null );
            properties[PROPERTY_toolkit] = new PropertyDescriptor ( "toolkit", JObjectResourceList.class, "getToolkit", null );
            properties[PROPERTY_toolTipText] = new PropertyDescriptor ( "toolTipText", JObjectResourceList.class, "getToolTipText", "setToolTipText" );
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor ( "topLevelAncestor", JObjectResourceList.class, "getTopLevelAncestor", null );
            properties[PROPERTY_transferHandler] = new PropertyDescriptor ( "transferHandler", JObjectResourceList.class, "getTransferHandler", "setTransferHandler" );
            properties[PROPERTY_treeLock] = new PropertyDescriptor ( "treeLock", JObjectResourceList.class, "getTreeLock", null );
            properties[PROPERTY_UI] = new PropertyDescriptor ( "UI", JObjectResourceList.class, "getUI", "setUI" );
            properties[PROPERTY_UIClassID] = new PropertyDescriptor ( "UIClassID", JObjectResourceList.class, "getUIClassID", null );
            properties[PROPERTY_valid] = new PropertyDescriptor ( "valid", JObjectResourceList.class, "isValid", null );
            properties[PROPERTY_validateRoot] = new PropertyDescriptor ( "validateRoot", JObjectResourceList.class, "isValidateRoot", null );
            properties[PROPERTY_valueIsAdjusting] = new PropertyDescriptor ( "valueIsAdjusting", JObjectResourceList.class, "getValueIsAdjusting", "setValueIsAdjusting" );
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor ( "verifyInputWhenFocusTarget", JObjectResourceList.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget" );
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor ( "vetoableChangeListeners", JObjectResourceList.class, "getVetoableChangeListeners", null );
            properties[PROPERTY_visible] = new PropertyDescriptor ( "visible", JObjectResourceList.class, "isVisible", "setVisible" );
            properties[PROPERTY_visibleRect] = new PropertyDescriptor ( "visibleRect", JObjectResourceList.class, "getVisibleRect", null );
            properties[PROPERTY_visibleRowCount] = new PropertyDescriptor ( "visibleRowCount", JObjectResourceList.class, "getVisibleRowCount", "setVisibleRowCount" );
            properties[PROPERTY_width] = new PropertyDescriptor ( "width", JObjectResourceList.class, "getWidth", null );
            properties[PROPERTY_x] = new PropertyDescriptor ( "x", JObjectResourceList.class, "getX", null );
            properties[PROPERTY_y] = new PropertyDescriptor ( "y", JObjectResourceList.class, "getY", null );
        }
        catch( IntrospectionException e) {}//GEN-HEADEREND:Properties
		
		// Here you can add code for customizing the properties array.
		
        return properties;         }//GEN-LAST:Properties
	
    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_ancestorListener = 0;
    private static final int EVENT_componentListener = 1;
    private static final int EVENT_containerListener = 2;
    private static final int EVENT_focusListener = 3;
    private static final int EVENT_hierarchyBoundsListener = 4;
    private static final int EVENT_hierarchyListener = 5;
    private static final int EVENT_inputMethodListener = 6;
    private static final int EVENT_keyListener = 7;
    private static final int EVENT_listSelectionListener = 8;
    private static final int EVENT_mouseListener = 9;
    private static final int EVENT_mouseMotionListener = 10;
    private static final int EVENT_mouseWheelListener = 11;
    private static final int EVENT_propertyChangeListener = 12;
    private static final int EVENT_vetoableChangeListener = 13;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[14];
    
            try {
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] {"ancestorAdded", "ancestorMoved", "ancestorRemoved"}, "addAncestorListener", "removeAncestorListener" );
            eventSets[EVENT_componentListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "componentListener", java.awt.event.ComponentListener.class, new String[] {"componentHidden", "componentMoved", "componentResized", "componentShown"}, "addComponentListener", "removeComponentListener" );
            eventSets[EVENT_containerListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "containerListener", java.awt.event.ContainerListener.class, new String[] {"componentAdded", "componentRemoved"}, "addContainerListener", "removeContainerListener" );
            eventSets[EVENT_focusListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "focusListener", java.awt.event.FocusListener.class, new String[] {"focusGained", "focusLost"}, "addFocusListener", "removeFocusListener" );
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] {"ancestorMoved", "ancestorResized"}, "addHierarchyBoundsListener", "removeHierarchyBoundsListener" );
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] {"hierarchyChanged"}, "addHierarchyListener", "removeHierarchyListener" );
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] {"caretPositionChanged", "inputMethodTextChanged"}, "addInputMethodListener", "removeInputMethodListener" );
            eventSets[EVENT_keyListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "keyListener", java.awt.event.KeyListener.class, new String[] {"keyPressed", "keyReleased", "keyTyped"}, "addKeyListener", "removeKeyListener" );
            eventSets[EVENT_listSelectionListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "listSelectionListener", javax.swing.event.ListSelectionListener.class, new String[] {"valueChanged"}, "addListSelectionListener", "removeListSelectionListener" );
            eventSets[EVENT_mouseListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "mouseListener", java.awt.event.MouseListener.class, new String[] {"mouseClicked", "mouseEntered", "mouseExited", "mousePressed", "mouseReleased"}, "addMouseListener", "removeMouseListener" );
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] {"mouseDragged", "mouseMoved"}, "addMouseMotionListener", "removeMouseMotionListener" );
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] {"mouseWheelMoved"}, "addMouseWheelListener", "removeMouseWheelListener" );
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" );
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] {"vetoableChange"}, "addVetoableChangeListener", "removeVetoableChangeListener" );
        }
        catch( IntrospectionException e) {}//GEN-HEADEREND:Events
		
		// Here you can add code for customizing the event sets array.
		
        return eventSets;         }//GEN-LAST:Events
	
    // Method identifiers //GEN-FIRST:Methods
    private static final int METHOD_action0 = 0;
    private static final int METHOD_add1 = 1;
    private static final int METHOD_addNotify2 = 2;
    private static final int METHOD_addPropertyChangeListener3 = 3;
    private static final int METHOD_addSelectionInterval4 = 4;
    private static final int METHOD_applyComponentOrientation5 = 5;
    private static final int METHOD_areFocusTraversalKeysSet6 = 6;
    private static final int METHOD_bounds7 = 7;
    private static final int METHOD_checkImage8 = 8;
    private static final int METHOD_clearSelection9 = 9;
    private static final int METHOD_computeVisibleRect10 = 10;
    private static final int METHOD_contains11 = 11;
    private static final int METHOD_countComponents12 = 12;
    private static final int METHOD_createImage13 = 13;
    private static final int METHOD_createToolTip14 = 14;
    private static final int METHOD_createVolatileImage15 = 15;
    private static final int METHOD_deliverEvent16 = 16;
    private static final int METHOD_disable17 = 17;
    private static final int METHOD_dispatchEvent18 = 18;
    private static final int METHOD_doLayout19 = 19;
    private static final int METHOD_enable20 = 20;
    private static final int METHOD_enableInputMethods21 = 21;
    private static final int METHOD_ensureIndexIsVisible22 = 22;
    private static final int METHOD_findComponentAt23 = 23;
    private static final int METHOD_firePropertyChange24 = 24;
    private static final int METHOD_getActionForKeyStroke25 = 25;
    private static final int METHOD_getBounds26 = 26;
    private static final int METHOD_getCellBounds27 = 27;
    private static final int METHOD_getClientProperty28 = 28;
    private static final int METHOD_getComponentAt29 = 29;
    private static final int METHOD_getConditionForKeyStroke30 = 30;
    private static final int METHOD_getDefaultLocale31 = 31;
    private static final int METHOD_getFontMetrics32 = 32;
    private static final int METHOD_getInputMap33 = 33;
    private static final int METHOD_getInsets34 = 34;
    private static final int METHOD_getListeners35 = 35;
    private static final int METHOD_getLocation36 = 36;
    private static final int METHOD_getNextMatch37 = 37;
    private static final int METHOD_getPropertyChangeListeners38 = 38;
    private static final int METHOD_getScrollableBlockIncrement39 = 39;
    private static final int METHOD_getScrollableUnitIncrement40 = 40;
    private static final int METHOD_getSize41 = 41;
    private static final int METHOD_getToolTipLocation42 = 42;
    private static final int METHOD_getToolTipText43 = 43;
    private static final int METHOD_gotFocus44 = 44;
    private static final int METHOD_grabFocus45 = 45;
    private static final int METHOD_handleEvent46 = 46;
    private static final int METHOD_hasFocus47 = 47;
    private static final int METHOD_hide48 = 48;
    private static final int METHOD_imageUpdate49 = 49;
    private static final int METHOD_indexToLocation50 = 50;
    private static final int METHOD_insets51 = 51;
    private static final int METHOD_inside52 = 52;
    private static final int METHOD_invalidate53 = 53;
    private static final int METHOD_isAncestorOf54 = 54;
    private static final int METHOD_isFocusCycleRoot55 = 55;
    private static final int METHOD_isLightweightComponent56 = 56;
    private static final int METHOD_isSelectedIndex57 = 57;
    private static final int METHOD_keyDown58 = 58;
    private static final int METHOD_keyUp59 = 59;
    private static final int METHOD_layout60 = 60;
    private static final int METHOD_list61 = 61;
    private static final int METHOD_locate62 = 62;
    private static final int METHOD_location63 = 63;
    private static final int METHOD_locationToIndex64 = 64;
    private static final int METHOD_lostFocus65 = 65;
    private static final int METHOD_minimumSize66 = 66;
    private static final int METHOD_mouseDown67 = 67;
    private static final int METHOD_mouseDrag68 = 68;
    private static final int METHOD_mouseEnter69 = 69;
    private static final int METHOD_mouseExit70 = 70;
    private static final int METHOD_mouseMove71 = 71;
    private static final int METHOD_mouseUp72 = 72;
    private static final int METHOD_move73 = 73;
    private static final int METHOD_nextFocus74 = 74;
    private static final int METHOD_paint75 = 75;
    private static final int METHOD_paintAll76 = 76;
    private static final int METHOD_paintComponents77 = 77;
    private static final int METHOD_paintImmediately78 = 78;
    private static final int METHOD_postEvent79 = 79;
    private static final int METHOD_preferredSize80 = 80;
    private static final int METHOD_prepareImage81 = 81;
    private static final int METHOD_print82 = 82;
    private static final int METHOD_printAll83 = 83;
    private static final int METHOD_printComponents84 = 84;
    private static final int METHOD_putClientProperty85 = 85;
    private static final int METHOD_registerKeyboardAction86 = 86;
    private static final int METHOD_remove87 = 87;
    private static final int METHOD_removeAll88 = 88;
    private static final int METHOD_removeNotify89 = 89;
    private static final int METHOD_removePropertyChangeListener90 = 90;
    private static final int METHOD_removeSelectionInterval91 = 91;
    private static final int METHOD_repaint92 = 92;
    private static final int METHOD_requestDefaultFocus93 = 93;
    private static final int METHOD_requestFocus94 = 94;
    private static final int METHOD_requestFocusInWindow95 = 95;
    private static final int METHOD_resetKeyboardActions96 = 96;
    private static final int METHOD_reshape97 = 97;
    private static final int METHOD_resize98 = 98;
    private static final int METHOD_revalidate99 = 99;
    private static final int METHOD_scrollRectToVisible100 = 100;
    private static final int METHOD_setBounds101 = 101;
    private static final int METHOD_setDefaultLocale102 = 102;
    private static final int METHOD_setInputMap103 = 103;
    private static final int METHOD_setListData104 = 104;
    private static final int METHOD_setLocation105 = 105;
    private static final int METHOD_setSelectedValue106 = 106;
    private static final int METHOD_setSize107 = 107;
    private static final int METHOD_show108 = 108;
    private static final int METHOD_size109 = 109;
    private static final int METHOD_toString110 = 110;
    private static final int METHOD_transferFocus111 = 111;
    private static final int METHOD_transferFocusBackward112 = 112;
    private static final int METHOD_transferFocusDownCycle113 = 113;
    private static final int METHOD_transferFocusUpCycle114 = 114;
    private static final int METHOD_unregisterKeyboardAction115 = 115;
    private static final int METHOD_update116 = 116;
    private static final int METHOD_updateUI117 = 117;
    private static final int METHOD_validate118 = 118;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[119];
    
        try {
            methods[METHOD_action0] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("action", new Class[] {java.awt.Event.class, java.lang.Object.class}));
            methods[METHOD_action0].setDisplayName ( "" );
            methods[METHOD_add1] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("add", new Class[] {java.awt.Component.class}));
            methods[METHOD_add1].setDisplayName ( "" );
            methods[METHOD_addNotify2] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("addNotify", new Class[] {}));
            methods[METHOD_addNotify2].setDisplayName ( "" );
            methods[METHOD_addPropertyChangeListener3] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("addPropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class}));
            methods[METHOD_addPropertyChangeListener3].setDisplayName ( "" );
            methods[METHOD_addSelectionInterval4] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("addSelectionInterval", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_addSelectionInterval4].setDisplayName ( "" );
            methods[METHOD_applyComponentOrientation5] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("applyComponentOrientation", new Class[] {java.awt.ComponentOrientation.class}));
            methods[METHOD_applyComponentOrientation5].setDisplayName ( "" );
            methods[METHOD_areFocusTraversalKeysSet6] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("areFocusTraversalKeysSet", new Class[] {Integer.TYPE}));
            methods[METHOD_areFocusTraversalKeysSet6].setDisplayName ( "" );
            methods[METHOD_bounds7] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("bounds", new Class[] {}));
            methods[METHOD_bounds7].setDisplayName ( "" );
            methods[METHOD_checkImage8] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("checkImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class}));
            methods[METHOD_checkImage8].setDisplayName ( "" );
            methods[METHOD_clearSelection9] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("clearSelection", new Class[] {}));
            methods[METHOD_clearSelection9].setDisplayName ( "" );
            methods[METHOD_computeVisibleRect10] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("computeVisibleRect", new Class[] {java.awt.Rectangle.class}));
            methods[METHOD_computeVisibleRect10].setDisplayName ( "" );
            methods[METHOD_contains11] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("contains", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_contains11].setDisplayName ( "" );
            methods[METHOD_countComponents12] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("countComponents", new Class[] {}));
            methods[METHOD_countComponents12].setDisplayName ( "" );
            methods[METHOD_createImage13] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("createImage", new Class[] {java.awt.image.ImageProducer.class}));
            methods[METHOD_createImage13].setDisplayName ( "" );
            methods[METHOD_createToolTip14] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("createToolTip", new Class[] {}));
            methods[METHOD_createToolTip14].setDisplayName ( "" );
            methods[METHOD_createVolatileImage15] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("createVolatileImage", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_createVolatileImage15].setDisplayName ( "" );
            methods[METHOD_deliverEvent16] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("deliverEvent", new Class[] {java.awt.Event.class}));
            methods[METHOD_deliverEvent16].setDisplayName ( "" );
            methods[METHOD_disable17] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("disable", new Class[] {}));
            methods[METHOD_disable17].setDisplayName ( "" );
            methods[METHOD_dispatchEvent18] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("dispatchEvent", new Class[] {java.awt.AWTEvent.class}));
            methods[METHOD_dispatchEvent18].setDisplayName ( "" );
            methods[METHOD_doLayout19] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("doLayout", new Class[] {}));
            methods[METHOD_doLayout19].setDisplayName ( "" );
            methods[METHOD_enable20] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("enable", new Class[] {}));
            methods[METHOD_enable20].setDisplayName ( "" );
            methods[METHOD_enableInputMethods21] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("enableInputMethods", new Class[] {Boolean.TYPE}));
            methods[METHOD_enableInputMethods21].setDisplayName ( "" );
            methods[METHOD_ensureIndexIsVisible22] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("ensureIndexIsVisible", new Class[] {Integer.TYPE}));
            methods[METHOD_ensureIndexIsVisible22].setDisplayName ( "" );
            methods[METHOD_findComponentAt23] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("findComponentAt", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_findComponentAt23].setDisplayName ( "" );
            methods[METHOD_firePropertyChange24] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, Byte.TYPE, Byte.TYPE}));
            methods[METHOD_firePropertyChange24].setDisplayName ( "" );
            methods[METHOD_getActionForKeyStroke25] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getActionForKeyStroke", new Class[] {javax.swing.KeyStroke.class}));
            methods[METHOD_getActionForKeyStroke25].setDisplayName ( "" );
            methods[METHOD_getBounds26] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getBounds", new Class[] {java.awt.Rectangle.class}));
            methods[METHOD_getBounds26].setDisplayName ( "" );
            methods[METHOD_getCellBounds27] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getCellBounds", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_getCellBounds27].setDisplayName ( "" );
            methods[METHOD_getClientProperty28] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getClientProperty", new Class[] {java.lang.Object.class}));
            methods[METHOD_getClientProperty28].setDisplayName ( "" );
            methods[METHOD_getComponentAt29] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getComponentAt", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_getComponentAt29].setDisplayName ( "" );
            methods[METHOD_getConditionForKeyStroke30] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getConditionForKeyStroke", new Class[] {javax.swing.KeyStroke.class}));
            methods[METHOD_getConditionForKeyStroke30].setDisplayName ( "" );
            methods[METHOD_getDefaultLocale31] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getDefaultLocale", new Class[] {}));
            methods[METHOD_getDefaultLocale31].setDisplayName ( "" );
            methods[METHOD_getFontMetrics32] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getFontMetrics", new Class[] {java.awt.Font.class}));
            methods[METHOD_getFontMetrics32].setDisplayName ( "" );
            methods[METHOD_getInputMap33] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getInputMap", new Class[] {Integer.TYPE}));
            methods[METHOD_getInputMap33].setDisplayName ( "" );
            methods[METHOD_getInsets34] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getInsets", new Class[] {java.awt.Insets.class}));
            methods[METHOD_getInsets34].setDisplayName ( "" );
            methods[METHOD_getListeners35] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getListeners", new Class[] {java.lang.Class.class}));
            methods[METHOD_getListeners35].setDisplayName ( "" );
            methods[METHOD_getLocation36] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getLocation", new Class[] {java.awt.Point.class}));
            methods[METHOD_getLocation36].setDisplayName ( "" );
            methods[METHOD_getNextMatch37] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getNextMatch", new Class[] {java.lang.String.class, Integer.TYPE, javax.swing.text.Position.Bias.class}));
            methods[METHOD_getNextMatch37].setDisplayName ( "" );
            methods[METHOD_getPropertyChangeListeners38] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getPropertyChangeListeners", new Class[] {java.lang.String.class}));
            methods[METHOD_getPropertyChangeListeners38].setDisplayName ( "" );
            methods[METHOD_getScrollableBlockIncrement39] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getScrollableBlockIncrement", new Class[] {java.awt.Rectangle.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_getScrollableBlockIncrement39].setDisplayName ( "" );
            methods[METHOD_getScrollableUnitIncrement40] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getScrollableUnitIncrement", new Class[] {java.awt.Rectangle.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_getScrollableUnitIncrement40].setDisplayName ( "" );
            methods[METHOD_getSize41] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getSize", new Class[] {java.awt.Dimension.class}));
            methods[METHOD_getSize41].setDisplayName ( "" );
            methods[METHOD_getToolTipLocation42] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getToolTipLocation", new Class[] {java.awt.event.MouseEvent.class}));
            methods[METHOD_getToolTipLocation42].setDisplayName ( "" );
            methods[METHOD_getToolTipText43] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("getToolTipText", new Class[] {java.awt.event.MouseEvent.class}));
            methods[METHOD_getToolTipText43].setDisplayName ( "" );
            methods[METHOD_gotFocus44] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("gotFocus", new Class[] {java.awt.Event.class, java.lang.Object.class}));
            methods[METHOD_gotFocus44].setDisplayName ( "" );
            methods[METHOD_grabFocus45] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("grabFocus", new Class[] {}));
            methods[METHOD_grabFocus45].setDisplayName ( "" );
            methods[METHOD_handleEvent46] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("handleEvent", new Class[] {java.awt.Event.class}));
            methods[METHOD_handleEvent46].setDisplayName ( "" );
            methods[METHOD_hasFocus47] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("hasFocus", new Class[] {}));
            methods[METHOD_hasFocus47].setDisplayName ( "" );
            methods[METHOD_hide48] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("hide", new Class[] {}));
            methods[METHOD_hide48].setDisplayName ( "" );
            methods[METHOD_imageUpdate49] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("imageUpdate", new Class[] {java.awt.Image.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_imageUpdate49].setDisplayName ( "" );
            methods[METHOD_indexToLocation50] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("indexToLocation", new Class[] {Integer.TYPE}));
            methods[METHOD_indexToLocation50].setDisplayName ( "" );
            methods[METHOD_insets51] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("insets", new Class[] {}));
            methods[METHOD_insets51].setDisplayName ( "" );
            methods[METHOD_inside52] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("inside", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_inside52].setDisplayName ( "" );
            methods[METHOD_invalidate53] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("invalidate", new Class[] {}));
            methods[METHOD_invalidate53].setDisplayName ( "" );
            methods[METHOD_isAncestorOf54] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("isAncestorOf", new Class[] {java.awt.Component.class}));
            methods[METHOD_isAncestorOf54].setDisplayName ( "" );
            methods[METHOD_isFocusCycleRoot55] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("isFocusCycleRoot", new Class[] {java.awt.Container.class}));
            methods[METHOD_isFocusCycleRoot55].setDisplayName ( "" );
            methods[METHOD_isLightweightComponent56] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("isLightweightComponent", new Class[] {java.awt.Component.class}));
            methods[METHOD_isLightweightComponent56].setDisplayName ( "" );
            methods[METHOD_isSelectedIndex57] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("isSelectedIndex", new Class[] {Integer.TYPE}));
            methods[METHOD_isSelectedIndex57].setDisplayName ( "" );
            methods[METHOD_keyDown58] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("keyDown", new Class[] {java.awt.Event.class, Integer.TYPE}));
            methods[METHOD_keyDown58].setDisplayName ( "" );
            methods[METHOD_keyUp59] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("keyUp", new Class[] {java.awt.Event.class, Integer.TYPE}));
            methods[METHOD_keyUp59].setDisplayName ( "" );
            methods[METHOD_layout60] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("layout", new Class[] {}));
            methods[METHOD_layout60].setDisplayName ( "" );
            methods[METHOD_list61] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("list", new Class[] {java.io.PrintStream.class, Integer.TYPE}));
            methods[METHOD_list61].setDisplayName ( "" );
            methods[METHOD_locate62] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("locate", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_locate62].setDisplayName ( "" );
            methods[METHOD_location63] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("location", new Class[] {}));
            methods[METHOD_location63].setDisplayName ( "" );
            methods[METHOD_locationToIndex64] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("locationToIndex", new Class[] {java.awt.Point.class}));
            methods[METHOD_locationToIndex64].setDisplayName ( "" );
            methods[METHOD_lostFocus65] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("lostFocus", new Class[] {java.awt.Event.class, java.lang.Object.class}));
            methods[METHOD_lostFocus65].setDisplayName ( "" );
            methods[METHOD_minimumSize66] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("minimumSize", new Class[] {}));
            methods[METHOD_minimumSize66].setDisplayName ( "" );
            methods[METHOD_mouseDown67] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("mouseDown", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseDown67].setDisplayName ( "" );
            methods[METHOD_mouseDrag68] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("mouseDrag", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseDrag68].setDisplayName ( "" );
            methods[METHOD_mouseEnter69] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("mouseEnter", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseEnter69].setDisplayName ( "" );
            methods[METHOD_mouseExit70] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("mouseExit", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseExit70].setDisplayName ( "" );
            methods[METHOD_mouseMove71] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("mouseMove", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseMove71].setDisplayName ( "" );
            methods[METHOD_mouseUp72] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("mouseUp", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseUp72].setDisplayName ( "" );
            methods[METHOD_move73] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("move", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_move73].setDisplayName ( "" );
            methods[METHOD_nextFocus74] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("nextFocus", new Class[] {}));
            methods[METHOD_nextFocus74].setDisplayName ( "" );
            methods[METHOD_paint75] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("paint", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_paint75].setDisplayName ( "" );
            methods[METHOD_paintAll76] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("paintAll", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_paintAll76].setDisplayName ( "" );
            methods[METHOD_paintComponents77] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("paintComponents", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_paintComponents77].setDisplayName ( "" );
            methods[METHOD_paintImmediately78] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("paintImmediately", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_paintImmediately78].setDisplayName ( "" );
            methods[METHOD_postEvent79] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("postEvent", new Class[] {java.awt.Event.class}));
            methods[METHOD_postEvent79].setDisplayName ( "" );
            methods[METHOD_preferredSize80] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("preferredSize", new Class[] {}));
            methods[METHOD_preferredSize80].setDisplayName ( "" );
            methods[METHOD_prepareImage81] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("prepareImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class}));
            methods[METHOD_prepareImage81].setDisplayName ( "" );
            methods[METHOD_print82] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("print", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_print82].setDisplayName ( "" );
            methods[METHOD_printAll83] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("printAll", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_printAll83].setDisplayName ( "" );
            methods[METHOD_printComponents84] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("printComponents", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_printComponents84].setDisplayName ( "" );
            methods[METHOD_putClientProperty85] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("putClientProperty", new Class[] {java.lang.Object.class, java.lang.Object.class}));
            methods[METHOD_putClientProperty85].setDisplayName ( "" );
            methods[METHOD_registerKeyboardAction86] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("registerKeyboardAction", new Class[] {java.awt.event.ActionListener.class, java.lang.String.class, javax.swing.KeyStroke.class, Integer.TYPE}));
            methods[METHOD_registerKeyboardAction86].setDisplayName ( "" );
            methods[METHOD_remove87] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("remove", new Class[] {Integer.TYPE}));
            methods[METHOD_remove87].setDisplayName ( "" );
            methods[METHOD_removeAll88] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("removeAll", new Class[] {}));
            methods[METHOD_removeAll88].setDisplayName ( "" );
            methods[METHOD_removeNotify89] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("removeNotify", new Class[] {}));
            methods[METHOD_removeNotify89].setDisplayName ( "" );
            methods[METHOD_removePropertyChangeListener90] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("removePropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class}));
            methods[METHOD_removePropertyChangeListener90].setDisplayName ( "" );
            methods[METHOD_removeSelectionInterval91] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("removeSelectionInterval", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_removeSelectionInterval91].setDisplayName ( "" );
            methods[METHOD_repaint92] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("repaint", new Class[] {Long.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_repaint92].setDisplayName ( "" );
            methods[METHOD_requestDefaultFocus93] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("requestDefaultFocus", new Class[] {}));
            methods[METHOD_requestDefaultFocus93].setDisplayName ( "" );
            methods[METHOD_requestFocus94] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("requestFocus", new Class[] {}));
            methods[METHOD_requestFocus94].setDisplayName ( "" );
            methods[METHOD_requestFocusInWindow95] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("requestFocusInWindow", new Class[] {}));
            methods[METHOD_requestFocusInWindow95].setDisplayName ( "" );
            methods[METHOD_resetKeyboardActions96] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("resetKeyboardActions", new Class[] {}));
            methods[METHOD_resetKeyboardActions96].setDisplayName ( "" );
            methods[METHOD_reshape97] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("reshape", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_reshape97].setDisplayName ( "" );
            methods[METHOD_resize98] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("resize", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_resize98].setDisplayName ( "" );
            methods[METHOD_revalidate99] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("revalidate", new Class[] {}));
            methods[METHOD_revalidate99].setDisplayName ( "" );
            methods[METHOD_scrollRectToVisible100] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("scrollRectToVisible", new Class[] {java.awt.Rectangle.class}));
            methods[METHOD_scrollRectToVisible100].setDisplayName ( "" );
            methods[METHOD_setBounds101] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("setBounds", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_setBounds101].setDisplayName ( "" );
            methods[METHOD_setDefaultLocale102] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("setDefaultLocale", new Class[] {java.util.Locale.class}));
            methods[METHOD_setDefaultLocale102].setDisplayName ( "" );
            methods[METHOD_setInputMap103] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("setInputMap", new Class[] {Integer.TYPE, javax.swing.InputMap.class}));
            methods[METHOD_setInputMap103].setDisplayName ( "" );
            methods[METHOD_setListData104] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("setListData", new Class[] {Class.forName("[Ljava.lang.Object;")}));
            methods[METHOD_setListData104].setDisplayName ( "" );
            methods[METHOD_setLocation105] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("setLocation", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_setLocation105].setDisplayName ( "" );
            methods[METHOD_setSelectedValue106] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("setSelectedValue", new Class[] {java.lang.Object.class, Boolean.TYPE}));
            methods[METHOD_setSelectedValue106].setDisplayName ( "" );
            methods[METHOD_setSize107] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("setSize", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_setSize107].setDisplayName ( "" );
            methods[METHOD_show108] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("show", new Class[] {}));
            methods[METHOD_show108].setDisplayName ( "" );
            methods[METHOD_size109] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("size", new Class[] {}));
            methods[METHOD_size109].setDisplayName ( "" );
            methods[METHOD_toString110] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("toString", new Class[] {}));
            methods[METHOD_toString110].setDisplayName ( "" );
            methods[METHOD_transferFocus111] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("transferFocus", new Class[] {}));
            methods[METHOD_transferFocus111].setDisplayName ( "" );
            methods[METHOD_transferFocusBackward112] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("transferFocusBackward", new Class[] {}));
            methods[METHOD_transferFocusBackward112].setDisplayName ( "" );
            methods[METHOD_transferFocusDownCycle113] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("transferFocusDownCycle", new Class[] {}));
            methods[METHOD_transferFocusDownCycle113].setDisplayName ( "" );
            methods[METHOD_transferFocusUpCycle114] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("transferFocusUpCycle", new Class[] {}));
            methods[METHOD_transferFocusUpCycle114].setDisplayName ( "" );
            methods[METHOD_unregisterKeyboardAction115] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("unregisterKeyboardAction", new Class[] {javax.swing.KeyStroke.class}));
            methods[METHOD_unregisterKeyboardAction115].setDisplayName ( "" );
            methods[METHOD_update116] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("update", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_update116].setDisplayName ( "" );
            methods[METHOD_updateUI117] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("updateUI", new Class[] {}));
            methods[METHOD_updateUI117].setDisplayName ( "" );
            methods[METHOD_validate118] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceList.class.getMethod("validate", new Class[] {}));
            methods[METHOD_validate118].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods
		
		// Here you can add code for customizing the methods array.
		
        return methods;         }//GEN-LAST:Methods
	
	private static java.awt.Image iconColor16 = null; //GEN-BEGIN:IconsDef
	private static java.awt.Image iconColor32 = null;
	private static java.awt.Image iconMono16 = null;
	private static java.awt.Image iconMono32 = null; //GEN-END:IconsDef
    private static String iconNameC16 = "/javax/swing/beaninfo/images/JListColor16.gif";//GEN-BEGIN:Icons
    private static String iconNameC32 = "/javax/swing/beaninfo/images/JListColor32.gif";
    private static String iconNameM16 = "/javax/swing/beaninfo/images/JListMono16.gif";
    private static String iconNameM32 = "/javax/swing/beaninfo/images/JListMono32.gif";//GEN-END:Icons
	
    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx
	
	
 //GEN-FIRST:Superclass
	
	// Here you can add code for customizing the Superclass BeanInfo.
	
 //GEN-LAST:Superclass
	
	/**
	 * Gets the bean's <code>BeanDescriptor</code>s.
	 *
	 * @return BeanDescriptor describing the editable
	 * properties of this bean.  May return null if the
	 * information should be obtained by automatic analysis.
	 */
	public BeanDescriptor getBeanDescriptor() {
		return getBdescriptor();
	}
	
	/**
	 * Gets the bean's <code>PropertyDescriptor</code>s.
	 *
	 * @return An array of PropertyDescriptors describing the editable
	 * properties supported by this bean.  May return null if the
	 * information should be obtained by automatic analysis.
	 * <p>
	 * If a property is indexed, then its entry in the result array will
	 * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
	 * A client of getPropertyDescriptors can use "instanceof" to check
	 * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		return getPdescriptor();
	}
	
	/**
	 * Gets the bean's <code>EventSetDescriptor</code>s.
	 *
	 * @return  An array of EventSetDescriptors describing the kinds of
	 * events fired by this bean.  May return null if the information
	 * should be obtained by automatic analysis.
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return getEdescriptor();
	}
	
	/**
	 * Gets the bean's <code>MethodDescriptor</code>s.
	 *
	 * @return  An array of MethodDescriptors describing the methods
	 * implemented by this bean.  May return null if the information
	 * should be obtained by automatic analysis.
	 */
	public MethodDescriptor[] getMethodDescriptors() {
		return getMdescriptor();
	}
	
	/**
	 * A bean may have a "default" property that is the property that will
	 * mostly commonly be initially chosen for update by human's who are
	 * customizing the bean.
	 * @return  Index of default property in the PropertyDescriptor array
	 * 		returned by getPropertyDescriptors.
	 * <P>	Returns -1 if there is no default property.
	 */
	public int getDefaultPropertyIndex() {
		return defaultPropertyIndex;
	}
	
	/**
	 * A bean may have a "default" event that is the event that will
	 * mostly commonly be used by human's when using the bean.
	 * @return Index of default event in the EventSetDescriptor array
	 *		returned by getEventSetDescriptors.
	 * <P>	Returns -1 if there is no default event.
	 */
	public int getDefaultEventIndex() {
		return defaultEventIndex;
	}
	
	/**
	 * This method returns an image object that can be used to
	 * represent the bean in toolboxes, toolbars, etc.   Icon images
	 * will typically be GIFs, but may in future include other formats.
	 * <p>
	 * Beans aren't required to provide icons and may return null from
	 * this method.
	 * <p>
	 * There are four possible flavors of icons (16x16 color,
	 * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
	 * support a single icon we recommend supporting 16x16 color.
	 * <p>
	 * We recommend that icons have a "transparent" background
	 * so they can be rendered onto an existing background.
	 *
	 * @param  iconKind  The kind of icon requested.  This should be
	 *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32,
	 *    ICON_MONO_16x16, or ICON_MONO_32x32.
	 * @return  An image object representing the requested icon.  May
	 *    return null if no suitable icon is available.
	 */
	public java.awt.Image getIcon(int iconKind) {
		switch ( iconKind ) {
			case ICON_COLOR_16x16:
				if ( iconNameC16 == null )
					return null;
				else {
					if( iconColor16 == null )
						iconColor16 = loadImage( iconNameC16 );
					return iconColor16;
				}
			case ICON_COLOR_32x32:
				if ( iconNameC32 == null )
					return null;
				else {
					if( iconColor32 == null )
						iconColor32 = loadImage( iconNameC32 );
					return iconColor32;
				}
			case ICON_MONO_16x16:
				if ( iconNameM16 == null )
					return null;
				else {
					if( iconMono16 == null )
						iconMono16 = loadImage( iconNameM16 );
					return iconMono16;
				}
			case ICON_MONO_32x32:
				if ( iconNameM32 == null )
					return null;
				else {
					if( iconMono32 == null )
						iconMono32 = loadImage( iconNameM32 );
					return iconMono32;
				}
			default: return null;
		}
	}
	
}

