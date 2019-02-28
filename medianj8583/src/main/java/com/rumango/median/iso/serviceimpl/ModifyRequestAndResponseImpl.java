//package com.rumango.median.iso.serviceimpl;
//
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//
//import org.apache.log4j.Logger;
//import org.jpos.iso.ISOException;
//import org.jpos.iso.ISOMsg;
//import org.jpos.iso.packager.GenericPackager;
//import org.jpos.iso.packager.ISO87APackager;
//import org.jpos.iso.packager.ISO93APackager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.rumango.median.iso.service.ModifyRequestAndResponse;
//import com.rumango.median.iso.service.ValidateFields;
//
//@Service
//public class ModifyRequestAndResponseImpl implements ModifyRequestAndResponse {
//
//	private final static Logger logger = Logger.getLogger(ModifyRequestAndResponseImpl.class);
//	private ISO93APackager packager93;
//	private ISO87APackager packager87;
//
//	private String originalRequestString, modifiedRequestString, originalResponseString, modifiedResponseString, reason;
//	private ISOMsg originalRequestISOMsg, modifiedRequestISOMsg, originalResponseISOMsg, modifiedResponseISOMsg,
//			response = null;
//	private String receivedMsgStatus, sentMsgStatus;
//
//	@Autowired
//	private ValidateFields validateFields;
//
//	@Override
//	public String modifyRequest(String requestMsg) throws Exception {
//		logger.info("inside modifyRequest");
//		String stringMessage = null;
//		// unpack
//		try {
//			if (requestMsg == null | requestMsg == "")
//				throw new Exception("Request message invalid");
//			originalRequestISOMsg = unpackMessage(requestMsg, "93");
//			logISOMsg(originalRequestISOMsg, "original Request message");
//
//			// read and convert
//			if (originalRequestISOMsg != null)
//				modifiedRequestISOMsg = validateSource(originalRequestISOMsg);
//
//			logISOMsg(modifiedRequestISOMsg, "modified Request message");
//
//		} catch (Exception e) {
//			stringMessage = "";
//			receivedMsgStatus = "FAIL";
//			logger.error(" Exception while converting request iso message ", e);
//			throw e;
//		}
//		if (stringMessage != "" && stringMessage != null)
//			receivedMsgStatus = "SUCCESS";
//		return stringMessage; // clientSocket.run(stringMessage);
//	}
//
//	@Override
//	public String modifyResponse(String responseMsg) throws Exception {
//		String stringMessage;
//		try {
//			// unpack
//			if (responseMsg == null | responseMsg == "")
//				throw new Exception("Response message invalid");
//			originalResponseISOMsg = unpackMessage(responseMsg, "gp");
//			// logger.info("original_response_isomsg.toString()" + new
//			// String(originalResponseISOMsg.pack()));
//			logISOMsg(originalResponseISOMsg, "original response iso message");
//			// isoMap.put("originalResponseISOMsg", originalResponseISOMsg);
//			// read and convert
//			modifiedResponseISOMsg = validateDestination(originalResponseISOMsg);
//			// isoMap.put("modifiedResponseISOMsg", modifiedResponseISOMsg);
//			logISOMsg(modifiedResponseISOMsg, "modified response message");
//			// pack
//			stringMessage = packMessage(modifiedResponseISOMsg, "93");
//		} catch (Exception e) {
//			stringMessage = "";
//			sentMsgStatus = "FAIL";
//			logger.warn("Exception while converting response iso message");
//			throw e;
//		}
//		if (stringMessage != "" && stringMessage != null)
//			sentMsgStatus = "SUCCESS";
//		return stringMessage;
//	}
//
//	private ISOMsg validateDestination(ISOMsg isoMessage) {
//		try {
//			validateFields.outgoing(isoMessage);
//			return isoMessage;
//		} catch (Exception e) {
//			logger.warn("Exception while updateRequestIso ", e);
//		}
//		return null;
//	}
//
//	private ISOMsg validateSource(ISOMsg isoMessage) {
//		try {
//			validateFields.incoming(isoMessage);
//			return null;// validateFields.iso87TO93(isoMessage);
//		} catch (Exception e) {
//			logger.warn("Exception while updateResponseIso ", e);
//		}
//		return null;
//	}
//
//	private void logISOMsg(@NotEmpty @NotNull ISOMsg msg, String stringMessage) {
//		// StringBuilder responseString = new StringBuilder();
//		try {
//			logger.info("-----------------" + stringMessage + "-----------------------");
//			for (int i = 0; i <= msg.getMaxField(); i++) {
//				if (msg.hasField(i)) {
//					if (i == 123 | i == 124)
//						logger.info(i + " " + ":" + mask(msg.getString(i)));
//					else
//						// responseString =
//						// responseString.append(i).append(":").append(msg.getString(i)).append(";");
//						logger.info(i + " " + ":" + msg.getString(i));
//				}
//			}
//		} catch (Exception e) {
//			logger.error("Exception occured", e);
//		}
//		// return responseString.toString();
//	}
//
//	private String mask(String accNo) {
//		StringBuilder sb = new StringBuilder(accNo);
//		if (sb.length() > 6)
//			sb.replace(6, accNo.length(), "X");
//		return sb.toString();
//	}
//
//	public GenericPackager getPackager() {
//		try {
//			InputStream inputstream = Thread.currentThread().getContextClassLoader().getResourceAsStream("basic.xml");
//			return new GenericPackager(inputstream);
//		} catch (ISOException e) {
//			logger.error("Exception while loading Generic Packager", e);
//			return null;
//		}
//	}
//
//	private ISOMsg unpackMessage(String stringMessage, String isoVersion)
//			throws UnsupportedEncodingException, ISOException {
//		try {
//			ISOMsg isoMsg = new ISOMsg();
//			if (isoVersion.equalsIgnoreCase("87")) {
//				packager87 = new ISO87APackager();
//				isoMsg.setPackager(packager87);
//			} else if (isoVersion.equalsIgnoreCase("93")) {
//				packager93 = new ISO93APackager();
//				isoMsg.setPackager(packager93);
//			} else {
//				isoMsg.setPackager(getPackager());
//			}
//			isoMsg.unpack(stringMessage.getBytes("US-ASCII"));
//			return isoMsg;
//		} catch (Exception e) {
//			logger.warn("Exception while unpacking ", e);
//			return null;
//		}
//	}
//
//	private String packMessage(ISOMsg isoMessage, String isoVersion) throws ISOException {
//		if (isoVersion.equalsIgnoreCase("87")) {
//			packager87 = new ISO87APackager();
//			isoMessage.setPackager(packager87);
//		} else if (isoVersion.equalsIgnoreCase("93")) {
//			packager93 = new ISO93APackager();
//			isoMessage.setPackager(packager93);
//		} else {
//			isoMessage.setPackager(getPackager());
//		}
//		byte[] binaryImage = isoMessage.pack();
//		return new String(binaryImage);
//	}
//
//}
