package nl.jeroenpeeters.eclipse.plugin.jacc.editor.scanners;

import nl.jeroenpeeters.eclipse.plugin.jacc.ColorManager;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

public class AbstractJaccRuleBasedScanner extends RuleBasedScanner {

	private final ColorManager colorManager;
	
	public AbstractJaccRuleBasedScanner(ColorManager colorManager, RGB defaultColor){
		this(colorManager, defaultColor, SWT.NORMAL);
	}
	
	public AbstractJaccRuleBasedScanner(ColorManager colorManager, RGB defaultColor, int style) {
		super();
		this.colorManager = colorManager;
		
		this.setDefaultReturnToken(this.getStyledToken(defaultColor, style));
	}

	IToken getStyledToken(RGB rgb){
		return new Token(
					new TextAttribute(
							colorManager.getColor(rgb)));
	}
	
	IToken getStyledToken(RGB rgb, int style){
		return new Token(
					new TextAttribute(
							colorManager.getColor(rgb), null, style));
	}
	
}
