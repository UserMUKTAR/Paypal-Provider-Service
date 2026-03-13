package com.hulkhiretech.payments.service.impl;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

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
import com.hulkhiretech.payments.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PaypalProviderHandlerAIGeneratedTest {

    @Mock
    private HttpServiceEngine httpServiceEngine;
    @Mock
    private PPGetOrderHelper ppGetOrderHelper;
    @Mock
    private PPCaptureOrderHelper ppCaptureOrderHelper;
    @Mock
    private TransactionDAO transactionDAO;
    @Mock
    private JsonUtils jsonUtils;

    @InjectMocks
    private PaypalProviderHandler paypalProviderHandler;

    private TransactionDTO createTxn(String status, int retryCount) {
        TransactionDTO txn = new TransactionDTO();
        txn.setTxnStatus(status);
        txn.setRetryCount(retryCount);
        return txn;
    }

    private PPOrder createOrder(String status) {
        PPOrder order = new PPOrder();
        order.setPaypalStatus(status);
        order.setOrderId("OID123");
        return order;
    }

    private void mockGetOrderFlow(String ppStatus) {
        when(ppGetOrderHelper.prepareHttpRequest(any())).thenReturn(new HttpRequest());
        when(httpServiceEngine.makeHttpCall(any())).thenReturn(ResponseEntity.ok("OK"));
        when(ppGetOrderHelper.processGetOrderResponse(any())).thenReturn(createOrder(ppStatus));
    }

    private void mockCaptureOrderFlow(String ppStatus) {
        when(ppCaptureOrderHelper.prepareHttpRequest(any())).thenReturn(new HttpRequest());
        when(httpServiceEngine.makeHttpCall(any())).thenReturn(ResponseEntity.ok("OK"));
        when(ppCaptureOrderHelper.processResponse(any())).thenReturn(createOrder(ppStatus));
    }

    @Test
    public void testCompletedPaypalStatus() {
        TransactionDTO txn = createTxn(TxnStatusEnum.PENDING.getName(), 0);
        mockGetOrderFlow(PaypalStatusEnum.COMPLETED.getName());

        paypalProviderHandler.reconTransaction(txn);

        assertEquals(TxnStatusEnum.SUCCESS.getName(), txn.getTxnStatus());
        assertEquals(1, txn.getRetryCount());
        verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
    }

    @Test
    public void testPayerActionRequiredStatus() {
        TransactionDTO txn = createTxn(TxnStatusEnum.PENDING.getName(), 0);
        mockGetOrderFlow(PaypalStatusEnum.PAYER_ACTION_REQUIRED.getName());

        paypalProviderHandler.reconTransaction(txn);

        assertEquals(TxnStatusEnum.PENDING.getName(), txn.getTxnStatus());
        verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
    }

    @Test
    public void testApprovedStatus_CaptureSuccess() {
        TransactionDTO txn = createTxn(TxnStatusEnum.PENDING.getName(), 0);
        mockGetOrderFlow(PaypalStatusEnum.APPROVED.getName());
        mockCaptureOrderFlow(PaypalStatusEnum.COMPLETED.getName());

        paypalProviderHandler.reconTransaction(txn);

        assertEquals(TxnStatusEnum.SUCCESS.getName(), txn.getTxnStatus());
        verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
    }

    @Test
    public void testApprovedStatus_CaptureFail() {
        TransactionDTO txn = createTxn(TxnStatusEnum.PENDING.getName(), 0);
        mockGetOrderFlow(PaypalStatusEnum.APPROVED.getName());
        mockCaptureOrderFlow(PaypalStatusEnum.PAYER_ACTION_REQUIRED.getName()); // Not COMPLETED

        paypalProviderHandler.reconTransaction(txn);

        assertEquals(TxnStatusEnum.PENDING.getName(), txn.getTxnStatus());
        verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
    }

    @Test
    public void testUnknownPaypalStatus_DefaultCase() {
        TransactionDTO txn = createTxn(TxnStatusEnum.PENDING.getName(), 0);
        mockGetOrderFlow("UNKNOWN_STATUS");

        paypalProviderHandler.reconTransaction(txn);

        assertEquals(TxnStatusEnum.PENDING.getName(), txn.getTxnStatus());
        verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
    }

    @Test
    public void testRetryLimitReachedWithoutException() {
        TransactionDTO txn = createTxn(TxnStatusEnum.PENDING.getName(), Constant.MAX_RETRY_ATTEMPT - 1);
        mockGetOrderFlow(PaypalStatusEnum.PAYER_ACTION_REQUIRED.getName());

        paypalProviderHandler.reconTransaction(txn);

        assertEquals(TxnStatusEnum.FAILED.getName(), txn.getTxnStatus());
        assertEquals(ErrorCodeEnum.RECON_PAYMENT_FAILED.getCode(), txn.getErrorCode());
        verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
    }

    @Test
    public void testExceptionInGetOrderFlow() {
        TransactionDTO txn = createTxn(TxnStatusEnum.PENDING.getName(), 0);

        when(ppGetOrderHelper.prepareHttpRequest(any())).thenThrow(new RuntimeException("Simulated error"));

        paypalProviderHandler.reconTransaction(txn);

        assertEquals(TxnStatusEnum.PENDING.getName(), txn.getTxnStatus());
        verify(transactionDAO, times(1)).updateTransactionForRecon(txn);
    }
}
