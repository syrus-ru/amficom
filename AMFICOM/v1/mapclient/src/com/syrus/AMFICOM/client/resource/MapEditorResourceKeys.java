/*-
 * $Id: MapEditorResourceKeys.java,v 1.10 2005/10/24 15:43:25 krupenn Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

/**
 * @version $Revision: 1.10 $, $Date: 2005/10/24 15:43:25 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapclient
 */
public interface MapEditorResourceKeys extends ResourceKeys {
	String ICON_CATALOG = "icon.catalog"; //$NON-NLS-1$

	String TITLE_SELECT_ELEMENT = "Title.SelectElement"; //$NON-NLS-1$

	String LABEL_CABLE_LIST = "Label.CableList"; //$NON-NLS-1$

	String TO_SITE_LOWERCASE = "Helper.ToSite_lowercase"; //$NON-NLS-1$

	String FILE_CHOOSER_SELECT_FILE_TO_OPEN = "FileChooser.SelectFileToOpen"; //$NON-NLS-1$
	String FILE_CHOOSER_FILE_EXISTS_OVERWRITE = "FileChooser.FileExists.Overwrite"; //$NON-NLS-1$
	String FILE_CHOOSER_SELECT_FILE_TO_SAVE = "FileChooser.SelectFileToSave"; //$NON-NLS-1$
	String FILE_CHOOSER_EXPORT_SAVE_FILE = "FileChooser.ExportSaveFile"; //$NON-NLS-1$
	String FILE_CHOOSER_PICTURE = "FileChooser.Picture"; //$NON-NLS-1$

	String MESSAGE_UNABLE_TO_PLACE_CABLE = "Message.UnableToPlaceCable"; //$NON-NLS-1$
	String MESASGE_ERROR_OPENING = "Mesasge.ErrorOpening"; //$NON-NLS-1$
	String MESSAGE_UNSAVED_ELEMENTS_PRESENT = "Message.UnsavedElementsPresent"; //$NON-NLS-1$
	String MESSAGE_INFORMATION_IMPORTING_PLS_WAIT = "Message.Information.ImportingPlsWait"; //$NON-NLS-1$
	String MESSAGE_ENTER_COLLECTOR_NAME = "Message.EnterCollectorName"; //$NON-NLS-1$
	String MESSAGE_OPEN_MAP_FRAME_FIRST = "Message.OpenMapFrameFirst"; //$NON-NLS-1$

	String ERROR_MAP_EXCEPTION_TOPOLOGICAL_IMAGE_TIMEOUT = "Error.MapException.TopologicalImageTimeout"; //$NON-NLS-1$
	String ERROR_WRITE_ERROR = "Error.WriteError"; //$NON-NLS-1$
	String ERROR_CABLE_END_ELEMENTS_NOT_PLACED = "Error.CableEndElementsNotPlaced"; //$NON-NLS-1$
	String ERROR_NUMBER_FORMAT = "Error.NumberFormat"; //$NON-NLS-1$
	String ERROR_XML_EXCEPTION = "Error.XmlException"; //$NON-NLS-1$
	String ERROR_APPLICATION_EXCEPTION = "Error.ApplicationException"; //$NON-NLS-1$
	String ERROR_MAP_EXCEPTION_SERVER_CONNECTION = "Error.MapException.ServerConnection"; //$NON-NLS-1$
	String ERROR_MAP_EXCEPTION_DATA = "Error.MapException.Data"; //$NON-NLS-1$
	String ERROR_MAP_VIEW_SAVED_BUT_FAILED_TO_OPEN = "Error.MapViewSavedButFailedToOpen"; //$NON-NLS-1$
	String ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE = "Error.LinkedObjectsExistCannotRemove"; //$NON-NLS-1$
	String ERROR_LINKED_OBJECTS_EXIST_CANNOT_CLOSE = "Error.LinkedObjectsExistCannotClose"; //$NON-NLS-1$

