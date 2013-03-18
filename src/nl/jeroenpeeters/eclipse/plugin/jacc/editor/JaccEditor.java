package nl.jeroenpeeters.eclipse.plugin.jacc.editor;

import nl.jeroenpeeters.eclipse.plugin.jacc.ColorManager;

import org.eclipse.ui.editors.text.TextEditor;

public class JaccEditor extends TextEditor {

	private ColorManager colorManager;

	public JaccEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new JaccConfiguration(colorManager));
		setDocumentProvider(new JaccDocumentProvider());
	}
	
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
