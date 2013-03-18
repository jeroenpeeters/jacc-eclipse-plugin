package nl.jeroenpeeters.eclipse.plugin.jacc.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.*;

public class JaccPartitionScanner extends RuleBasedPartitionScanner {
	//public final static String XML_COMMENT = "__xml_comment";
	//public final static String XML_TAG = "__xml_tag";
	
	public final static String JACC_DIRECTIVE = "__jacc_directive";
	
	public final static String JACC_IMPORTS = "__jacc_imports";
	
	public final static String JACC_RULES = "__jacc_rules";
	
	public final static String JACC_JAVA = "__jacc_java";
	
	public JaccPartitionScanner() {
		
		this.setDefaultReturnToken(new Token(IDocument.DEFAULT_CONTENT_TYPE));
		
		//IToken xmlComment = new Token(XML_COMMENT);
		//IToken tag = new Token(XML_TAG);
		IToken directive = new Token(JACC_DIRECTIVE);
		IToken imports = new Token(JACC_IMPORTS);
		IToken rule = new Token(JACC_RULES);
		IToken java = new Token(JACC_JAVA);

		IPredicateRule[] rules = new IPredicateRule[3];
		
		rules[0] = new MultiLineRule("%%", "%%", rule, '/', true);
		//rules[1] = new MultiLineRule("%%", "", java, '\0', true);
		rules[1] = new MultiLineRule("%{", "%}", imports);
		rules[2] = new SingleLineRule("%", "\n", directive);
		
		
		
		setPredicateRules(rules);
	}
	
	@Override
	public IToken nextToken(){
		IToken t = super.nextToken();
		System.out.println("Token: " + t.getData());
		
		return  t;
	}
}