	String DOT = "."; //$NON-NLS-1$
	String EXTENSION_GIF = "gif"; //$NON-NLS-1$
	String EXTENSION_XML = "xml"; //$NON-NLS-1$
	String EXTENSION_ESF = "esf"; //$NON-NLS-1$
	String EXTENSION_DOT_ESF = DOT + EXTENSION_ESF;
	String EXTENSION_DOT_XML = DOT + EXTENSION_XML;

	String EMPTY_STRING = ""; //$NON-NLS-1$
	String IS_ACOPY_IN_PARENTHESIS = "Helper.IsACopyInParenthesis"; //$NON-NLS-1$
	String COPY_OF = "Helper.CopyOf"; //$NON-NLS-1$
	String NONAME = "Helper.NoName"; //$NON-NLS-1$
	String IMPORTED = "Helper.Imported"; //$NON-NLS-1$
	String OUT_OF_LOWERCASE = "Helper.OutOf_lowercase"; //$NON-NLS-1$
	String TO_LOWERCASE = "Helper.To_lowercase"; //$NON-NLS-1$
	String FROM_LOWERCASE = "Helper.From_lowercase"; //$NON-NLS-1$
	String TO_NODE_LOWERCASE = "Helper.ToNode_lowercase"; //$NON-NLS-1$
	String DISTANCE_LOWERCASE = "Helper.Distance_lowercase"; //$NON-NLS-1$
	String PATH_LOWERCASE = "Helper.Path_lowercase"; //$NON-NLS-1$

	String BUTTON_OK = "Button.OK"; //$NON-NLS-1$
	String BUTTON_APPLY = "Button.Apply"; //$NON-NLS-1$
	String BUTTON_CANCEL = "Button.Cancel"; //$NON-NLS-1$
	String BUTTON_CHOOSE = "Button.Choose"; //$NON-NLS-1$
	String BUTTON_CHANGE = "Button.Change"; //$NON-NLS-1$
	String BUTTON_ADD = "Button.Add"; //$NON-NLS-1$
	String BUTTON_REMOVE = "Button.Remove"; //$NON-NLS-1$
	String BUTTON_SELECT_ELEMENT = "Button.SelectElement"; //$NON-NLS-1$
	String BUTTON_UNBIND_CABLE = "Button.UnbindCable"; //$NON-NLS-1$
	String BUTTON_UNBIND = "Button.Unbind"; //$NON-NLS-1$
	String BUTTON_BIND_CHAIN = "Button.BindChain"; //$NON-NLS-1$
	String BUTTON_BIND = "Button.Bind"; //$NON-NLS-1$
	String BUTTON_NUMBERING_DIRECTION = "Button.NumberingDirection"; //$NON-NLS-1$
	String BUTTON_VERTICAL_ORDER = "Button.VerticalOrder"; //$NON-NLS-1$
	String BUTTON_HORIZONTAL_ORDER = "Button.HorizontalOrder"; //$NON-NLS-1$
	String BUTTON_UNBIND_LINK_BINDING = "Button.UnbindLinkBinding"; //$NON-NLS-1$
	String BUTTON_BIND_CABLE_TO_PIPE = "Button.BindCableToPipe"; //$NON-NLS-1$

	String ENTITY_TOPOLOGICAL_NODE = "Entity.TopologicalNode"; //$NON-NLS-1$
	String ENTITY_SITE_NODE = "Entity.SiteNode"; //$NON-NLS-1$
	String ENTITY_ALARM = "Entity.Alarm"; //$NON-NLS-1$
	String ENTITY_CABLEPATH = "Entity.CablePath"; //$NON-NLS-1$
	String ENTITY_COLLECTOR = "Entity.Collector"; //$NON-NLS-1$
	String ENTITY_EVENT = "Entity.Event"; //$NON-NLS-1$
	String ENTITY_MARKER = "Entity.Marker"; //$NON-NLS-1$
	String ENTITY_MARK = "Entity.Mark"; //$NON-NLS-1$
	String ENTITY_MEASUREMENT_PATH = "Entity.MeasurementPath"; //$NON-NLS-1$
	String ENTITY_NODE_LINK = "Entity.NodeLink"; //$NON-NLS-1$
	String ENTITY_PHYSICAL_LINK_TYPE = "Entity.PhysicalLinkType"; //$NON-NLS-1$
	String ENTITY_SITE_NODE_TYPE = "Entity.SiteNodeType"; //$NON-NLS-1$
	String ENTITY_PHYSICAL_LINK = "Entity.PhysicalLink"; //$NON-NLS-1$
	String ENTITY_PIQUET = "Entity.Piquet"; //$NON-NLS-1$

