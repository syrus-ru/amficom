package com.syrus.AMFICOM.Client.General.UI;

public final class ObjectResourceCatalogActionModel
{
	static public final boolean PANEL = true;
	static public final boolean NO_PANEL = false;
	static public final boolean ADD_BUTTON = true;
	static public final boolean NO_ADD_BUTTON = false;
	static public final boolean SAVE_BUTTON = true;
	static public final boolean NO_SAVE_BUTTON = false;
	static public final boolean REMOVE_BUTTON = true;
	static public final boolean NO_REMOVE_BUTTON = false;
	static public final boolean PROPS_BUTTON = true;
	static public final boolean NO_PROPS_BUTTON = false;
	static public final boolean CANCEL_BUTTON = true;
	static public final boolean NO_CANCEL_BUTTON = false;

	boolean panel = false;
	boolean add_button = false;
	boolean save_button = false;
	boolean remove_button = false;
	boolean props_button = false;
	boolean cancel_button = false;

	public ObjectResourceCatalogActionModel(
			boolean panel,
			boolean add_button,
			boolean save_button,
			boolean remove_button,
			boolean props_button,
			boolean cancel_button)
	{
		this.panel = panel;
		this.add_button = add_button;
		this.save_button = save_button;
		this.remove_button = remove_button;
		this.props_button = props_button;
		this.cancel_button = cancel_button;
	}
}
