/*******************************************************************************
 * Copyright (c) 2010, 2011 Alena Laskavaia and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alena Laskavaia - initial API and implementation
 *     IBM Corporation
 *******************************************************************************/
package org.eclipse.cdt.codan.internal.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.codan.core.model.CheckerLaunchMode;
import org.eclipse.cdt.codan.core.model.Checkers;
import org.eclipse.cdt.codan.core.model.IChecker;
import org.eclipse.cdt.codan.core.model.IProblem;
import org.eclipse.cdt.codan.internal.core.CheckersRegistry;
import org.eclipse.cdt.codan.internal.ui.CodanUIMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferenceStore;

public class LaunchModesPropertyPage extends FieldEditorPreferencePage {
	private final List<FieldEditor> editors;
	private final boolean runInEditor;

	/**
	 * @param problem
	 * @param prefStore
	 */
	public LaunchModesPropertyPage(IProblem problem, PreferenceStore prefStore) {
		super(GRID);
		CheckersRegistry registry = CheckersRegistry.getInstance();
		IChecker checker = registry.getCheckerForProblem(problem);
		if (checker != null) {
			runInEditor = Checkers.canCheckerRunAsYouType(checker);
		} else {
			runInEditor = false;
		}
		setPreferenceStore(prefStore);
		editors = new ArrayList<FieldEditor>();
	}

	@Override
	public void noDefaultAndApplyButton() {
		super.noDefaultAndApplyButton();
	}

	@Override
	protected void createFieldEditors() {
		if (runInEditor) {
			addField(new BooleanFieldEditor(CheckerLaunchMode.RUN_AS_YOU_TYPE.name(), CodanUIMessages.LaunchModesPropertyPage_RunAsYouType, getFieldEditorParent()));
		}
		addField(new BooleanFieldEditor(CheckerLaunchMode.RUN_ON_FILE_OPEN.name(), CodanUIMessages.LaunchModesPropertyPage_RunOnFileOpen, getFieldEditorParent()));
		addField(new BooleanFieldEditor(CheckerLaunchMode.RUN_ON_FILE_SAVE.name(), CodanUIMessages.LaunchModesPropertyPage_RunOnFileSave, getFieldEditorParent()));
		addField(new BooleanFieldEditor(CheckerLaunchMode.RUN_ON_INC_BUILD.name(), CodanUIMessages.LaunchModesPropertyPage_RunOnIncrementalBuild, getFieldEditorParent()));
		addField(new BooleanFieldEditor(CheckerLaunchMode.RUN_ON_FULL_BUILD.name(), CodanUIMessages.LaunchModesPropertyPage_RunOnFullBuild, getFieldEditorParent()));
		addField(new BooleanFieldEditor(CheckerLaunchMode.RUN_ON_DEMAND.name(), CodanUIMessages.LaunchModesPropertyPage_RunOnDemand, getFieldEditorParent()));
	}

	@Override
	protected void addField(FieldEditor editor) {
		editors.add(editor);
		super.addField(editor);
	}

	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		return result;
	}
}