	String LABEL_SCALE = "Label.Scale"; //$NON-NLS-1$
	String LABEL_SITE_BINDING = "Label.SiteBinding"; //$NON-NLS-1$
	String LABEL_SCHEME_ELEMENT = "Label.SchemeElement"; //$NON-NLS-1$
	String LABEL_DISTANCES_TO_NEAREST_NODES = "Label.DistancesToNearestNodes"; //$NON-NLS-1$
	String LABEL_CABLES_BINDING = "Label.CablesBinding"; //$NON-NLS-1$
	String LABEL_CABLE_BINDING = "Label.CableBinding"; //$NON-NLS-1$
	String LABEL_MAP = "Label.Map"; //$NON-NLS-1$
	String LABEL_PLACE_IN_COLLECTOR = "Label.PlaceInCollector"; //$NON-NLS-1$
	String LABEL_PLACE_IN_TUNNEL = "Label.PlaceInTunnel"; //$NON-NLS-1$
	String LABEL_SCHEMES = "Label.Schemes"; //$NON-NLS-1$
	String LABEL_LAYERS = "Label.Layers"; //$NON-NLS-1$
	String LABEL_TOPOLOGICAL_LENGTH = "Label.TopologicalLength"; //$NON-NLS-1$
	String LABEL_PHYSICAL_LENGTH = "Label.PhysicalLength"; //$NON-NLS-1$
	String LABEL_OPTICAL_LENGTH = "Label.OpticalLength"; //$NON-NLS-1$
	String LABEL_STYLE = "Label.Style"; //$NON-NLS-1$
	String LABEL_THICKNESS = "Label.Thickness"; //$NON-NLS-1$
	String LABEL_LABEL = "Label.Label"; //$NON-NLS-1$
	String LABEL_IN_LIBRARY = "Label.InLibrary"; //$NON-NLS-1$
	String LABEL_IMAGE = "Label.Image"; //$NON-NLS-1$
	String LABEL_GEOGRAPHICAL_COORDINATES = "Label.GeographicalCoordinates"; //$NON-NLS-1$
	String LABEL_START_SPARE = "Label.StartSpare"; //$NON-NLS-1$
	String LABEL_END_SPARE = "Label.EndSpare"; //$NON-NLS-1$
	String LABEL_FROM_END = "Label.FromEnd"; //$NON-NLS-1$
	String LABEL_FROM_START = "Label.FromStart"; //$NON-NLS-1$
	String LABEL_TUNNEL = "Label.Tunnel"; //$NON-NLS-1$
	String LABEL_NODE = "Label.Node"; //$NON-NLS-1$
	String LABEL_END_NODE = "Label.EndNode"; //$NON-NLS-1$
	String LABEL_START_NODE = "Label.StartNode"; //$NON-NLS-1$
	String LABEL_SELECTION_COUNT = "Label.SelectionCount"; //$NON-NLS-1$
	String LABEL_ELEMENTS = "Label.Elements"; //$NON-NLS-1$
	String LABEL_DISTANCE = "Label.Distance"; //$NON-NLS-1$
	String LABEL_DIMENSION = "Label.Dimension"; //$NON-NLS-1$
	String LABEL_DOMAIN = "Label.Domain"; //$NON-NLS-1$
	String LABEL_USER = "Label.User"; //$NON-NLS-1$
	String LABEL_MODIFIED = "Label.Modified"; //$NON-NLS-1$
	String LABEL_CREATED = "Label.Created"; //$NON-NLS-1$
	String LABEL_COMPONENTS = "Label.Components"; //$NON-NLS-1$
	String LABEL_COLOR = "Label.Color"; //$NON-NLS-1$
	String LABEL_STREET = "Label.Street"; //$NON-NLS-1$
	String LABEL_ALL = "Label.All"; //$NON-NLS-1$
	String LABEL_ADDRESS = "Label.Address"; //$NON-NLS-1$
	String LABEL_CABLE = "Label.Cable"; //$NON-NLS-1$
	String LABEL_DESCRIPTION = "Label.Description"; //$NON-NLS-1$
	String LABEL_LONGITUDE = "Label.Longitude"; //$NON-NLS-1$
	String LABEL_LATITUDE = "Label.Latitude"; //$NON-NLS-1$
	String LABEL_TYPE = "Label.Type"; //$NON-NLS-1$
	String LABEL_NAME = "Label.Name"; //$NON-NLS-1$
	String LABEL_CITY_KURZ = "Label.CityKurz"; //$NON-NLS-1$
	String LABEL_STREET_KURZ = "Label.StreetKurz"; //$NON-NLS-1$
	String LABEL_BUILDING_KURZ = "Label.BuildingKurz"; //$NON-NLS-1$
	String LABEL_MARKER_INFO = "Label.MarkerInfo"; //$NON-NLS-1$
	String LABEL_SORT = "Label.Sort"; //$NON-NLS-1$

