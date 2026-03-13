package com.hulkhiretech.payments.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.constant.PaypalStatusEnum;
import com.hulkhiretech.payments.constant.TxnStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.paypalprovider.PPOrder;
import com.hulkhiretech.payments.service.helper.PPCaptureOrderHelper;
import com.hulkhiretech.payments.service.helper.PPGetOrderHelper;
import com.hulkhiretech.payments.service.interfaces.ProviderHandler;
import com.hulkhiretech.payments.util.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaypalProviderHandler implements ProviderHandler {


	private final HttpServiceEngine httpServiceEngine ;

	private final JsonUtils jsonUtils;

	private final PPGetOrderHelper ppGetOrderHelper;

	private final PPCaptureOrderHelper ppCaptureOrderHelper;

	private final TransactionDAO transactionDAO;
	@Override
	public void reconTransaction(TransactionDTO txn) {
		log.info("PypalProviderHandler.reconTransaction() called txn: {}", txn);

		txn.setRetryCount(txn.getRetryCount() + 1);

		String initialTxnStatus = txn.getTxnStatus();


		boolean isExceptionWhileStatus = false;

		try {

			PPOrder captureResponse = ppCaptureOrder(txn);// TEMP

			PPOrder succesObj = getOrderFromPP(txn);
			log.info("PaypalProviderHandler.reconTransaction() - " + "succesObj: {} ", succesObj);

			PaypalStatusEnum  statusEnum = PaypalStatusEnum.fromString(
					succesObj.getPaypalStatus());

			switch (statusEnum) {
			case PAYER_ACTION_REQUIRED:
				log.info("PaypalProviderHandler.reconTransaction() - " + "PaypalStatus:PAYER_ACTION_REQUIRED");
				// NO ACTION

				break;

			case APPROVED:
				log.info("PaypalProviderHandler.reconTransaction() - " + "succesObj: APPROVED");
				//Call Capture API 
				PPOrder captureRes = ppCaptureOrder(txn);
				if(captureRes.getPaypalStatus().equals(
						PaypalStatusEnum.COMPLETED.getName())) {
					//if capture is success , then update out txn as success
					txn.setTxnStatus(TxnStatusEnum.SUCCESS.getName());

				}else {
					//if capture is Failed , then update out txn as FAILED
					log.error("PaypalProviderHandler.reconTransaction() - " + "CaptureAPI failed, paypal status NOT COMPLETED");
				}
				//if failed wait for next recon cycle.
				break;

			case COMPLETED:
				log.info("PaypalProviderHandler.reconTransaction() - " + "succesObj: COMPLETED");
				//update our txn as successful
				txn.setTxnStatus(TxnStatusEnum.SUCCESS.getName());

				break;

			default:
				log.error("PaypalProviderHandler.reconTransaction() - " + "Unknown PaypalStatus");
			}

		}catch(Exception e){
			log.error("PypalProviderHandler.reconTransaction() - " + "Exception: {}", e);
			isExceptionWhileStatus = true;
		}

		//if initialTxnStatus is not equal to txn.getTxnStatus(),then call transactionDAO.updateTransactionForRecon();

		if(!initialTxnStatus.equals(txn.getTxnStatus())) {
			log.info("PaypalProviderHandler.reconTransaction() - " + "initialTxnStatus: {}, txn.getTxnStatus()",
					initialTxnStatus, txn.getTxnStatus());
			transactionDAO.updateTransactionForRecon(txn);
			return;
		}

		//if txn.getRetryCount() >= 3, then then update txn as FAILED
		if(txn.getRetryCount() >= Constant.MAX_RETRY_ATTEMPT && !isExceptionWhileStatus) {
			log.info("PaypalProviderHandler.reconTransaction() - " + "txn.getRetryCount(): {}", txn.getRetryCount() );
			txn.setTxnStatus(TxnStatusEnum.FAILED.getName());
			txn.setErrorCode(ErrorCodeEnum.RECON_PAYMENT_FAILED.getCode());
			txn.setErrorMessage(ErrorCodeEnum.RECON_PAYMENT_FAILED.getMessage());
		}


		transactionDAO.updateTransactionForRecon(txn); // update retry count in DB ;

	}

	private PPOrder getOrderFromPP(TransactionDTO txn) {

		HttpRequest httpRequest = ppGetOrderHelper.prepareHttpRequest(txn);

		log.info("PaypalProviderHandler.reconTransaction() - " + "httpRequest: {}",httpRequest);

		ResponseEntity<String> response =  httpServiceEngine.makeHttpCall(httpRequest);

		log.info("PaypalProviderHandler.reconTransaction() - " + "response: {} ", response);

		PPOrder succesObj = ppGetOrderHelper.processGetOrderResponse(response);
		log.info("PaypalProviderHandler.reconTransaction() - " + "succesObj: {} ", succesObj);


		return succesObj;
	}

	private PPOrder ppCaptureOrder(TransactionDTO txn) {

		HttpRequest httpRequest = ppCaptureOrderHelper.prepareHttpRequest(txn);

		log.info("PaypalProviderHandler.reconTransaction() - " + "httpRequest: {}",httpRequest);

		ResponseEntity<String> response =  httpServiceEngine.makeHttpCall(httpRequest);

		log.info("PaypalProviderHandler.reconTransaction() - " + "response: {} ", response);

		PPOrder succesObj = ppCaptureOrderHelper.processResponse(response);
		log.info("PaypalProviderHandler.reconTransaction() - " + "succesObj: {} ", succesObj);


		return succesObj;
	}


}
