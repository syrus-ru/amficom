/*
 * $Id: BoxLayout2BeanInfo.java,v 1.1 2005/03/18 09:44:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.Image;
import java.beans.*;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/18 09:44:58 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class BoxLayout2BeanInfo extends SimpleBeanInfo {
	static Class class$oracle$jdeveloper$layout$BoxLayout2;

	static Class class$oracle$jdevimpl$propertyeditor$BoxAxisEditor;

	Class beanClass;

	String iconColor16x16Filename;

	String iconColor32x32Filename;

	String iconMono16x16Filename;

	String iconMono32x32Filename;

	public BoxLayout2BeanInfo() {
		this.beanClass = class$oracle$jdeveloper$layout$BoxLayout2 != null ? class$oracle$jdeveloper$layout$BoxLayout2
				: (class$oracle$jdeveloper$layout$BoxLayout2 = _mthclass$("oracle.jdeveloper.layout.BoxLayout2")); //$NON-NLS-1$
	}

	static Class _mthclass$(String s) {
		try {
			Class class1 = Class.forName(s);
			return class1;
		} catch (ClassNotFoundException classnotfoundexception) {
			throw new NoClassDefFoundError(classnotfoundexception
					.getMessage());
		}
	}

	public Image getIcon(int i) {
		switch (i) {
			case 1: // '\001'
				return this.iconColor16x16Filename == null ? null
						: loadImage(this.iconColor16x16Filename);

			case 2: // '\002'
				return this.iconColor32x32Filename == null ? null
						: loadImage(this.iconColor32x32Filename);

			case 3: // '\003'
				return this.iconMono16x16Filename == null ? null
						: loadImage(this.iconMono16x16Filename);

			case 4: // '\004'
				return this.iconMono32x32Filename == null ? null
						: loadImage(this.iconMono32x32Filename);
		}
		return null;
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor propertydescriptor = new PropertyDescriptor(
					"axis", this.beanClass, "getAxis", "setAxis"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			propertydescriptor.setDisplayName("axis"); //$NON-NLS-1$
			propertydescriptor.setShortDescription("major axis"); //$NON-NLS-1$
			propertydescriptor
					.setPropertyEditorClass(class$oracle$jdevimpl$propertyeditor$BoxAxisEditor != null ? class$oracle$jdevimpl$propertyeditor$BoxAxisEditor
							: (class$oracle$jdevimpl$propertyeditor$BoxAxisEditor = _mthclass$("oracle.jdevimpl.propertyeditor.BoxAxisEditor"))); //$NON-NLS-1$
			PropertyDescriptor apropertydescriptor[] = {propertydescriptor};
			PropertyDescriptor apropertydescriptor2[] = apropertydescriptor;
			return apropertydescriptor2;
		} catch (IntrospectionException introspectionexception) {
			introspectionexception.printStackTrace();
		}
		PropertyDescriptor apropertydescriptor1[] = null;
		return apropertydescriptor1;
	}
}
