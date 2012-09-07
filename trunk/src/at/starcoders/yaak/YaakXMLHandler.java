package at.starcoders.yaak;

import java.io.File;

import org.apache.http.entity.InputStreamEntity;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import at.starcoders.yaak.R;
import at.starcoders.yaak.communication.Sink;
import at.starcoders.yaak.communication.tcp.TCPServer;
import at.starcoders.yaak.painter.*;
import at.starcoders.yaak.scanning.ColumnScanner;
import at.starcoders.yaak.scanning.RowScanner;
import at.starcoders.yaak.scanning.Scanner;
import at.starcoders.yaak.scanning.SingleScanner;

public class YaakXMLHandler extends DefaultHandler {
	
	private Keyboard keyboard;
	private Scanner scanner;
	private Painter painter;

	private StringBuilder sb;
	private int row, col;

	private Button tmpButton;

	private String scanningMethod;
	private int scanTime;
	private int repeatTime;

	private String paintMethod;
	private int globalFontColor;
	private int globalBgColor;
	private int borderColor;
	private float borderSize;
    private int timeoutRounds; 
    
    private Sink communication;
        
    public YaakXMLHandler() {
    	sb = new StringBuilder();
    	row = 0;
    	col = 0;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) 
    		throws SAXException {

    	if (localName.equalsIgnoreCase("keyboard")) {
    		try {
    			int rows = Integer.parseInt(attributes.getValue("rows"));
    			int cols = Integer.parseInt(attributes.getValue("cols"));
    			String color = attributes.getValue("bgcolor");
    			keyboard = new Keyboard(rows, cols);
    			keyboard.setBgColor(Color.parseColor(color));
    		} catch (NumberFormatException ne) {
    			ne.printStackTrace();
    		}

    	} else if (localName.equalsIgnoreCase("tcp")) {
    		int enable = Integer.parseInt(attributes.getValue("enable"));
    		int port = Integer.parseInt(attributes.getValue("port"));
    		if (enable == 1) 
    			communication = new TCPServer(port);
    		else
    			communication = null;
    	} else if (localName.equalsIgnoreCase("button")) {
    		tmpButton = new Button();
    		String bgColor = attributes.getValue("bgColor");
    		if (bgColor != null)
    			tmpButton.setBgColor(Color.parseColor(bgColor));
    		else
    			tmpButton.setBgColor(globalBgColor);
    		String fontColor = attributes.getValue("fontColor");
    		if (fontColor != null)
    			tmpButton.setFontColor(Color.parseColor(fontColor));
    		else
    			tmpButton.setFontColor(globalFontColor);
    	} else if (localName.equalsIgnoreCase("painter")) {
    		paintMethod = attributes.getValue("method");
    		globalFontColor = Color.parseColor(attributes.getValue("fontcolor"));
    		String globalBgCol = attributes.getValue("bgcolor");
    		if (globalBgCol != null)
    			globalBgColor = Color.parseColor(globalBgCol);
    		else
    			globalBgColor = Color.GRAY;
    		String bordCol = attributes.getValue("bordercolor");
    		if (bordCol != null)
    			borderColor = Color.parseColor(bordCol);
    		else
    			borderColor = Color.RED;
    		String bordSize = attributes.getValue("bordersize");
    		if (bordSize != null)
    			borderSize = Float.parseFloat(bordSize);
    		else
    			borderSize = 2.0f;
    	} else if (localName.equalsIgnoreCase("scanner")) {
    		scanningMethod = attributes.getValue("method");
    		scanTime = Integer.parseInt(attributes.getValue("scantime"));
    		repeatTime = Integer.parseInt(attributes.getValue("repeattime"));
    		String timeout = attributes.getValue("timeoutrounds");
    		if (timeout != null)
    			timeoutRounds = Integer.parseInt(timeout);
    		else 
    			timeoutRounds = -1;
    	} else if (localName.equalsIgnoreCase("icon")) {
    		sb = new StringBuilder();
    	} else if (localName.equalsIgnoreCase("text")) {
    		sb = new StringBuilder();
    	} else if (localName.equalsIgnoreCase("action")) {
    		sb = new StringBuilder();
    	}
    }

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		sb.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase("row")) {
			row++;
			col = 0;
		} else if (localName.equalsIgnoreCase("button")) {
			Logger.logv(row + " " + col);
			keyboard.setButton(row,col,tmpButton);
			col++;
		}
		else if (localName.equalsIgnoreCase("icon")) {
			String iconPath = sb.toString(); 
			if (iconPath.length() > 0) {
				tmpButton.setIcon(getIcon(iconPath));
				Logger.logv("Added icon: " + iconPath);
			}
			else
				tmpButton.setIcon(null);
		} else if (localName.equalsIgnoreCase("text")) {
			tmpButton.setText(sb.toString());
		} else if (localName.equalsIgnoreCase("action")) {
			tmpButton.setAction(sb.toString());
		}

	}

	@Override
	public void startDocument() throws SAXException {
		Logger.logv("Start of XML document");
		row = 0;
		col = 0;
	}


	public Painter getPainter() {
		if (paintMethod == null)
			return null;
		if (paintMethod.equalsIgnoreCase("text")) {
			painter = new TextPainter(keyboard);
		} else if (paintMethod.equalsIgnoreCase("icon")) {
			painter = new IconPainter(keyboard);
		} else if (paintMethod.equalsIgnoreCase("invert")) {
			painter = new InvertPainter(keyboard);
		} else if (paintMethod.equalsIgnoreCase("border")) {
			painter = new BorderPainter(keyboard, borderColor, borderSize, globalBgColor);
		} else {
			painter = new SimplePainter(keyboard);
		}
		return painter;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	public Scanner getScanner() {
		if (scanningMethod == null)
			return null;
		if (scanningMethod.equalsIgnoreCase("single")) {
			scanner = new SingleScanner(keyboard,scanTime,repeatTime);
		} else if (scanningMethod.equalsIgnoreCase("row")) {
			scanner = new RowScanner(keyboard,scanTime,repeatTime,timeoutRounds);
		} else if (scanningMethod.equalsIgnoreCase("column")) {
			scanner = new ColumnScanner(keyboard,scanTime,repeatTime,timeoutRounds);
		}
		return scanner;
	}

	public Sink getCommunicationSink() {
		return communication;
	}
	
	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}
	
	private Bitmap getIcon(String path) {
		Bitmap bm = null;
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdcard = Environment.getExternalStorageDirectory();
			bm = BitmapFactory.decodeFile(sdcard.getAbsolutePath() + "/yaak/" + path);
		}
		
		return bm;
	}
}
