package nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners;

import nl.jeroenpeeters.eclipse.plugin.jacc.ColorManager;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.JaccColorConstants;

public class ImportScanner extends AbstractJaccRuleBasedScanner {

	public ImportScanner(ColorManager manager) {
		super(manager, JaccColorConstants.JACC_IMPORT);

		/*IRule[] rules = new IRule[2];
		rules[0] = new MultiLineRule("%{", "%}", getColorStylerLiteral(JaccColorConstants.JACC_IMPORT));
		rules[1] = new WhitespaceRule(new XMLWhitespaceDetector());
		
		setRules(rules);
		*/
	}
}
