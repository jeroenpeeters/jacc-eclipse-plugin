package nl.jeroenpeeters.eclipse.plugin.jacc.editor;

import nl.jeroenpeeters.eclipse.plugin.jacc.ColorManager;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners.DirectiveScanner;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners.ImportScanner;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners.JavaScanner;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners.RulesScanner;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;


public class JaccConfiguration extends SourceViewerConfiguration {
	private XMLDoubleClickStrategy doubleClickStrategy;

	private DirectiveScanner directiveScanner;
	private ImportScanner importScanner;
	private RulesScanner rulesScanner;
	private JavaScanner javaScanner;
	private ColorManager colorManager;

	public JaccConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
				IDocument.DEFAULT_CATEGORY,
				JaccPartitionScanner.JACC_DIRECTIVE,
				JaccPartitionScanner.JACC_IMPORTS,
				JaccPartitionScanner.JACC_RULES};
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new XMLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected DirectiveScanner getDirectiveScanner() {
		if (directiveScanner == null) {
			directiveScanner = new DirectiveScanner(colorManager);
		}
		return directiveScanner;
	}
	
	protected ImportScanner getImportScanner() {
		if (importScanner == null) {
			importScanner = new ImportScanner(colorManager);
		}
		return importScanner;
	}
	
	protected RulesScanner getRulesScanner() {
		if (rulesScanner == null) {
			rulesScanner = new RulesScanner(colorManager);
		}
		return rulesScanner;
	}
	
	protected JavaScanner getJavaScanner() {
		if (javaScanner == null) {
			javaScanner = new JavaScanner(colorManager);
		}
		return javaScanner;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		/*
		 * DefaultDamagerRepairer dr = new
		 * DefaultDamagerRepairer(getXMLTagScanner()); reconciler.setDamager(dr,
		 * JaccPartitionScanner.XML_TAG); reconciler.setRepairer(dr,
		 * JaccPartitionScanner.XML_TAG);
		 */
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getDirectiveScanner());
		reconciler.setDamager(dr, JaccPartitionScanner.JACC_DIRECTIVE);
		reconciler.setRepairer(dr, JaccPartitionScanner.JACC_DIRECTIVE);
		
		dr = new DefaultDamagerRepairer(getImportScanner());
		reconciler.setDamager(dr, JaccPartitionScanner.JACC_IMPORTS);
		reconciler.setRepairer(dr, JaccPartitionScanner.JACC_IMPORTS);
		
		dr = new DefaultDamagerRepairer(getRulesScanner());
		reconciler.setDamager(new MyDamagerRepairer(), JaccPartitionScanner.JACC_RULES);
		reconciler.setRepairer(dr, JaccPartitionScanner.JACC_RULES);
		
		dr = new DefaultDamagerRepairer(getJavaScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		//reconciler.setDamager(new MyDamagerRepairer(), IDocument.DEFAULT_CATEGORY);
		

		// dr = new DefaultDamagerRepairer(getJACCScanner());
		// reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		// reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

//		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
//				new TextAttribute(
//						colorManager.getColor(IXMLColorConstants.STRING)));
//		reconciler.setDamager(ndr, JaccPartitionScanner.JACC_IMPORTS);
//		reconciler.setRepairer(ndr, JaccPartitionScanner.JACC_IMPORTS);

		return reconciler;
	}

}