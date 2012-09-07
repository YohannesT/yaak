package at.starcoders.yaak;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import android.content.res.Resources;
import android.util.Xml;
import at.starcoders.yaak.communication.Sink;
import at.starcoders.yaak.painter.Painter;
import at.starcoders.yaak.scanning.Scanner;

public class XMLKeyboardLoader {

	YaakXMLHandler handler;
	
	public XMLKeyboardLoader() {
		handler =  new YaakXMLHandler();
	}
	
	public boolean parseXML(InputStream is) {
		try {
			Xml.parse(is,Xml.Encoding.UTF_8,handler);
		} catch (SAXException e) {
			System.out.println("Error while parsing");
			return false;
		} catch (IOException e) {
			System.out.println("Error while parsing! IOException");
			return false;
		}
		return true;
	}
	
	
	public Keyboard getKeyboard() {
		return handler.getKeyboard();
	}
	
	public Scanner getScanner() {
		return handler.getScanner();
	}
	
	public Painter getPainter() {
		return handler.getPainter();
	}
	
	public Sink getCommunicationSink() {
		return handler.getCommunicationSink();
	}
	
}