	String POPUP_GENERATE_CABLING = "Popup.GenerateCabling"; //$NON-NLS-1$
	String POPUP_BIND = "Popup.Bind"; //$NON-NLS-1$
	String POPUP_GENERATE_SITE = "Popup.GenerateSite"; //$NON-NLS-1$
	String POPUP_ATTACH_CABLE_INLET = "Popup.AttachCableInlet"; //$NON-NLS-1$
	String POPUP_ADD_SITE = "Popup.AddSite"; //$NON-NLS-1$
	String POPUP_ADD_MARKER = "Popup.AddMarker"; //$NON-NLS-1$
	String POPUP_CREATE_COLLECTOR = "Popup.CreateCollector"; //$NON-NLS-1$
	String POPUP_ADD_MARK = "Popup.AddMark"; //$NON-NLS-1$
	String POPUP_DELETE = "Popup.Delete"; //$NON-NLS-1$
	String POPUP_REMOVE_FROM_COLLECTOR = "Popup.RemoveFromCollector"; //$NON-NLS-1$
	String POPUP_REMOVE_COLLECTOR = "Popup.RemoveCollector"; //$NON-NLS-1$
	String POPUP_ADD_TO_COLLECTOR = "Popup.AddToCollector"; //$NON-NLS-1$
	String POPUP_PLACE_SITE = "Popup.PlaceSite"; //$NON-NLS-1$
	String POPUP_COPY = "Popup.Copy"; //$NON-NLS-1$

	String STATUS_MAP_OPENING = "Status.MapOpening"; //$NON-NLS-1$
	String STATUS_ADDING_INTERNAL_MAP = "Status.AddingInternalMap"; //$NON-NLS-1$
	String STATUS_ADDING_EXTERNAL_NODE = "Status.AddingExternalNode"; //$NON-NLS-1$
	String STATUS_CREATING_NEW_MAP = "Status.CreatingNewMap"; //$NON-NLS-1$
	String STATUS_MAP_SAVING = "Status.MapSaving"; //$NON-NLS-1$
	String STATUS_REMOVING_SCHEME_FROM_MAP_VIEW = "Status.RemovingSchemeFromMapView"; //$NON-NLS-1$
	String STATUS_MAP_VIEW_OPENING = "Status.MapViewOpening"; //$NON-NLS-1$
	String STATUS_ADDING_SCHEME_TO_MAP_VIEW = "Status.AddingSchemeToMapView"; //$NON-NLS-1$
	String STATUS_REMOVING_INTERNAL_MAP = "Status.RemovingInternalMap"; //$NON-NLS-1$
	String STATUS_MAP_VIEW_SAVING = "Status.MapViewSaving"; //$NON-NLS-1$
	String STATUS_CREATING_NEW_MAP_VIEW = "Status.CreatingNewMapView"; //$NON-NLS-1$

