package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AddOfferException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.user.BorrowerRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.user.LenderRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
@Service
@AllArgsConstructor
public class AuctionService {
	private static final String BORROWER_NOT_FOUND_MSG = "Borrower with username:%s not found.";
	private static final String BORROWER_NOT_ALLOWED = "Borrower with username:%s is not allowed to create new auction.";
	private static final String AUCTION_WITH_ID_NOT_FOUND = "Auction with id:%s not found.";
	private static final String LENDER_NOT_FOUND = "Lender with username:%s not found.";
	
	private final AuctionRepository auctionRepository;
	private final BorrowerRepository borrowerRepository;
	private final OfferRepository offerRepository;
	private final LenderRepository lenderRepository;
	private final AuctionFactory auctionFactory;
	private final LoanRiskService loanRiskService;
	
	public Long createNewAuction(String username,
								 Period auctionDuration,
								 double loanAmount,
								 Period loanDuration,
								 double rate,
								 LocalDate loanStartDate){
		
		Borrower borrower = borrowerRepository.findBorrowerByUserName(username)
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
		Auction auction = auctionFactory.creteAuction( username,
				borrower.getRating(),
				auctionDuration,
				loanParams);
		return auctionRepository.save(auction);
	}
	
	public boolean allowedToCreateAuction(Borrower auctionOwner){
		//TODO: implement
		return true;
	}
	
	public List<Auction> getUserAuctions(String userName){
		if(!borrowerRepository.borrowerExists(userName))
			throw new UserNotFoundException(String.format(BORROWER_NOT_FOUND_MSG, userName));
		return auctionRepository.findAllByUsername(userName);
	}
	
	public List<Auction> getPlatformAuctions(){
		return auctionRepository.findAll();
	}
	
	public void addOffer(Offer offer){
		Long auctionId = offer.getAuctionId();
		Auction auction = auctionRepository.findById(auctionId).
				orElseThrow(() -> new AddOfferException(String.format(AUCTION_WITH_ID_NOT_FOUND, auctionId)));
		auction.addNewOffer(offer);
		Long offerId = offerRepository.save(offer);
		offer.setId(offerId);
		auctionRepository.updateAuction(auctionId, auction);
	}
	
	public List<Offer> getLenderOffers(String lenderName) {
		if(!lenderRepository.lenderExist(lenderName))
			throw new UserNotFoundException(String.format(LENDER_NOT_FOUND, lenderName));
		return offerRepository.findAllByUserName(lenderName);
	}
}
