/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.targets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class IdentificationTargetTransfer extends ByteArrayTransfer {

	private static final Logger logger = Logger.getLogger(IdentificationTargetTransfer.class);

	private static final String TYPE_NAME = "Identification Target";
	private static final int TYPE_ID = registerType(TYPE_NAME);
	private static final IdentificationTargetTransfer INSTANCE = new IdentificationTargetTransfer();

	public static IdentificationTargetTransfer getInstance() {

		return INSTANCE;
	}

	@Override
	protected String[] getTypeNames() {

		return new String[]{TYPE_NAME};
	}

	@Override
	protected int[] getTypeIds() {

		return new int[]{TYPE_ID};
	}

	@Override
	protected void javaToNative(Object object, TransferData transferData) {

		if(object instanceof IIdentificationTarget identificationTarget) {
			try (ByteArrayOutputStream byteArrayOutputstream = new ByteArrayOutputStream()) {
				try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputstream)) {
					objectOutputStream.writeObject(identificationTarget);
					byte[] bytes = byteArrayOutputstream.toByteArray();
					super.javaToNative(bytes, transferData);
				}
			} catch(IOException e) {
				logger.warn(e);
			}
		}
	}

	@Override
	protected Object nativeToJava(TransferData transferData) {

		Object data = super.nativeToJava(transferData);
		if(data instanceof byte[] bytes) {
			try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
				Object object = objectInputStream.readObject();
				if(object instanceof IIdentificationTarget identificationTarget) {
					return identificationTarget;
				}
			} catch(IOException e) {
				logger.warn(e);
			} catch(ClassNotFoundException e) {
				logger.warn(e);
			}
		}

		return data;
	}
}