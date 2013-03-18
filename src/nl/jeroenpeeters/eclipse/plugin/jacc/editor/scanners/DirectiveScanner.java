package nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners;

import java.util.ArrayList;
import java.util.List;

import nl.jeroenpeeters.eclipse.plugin.jacc.ColorManager;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.JaccColorConstants;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.XMLWhitespaceDetector;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;

public class DirectiveScanner extends AbstractJaccRuleBasedScanner {

	public DirectiveScanner(final ColorManager manager) {
		super(manager, JaccColorConstants.DEFAULT, SWT.BOLD);
		
		List<IRule> rules = new ArrayList<IRule>();
		//Add rule for processing instructions
		rules.add(new SingleLineRule("%", " ", this.getStyledToken(JaccColorConstants.JACC_DIRECTIVE_TOKEN)));
		rules.add(new SingleLineRule("<", ">", this.getStyledToken(JaccColorConstants.JACC_DIRECTIVE_KEYWORD)));
		rules.add(new SingleLineRule("'", "'", this.getStyledToken(JaccColorConstants.JACC_DIRECTIVE_LITERAL)));
		
		rules.add(new WhitespaceRule(new XMLWhitespaceDetector()));
		// Add generic whitespace rule.
		//rules[1] = new WhitespaceRule(new XMLWhitespaceDetector());

		setRules(rules.toArray(new IRule[rules.size()]));
	}
	
}
