/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.editor;

import java.util.ResourceBundle;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.partition.ContentTypeRuleBasedPartitionScannerFactory;
import com.technophobia.substeps.document.content.view.ContentTypeViewConfiguration;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;
import com.technophobia.substeps.document.formatting.partition.PartitionedFormattingContextFactory;
import com.technophobia.substeps.document.partition.PartitionScannedDocumentProvider;

public class FeatureEditor extends TextEditor {

	private final ColourManager colourManager;

	public FeatureEditor() {

		final ContentTypeDefinitionFactory contentTypeDefinitionFactory = new FeatureContentTypeDefinitionFactory();
		final FormattingContextFactory formattingContextFactory = new PartitionedFormattingContextFactory(contentTypeDefinitionFactory);
		colourManager = new ColourManager();

		setSourceViewerConfiguration(new ContentTypeViewConfiguration(colourManager, contentTypeDefinitionFactory, formattingContextFactory));
		setDocumentProvider(new PartitionScannedDocumentProvider(new ContentTypeRuleBasedPartitionScannerFactory(contentTypeDefinitionFactory)));
	}

	@Override
	public void dispose() {
		colourManager.dispose();
		super.dispose();
	}

	@Override
	protected void createActions() {
		super.createActions();

		final ResourceBundle resourceBundle = FeatureEditorPlugin.instance().getResourceBundle();
		final TextOperationAction action = new TextOperationAction(resourceBundle, "ContentFormatProposal.", this, ISourceViewer.FORMAT);
		setAction("ContentFormatProposal", action);
		getEditorSite().getActionBars().setGlobalActionHandler("ContentFormatProposal", action);
	}
}
