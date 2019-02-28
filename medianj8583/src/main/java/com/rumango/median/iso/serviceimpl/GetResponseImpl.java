//package com.rumango.median.iso.serviceimpl;
//
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//
//import org.apache.log4j.Logger;
//import org.jpos.iso.ISOException;
//import org.jpos.iso.ISOMsg;
//import org.jpos.iso.packager.GenericPackager;
//import org.springframework.stereotype.Service;
//
//import com.rumango.median.iso.client.IsoJposResponse;
//import com.rumango.median.iso.service.GetResponse;
//
//@Service
//public class GetResponseImpl implements GetResponse {
//
//	private ISOMsg response = null;
//	private String receivedMsgStatus, sentMsgStatus, reason;
//
//	private final static Logger logger = Logger.getLogger(GetResponseImpl.class);
//
//	private Map<String, String> arrayToMap(String[] arrayOfString) {
//		return Arrays.asList(arrayOfString).stream().map(str -> str.split(":"))
//				.collect(Collectors.toMap(str -> str[0], str -> str[1]));
//	}
//
//	private boolean validateRequest(ISOMsg isoMsg) {
//		//		boolean response = false;
//		//		ValidateChannel vc = null;
//		//		try {
//		//			logger.info("Calling rest call to validate ");
//		//			String channel = isoMsg == null ? "Channel Id Invalid" : isoMsg.getString(2);
//		//			String tXiD = isoMsg == null ? "Transaction Id Invalid" : isoMsg.getString(125);
//		//			logger.info("Channel Id :" + channel + " Transaction Id : " + tXiD);
//		//			if (!channel.equalsIgnoreCase("Channel Id Invalid") && channel != null
//		//					&& !tXiD.equalsIgnoreCase("Transaction Id Invalid") && tXiD != null)
//		//				vc = RestClient.callRestApi(channel, tXiD);
//		//			logger.info("ValidateChannel " + vc);
//		//			logger.info("Amount " + isoMsg.getString(4) + "  Account Number" + isoMsg.getString(102));
//		//			if (vc.getStatus().startsWith("00")) {
//		//				if (vc.getAmount() == Long.parseLong(isoMsg.getString(4))) {
//		//					if (vc.getAccountNumber().equalsIgnoreCase(isoMsg.getString(102)))
//		//						response = true;
//		//					else {
//		//						response = false;
//		//						reason = "Account numbers do not match";
//		//					}
//		//				} else {
//		//					response = false;
//		//					reason = "Amount do not match";
//		//				}
//		//			} else {
//		//				response = false;
//		//				reason = "Transaction Id | Channel Id Does not exists";
//		//			}
//		//		} catch (Exception e) {
//		//			logger.error("Exception in validate", e);
//		//			response = false;
//		//		}
//		//		return response;
//		return true;
//	}
//
//	private ISOMsg getIsoFromString(String message) {
//		ISOMsg msg = new ISOMsg();
//		try {
//			String[] splitted = message.split(";");
//			for (Map.Entry<String, String> entry : arrayToMap(splitted).entrySet()) {
//				msg.set(Integer.parseInt(entry.getKey()), entry.getValue());
//			}
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return msg;
//	}
//
//	public String convertAndRespond(String stringMessage, Map<String, String> map) {
//		ISOMsg msg = null;
//		ISOMsg isoMsg = null;
//		String response = "SUCCESS";
//		try {
//			map.put("originalRequestString", stringMessage);
//			isoMsg = getIsoFromString(stringMessage);
//			logISOMsg(isoMsg, "REQUEST");
//			receivedMsgStatus = "SUCCESS";
//		} catch (Exception e) {
//			receivedMsgStatus = "FAIL";
//			logger.error("Exception while unpacking", e);
//		}
//		logger.info("inside convertAndRespond of GetResponseImpl ");
//		try {
//			if (validateRequest(isoMsg)) {
//				isoMsg.setPackager(getPackager());
//				Object[] objArray = IsoJposResponse.main();
//				logger.info("objArray size ::" + objArray.length);
//				response = (String) objArray[0];
//				map.put("originalResponseString", response);
//				logger.info("response ::" + response);
//				msg = (ISOMsg) objArray[1];
//				if (msg != null) {
//					logISOMsg(msg, "RESPONSE");
//					sentMsgStatus = "SUCCESS";
//				}
//			} else {
//				logger.info("Validation failed with rest Api");
//				sentMsgStatus = "FAIL";
//				map.put("reason", reason);
//			}
//		} catch (Exception e) {
//			logger.error("Exception inside convertAndRespond of GetResponseImpl ", e);
//		} finally {
//			map.put("receivedMsgStatus", receivedMsgStatus);
//			map.put("sentMsgStatus", sentMsgStatus);
//			logger.info("receivedMsgStatus " + receivedMsgStatus + " received Response Status   " + sentMsgStatus);
//			try {
//				logger.info("Map Size" + map.size());
//				for (Map.Entry<String, String> set : map.entrySet()) {
//					logger.info(set.getKey() + ":" + set.getValue());
//				}
//				// auditLogService.saveData(map);
//			} catch (Exception e) {
//				logger.warn("Exception while saving log information ", e);
//			}
//		}
//		return msg == null ? reason : msg.getString(39);
//	}
//
//	private ISOMsg getResponse(ISOMsg isoMessage) throws Exception {
//		logger.info("inside getResponse of IsoMessageConvertionImpl");
//		try {
//			response = null;// new IsoJposResponse().call(isoMessage);
//		} catch (Exception e) {
//			response = null;
//			sentMsgStatus = "Exception while getResponse of IsoMessageConvertionImpl";
//			logger.warn(sentMsgStatus);
//			throw e;
//		}
//		if (response.toString() != "" && response != null)
//			sentMsgStatus = "SUCCESS";
//		return response;
//	}
//
//	private String convertAndRespond(String input, Map<String, String> map, String str) {
//		ISOMsg msg = null;
//		ISOMsg isoMsg = null;
//		String response = null;
//		try {
//			isoMsg = getIsoFromString(input);
//			logISOMsg(isoMsg, "ORIGINAL MESSAGE");
//
//			isoMsg.setPackager(getPackager());
//			isoMsg.pack();
//
//			logISOMsg(isoMsg, "GENERIC MESSAGE");
//		} catch (ISOException e) {
//			logger.error("Exception while unpacking", e);
//		}
//		logger.info("inside convertAndRespond of GetResponseImpl ");
//		try {
//			if (validateRequest(isoMsg)) {
//				isoMsg.setPackager(getPackager());
//				Object[] objArray = IsoJposResponse.main(isoMsg);
//				logger.info("objArray size ::" + objArray.length);
//				response = (String) objArray[0];
//				logger.info("response ::" + response);
//				msg = (ISOMsg) objArray[1];
//				if (msg != null)
//					logISOMsg(msg, "RESPONSE MESSAGE");
//			} else
//				logger.info("Validation failed with rest Api");
//		} catch (Exception e) {
//			logger.error("Exception inside convertAndRespond of GetResponseImpl ", e);
//		} finally {
//			map.put("receivedMsgStatus", receivedMsgStatus);
//			map.put("sentMsgStatus", sentMsgStatus);
//			logger.info("receivedMsgStatus " + receivedMsgStatus + " received Response Status   " + sentMsgStatus);
//			try {
//				// auditLogService.saveData(map);
//			} catch (Exception e) {
//				logger.warn("Exception while saving log information ", e);
//			}
//		}
//		return msg == null ? "ERROR" : msg.getString(39);
//	}
//
//	private void logISOMsg(@NotEmpty @NotNull ISOMsg msg, String stringMessage) {
//		// StringBuilder responseString = new StringBuilder();
//		try {
//			logger.info("-----------------" + stringMessage + "-----------------------");
//			for (int i = 0; i <= msg.getMaxField(); i++) {
//				if (msg.hasField(i)) {
//					if (i == 102 | i == 103)
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
//	private ISOMsg getResponse() {
//
//		return null;
//	}
//}
