//package com.rumango.median.iso.client;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import org.apache.log4j.Logger;
//import org.jpos.iso.ISOChannel;
//import org.jpos.iso.ISOException;
//import org.jpos.iso.ISOMsg;
//import org.jpos.iso.ISOPackager;
//import org.jpos.iso.channel.ASCIIChannel;
//import org.jpos.iso.packager.GenericPackager;
//
//public class IsoJposResponse implements Callable<String> {
//
//	private static String response = "SUCCESS";
//	private static ISOMsg responseIsoMsg = null;
//	private ISOMsg isoMsg;
//	private ISOChannel channel;
//	private GenericPackager genericPackager = null;
//	private final static Logger logger = Logger.getLogger(IsoJposResponse.class);
//
//	public static Object[] main() {
//		logger.info("Finacle response code is: " + response);
//		Object[] elements = null;
//		try {
//			elements = new Object[2];
//			elements[0] = response;
//			elements[1] = getResponse();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return elements;
//	}
//
//	public static Object[] main(ISOMsg isoMsg) {
//		ExecutorService service = Executors.newFixedThreadPool(1);
//		Future<String> task = service.submit(new IsoJposResponse(isoMsg));
//		try {
//			response = task.get();
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
//		service.shutdownNow();
//		logger.info("Finacle response code is: " + response);
//		Object[] elements = new Object[2];
//		elements[0] = response;
//		elements[1] = responseIsoMsg;
//		return elements;
//	}
//
//	public IsoJposResponse(ISOMsg iso) {
//		this.isoMsg = iso;
//		try {
//			ASCIIChannel ac = new ASCIIChannel("172.16.2.225", 52000, getPackager());
//			ac.setTimeout(5000);
//			this.channel = ac;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public IsoJposResponse(String ipAddress, int port, ISOPackager packager) {
//		try {
//			this.channel = new ASCIIChannel(ipAddress, port, packager);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private GenericPackager getPackager() {
//		try {
//			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//			InputStream inputstream = classLoader.getResourceAsStream("basic.xml");
//			genericPackager = new GenericPackager(inputstream);
//			return genericPackager;
//		} catch (ISOException e) {
//			logger.error(e);
//			return null;
//		}
//	}
//
//	@Override
//	public String call() throws Exception {
//		logger.info("Call ...");
//		ISOMsg r = null;
//		StringBuilder responseString = new StringBuilder();
//		try {
//			channel.connect();
//			if (channel.isConnected()) {
//				logger.info("Channel connected. Sending...");
//			}
//			channel.send(this.isoMsg);
//			r = channel.receive();
//			if (r != null) {
//				logger.info("Response received");
//			}
//			if (r.getString(39).equals("000")) {
//				logger.info("Transaction SUCCESS");
//			} else {
//				logger.info("Transaction FAIL");
//			}
//
//			for (int i = 0; i <= r.getMaxField(); i++) {
//				if (r.hasField(i)) {
//					responseString = responseString.append(i).append(":").append(r.getString(i)).append(";");
//				}
//			}
//			responseIsoMsg = r;
//			channel.disconnect();
//		} catch (IOException e) {
//			logger.error(e);
//			return null;
//		} catch (ISOException e) {
//			logger.error(e);
//			return null;
//		}
//		return responseString.toString();
//	}
//
//	static ISOMsg getResponse() throws ISOException {
//		ISOMsg m = new ISOMsg();
//
//		Calendar cal = Calendar.getInstance();
//		String MM = "" + (1 + cal.get(Calendar.MONTH));
//		String DD = "" + cal.get(Calendar.DAY_OF_MONTH);
//		String hh = "" + cal.get(Calendar.HOUR_OF_DAY);
//		String mm = "" + cal.get(Calendar.MINUTE);
//		String ss = "" + cal.get(Calendar.SECOND);
//		String YY = "" + cal.get(Calendar.YEAR);
//		if (MM.length() < 2) {
//			MM = "0" + MM;
//		}
//		if (DD.length() < 2) {
//			DD = "0" + DD;
//		}
//		if (hh.length() < 2) {
//			hh = "0" + hh;
//		}
//		if (mm.length() < 2) {
//			mm = "0" + mm;
//		}
//		if (ss.length() < 2) {
//			ss = "0" + ss;
//		}
//		if (YY.length() < 2) {
//			YY = "0" + YY;
//		}
//		m.setMTI("1200");
//		m.set(2, "INTB");
//		// m.set(3, "400000");//Funds transfer
//		// m.set(3, "010000");// cash with draw
//		m.set(3, "310000");// Balance inquiry
//		m.set(4, new DecimalFormat("0000000000000000").format(100));
//		m.set(11, "622279");
//		m.set(12, YY + MM + DD + hh + mm + ss);
//		m.set(17, YY + MM + DD);
//		m.set(24, "200");
//		m.set(32, "000000");
//		m.set(49, "840");
//		// m.set(62,"00000000101");
//		m.set(102, "");
//		// m.set(103, crAccount);
//		m.set(123, "SWT");
//		m.set(124, "ITB");
//		m.set(125, "");
//		return m;
//	}
//
//}