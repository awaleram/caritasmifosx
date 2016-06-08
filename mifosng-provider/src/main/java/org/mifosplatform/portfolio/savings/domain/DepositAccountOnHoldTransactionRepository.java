/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.portfolio.savings.domain;

import java.math.BigDecimal;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DepositAccountOnHoldTransactionRepository extends JpaRepository<DepositAccountOnHoldTransaction, Long>,
        JpaSpecificationExecutor<DepositAccountOnHoldTransaction> {

	@Query("from DepositAccountOnHoldTransaction d where "
			+ " d.savingsAccount.id =  :savingId "
			+ " and d.amount = :amount "
			+ " and d.transactionType = :transactionType"
			+ " and d.transactionDate = :transactionDate" )
	List<DepositAccountOnHoldTransaction> findOnHoldTransactionDetail(@Param("savingId") Long savingId,
			@Param("amount") BigDecimal amount, @Param("transactionType") Integer transactionType,
			@Param("transactionDate") Date transactionDate);
	
}
