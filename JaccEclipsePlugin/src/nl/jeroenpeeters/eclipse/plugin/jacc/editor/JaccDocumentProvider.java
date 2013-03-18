package nl.jeroenpeeters.eclipse.plugin.jacc.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class JaccDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new JaccPartitionScanner(),
					new String[] {
						JaccPartitionScanner.JACC_DIRECTIVE,
						JaccPartitionScanner.JACC_IMPORTS,
						JaccPartitionScanner.JACC_RULES
					});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}