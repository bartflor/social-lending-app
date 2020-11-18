package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentRequest {
	UUID accountNumber;
	double amount;
}
