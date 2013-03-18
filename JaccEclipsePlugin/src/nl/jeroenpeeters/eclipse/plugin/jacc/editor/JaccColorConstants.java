package nl.jeroenpeeters.eclipse.plugin.jacc.editor;

import org.eclipse.swt.graphics.RGB;

public interface JaccColorConstants {
	
	RGB JACC_DIRECTIVE_TOKEN =  HexHelper.hex2Rgb("#2F3540");
	RGB JACC_DIRECTIVE_KEYWORD =  HexHelper.hex2Rgb("#4E7AC7");
	RGB JACC_DIRECTIVE_LITERAL =  HexHelper.hex2Rgb("#94090D");
	
	RGB JACC_RULES_LITERAL =  HexHelper.hex2Rgb("#94090D");
	RGB JACC_RULES_JAVA_CODE =  HexHelper.hex2Rgb("#8C8681");
	RGB JACC_RULES_WORD =  HexHelper.hex2Rgb("#2F3540");
	
	RGB JACC_IMPORT =  HexHelper.hex2Rgb("#8C8681");
	
	RGB STRING = new RGB(0, 128, 0);
	RGB DEFAULT = new RGB(0, 0, 0);
	RGB TAG = new RGB(0, 0, 128);
	
	public static class HexHelper {
		
		public static RGB hex2Rgb(String colorStr) {
		    return new RGB(
		            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
		            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
		            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
		}
		
	}
}
