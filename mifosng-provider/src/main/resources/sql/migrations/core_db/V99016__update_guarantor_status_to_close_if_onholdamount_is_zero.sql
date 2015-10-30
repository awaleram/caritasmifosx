 UPDATE m_portfolio_account_associations ms
 INNER JOIN(
  
   SELECT loanId 
	  FROM
     (
	   SELECT mpl.loan_account_id as loanId
      FROM m_portfolio_account_associations mpl
      LEFT JOIN m_guarantor mg ON mg.loan_id = mpl.loan_account_id
      LEFT JOIN m_guarantor_funding_details mgfd ON mgfd.account_associations_id = mpl.id
      LEFT JOIN m_savings_account msa ON mpl.linked_savings_account_id = msa.id
      WHERE mpl.is_active = 1
      AND mg.is_active = 1
      AND mgfd.status_enum = 200
      AND msa.on_hold_funds_derived = 0
      GROUP BY mpl.loan_account_id
	  ) s
   ) t ON t.loanId = ms.loan_account_id
 
 SET ms.is_active = 0