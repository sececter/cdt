/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Niefer (IBM) - Initial API and implementation
 *    Markus Schorn (Wind River Systems)
 *******************************************************************************/
package org.eclipse.cdt.internal.core.dom.parser.cpp;

import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassTemplatePartialSpecialization;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPTemplateArgument;

/**
 * Interface for class templates used in the AST.
 */
public interface ICPPInternalClassTemplate extends ICPPInternalTemplate {
	public void addPartialSpecialization(ICPPClassTemplatePartialSpecialization spec);

	/**
	 * Returns a deferred instance that allows lookups within this class template.
	 */
	public ICPPDeferredClassInstance asDeferredInstance() throws DOMException;

	/**
	 * Tries to obtain a default argument for a template parameter from the index.
	 * @throws DOMException
	 */
	public ICPPTemplateArgument getDefaultArgFromIndex(int paramPos) throws DOMException;
}
