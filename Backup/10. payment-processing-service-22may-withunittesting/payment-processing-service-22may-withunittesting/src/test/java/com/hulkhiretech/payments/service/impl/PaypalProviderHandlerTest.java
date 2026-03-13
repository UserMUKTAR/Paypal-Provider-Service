package com.hulkhiretech.payments.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hulkhiretech.payments.constant.PaypalStatusEnum;
import com.hulkhiretech.payments.constant.TxnStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.paypalprovider.PPOrder;
import com.hulkhiretech.payments.service.helper.PPGetOrderHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PaypalProviderHandlerTest {

	@Mock
	private PPGetOrderHelper ppGetOrderHelper;

	@Mock
	private HttpServiceEngine httpServiceEngine;
	
	@Mock
	private TransactionDAO transactionDAO;

	@InjectMocks
	private PaypalProviderHandler paypalProviderHandler;

	@Test
	public void testMethodCOmpletedPaypalProviderCase() {
		log.info("Test method executed");

		//Arrange
		TransactionDTO txn = new TransactionDTO();
		txn.setRetryCount(0);
		txn.setTxnStatus(TxnStatusEnum.PENDING.name());

		//PPOrder successObj = ppGetOrderHelper.processGetOrderResponse(response);
		PPOrder successObj = new PPOrder();
		successObj.setPaypalStatus("COMPLETED");
		successObj.setOrderId("12345");

		//Mocking the behavior of ppGetOrderHelper.processGetOrderResponse(response)
		when(ppGetOrderHelper.processGetOrderResponse(any())
				).thenReturn(successObj);

		//Act
		paypalProviderHandler.reconTransaction(txn);

		//Verify
		assertEquals(TxnStatusEnum.SUCCESS.getName(), txn.getTxnStatus());
		assertEquals(1, txn.getRetryCount());
		
		// transactionDAO.updateTransactionForRecon(txn); ensure this method is called 1 time
		verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
	}
	
	@Test
	public void testMethodPayerActionRequiredPaypalProviderCase() {
		log.info("Test method executed");

		//Arrange
		TransactionDTO txn = new TransactionDTO();
		txn.setRetryCount(0);
		txn.setTxnStatus(TxnStatusEnum.PENDING.name());

		//PPOrder successObj = ppGetOrderHelper.processGetOrderResponse(response);
		PPOrder successObj = new PPOrder();
		successObj.setPaypalStatus(PaypalStatusEnum.PAYER_ACTION_REQUIRED.getName());
		successObj.setOrderId("12345");

		//Mocking the behavior of ppGetOrderHelper.processGetOrderResponse(response)
		when(ppGetOrderHelper.processGetOrderResponse(any())
				).thenReturn(successObj);

		//Act
		paypalProviderHandler.reconTransaction(txn);

		//Verify
		assertEquals(TxnStatusEnum.PENDING.name(), txn.getTxnStatus());
		assertEquals(1, txn.getRetryCount());
		
		// transactionDAO.updateTransactionForRecon(txn); ensure this method is called 1 time
		verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
	}

}
