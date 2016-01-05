/*
 * Copyright (C) 2016 ezander
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package rcdemo.io;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ezander
 */
public class XMLHelper {

    /**
     *
     * @param parent
     * @param name
     * @return
     */
    public static Element getChildByName(Element parent, String name) {
        return (Element) parent.getElementsByTagName(name).item(0);
    }

    public static ArrayList<Element> getChildrenByName(Element parent, String name) {
        ArrayList<Element> nodes = new ArrayList<>();
        NodeList list = parent.getElementsByTagName(name);
        for (int i = 0; i < list.getLength(); i++) {
            nodes.add((Element) list.item(i));
        }
        return nodes;
    }

    public static String getString(Element parent, String name) {
        Element node = getChildByName(parent, name);
        return node.getTextContent();
    }

    public static double getDouble(Element parent, String name) {
        String s = getString(parent, name);
        return Double.parseDouble(s);
    }

    public static int getInt(Element parent, String name) {
        String s = getString(parent, name);
        return Integer.parseInt(s);
    }

    public static Element loadAndGetDocRoot(String filename) {
        try {
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = builder.parse(filename);
            Element root = doc.getDocumentElement();
            return root;
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            throw new RuntimeException("Error loading XML file: " + filename, ex);
        }
    }
    
}
