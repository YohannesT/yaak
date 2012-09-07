/*
 * 
 * |\     /|(  ___  )(  ___  )| \    /\
 * ( \   / )| (   ) || (   ) ||  \  / /
 *  \ (_) / | (___) || (___) ||  (_/ / 
 *   \   /  |  ___  ||  ___  ||   _ (  
 *    ) (   | (   ) || (   ) ||  ( \ \ 
 *    | |   | )   ( || )   ( ||  /  \ \
 *    \_/   |/     \||/     \||_/    \/
 * 
 * Yet another Accessible Keyboard
 * Author: Sebastian & David
 * Coding Event SS12
 */


package at.starcoders.yaak;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import at.starcoders.yaak.R;
import at.starcoders.yaak.communication.Sink;
import at.starcoders.yaak.communication.tcp.TCPServer;
import at.starcoders.yaak.painter.Painter;
import at.starcoders.yaak.scanning.ButtonPressListener;
import at.starcoders.yaak.scanning.Scanner;
import at.starcoders.yaak.trigger.Trigger;
import at.starcoders.yaak.trigger.UniversalTouchTrigger;

public class YAAK extends Activity implements ButtonPressListener{
	
	private YaakView view;
	
	private Trigger trigger;
	
	private Sink server;
	
	private Keyboard kb ;
	
	private Scanner scanner;
	private Painter p;
	
	private XMLKeyboardLoader loader;
	
	private WakeLock wl;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        // Powermanager to disable screen off 
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
        
        Logger.log("YAAK started - WUHAHAHAAAA!!!");
        getWifiIp();
        loader = new XMLKeyboardLoader();
        
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard.getAbsolutePath() + "/yaak/default.xml");
        if (!file.exists()) {
        	Logger.loge("default.xml file doesn't exist!");
        	showFileNotFoundDialog();
        	if (wl != null)
        		wl.release();
        	return;
        }
        InputStream in = null;
        try {
        	in = new BufferedInputStream(new FileInputStream(file));
        	loader.parseXML(in);
        } catch (Exception ex) {
        	Logger.loge("damit!!", ex);
        } finally {
        	if (in != null) {
        		try { in.close(); }
        		catch (IOException ioe) { Logger.log(ioe.getMessage()); }
        	}
        }
        
        Keyboard kb = loader.getKeyboard();
        Painter p = loader.getPainter();
        view = new YaakView(this,p);
        
        server = loader.getCommunicationSink();
        
        scanner = loader.getScanner();
		server.addTriggerListener(scanner);
		Thread serverThread = new Thread(server);
    	serverThread.start();
        scanner.addButtonPressListener(this);
		view = new YaakView(this,p);

   	 	setContentView(view);

        scanner.setView(view);
		scanner.start();
//      setContentView(R.layout.main);
        trigger = new UniversalTouchTrigger();
        trigger.addTriggerListener(scanner);
    }    
    
	private void showFileNotFoundDialog() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	       builder.setMessage(R.string.notfound)
	              .setCancelable(false)
	              .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
	                  public void onClick(DialogInterface dialog, int id) {
	                	  onDestroy();
	                      YAAK.this.finish();
	                  }
	              });
	       AlertDialog alert = builder.create();
	       alert.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (trigger.analyzeEvent(event)) {
			return true;
		}
		return false;
//		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (server != null)
			server.close();
		if (wl != null)
			wl.release();
	}

	@Override
	public void buttonPressed(Button b) {
		String action = b.getAction();
		if (action.startsWith("@load:")) {
			String filename = action.substring(6);
			// TODO fix double output when switch over tcp bug
			Logger.log("YAAK.buttonPressed(): Load new keyboard");
			scanner.close();
			
			loader = new XMLKeyboardLoader();
			

	        File sdcard = Environment.getExternalStorageDirectory();
	        File file = new File(sdcard.getAbsolutePath() + "/yaak/"+filename);
	        if (!file.exists()) {
	        	Logger.loge(sdcard.getAbsolutePath() + "/yaak/" + filename + " file doesn't exist!");
	        	YAAK.this.finish();
	        }
	        InputStream in = null;
	        try {
	        	in = new BufferedInputStream(new FileInputStream(file));
	        	loader.parseXML(in);
	        } catch (Exception ex) {
	        	Logger.log("damit!!", ex);
	        } finally {
	        	if (in != null) {
	        		try { in.close(); }
	        		catch (IOException ioe) { Logger.log(ioe.getMessage()); }
	        	}
	        }
			
	        kb = loader.getKeyboard();
	        p = loader.getPainter();
	        view.setPainter(p);
	        //server = loader.getCommunicationSink();
	        server.removeListener(scanner);
	        scanner = loader.getScanner();
	        scanner.addButtonPressListener(this);
	        server.addTriggerListener(scanner);
	        
			scanner.setView(view);
			scanner.start();
			trigger.clearTriggerListeners();
	        trigger.addTriggerListener(scanner);
		} else if (action.startsWith("@intent:")) {
			final Intent intent = new Intent(Intent.ACTION_MAIN, null);
			final ComponentName cn = new ComponentName(
					"com.android.settings",
					"com.android.settings.fuelgauge.PowerUsageSummary");
			intent.setComponent(cn);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intent);

		} else if (action.equalsIgnoreCase("@quit")) {
			if (server != null)
				server.sendMessage(action);
			onDestroy();
			YAAK.this.finish();
		} else {
			if (server != null)
				server.sendMessage(action);
		}
	}

	
	private String getWifiIp() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		if (wifiManager == null)
			return "IP: x.x.x.x";
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
		byte[] reversed = new byte[bytes.length];
		for (int i = bytes.length-1; i >= 0;i--) {
			reversed[bytes.length-1-i] = bytes[i];
		}
		InetAddress address;
		try {
			 address = InetAddress.getByAddress(reversed);
		} catch (UnknownHostException e) {
			return "IP: x.x.x.x";
		}
		if (address.toString().length() > 1)
			return "IP: " + address.toString().substring(1);
		return "IP: x.x.x.x";
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    return true;
	}
	
	@Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	       if (item.getItemId() != R.id.ip)
	    	   return true;
	       AlertDialog.Builder builder = new AlertDialog.Builder(this);
	       builder.setMessage(getWifiIp())
	              .setCancelable(false)
	              .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
	                  public void onClick(DialogInterface dialog, int id) {
	                	  dialog.cancel();
	                  }
	              });
	       AlertDialog alert = builder.create();
	       alert.show();
	       return true;
	   }
}
