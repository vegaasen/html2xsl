package com.telenor.xsl.elements.execute;

import com.telenor.xsl.elements.gui.model.Size;
import com.telenor.xsl.elements.gui.view.ParserGui;

import javax.swing.*;

/**
 * "ParserGUI Executable"
 * 
 * @author vegaasen
 */
public class RunDomParseGui extends JFrame {

    public static void main(String... args) {
        ParserGui parserGui = new ParserGui();
        Size s = new Size();
        
        s.setHeight(300);
        s.setWidth(400);

        parserGui.setW_size(s);
        parserGui.setW_title("Test");

        parserGui.openGui();
    }

}
