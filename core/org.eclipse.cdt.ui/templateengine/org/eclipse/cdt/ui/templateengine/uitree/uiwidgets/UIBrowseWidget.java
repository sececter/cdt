/*******************************************************************************
 * Copyright (c) 2007 Symbian Software Limited and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Bala Torati (Symbian) - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.ui.templateengine.uitree.uiwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.cdt.ui.templateengine.uitree.InputUIElement;
import org.eclipse.cdt.ui.templateengine.uitree.UIAttributes;
import org.eclipse.cdt.ui.templateengine.uitree.UIElement;


/**
 * This gives a Label and Browse widget.
 */

public class UIBrowseWidget extends UITextWidget implements ModifyListener {

	/**
	 * Browse Button of this widget.
	 */
	protected  Button button;

	/**
	 * Constructor.
	 * 
	 * @param uiAttribute
	 *            attribute associated with this widget.
	 */
	public UIBrowseWidget(UIAttributes uiAttribute) {
		super(uiAttribute);
		this.textValue = (String) uiAttribute.get(InputUIElement.DEFAULT);
	}

	/**
	 * create a Label and Browse widget, add it to UIComposite. set Layout for
	 * the widgets to be added to UIComposite. set required parameters to the
	 * Widgets.
	 * 
	 * @param composite
	 */
	public void createWidgets(UIComposite composite) {
		uiComposite = composite;

		label = new Label(uiComposite, SWT.NONE | SWT.LEFT);
		label.setText((String) uiAttributes.get(InputUIElement.WIDGETLABEL));

		// set the tool tip text
		if (uiAttributes.get(UIElement.DESCRIPTION) != null){
			String tipText = (String) uiAttributes.get(UIElement.DESCRIPTION);
			tipText = tipText.replaceAll("\\\\r\\\\n", "\r\n"); //$NON-NLS-1$, //$NON-NLS-2$
			label.setToolTipText(tipText);
		}
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 70;

		Composite textConatiner = new Composite(uiComposite, SWT.NONE);
		textConatiner.setLayout(new GridLayout(2, false));

		textConatiner.setLayoutData(gridData);

		text = new Text(textConatiner, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(this);
		text.setText(textValue);

		button = new Button(textConatiner, SWT.PUSH | SWT.LEFT);
		button.setText(InputUIElement.BROWSELABEL);

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String fileName = new FileDialog(uiComposite.getShell()).open();
				if (fileName != null) {
					textValue = fileName.toString();
					text.setText(textValue);
				}
			}
		});
	}

	/**
	 * call the dispose method on the widgets. This is to ensure that the
	 * widgets are properly disposed.
	 */
	public void disposeWidget() {
		label.dispose();
		text.dispose();
		button.dispose();
	}
}