	String TITLE_ADDITIONAL_PROPERTIES = "Title.AdditionalProperties"; //$NON-NLS-1$
	String TITLE_PROPERTIES = "Title.Properties"; //$NON-NLS-1$
	String TITLE_CHARACTERISTICS = "Title.Characteristics"; //$NON-NLS-1$
	String TITLE_CHOOSE_MAP = "Title.ChooseMap"; //$NON-NLS-1$
	String TITLE_CONFIGURE_TOPOLOGICAL_LAYERS = "Title.ConfigureTopologicalLayers"; //$NON-NLS-1$
	String TITLE_IMAGES_DIALOG_IMAGE_SELECTION = "Title.ImagesDialog.ImageSelection"; //$NON-NLS-1$
	String TITLE_MAP = "Title.Map"; //$NON-NLS-1$
	String TITLE_MAP_EDITOR = "Title.MapEditor"; //$NON-NLS-1$
	String TITLE_MAP_LIBRARY = "Title.MapLibrary"; //$NON-NLS-1$
	String TITLE_MAP_VIEW = "Title.MapView"; //$NON-NLS-1$
	String TITLE_MAP_PROPERTIES = "Title.MapProperties"; //$NON-NLS-1$
	String TITLE_MAP_VIEW_PROPERTIES = "Title.MapViewProperties"; //$NON-NLS-1$
	String TITLE_OBJECT_WAS_CHANGED = "Title.ObjectWasChanged"; //$NON-NLS-1$
	String TITLE_NAVIGATOR = "Title.Navigator"; //$NON-NLS-1$
	String TITLE_SCHEME = "Title.Scheme"; //$NON-NLS-1$

	String TOOLTIP_ZOOM_IN = "Tooltip.ZoomIn"; //$NON-NLS-1$
	String TOOLTIP_ZOOM_OUT = "Tooltip.ZoomOut"; //$NON-NLS-1$
	String TOOLTIP_ZOOM_TO_POINT = "Tooltip.ZoomToPoint"; //$NON-NLS-1$
	String TOOLTIP_ZOOM_BOX = "Tooltip.ZoomBox"; //$NON-NLS-1$
	String TOOLTIP_VIEW_NODES = "Tooltip.ViewNodes"; //$NON-NLS-1$
	String TOOLTIP_VIEW_INDICATION = "Tooltip.ViewIndication"; //$NON-NLS-1$
	String TOOLTIP_UNDO = "Tooltip.Undo"; //$NON-NLS-1$
	String TOOLTIP_REDO = "Tooltip.Redo"; //$NON-NLS-1$
	String TOOLTIP_MAP_FRAME_OPTIONS = "Tooltip.MapFrameOptions"; //$NON-NLS-1$
	String TOOLTIP_PHYSICAL_LINK_MODE = "Tooltip.PhysicalLinkMode"; //$NON-NLS-1$
	String TOOLTIP_MEASUREMENT_PATH_MODE = "Tooltip.MeasurementPathMode"; //$NON-NLS-1$
	String TOOLTIP_NODE_LINK_MODE = "Tooltip.NodeLinkMode"; //$NON-NLS-1$
	String TOOLTIP_CABLE_MODE = "Tooltip.CableMode"; //$NON-NLS-1$
	String TOOLTIP_CENTER_SELECTION = "Tooltip.CenterSelection"; //$NON-NLS-1$
	String TOOLTIP_REDUCE_ICON = "Tooltip.ReduceIcon"; //$NON-NLS-1$
	String TOOLTIP_ENLARGE_ICON = "Tooltip.EnlargeIcon"; //$NON-NLS-1$
	String TOOLTIP_HAND_PAN = "Tooltip.HandPan"; //$NON-NLS-1$
	String TOOLTIP_LAYERS = LABEL_LAYERS;
	String TOOLTIP_MOVE_TO_CENTER = "Tooltip.MoveToCenter"; //$NON-NLS-1$
	String TOOLTIP_MOVE_FIXED = "Tooltip.MoveFixed"; //$NON-NLS-1$
	String TOOLTIP_MEASURE_DISTANCE = "Tooltip.MeasureDistance"; //$NON-NLS-1$
	String TITLE_MEASURE_DISTANCE = TOOLTIP_MEASURE_DISTANCE;

