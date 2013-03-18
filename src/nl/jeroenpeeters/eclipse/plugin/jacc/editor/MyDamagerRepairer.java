package nl.jeroenpeeters.eclipse.plugin.jacc.editor;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;

public class MyDamagerRepairer implements IPresentationDamager, IPresentationRepairer {
	
	private IDocument document;

	@Override
	public IRegion getDamageRegion(ITypedRegion region, DocumentEvent event,
			boolean documentPartitioningChanged) {
		System.out.println("Region: "+region);
		System.out.println("event: "+region);
		System.out.println("documentPartitioningChanged: "+region);
		
		return region;
	}

	@Override
	public void setDocument(IDocument document) {
		this.document = document;
	}

	@Override
	public void createPresentation(TextPresentation arg0, ITypedRegion arg1) {
		
		
	}

}
