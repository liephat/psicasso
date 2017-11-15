package util.logging;

import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * 
 *
 * @author Mike Imhof
 */
public class JTextAreaOutputStream extends OutputStream {

	JTextArea textArea;
	
    public JTextAreaOutputStream (JTextArea textArea) {
        super();
        this.textArea = textArea;
    }

    public void write (int i) {
        char[] chars = new char[1];
        chars[0] = (char) i;
        String s = new String (chars);
        textArea.append(s);
    }
    
    public void write (char[] buf, int off, int len) 
    {
        String s = new String (buf, off, len);
        textArea.append(s);            
    }

}
