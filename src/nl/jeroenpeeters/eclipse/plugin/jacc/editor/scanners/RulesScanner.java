package nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners;

import java.util.ArrayList;
import java.util.List;

import nl.jeroenpeeters.eclipse.plugin.jacc.ColorManager;
import nl.jeroenpeeters.eclipse.plugin.jacc.editor.JaccColorConstants;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class RulesScanner extends AbstractJaccRuleBasedScanner implements IWordDetector {

	public RulesScanner(ColorManager manager) {
		super(manager, JaccColorConstants.DEFAULT, SWT.BOLD);
		
		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("'", "'", this.getStyledToken(JaccColorConstants.JACC_RULES_LITERAL)));
		rules.add(new SingleLineRule("{", "}", this.getStyledToken(JaccColorConstants.JACC_RULES_JAVA_CODE)));
		
		rules.add(new WordRule(this, this.getStyledToken(JaccColorConstants.JACC_RULES_WORD)));
		
		setRules(rules.toArray(new IRule[rules.size()]));
	}

	@Override
	public boolean isWordPart(char c) {
		return this.isWord(c);
	}

	@Override
	public boolean isWordStart(char c) {
		return this.isWord(c);
	}
	
	protected boolean isWord(char c){
		return c == '|' || c == ':' || c == ';';
	}
}
