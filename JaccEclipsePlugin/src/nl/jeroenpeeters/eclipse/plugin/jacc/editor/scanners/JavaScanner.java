package nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners;

import nl.jeroenpeeters.eclipse.plugin.jacc.ColorManager;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.JaccColorConstants;

import org.eclipse.jface.text.rules.IRule;

public class JavaScanner extends AbstractJaccRuleBasedScanner {

	public JavaScanner(ColorManager manager) {
		super(manager, JaccColorConstants.TAG);

		IRule[] rules = new IRule[0];
		//rules[0] = new MultiLineRule("%%", "%%", getColorStylerLiteral(JaccColorConstants.STRING));

		setRules(rules);
	}
}
