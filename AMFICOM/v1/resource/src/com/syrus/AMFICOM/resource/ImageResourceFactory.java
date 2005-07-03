/*-
 * $Id: ImageResourceFactory.java,v 1.4 2005/06/24 09:40:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/06/24 09:40:48 $
 * @module resource_v1
 */
final class ImageResourceFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObjectFactory#newInstance(IDLEntity)
	 */
	protected StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException {
		final IdlImageResource imageResource = (IdlImageResource) transferable;
		switch (imageResource.data.discriminator().value()) {
			case ImageResourceSort._BITMAP:
				return new BitmapImageResource(imageResource);
			case ImageResourceSort._FILE:
				return new FileImageResource(imageResource);
			case ImageResourceSort._SCHEME:
				return new SchemeImageResource(imageResource);
			default:
				throw new CreateObjectException("ImageResourceFactory.newInstance() | " + ErrorMessages.NATURE_INVALID);
		}
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#getId(org.omg.CORBA.portable.IDLEntity)
	 */
	protected Identifier getId(final IDLEntity transferable) {
		return new Identifier(((IdlImageResource) transferable).header.id);
	}

	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	protected IDLEntity[] allocateArrayOfTransferables(final int length) {
		return new IdlImageResource[length];
	}
}
