package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import java.beans.*;

public class JObjectResourceComboBoxBeanInfo extends SimpleBeanInfo {
	
    // Bean descriptor //GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( JObjectResourceComboBox.class , null );//GEN-HEADEREND:BeanDescriptor
		
		// Here you can add code for customizing the BeanDescriptor.
		
        return beanDescriptor;         }//GEN-LAST:BeanDescriptor
	
	
    // Property identifiers //GEN-FIRST:Properties
    private static final int PROPERTY_accessibleContext = 0;
    private static final int PROPERTY_action = 1;
    private static final int PROPERTY_actionCommand = 2;
    private static final int PROPERTY_actionListeners = 3;
    private static final int PROPERTY_actionMap = 4;
    private static final int PROPERTY_alignmentX = 5;
    private static final int PROPERTY_alignmentY = 6;
    private static final int PROPERTY_ancestorListeners = 7;
    private static final int PROPERTY_autoscrolls = 8;
    private static final int PROPERTY_background = 9;
    private static final int PROPERTY_backgroundSet = 10;
    private static final int PROPERTY_border = 11;
    private static final int PROPERTY_bounds = 12;
    private static final int PROPERTY_colorModel = 13;
    private static final int PROPERTY_component = 14;
    private static final int PROPERTY_componentCount = 15;
    private static final int PROPERTY_componentListeners = 16;
    private static final int PROPERTY_componentOrientation = 17;
    private static final int PROPERTY_components = 18;
    private static final int PROPERTY_containerListeners = 19;
    private static final int PROPERTY_cursor = 20;
    private static final int PROPERTY_cursorSet = 21;
    private static final int PROPERTY_debugGraphicsOptions = 22;
    private static final int PROPERTY_displayable = 23;
    private static final int PROPERTY_doubleBuffered = 24;
    private static final int PROPERTY_dropTarget = 25;
    private static final int PROPERTY_editable = 26;
    private static final int PROPERTY_editor = 27;
    private static final int PROPERTY_enabled = 28;
    private static final int PROPERTY_focusable = 29;
    private static final int PROPERTY_focusCycleRoot = 30;
    private static final int PROPERTY_focusCycleRootAncestor = 31;
    private static final int PROPERTY_focusListeners = 32;
    private static final int PROPERTY_focusOwner = 33;
    private static final int PROPERTY_focusTraversable = 34;
    private static final int PROPERTY_focusTraversalKeys = 35;
    private static final int PROPERTY_focusTraversalKeysEnabled = 36;
    private static final int PROPERTY_focusTraversalPolicy = 37;
    private static final int PROPERTY_focusTraversalPolicySet = 38;
    private static final int PROPERTY_font = 39;
    private static final int PROPERTY_fontSet = 40;
    private static final int PROPERTY_foreground = 41;
    private static final int PROPERTY_foregroundSet = 42;
    private static final int PROPERTY_graphics = 43;
    private static final int PROPERTY_graphicsConfiguration = 44;
    private static final int PROPERTY_height = 45;
    private static final int PROPERTY_hierarchyBoundsListeners = 46;
    private static final int PROPERTY_hierarchyListeners = 47;
    private static final int PROPERTY_ignoreRepaint = 48;
    private static final int PROPERTY_inputContext = 49;
    private static final int PROPERTY_inputMethodListeners = 50;
    private static final int PROPERTY_inputMethodRequests = 51;
    private static final int PROPERTY_inputVerifier = 52;
    private static final int PROPERTY_insets = 53;
    private static final int PROPERTY_itemAt = 54;
    private static final int PROPERTY_itemCount = 55;
    private static final int PROPERTY_itemListeners = 56;
    private static final int PROPERTY_keyListeners = 57;
    private static final int PROPERTY_keySelectionManager = 58;
    private static final int PROPERTY_layout = 59;
    private static final int PROPERTY_lightweight = 60;
    private static final int PROPERTY_lightWeightPopupEnabled = 61;
    private static final int PROPERTY_locale = 62;
    private static final int PROPERTY_locationOnScreen = 63;
    private static final int PROPERTY_managingFocus = 64;
    private static final int PROPERTY_maximumRowCount = 65;
    private static final int PROPERTY_maximumSize = 66;
    private static final int PROPERTY_maximumSizeSet = 67;
    private static final int PROPERTY_minimumSize = 68;
    private static final int PROPERTY_minimumSizeSet = 69;
    private static final int PROPERTY_model = 70;
    private static final int PROPERTY_mouseListeners = 71;
    private static final int PROPERTY_mouseMotionListeners = 72;
    private static final int PROPERTY_mouseWheelListeners = 73;
    private static final int PROPERTY_name = 74;
    private static final int PROPERTY_nextFocusableComponent = 75;
    private static final int PROPERTY_opaque = 76;
    private static final int PROPERTY_optimizedDrawingEnabled = 77;
    private static final int PROPERTY_paintingTile = 78;
    private static final int PROPERTY_parent = 79;
    private static final int PROPERTY_peer = 80;
    private static final int PROPERTY_popupMenuListeners = 81;
    private static final int PROPERTY_popupVisible = 82;
    private static final int PROPERTY_preferredSize = 83;
    private static final int PROPERTY_preferredSizeSet = 84;
    private static final int PROPERTY_propertyChangeListeners = 85;
    private static final int PROPERTY_prototypeDisplayValue = 86;
    private static final int PROPERTY_registeredKeyStrokes = 87;
    private static final int PROPERTY_renderer = 88;
    private static final int PROPERTY_requestFocusEnabled = 89;
    private static final int PROPERTY_rootPane = 90;
    private static final int PROPERTY_selectedIndex = 91;
    private static final int PROPERTY_selectedItem = 92;
    private static final int PROPERTY_selectedObjects = 93;
    private static final int PROPERTY_showing = 94;
    private static final int PROPERTY_toolkit = 95;
    private static final int PROPERTY_toolTipText = 96;
    private static final int PROPERTY_topLevelAncestor = 97;
    private static final int PROPERTY_transferHandler = 98;
    private static final int PROPERTY_treeLock = 99;
    private static final int PROPERTY_UI = 100;
    private static final int PROPERTY_UIClassID = 101;
    private static final int PROPERTY_valid = 102;
    private static final int PROPERTY_validateRoot = 103;
    private static final int PROPERTY_verifyInputWhenFocusTarget = 104;
    private static final int PROPERTY_vetoableChangeListeners = 105;
    private static final int PROPERTY_visible = 106;
    private static final int PROPERTY_visibleRect = 107;
    private static final int PROPERTY_width = 108;
    private static final int PROPERTY_x = 109;
    private static final int PROPERTY_y = 110;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[111];
    
        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor ( "accessibleContext", JObjectResourceComboBox.class, "getAccessibleContext", null );
            properties[PROPERTY_action] = new PropertyDescriptor ( "action", JObjectResourceComboBox.class, "getAction", "setAction" );
            properties[PROPERTY_actionCommand] = new PropertyDescriptor ( "actionCommand", JObjectResourceComboBox.class, "getActionCommand", "setActionCommand" );
            properties[PROPERTY_actionListeners] = new PropertyDescriptor ( "actionListeners", JObjectResourceComboBox.class, "getActionListeners", null );
            properties[PROPERTY_actionMap] = new PropertyDescriptor ( "actionMap", JObjectResourceComboBox.class, "getActionMap", "setActionMap" );
            properties[PROPERTY_alignmentX] = new PropertyDescriptor ( "alignmentX", JObjectResourceComboBox.class, "getAlignmentX", "setAlignmentX" );
            properties[PROPERTY_alignmentY] = new PropertyDescriptor ( "alignmentY", JObjectResourceComboBox.class, "getAlignmentY", "setAlignmentY" );
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor ( "ancestorListeners", JObjectResourceComboBox.class, "getAncestorListeners", null );
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor ( "autoscrolls", JObjectResourceComboBox.class, "getAutoscrolls", "setAutoscrolls" );
            properties[PROPERTY_background] = new PropertyDescriptor ( "background", JObjectResourceComboBox.class, "getBackground", "setBackground" );
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor ( "backgroundSet", JObjectResourceComboBox.class, "isBackgroundSet", null );
            properties[PROPERTY_border] = new PropertyDescriptor ( "border", JObjectResourceComboBox.class, "getBorder", "setBorder" );
            properties[PROPERTY_bounds] = new PropertyDescriptor ( "bounds", JObjectResourceComboBox.class, "getBounds", "setBounds" );
            properties[PROPERTY_colorModel] = new PropertyDescriptor ( "colorModel", JObjectResourceComboBox.class, "getColorModel", null );
            properties[PROPERTY_component] = new IndexedPropertyDescriptor ( "component", JObjectResourceComboBox.class, null, null, "getComponent", null );
            properties[PROPERTY_componentCount] = new PropertyDescriptor ( "componentCount", JObjectResourceComboBox.class, "getComponentCount", null );
            properties[PROPERTY_componentListeners] = new PropertyDescriptor ( "componentListeners", JObjectResourceComboBox.class, "getComponentListeners", null );
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor ( "componentOrientation", JObjectResourceComboBox.class, "getComponentOrientation", "setComponentOrientation" );
            properties[PROPERTY_components] = new PropertyDescriptor ( "components", JObjectResourceComboBox.class, "getComponents", null );
            properties[PROPERTY_containerListeners] = new PropertyDescriptor ( "containerListeners", JObjectResourceComboBox.class, "getContainerListeners", null );
            properties[PROPERTY_cursor] = new PropertyDescriptor ( "cursor", JObjectResourceComboBox.class, "getCursor", "setCursor" );
            properties[PROPERTY_cursorSet] = new PropertyDescriptor ( "cursorSet", JObjectResourceComboBox.class, "isCursorSet", null );
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor ( "debugGraphicsOptions", JObjectResourceComboBox.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions" );
            properties[PROPERTY_displayable] = new PropertyDescriptor ( "displayable", JObjectResourceComboBox.class, "isDisplayable", null );
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor ( "doubleBuffered", JObjectResourceComboBox.class, "isDoubleBuffered", "setDoubleBuffered" );
            properties[PROPERTY_dropTarget] = new PropertyDescriptor ( "dropTarget", JObjectResourceComboBox.class, "getDropTarget", "setDropTarget" );
            properties[PROPERTY_editable] = new PropertyDescriptor ( "editable", JObjectResourceComboBox.class, "isEditable", "setEditable" );
            properties[PROPERTY_editor] = new PropertyDescriptor ( "editor", JObjectResourceComboBox.class, "getEditor", "setEditor" );
            properties[PROPERTY_enabled] = new PropertyDescriptor ( "enabled", JObjectResourceComboBox.class, "isEnabled", "setEnabled" );
            properties[PROPERTY_focusable] = new PropertyDescriptor ( "focusable", JObjectResourceComboBox.class, "isFocusable", "setFocusable" );
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor ( "focusCycleRoot", JObjectResourceComboBox.class, "isFocusCycleRoot", "setFocusCycleRoot" );
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor ( "focusCycleRootAncestor", JObjectResourceComboBox.class, "getFocusCycleRootAncestor", null );
            properties[PROPERTY_focusListeners] = new PropertyDescriptor ( "focusListeners", JObjectResourceComboBox.class, "getFocusListeners", null );
            properties[PROPERTY_focusOwner] = new PropertyDescriptor ( "focusOwner", JObjectResourceComboBox.class, "isFocusOwner", null );
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor ( "focusTraversable", JObjectResourceComboBox.class, "isFocusTraversable", null );
            properties[PROPERTY_focusTraversalKeys] = new IndexedPropertyDescriptor ( "focusTraversalKeys", JObjectResourceComboBox.class, null, null, "getFocusTraversalKeys", "setFocusTraversalKeys" );
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor ( "focusTraversalKeysEnabled", JObjectResourceComboBox.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled" );
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor ( "focusTraversalPolicy", JObjectResourceComboBox.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy" );
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor ( "focusTraversalPolicySet", JObjectResourceComboBox.class, "isFocusTraversalPolicySet", null );
            properties[PROPERTY_font] = new PropertyDescriptor ( "font", JObjectResourceComboBox.class, "getFont", "setFont" );
            properties[PROPERTY_fontSet] = new PropertyDescriptor ( "fontSet", JObjectResourceComboBox.class, "isFontSet", null );
            properties[PROPERTY_foreground] = new PropertyDescriptor ( "foreground", JObjectResourceComboBox.class, "getForeground", "setForeground" );
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor ( "foregroundSet", JObjectResourceComboBox.class, "isForegroundSet", null );
            properties[PROPERTY_graphics] = new PropertyDescriptor ( "graphics", JObjectResourceComboBox.class, "getGraphics", null );
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor ( "graphicsConfiguration", JObjectResourceComboBox.class, "getGraphicsConfiguration", null );
            properties[PROPERTY_height] = new PropertyDescriptor ( "height", JObjectResourceComboBox.class, "getHeight", null );
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor ( "hierarchyBoundsListeners", JObjectResourceComboBox.class, "getHierarchyBoundsListeners", null );
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor ( "hierarchyListeners", JObjectResourceComboBox.class, "getHierarchyListeners", null );
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor ( "ignoreRepaint", JObjectResourceComboBox.class, "getIgnoreRepaint", "setIgnoreRepaint" );
            properties[PROPERTY_inputContext] = new PropertyDescriptor ( "inputContext", JObjectResourceComboBox.class, "getInputContext", null );
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor ( "inputMethodListeners", JObjectResourceComboBox.class, "getInputMethodListeners", null );
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor ( "inputMethodRequests", JObjectResourceComboBox.class, "getInputMethodRequests", null );
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor ( "inputVerifier", JObjectResourceComboBox.class, "getInputVerifier", "setInputVerifier" );
            properties[PROPERTY_insets] = new PropertyDescriptor ( "insets", JObjectResourceComboBox.class, "getInsets", null );
            properties[PROPERTY_itemAt] = new IndexedPropertyDescriptor ( "itemAt", JObjectResourceComboBox.class, null, null, "getItemAt", null );
            properties[PROPERTY_itemCount] = new PropertyDescriptor ( "itemCount", JObjectResourceComboBox.class, "getItemCount", null );
            properties[PROPERTY_itemListeners] = new PropertyDescriptor ( "itemListeners", JObjectResourceComboBox.class, "getItemListeners", null );
            properties[PROPERTY_keyListeners] = new PropertyDescriptor ( "keyListeners", JObjectResourceComboBox.class, "getKeyListeners", null );
            properties[PROPERTY_keySelectionManager] = new PropertyDescriptor ( "keySelectionManager", JObjectResourceComboBox.class, "getKeySelectionManager", "setKeySelectionManager" );
            properties[PROPERTY_layout] = new PropertyDescriptor ( "layout", JObjectResourceComboBox.class, "getLayout", "setLayout" );
            properties[PROPERTY_lightweight] = new PropertyDescriptor ( "lightweight", JObjectResourceComboBox.class, "isLightweight", null );
            properties[PROPERTY_lightWeightPopupEnabled] = new PropertyDescriptor ( "lightWeightPopupEnabled", JObjectResourceComboBox.class, "isLightWeightPopupEnabled", "setLightWeightPopupEnabled" );
            properties[PROPERTY_locale] = new PropertyDescriptor ( "locale", JObjectResourceComboBox.class, "getLocale", "setLocale" );
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor ( "locationOnScreen", JObjectResourceComboBox.class, "getLocationOnScreen", null );
            properties[PROPERTY_managingFocus] = new PropertyDescriptor ( "managingFocus", JObjectResourceComboBox.class, "isManagingFocus", null );
            properties[PROPERTY_maximumRowCount] = new PropertyDescriptor ( "maximumRowCount", JObjectResourceComboBox.class, "getMaximumRowCount", "setMaximumRowCount" );
            properties[PROPERTY_maximumSize] = new PropertyDescriptor ( "maximumSize", JObjectResourceComboBox.class, "getMaximumSize", "setMaximumSize" );
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor ( "maximumSizeSet", JObjectResourceComboBox.class, "isMaximumSizeSet", null );
            properties[PROPERTY_minimumSize] = new PropertyDescriptor ( "minimumSize", JObjectResourceComboBox.class, "getMinimumSize", "setMinimumSize" );
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor ( "minimumSizeSet", JObjectResourceComboBox.class, "isMinimumSizeSet", null );
            properties[PROPERTY_model] = new PropertyDescriptor ( "model", JObjectResourceComboBox.class, "getModel", "setModel" );
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor ( "mouseListeners", JObjectResourceComboBox.class, "getMouseListeners", null );
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor ( "mouseMotionListeners", JObjectResourceComboBox.class, "getMouseMotionListeners", null );
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor ( "mouseWheelListeners", JObjectResourceComboBox.class, "getMouseWheelListeners", null );
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", JObjectResourceComboBox.class, "getName", "setName" );
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor ( "nextFocusableComponent", JObjectResourceComboBox.class, "getNextFocusableComponent", "setNextFocusableComponent" );
            properties[PROPERTY_opaque] = new PropertyDescriptor ( "opaque", JObjectResourceComboBox.class, "isOpaque", "setOpaque" );
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor ( "optimizedDrawingEnabled", JObjectResourceComboBox.class, "isOptimizedDrawingEnabled", null );
            properties[PROPERTY_paintingTile] = new PropertyDescriptor ( "paintingTile", JObjectResourceComboBox.class, "isPaintingTile", null );
            properties[PROPERTY_parent] = new PropertyDescriptor ( "parent", JObjectResourceComboBox.class, "getParent", null );
            properties[PROPERTY_peer] = new PropertyDescriptor ( "peer", JObjectResourceComboBox.class, "getPeer", null );
            properties[PROPERTY_popupMenuListeners] = new PropertyDescriptor ( "popupMenuListeners", JObjectResourceComboBox.class, "getPopupMenuListeners", null );
            properties[PROPERTY_popupVisible] = new PropertyDescriptor ( "popupVisible", JObjectResourceComboBox.class, "isPopupVisible", "setPopupVisible" );
            properties[PROPERTY_preferredSize] = new PropertyDescriptor ( "preferredSize", JObjectResourceComboBox.class, "getPreferredSize", "setPreferredSize" );
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor ( "preferredSizeSet", JObjectResourceComboBox.class, "isPreferredSizeSet", null );
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor ( "propertyChangeListeners", JObjectResourceComboBox.class, "getPropertyChangeListeners", null );
            properties[PROPERTY_prototypeDisplayValue] = new PropertyDescriptor ( "prototypeDisplayValue", JObjectResourceComboBox.class, "getPrototypeDisplayValue", "setPrototypeDisplayValue" );
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor ( "registeredKeyStrokes", JObjectResourceComboBox.class, "getRegisteredKeyStrokes", null );
            properties[PROPERTY_renderer] = new PropertyDescriptor ( "renderer", JObjectResourceComboBox.class, "getRenderer", "setRenderer" );
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor ( "requestFocusEnabled", JObjectResourceComboBox.class, "isRequestFocusEnabled", "setRequestFocusEnabled" );
            properties[PROPERTY_rootPane] = new PropertyDescriptor ( "rootPane", JObjectResourceComboBox.class, "getRootPane", null );
            properties[PROPERTY_selectedIndex] = new PropertyDescriptor ( "selectedIndex", JObjectResourceComboBox.class, "getSelectedIndex", "setSelectedIndex" );
            properties[PROPERTY_selectedItem] = new PropertyDescriptor ( "selectedItem", JObjectResourceComboBox.class, "getSelectedItem", "setSelectedItem" );
            properties[PROPERTY_selectedObjects] = new PropertyDescriptor ( "selectedObjects", JObjectResourceComboBox.class, "getSelectedObjects", null );
            properties[PROPERTY_showing] = new PropertyDescriptor ( "showing", JObjectResourceComboBox.class, "isShowing", null );
            properties[PROPERTY_toolkit] = new PropertyDescriptor ( "toolkit", JObjectResourceComboBox.class, "getToolkit", null );
            properties[PROPERTY_toolTipText] = new PropertyDescriptor ( "toolTipText", JObjectResourceComboBox.class, "getToolTipText", "setToolTipText" );
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor ( "topLevelAncestor", JObjectResourceComboBox.class, "getTopLevelAncestor", null );
            properties[PROPERTY_transferHandler] = new PropertyDescriptor ( "transferHandler", JObjectResourceComboBox.class, "getTransferHandler", "setTransferHandler" );
            properties[PROPERTY_treeLock] = new PropertyDescriptor ( "treeLock", JObjectResourceComboBox.class, "getTreeLock", null );
            properties[PROPERTY_UI] = new PropertyDescriptor ( "UI", JObjectResourceComboBox.class, "getUI", "setUI" );
            properties[PROPERTY_UIClassID] = new PropertyDescriptor ( "UIClassID", JObjectResourceComboBox.class, "getUIClassID", null );
            properties[PROPERTY_valid] = new PropertyDescriptor ( "valid", JObjectResourceComboBox.class, "isValid", null );
            properties[PROPERTY_validateRoot] = new PropertyDescriptor ( "validateRoot", JObjectResourceComboBox.class, "isValidateRoot", null );
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor ( "verifyInputWhenFocusTarget", JObjectResourceComboBox.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget" );
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor ( "vetoableChangeListeners", JObjectResourceComboBox.class, "getVetoableChangeListeners", null );
            properties[PROPERTY_visible] = new PropertyDescriptor ( "visible", JObjectResourceComboBox.class, "isVisible", "setVisible" );
            properties[PROPERTY_visibleRect] = new PropertyDescriptor ( "visibleRect", JObjectResourceComboBox.class, "getVisibleRect", null );
            properties[PROPERTY_width] = new PropertyDescriptor ( "width", JObjectResourceComboBox.class, "getWidth", null );
            properties[PROPERTY_x] = new PropertyDescriptor ( "x", JObjectResourceComboBox.class, "getX", null );
            properties[PROPERTY_y] = new PropertyDescriptor ( "y", JObjectResourceComboBox.class, "getY", null );
        }
        catch( IntrospectionException e) {}//GEN-HEADEREND:Properties
		
		// Here you can add code for customizing the properties array.
		
        return properties;         }//GEN-LAST:Properties
	
    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_actionListener = 0;
    private static final int EVENT_ancestorListener = 1;
    private static final int EVENT_componentListener = 2;
    private static final int EVENT_containerListener = 3;
    private static final int EVENT_focusListener = 4;
    private static final int EVENT_hierarchyBoundsListener = 5;
    private static final int EVENT_hierarchyListener = 6;
    private static final int EVENT_inputMethodListener = 7;
    private static final int EVENT_itemListener = 8;
    private static final int EVENT_keyListener = 9;
    private static final int EVENT_mouseListener = 10;
    private static final int EVENT_mouseMotionListener = 11;
    private static final int EVENT_mouseWheelListener = 12;
    private static final int EVENT_popupMenuListener = 13;
    private static final int EVENT_propertyChangeListener = 14;
    private static final int EVENT_vetoableChangeListener = 15;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[16];
    
            try {
            eventSets[EVENT_actionListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "actionListener", java.awt.event.ActionListener.class, new String[] {"actionPerformed"}, "addActionListener", "removeActionListener" );
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] {"ancestorAdded", "ancestorMoved", "ancestorRemoved"}, "addAncestorListener", "removeAncestorListener" );
            eventSets[EVENT_componentListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "componentListener", java.awt.event.ComponentListener.class, new String[] {"componentHidden", "componentMoved", "componentResized", "componentShown"}, "addComponentListener", "removeComponentListener" );
            eventSets[EVENT_containerListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "containerListener", java.awt.event.ContainerListener.class, new String[] {"componentAdded", "componentRemoved"}, "addContainerListener", "removeContainerListener" );
            eventSets[EVENT_focusListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "focusListener", java.awt.event.FocusListener.class, new String[] {"focusGained", "focusLost"}, "addFocusListener", "removeFocusListener" );
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] {"ancestorMoved", "ancestorResized"}, "addHierarchyBoundsListener", "removeHierarchyBoundsListener" );
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] {"hierarchyChanged"}, "addHierarchyListener", "removeHierarchyListener" );
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] {"caretPositionChanged", "inputMethodTextChanged"}, "addInputMethodListener", "removeInputMethodListener" );
            eventSets[EVENT_itemListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "itemListener", java.awt.event.ItemListener.class, new String[] {"itemStateChanged"}, "addItemListener", "removeItemListener" );
            eventSets[EVENT_keyListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "keyListener", java.awt.event.KeyListener.class, new String[] {"keyPressed", "keyReleased", "keyTyped"}, "addKeyListener", "removeKeyListener" );
            eventSets[EVENT_mouseListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "mouseListener", java.awt.event.MouseListener.class, new String[] {"mouseClicked", "mouseEntered", "mouseExited", "mousePressed", "mouseReleased"}, "addMouseListener", "removeMouseListener" );
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] {"mouseDragged", "mouseMoved"}, "addMouseMotionListener", "removeMouseMotionListener" );
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] {"mouseWheelMoved"}, "addMouseWheelListener", "removeMouseWheelListener" );
            eventSets[EVENT_popupMenuListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "popupMenuListener", javax.swing.event.PopupMenuListener.class, new String[] {"popupMenuCanceled", "popupMenuWillBecomeInvisible", "popupMenuWillBecomeVisible"}, "addPopupMenuListener", "removePopupMenuListener" );
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" );
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] {"vetoableChange"}, "addVetoableChangeListener", "removeVetoableChangeListener" );
        }
        catch( IntrospectionException e) {}//GEN-HEADEREND:Events
		
		// Here you can add code for customizing the event sets array.
		
        return eventSets;         }//GEN-LAST:Events
	
    // Method identifiers //GEN-FIRST:Methods
    private static final int METHOD_action0 = 0;
    private static final int METHOD_actionPerformed1 = 1;
    private static final int METHOD_add2 = 2;
    private static final int METHOD_addItem3 = 3;
    private static final int METHOD_addNotify4 = 4;
    private static final int METHOD_addPropertyChangeListener5 = 5;
    private static final int METHOD_applyComponentOrientation6 = 6;
    private static final int METHOD_areFocusTraversalKeysSet7 = 7;
    private static final int METHOD_bounds8 = 8;
    private static final int METHOD_checkImage9 = 9;
    private static final int METHOD_computeVisibleRect10 = 10;
    private static final int METHOD_configureEditor11 = 11;
    private static final int METHOD_contains12 = 12;
    private static final int METHOD_contentsChanged13 = 13;
    private static final int METHOD_countComponents14 = 14;
    private static final int METHOD_createImage15 = 15;
    private static final int METHOD_createToolTip16 = 16;
    private static final int METHOD_createVolatileImage17 = 17;
    private static final int METHOD_deliverEvent18 = 18;
    private static final int METHOD_disable19 = 19;
    private static final int METHOD_dispatchEvent20 = 20;
    private static final int METHOD_doLayout21 = 21;
    private static final int METHOD_enable22 = 22;
    private static final int METHOD_enableInputMethods23 = 23;
    private static final int METHOD_findComponentAt24 = 24;
    private static final int METHOD_firePopupMenuCanceled25 = 25;
    private static final int METHOD_firePopupMenuWillBecomeInvisible26 = 26;
    private static final int METHOD_firePopupMenuWillBecomeVisible27 = 27;
    private static final int METHOD_firePropertyChange28 = 28;
    private static final int METHOD_getActionForKeyStroke29 = 29;
    private static final int METHOD_getBounds30 = 30;
    private static final int METHOD_getClientProperty31 = 31;
    private static final int METHOD_getComponentAt32 = 32;
    private static final int METHOD_getConditionForKeyStroke33 = 33;
    private static final int METHOD_getDefaultLocale34 = 34;
    private static final int METHOD_getFontMetrics35 = 35;
    private static final int METHOD_getInputMap36 = 36;
    private static final int METHOD_getInsets37 = 37;
    private static final int METHOD_getListeners38 = 38;
    private static final int METHOD_getLocation39 = 39;
    private static final int METHOD_getPropertyChangeListeners40 = 40;
    private static final int METHOD_getSize41 = 41;
    private static final int METHOD_getToolTipLocation42 = 42;
    private static final int METHOD_getToolTipText43 = 43;
    private static final int METHOD_gotFocus44 = 44;
    private static final int METHOD_grabFocus45 = 45;
    private static final int METHOD_handleEvent46 = 46;
    private static final int METHOD_hasFocus47 = 47;
    private static final int METHOD_hide48 = 48;
    private static final int METHOD_hidePopup49 = 49;
    private static final int METHOD_imageUpdate50 = 50;
    private static final int METHOD_insertItemAt51 = 51;
    private static final int METHOD_insets52 = 52;
    private static final int METHOD_inside53 = 53;
    private static final int METHOD_intervalAdded54 = 54;
    private static final int METHOD_intervalRemoved55 = 55;
    private static final int METHOD_invalidate56 = 56;
    private static final int METHOD_isAncestorOf57 = 57;
    private static final int METHOD_isFocusCycleRoot58 = 58;
    private static final int METHOD_isLightweightComponent59 = 59;
    private static final int METHOD_keyDown60 = 60;
    private static final int METHOD_keyUp61 = 61;
    private static final int METHOD_layout62 = 62;
    private static final int METHOD_list63 = 63;
    private static final int METHOD_locate64 = 64;
    private static final int METHOD_location65 = 65;
    private static final int METHOD_lostFocus66 = 66;
    private static final int METHOD_minimumSize67 = 67;
    private static final int METHOD_mouseDown68 = 68;
    private static final int METHOD_mouseDrag69 = 69;
    private static final int METHOD_mouseEnter70 = 70;
    private static final int METHOD_mouseExit71 = 71;
    private static final int METHOD_mouseMove72 = 72;
    private static final int METHOD_mouseUp73 = 73;
    private static final int METHOD_move74 = 74;
    private static final int METHOD_nextFocus75 = 75;
    private static final int METHOD_paint76 = 76;
    private static final int METHOD_paintAll77 = 77;
    private static final int METHOD_paintComponents78 = 78;
    private static final int METHOD_paintImmediately79 = 79;
    private static final int METHOD_postEvent80 = 80;
    private static final int METHOD_preferredSize81 = 81;
    private static final int METHOD_prepareImage82 = 82;
    private static final int METHOD_print83 = 83;
    private static final int METHOD_printAll84 = 84;
    private static final int METHOD_printComponents85 = 85;
    private static final int METHOD_processKeyEvent86 = 86;
    private static final int METHOD_putClientProperty87 = 87;
    private static final int METHOD_registerKeyboardAction88 = 88;
    private static final int METHOD_remove89 = 89;
    private static final int METHOD_removeAll90 = 90;
    private static final int METHOD_removeAllItems91 = 91;
    private static final int METHOD_removeItem92 = 92;
    private static final int METHOD_removeItemAt93 = 93;
    private static final int METHOD_removeNotify94 = 94;
    private static final int METHOD_removePropertyChangeListener95 = 95;
    private static final int METHOD_repaint96 = 96;
    private static final int METHOD_requestDefaultFocus97 = 97;
    private static final int METHOD_requestFocus98 = 98;
    private static final int METHOD_requestFocusInWindow99 = 99;
    private static final int METHOD_resetKeyboardActions100 = 100;
    private static final int METHOD_reshape101 = 101;
    private static final int METHOD_resize102 = 102;
    private static final int METHOD_revalidate103 = 103;
    private static final int METHOD_scrollRectToVisible104 = 104;
    private static final int METHOD_selectWithKeyChar105 = 105;
    private static final int METHOD_setBounds106 = 106;
    private static final int METHOD_setDefaultLocale107 = 107;
    private static final int METHOD_setInputMap108 = 108;
    private static final int METHOD_setLocation109 = 109;
    private static final int METHOD_setSize110 = 110;
    private static final int METHOD_show111 = 111;
    private static final int METHOD_showPopup112 = 112;
    private static final int METHOD_size113 = 113;
    private static final int METHOD_toString114 = 114;
    private static final int METHOD_transferFocus115 = 115;
    private static final int METHOD_transferFocusBackward116 = 116;
    private static final int METHOD_transferFocusDownCycle117 = 117;
    private static final int METHOD_transferFocusUpCycle118 = 118;
    private static final int METHOD_unregisterKeyboardAction119 = 119;
    private static final int METHOD_update120 = 120;
    private static final int METHOD_updateUI121 = 121;
    private static final int METHOD_validate122 = 122;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[123];
    
        try {
            methods[METHOD_action0] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("action", new Class[] {java.awt.Event.class, java.lang.Object.class}));
            methods[METHOD_action0].setDisplayName ( "" );
            methods[METHOD_actionPerformed1] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("actionPerformed", new Class[] {java.awt.event.ActionEvent.class}));
            methods[METHOD_actionPerformed1].setDisplayName ( "" );
            methods[METHOD_add2] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("add", new Class[] {java.awt.Component.class}));
            methods[METHOD_add2].setDisplayName ( "" );
            methods[METHOD_addItem3] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("addItem", new Class[] {java.lang.Object.class}));
            methods[METHOD_addItem3].setDisplayName ( "" );
            methods[METHOD_addNotify4] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("addNotify", new Class[] {}));
            methods[METHOD_addNotify4].setDisplayName ( "" );
            methods[METHOD_addPropertyChangeListener5] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("addPropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class}));
            methods[METHOD_addPropertyChangeListener5].setDisplayName ( "" );
            methods[METHOD_applyComponentOrientation6] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("applyComponentOrientation", new Class[] {java.awt.ComponentOrientation.class}));
            methods[METHOD_applyComponentOrientation6].setDisplayName ( "" );
            methods[METHOD_areFocusTraversalKeysSet7] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("areFocusTraversalKeysSet", new Class[] {Integer.TYPE}));
            methods[METHOD_areFocusTraversalKeysSet7].setDisplayName ( "" );
            methods[METHOD_bounds8] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("bounds", new Class[] {}));
            methods[METHOD_bounds8].setDisplayName ( "" );
            methods[METHOD_checkImage9] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("checkImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class}));
            methods[METHOD_checkImage9].setDisplayName ( "" );
            methods[METHOD_computeVisibleRect10] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("computeVisibleRect", new Class[] {java.awt.Rectangle.class}));
            methods[METHOD_computeVisibleRect10].setDisplayName ( "" );
            methods[METHOD_configureEditor11] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("configureEditor", new Class[] {javax.swing.ComboBoxEditor.class, java.lang.Object.class}));
            methods[METHOD_configureEditor11].setDisplayName ( "" );
            methods[METHOD_contains12] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("contains", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_contains12].setDisplayName ( "" );
            methods[METHOD_contentsChanged13] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("contentsChanged", new Class[] {javax.swing.event.ListDataEvent.class}));
            methods[METHOD_contentsChanged13].setDisplayName ( "" );
            methods[METHOD_countComponents14] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("countComponents", new Class[] {}));
            methods[METHOD_countComponents14].setDisplayName ( "" );
            methods[METHOD_createImage15] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("createImage", new Class[] {java.awt.image.ImageProducer.class}));
            methods[METHOD_createImage15].setDisplayName ( "" );
            methods[METHOD_createToolTip16] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("createToolTip", new Class[] {}));
            methods[METHOD_createToolTip16].setDisplayName ( "" );
            methods[METHOD_createVolatileImage17] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("createVolatileImage", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_createVolatileImage17].setDisplayName ( "" );
            methods[METHOD_deliverEvent18] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("deliverEvent", new Class[] {java.awt.Event.class}));
            methods[METHOD_deliverEvent18].setDisplayName ( "" );
            methods[METHOD_disable19] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("disable", new Class[] {}));
            methods[METHOD_disable19].setDisplayName ( "" );
            methods[METHOD_dispatchEvent20] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("dispatchEvent", new Class[] {java.awt.AWTEvent.class}));
            methods[METHOD_dispatchEvent20].setDisplayName ( "" );
            methods[METHOD_doLayout21] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("doLayout", new Class[] {}));
            methods[METHOD_doLayout21].setDisplayName ( "" );
            methods[METHOD_enable22] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("enable", new Class[] {}));
            methods[METHOD_enable22].setDisplayName ( "" );
            methods[METHOD_enableInputMethods23] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("enableInputMethods", new Class[] {Boolean.TYPE}));
            methods[METHOD_enableInputMethods23].setDisplayName ( "" );
            methods[METHOD_findComponentAt24] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("findComponentAt", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_findComponentAt24].setDisplayName ( "" );
            methods[METHOD_firePopupMenuCanceled25] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("firePopupMenuCanceled", new Class[] {}));
            methods[METHOD_firePopupMenuCanceled25].setDisplayName ( "" );
            methods[METHOD_firePopupMenuWillBecomeInvisible26] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("firePopupMenuWillBecomeInvisible", new Class[] {}));
            methods[METHOD_firePopupMenuWillBecomeInvisible26].setDisplayName ( "" );
            methods[METHOD_firePopupMenuWillBecomeVisible27] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("firePopupMenuWillBecomeVisible", new Class[] {}));
            methods[METHOD_firePopupMenuWillBecomeVisible27].setDisplayName ( "" );
            methods[METHOD_firePropertyChange28] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, Byte.TYPE, Byte.TYPE}));
            methods[METHOD_firePropertyChange28].setDisplayName ( "" );
            methods[METHOD_getActionForKeyStroke29] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getActionForKeyStroke", new Class[] {javax.swing.KeyStroke.class}));
            methods[METHOD_getActionForKeyStroke29].setDisplayName ( "" );
            methods[METHOD_getBounds30] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getBounds", new Class[] {java.awt.Rectangle.class}));
            methods[METHOD_getBounds30].setDisplayName ( "" );
            methods[METHOD_getClientProperty31] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getClientProperty", new Class[] {java.lang.Object.class}));
            methods[METHOD_getClientProperty31].setDisplayName ( "" );
            methods[METHOD_getComponentAt32] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getComponentAt", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_getComponentAt32].setDisplayName ( "" );
            methods[METHOD_getConditionForKeyStroke33] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getConditionForKeyStroke", new Class[] {javax.swing.KeyStroke.class}));
            methods[METHOD_getConditionForKeyStroke33].setDisplayName ( "" );
            methods[METHOD_getDefaultLocale34] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getDefaultLocale", new Class[] {}));
            methods[METHOD_getDefaultLocale34].setDisplayName ( "" );
            methods[METHOD_getFontMetrics35] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getFontMetrics", new Class[] {java.awt.Font.class}));
            methods[METHOD_getFontMetrics35].setDisplayName ( "" );
            methods[METHOD_getInputMap36] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getInputMap", new Class[] {Integer.TYPE}));
            methods[METHOD_getInputMap36].setDisplayName ( "" );
            methods[METHOD_getInsets37] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getInsets", new Class[] {java.awt.Insets.class}));
            methods[METHOD_getInsets37].setDisplayName ( "" );
            methods[METHOD_getListeners38] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getListeners", new Class[] {java.lang.Class.class}));
            methods[METHOD_getListeners38].setDisplayName ( "" );
            methods[METHOD_getLocation39] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getLocation", new Class[] {java.awt.Point.class}));
            methods[METHOD_getLocation39].setDisplayName ( "" );
            methods[METHOD_getPropertyChangeListeners40] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getPropertyChangeListeners", new Class[] {java.lang.String.class}));
            methods[METHOD_getPropertyChangeListeners40].setDisplayName ( "" );
            methods[METHOD_getSize41] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getSize", new Class[] {java.awt.Dimension.class}));
            methods[METHOD_getSize41].setDisplayName ( "" );
            methods[METHOD_getToolTipLocation42] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getToolTipLocation", new Class[] {java.awt.event.MouseEvent.class}));
            methods[METHOD_getToolTipLocation42].setDisplayName ( "" );
            methods[METHOD_getToolTipText43] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("getToolTipText", new Class[] {java.awt.event.MouseEvent.class}));
            methods[METHOD_getToolTipText43].setDisplayName ( "" );
            methods[METHOD_gotFocus44] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("gotFocus", new Class[] {java.awt.Event.class, java.lang.Object.class}));
            methods[METHOD_gotFocus44].setDisplayName ( "" );
            methods[METHOD_grabFocus45] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("grabFocus", new Class[] {}));
            methods[METHOD_grabFocus45].setDisplayName ( "" );
            methods[METHOD_handleEvent46] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("handleEvent", new Class[] {java.awt.Event.class}));
            methods[METHOD_handleEvent46].setDisplayName ( "" );
            methods[METHOD_hasFocus47] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("hasFocus", new Class[] {}));
            methods[METHOD_hasFocus47].setDisplayName ( "" );
            methods[METHOD_hide48] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("hide", new Class[] {}));
            methods[METHOD_hide48].setDisplayName ( "" );
            methods[METHOD_hidePopup49] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("hidePopup", new Class[] {}));
            methods[METHOD_hidePopup49].setDisplayName ( "" );
            methods[METHOD_imageUpdate50] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("imageUpdate", new Class[] {java.awt.Image.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_imageUpdate50].setDisplayName ( "" );
            methods[METHOD_insertItemAt51] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("insertItemAt", new Class[] {java.lang.Object.class, Integer.TYPE}));
            methods[METHOD_insertItemAt51].setDisplayName ( "" );
            methods[METHOD_insets52] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("insets", new Class[] {}));
            methods[METHOD_insets52].setDisplayName ( "" );
            methods[METHOD_inside53] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("inside", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_inside53].setDisplayName ( "" );
            methods[METHOD_intervalAdded54] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("intervalAdded", new Class[] {javax.swing.event.ListDataEvent.class}));
            methods[METHOD_intervalAdded54].setDisplayName ( "" );
            methods[METHOD_intervalRemoved55] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("intervalRemoved", new Class[] {javax.swing.event.ListDataEvent.class}));
            methods[METHOD_intervalRemoved55].setDisplayName ( "" );
            methods[METHOD_invalidate56] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("invalidate", new Class[] {}));
            methods[METHOD_invalidate56].setDisplayName ( "" );
            methods[METHOD_isAncestorOf57] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("isAncestorOf", new Class[] {java.awt.Component.class}));
            methods[METHOD_isAncestorOf57].setDisplayName ( "" );
            methods[METHOD_isFocusCycleRoot58] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("isFocusCycleRoot", new Class[] {java.awt.Container.class}));
            methods[METHOD_isFocusCycleRoot58].setDisplayName ( "" );
            methods[METHOD_isLightweightComponent59] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("isLightweightComponent", new Class[] {java.awt.Component.class}));
            methods[METHOD_isLightweightComponent59].setDisplayName ( "" );
            methods[METHOD_keyDown60] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("keyDown", new Class[] {java.awt.Event.class, Integer.TYPE}));
            methods[METHOD_keyDown60].setDisplayName ( "" );
            methods[METHOD_keyUp61] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("keyUp", new Class[] {java.awt.Event.class, Integer.TYPE}));
            methods[METHOD_keyUp61].setDisplayName ( "" );
            methods[METHOD_layout62] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("layout", new Class[] {}));
            methods[METHOD_layout62].setDisplayName ( "" );
            methods[METHOD_list63] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("list", new Class[] {java.io.PrintStream.class, Integer.TYPE}));
            methods[METHOD_list63].setDisplayName ( "" );
            methods[METHOD_locate64] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("locate", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_locate64].setDisplayName ( "" );
            methods[METHOD_location65] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("location", new Class[] {}));
            methods[METHOD_location65].setDisplayName ( "" );
            methods[METHOD_lostFocus66] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("lostFocus", new Class[] {java.awt.Event.class, java.lang.Object.class}));
            methods[METHOD_lostFocus66].setDisplayName ( "" );
            methods[METHOD_minimumSize67] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("minimumSize", new Class[] {}));
            methods[METHOD_minimumSize67].setDisplayName ( "" );
            methods[METHOD_mouseDown68] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("mouseDown", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseDown68].setDisplayName ( "" );
            methods[METHOD_mouseDrag69] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("mouseDrag", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseDrag69].setDisplayName ( "" );
            methods[METHOD_mouseEnter70] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("mouseEnter", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseEnter70].setDisplayName ( "" );
            methods[METHOD_mouseExit71] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("mouseExit", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseExit71].setDisplayName ( "" );
            methods[METHOD_mouseMove72] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("mouseMove", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseMove72].setDisplayName ( "" );
            methods[METHOD_mouseUp73] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("mouseUp", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_mouseUp73].setDisplayName ( "" );
            methods[METHOD_move74] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("move", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_move74].setDisplayName ( "" );
            methods[METHOD_nextFocus75] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("nextFocus", new Class[] {}));
            methods[METHOD_nextFocus75].setDisplayName ( "" );
            methods[METHOD_paint76] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("paint", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_paint76].setDisplayName ( "" );
            methods[METHOD_paintAll77] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("paintAll", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_paintAll77].setDisplayName ( "" );
            methods[METHOD_paintComponents78] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("paintComponents", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_paintComponents78].setDisplayName ( "" );
            methods[METHOD_paintImmediately79] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("paintImmediately", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_paintImmediately79].setDisplayName ( "" );
            methods[METHOD_postEvent80] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("postEvent", new Class[] {java.awt.Event.class}));
            methods[METHOD_postEvent80].setDisplayName ( "" );
            methods[METHOD_preferredSize81] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("preferredSize", new Class[] {}));
            methods[METHOD_preferredSize81].setDisplayName ( "" );
            methods[METHOD_prepareImage82] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("prepareImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class}));
            methods[METHOD_prepareImage82].setDisplayName ( "" );
            methods[METHOD_print83] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("print", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_print83].setDisplayName ( "" );
            methods[METHOD_printAll84] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("printAll", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_printAll84].setDisplayName ( "" );
            methods[METHOD_printComponents85] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("printComponents", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_printComponents85].setDisplayName ( "" );
            methods[METHOD_processKeyEvent86] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("processKeyEvent", new Class[] {java.awt.event.KeyEvent.class}));
            methods[METHOD_processKeyEvent86].setDisplayName ( "" );
            methods[METHOD_putClientProperty87] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("putClientProperty", new Class[] {java.lang.Object.class, java.lang.Object.class}));
            methods[METHOD_putClientProperty87].setDisplayName ( "" );
            methods[METHOD_registerKeyboardAction88] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("registerKeyboardAction", new Class[] {java.awt.event.ActionListener.class, java.lang.String.class, javax.swing.KeyStroke.class, Integer.TYPE}));
            methods[METHOD_registerKeyboardAction88].setDisplayName ( "" );
            methods[METHOD_remove89] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("remove", new Class[] {Integer.TYPE}));
            methods[METHOD_remove89].setDisplayName ( "" );
            methods[METHOD_removeAll90] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("removeAll", new Class[] {}));
            methods[METHOD_removeAll90].setDisplayName ( "" );
            methods[METHOD_removeAllItems91] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("removeAllItems", new Class[] {}));
            methods[METHOD_removeAllItems91].setDisplayName ( "" );
            methods[METHOD_removeItem92] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("removeItem", new Class[] {java.lang.Object.class}));
            methods[METHOD_removeItem92].setDisplayName ( "" );
            methods[METHOD_removeItemAt93] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("removeItemAt", new Class[] {Integer.TYPE}));
            methods[METHOD_removeItemAt93].setDisplayName ( "" );
            methods[METHOD_removeNotify94] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("removeNotify", new Class[] {}));
            methods[METHOD_removeNotify94].setDisplayName ( "" );
            methods[METHOD_removePropertyChangeListener95] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("removePropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class}));
            methods[METHOD_removePropertyChangeListener95].setDisplayName ( "" );
            methods[METHOD_repaint96] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("repaint", new Class[] {Long.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_repaint96].setDisplayName ( "" );
            methods[METHOD_requestDefaultFocus97] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("requestDefaultFocus", new Class[] {}));
            methods[METHOD_requestDefaultFocus97].setDisplayName ( "" );
            methods[METHOD_requestFocus98] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("requestFocus", new Class[] {}));
            methods[METHOD_requestFocus98].setDisplayName ( "" );
            methods[METHOD_requestFocusInWindow99] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("requestFocusInWindow", new Class[] {}));
            methods[METHOD_requestFocusInWindow99].setDisplayName ( "" );
            methods[METHOD_resetKeyboardActions100] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("resetKeyboardActions", new Class[] {}));
            methods[METHOD_resetKeyboardActions100].setDisplayName ( "" );
            methods[METHOD_reshape101] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("reshape", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_reshape101].setDisplayName ( "" );
            methods[METHOD_resize102] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("resize", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_resize102].setDisplayName ( "" );
            methods[METHOD_revalidate103] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("revalidate", new Class[] {}));
            methods[METHOD_revalidate103].setDisplayName ( "" );
            methods[METHOD_scrollRectToVisible104] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("scrollRectToVisible", new Class[] {java.awt.Rectangle.class}));
            methods[METHOD_scrollRectToVisible104].setDisplayName ( "" );
            methods[METHOD_selectWithKeyChar105] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("selectWithKeyChar", new Class[] {Character.TYPE}));
            methods[METHOD_selectWithKeyChar105].setDisplayName ( "" );
            methods[METHOD_setBounds106] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("setBounds", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}));
            methods[METHOD_setBounds106].setDisplayName ( "" );
            methods[METHOD_setDefaultLocale107] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("setDefaultLocale", new Class[] {java.util.Locale.class}));
            methods[METHOD_setDefaultLocale107].setDisplayName ( "" );
            methods[METHOD_setInputMap108] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("setInputMap", new Class[] {Integer.TYPE, javax.swing.InputMap.class}));
            methods[METHOD_setInputMap108].setDisplayName ( "" );
            methods[METHOD_setLocation109] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("setLocation", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_setLocation109].setDisplayName ( "" );
            methods[METHOD_setSize110] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("setSize", new Class[] {Integer.TYPE, Integer.TYPE}));
            methods[METHOD_setSize110].setDisplayName ( "" );
            methods[METHOD_show111] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("show", new Class[] {}));
            methods[METHOD_show111].setDisplayName ( "" );
            methods[METHOD_showPopup112] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("showPopup", new Class[] {}));
            methods[METHOD_showPopup112].setDisplayName ( "" );
            methods[METHOD_size113] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("size", new Class[] {}));
            methods[METHOD_size113].setDisplayName ( "" );
            methods[METHOD_toString114] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("toString", new Class[] {}));
            methods[METHOD_toString114].setDisplayName ( "" );
            methods[METHOD_transferFocus115] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("transferFocus", new Class[] {}));
            methods[METHOD_transferFocus115].setDisplayName ( "" );
            methods[METHOD_transferFocusBackward116] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("transferFocusBackward", new Class[] {}));
            methods[METHOD_transferFocusBackward116].setDisplayName ( "" );
            methods[METHOD_transferFocusDownCycle117] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("transferFocusDownCycle", new Class[] {}));
            methods[METHOD_transferFocusDownCycle117].setDisplayName ( "" );
            methods[METHOD_transferFocusUpCycle118] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("transferFocusUpCycle", new Class[] {}));
            methods[METHOD_transferFocusUpCycle118].setDisplayName ( "" );
            methods[METHOD_unregisterKeyboardAction119] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("unregisterKeyboardAction", new Class[] {javax.swing.KeyStroke.class}));
            methods[METHOD_unregisterKeyboardAction119].setDisplayName ( "" );
            methods[METHOD_update120] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("update", new Class[] {java.awt.Graphics.class}));
            methods[METHOD_update120].setDisplayName ( "" );
            methods[METHOD_updateUI121] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("updateUI", new Class[] {}));
            methods[METHOD_updateUI121].setDisplayName ( "" );
            methods[METHOD_validate122] = new MethodDescriptor ( com.syrus.AMFICOM.Client.Resource.Alarm.ui.JObjectResourceComboBox.class.getMethod("validate", new Class[] {}));
            methods[METHOD_validate122].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods
		
		// Here you can add code for customizing the methods array.
		
        return methods;         }//GEN-LAST:Methods
	
	private static java.awt.Image iconColor16 = null; //GEN-BEGIN:IconsDef
	private static java.awt.Image iconColor32 = null;
	private static java.awt.Image iconMono16 = null;
	private static java.awt.Image iconMono32 = null; //GEN-END:IconsDef
    private static String iconNameC16 = "/javax/swing/beaninfo/images/JComboBoxColor16.gif";//GEN-BEGIN:Icons
    private static String iconNameC32 = "/javax/swing/beaninfo/images/JComboBoxColor32.gif";
    private static String iconNameM16 = "/javax/swing/beaninfo/images/JComboBoxMono16.gif";
    private static String iconNameM32 = "/javax/swing/beaninfo/images/JComboBoxMono32.gif";//GEN-END:Icons
	
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

