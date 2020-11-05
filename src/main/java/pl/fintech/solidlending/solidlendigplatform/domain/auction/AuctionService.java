package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.user.BorrowerRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService;

import java.time.Period;
import java.util.Date;
import java.util.List;
@Service
@AllArgsConstructor
public class AuctionService {
	private static final String BORROWER_NOT_FOUND_MSG = "Borrower with username:%s not found.";
	private static final String BORROWER_NOT_ALLOWED = "Borrower with username:%s is not allowed to create new auction.";
	private final AuctionRepository auctionRepository;
	private final BorrowerRepository borrowerRepository;
	private final AuctionFactory auctionFactory;
	private final LoanRiskService loanRiskService;
	
	public void createNewAuction(String username,
								 Period auctionDuration,
								 double loanAmount,
								 Period loanDuration,
								 double rate,
								 Date loanStartDate){
		
		Borrower borrower = borrowerRepository.findByUserName(username)
				.orElseThrow(() -> new AuctionCreationException(String.format(BORROWER_NOT_FOUND_MSG, username)));
		if(!allowedToCreateAuction(borrower)){
			throw new AuctionCreationException(String.format(BORROWER_NOT_ALLOWED, username));
		}
		Money loanValue = new Money(loanAmount);
		final Risk loanRisk = loanRiskService.estimateLoanRisk(borrower, loanValue);
		LoanParams loanParams = LoanParams.builder()
				.loanAmount(loanValue)
				.loanDuration(loanDuration)
				.loanRate(new Rate(rate))
				.loanStartDate(loanStartDate)
				.loanRisk(loanRisk)
				.build();
		Auction auction = auctionFactory.creteAuction( username,  auctionDuration, loanParams);
		
		Long auctionId = auctionRepository.save(auction);
		borrowerRepository.addBorrowerAuction(username, auctionId);
		
	}
	
	public boolean allowedToCreateAuction(Borrower auctionOwner){
		//TODO: implement
		return true;
	}
	
	public List<Auction> getUserAuctions(String userName){
		return auctionRepository.findAllByUsername(userName);
	}
	
	public List<Auction> getPlatformAuctions(){
		return auctionRepository.findAll();
	}
	
}