	String TREE_ALL_MAPS = "Tree.AllMaps"; //$NON-NLS-1$
	String TREE_EXTERNAL_NODE = "Tree.ExternalNode"; //$NON-NLS-1$
	String TREE_INNER_MAPS = "Tree.InnerMaps"; //$NON-NLS-1$
	String TREE_SCHEME_PATHS = "Tree.SchemePaths"; //$NON-NLS-1$
	String TREE_SCHEME_CABLE_LINKS = "Tree.SchemeCableLinks"; //$NON-NLS-1$
	String TREE_SCHEME_LINKS = "Tree.SchemeLinks"; //$NON-NLS-1$
	String TREE_SCHEME_ELEMENTS = "Tree.SchemeElements"; //$NON-NLS-1$
	String TREE_INNER_SCHEMES = "Tree.InnerSchemes"; //$NON-NLS-1$
	String TREE_ALL_MAP_VIEWS = "Tree.AllMapViews"; //$NON-NLS-1$
	String TREE_SCHEMES = LABEL_SCHEMES;
	String TREE_LIBRARIES = "Tree.Libraries"; //$NON-NLS-1$
	String TREE_MAP = "Tree.Map"; //$NON-NLS-1$
	String TREE_MAP_EDITOR_ROOT = "Tree.MapEditorRoot"; //$NON-NLS-1$
	String TREE_ELEMENTS_IN_NODE = "Tree.ElementsInNode"; //$NON-NLS-1$
	String TREE_TOPOLOGY = "Tree.Topology"; //$NON-NLS-1$

	String VALUE_DEFAULT_COLLECTOR_NAME = "Value.DefaultCollectorName"; //$NON-NLS-1$
	String VALUE_NEW_MAP_VIEW = "Value.NewMapView"; //$NON-NLS-1$
	String VALUE_ALARMED_COLOR = "Value.AlarmedColor"; //$NON-NLS-1$
	String VALUE_ALARMED_THICKNESS = "Value.AlarmedThickness"; //$NON-NLS-1$
	String VALUE_COLOR = "Value.Color"; //$NON-NLS-1$
	String VALUE_THICKNESS = "Value.Thickness"; //$NON-NLS-1$
	String VALUE_STYLE = "Value.Style"; //$NON-NLS-1$
	String VALUE_METRIC = "Value.Metric"; //$NON-NLS-1$
	String VALUE_NO_DESCRIPTION = "Value.NoDescription"; //$NON-NLS-1$
	String VALUE_NEW = "Value.New"; //$NON-NLS-1$
	String VALUE_UPDATING = "Value.Updating"; //$NON-NLS-1$

	String LABEL_PIPEBLOCK = "Label.PipeBlock"; //$NON-NLS-1$

	String LABEL_NUMBER = "Label.Number";

	String ERROR_UNBOUND_ELEMENTS_EXIST = "Error.UnboundElementsExist";

	String ERROR = "Error.Error";

	String LABEL_COLLECTOR = "Label.Collector";

}
