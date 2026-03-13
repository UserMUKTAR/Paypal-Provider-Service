package com.hulkhiretech.payments.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.constant.TxnStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.entity.TransactionEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TransactionDAOImpl implements TransactionDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<TransactionEntity> loadTransactionsForRecon() {
		log.info("TransactionDAOImpl.loadTransactionsForRecon() called");
		String sql = "SELECT * FROM payments.Transaction " +
				"WHERE txnStatusId IN (:status1, :status2) " +
				"AND retryCount < :retryMax";

		Map<String, Object> params = new HashMap<>();
		params.put("status1", TxnStatusEnum.PENDING.getId());
		params.put("status2", TxnStatusEnum.APPROVED.getId());
		params.put("retryMax", Constant.MAX_RETRY_ATTEMPT);

		List<TransactionEntity> txnsForRecon = jdbcTemplate.query(
				sql,
				params,
				BeanPropertyRowMapper.newInstance(TransactionEntity.class)
				);
		
		log.info("TransactionDAOImpl.loadTransactionsForRecon() - "
				+ "txnsForRecon.size(): {}", txnsForRecon.size());
		
		return txnsForRecon;
	}

	@Override
	public void updateTransactionForRecon(TransactionDTO txn) {
	    String sql = "UPDATE payments.Transaction SET "
	            + "txnStatusId = :txnStatusId, "
	            + "errorCode = :errorCode, "
	            + "errorMessage = :errorMessage, "
	            + "retryCount = :retryCount "
	            + "WHERE id = :id";
	    

	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("txnStatusId",TxnStatusEnum.fromName(txn.getTxnStatus()).getId());
	    params.put("errorCode", txn.getErrorCode());
	    params.put("errorMessage", txn.getErrorMessage());
	    params.put("retryCount", txn.getRetryCount());
	    params.put("id", txn.getId());

	    jdbcTemplate.update(sql, params);
	}
}
