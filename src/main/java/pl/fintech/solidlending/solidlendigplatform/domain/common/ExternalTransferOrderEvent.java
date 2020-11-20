package pl.fintech.solidlending.solidlendigplatform.domain.common;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

@Value
@Builder
public class ExternalTransferOrderEvent {
	String userName;
	Money amount;
	TransferType transferType;
	
	public enum TransferType{
		DEPOSIT, WITHDRAWAL
	}
	
	public boolean isDepositTransfer(){
		return transferType.equals(TransferType.DEPOSIT);
	}
	
	public boolean isWithdrawalTransfer(){
		return transferType.equals(TransferType.WITHDRAWAL);
	}
}
