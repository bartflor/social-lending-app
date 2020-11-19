package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AddOfferException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService;

import java.time.Period;
import java.util.List;
@Transactional
@Service
@AllArgsConstructor
public class AuctionDomainServiceImpl implements AuctionDomainService {
	private static final String BORROWER_NOT_FOUND_MSG = "Borrower with username:%s not found.";
	private static final String BORROWER_NOT_ALLOWED = "Borrower with username:%s is not allowed to create new auction.";
	private static final String AUCTION_WITH_ID_NOT_FOUND = "Auction with id:%s not found.";
	private static final String LENDER_NOT_FOUND = "Lender with username:%s not found.";
	private static final String NOT_ALLOWED_OFFER = "Can not add invalid offer to auction. Provided rate: %s, amount: %s";
	
	private final AuctionRepository auctionRepository;
	private final BorrowerRepository borrowerRepository;
	private final OfferRepository offerRepository;
	private final LenderRepository lenderRepository;
	private final LoanRiskService loanRiskService;
	private final TimeService timeService;
	
	@Override
	public Long createNewAuction(String username,
								 Period auctionDuration,
								 double loanAmount,
								 Period loanDuration,
								 double rate){
		
		Borrower borrower = borrowerRepository.findBorrowerByUserName(username)
				.orElseThrow(() -> new AuctionCreationException(String.format(BORROWER_NOT_FOUND_MSG, username)));
		if(!allowedToCreateAuction(borrower)){
			throw new AuctionCreationException(String.format(BORROWER_NOT_ALLOWED, username));
		}
		Money loanValue = new Money(loanAmount);
		AuctionLoanParams auctionLoanParams = AuctionLoanParams.builder()
				.loanAmount(loanValue)
				.loanDuration(loanDuration)
				.loanRate(Rate.fromPercentValue(rate))
				.loanRisk(loanRiskService.estimateLoanRisk(borrower, loanValue))
				.build();
		
		Auction auction = Auction.builder()
				.borrowerUserName(username)
				.startDate(timeService.now())
				.auctionDuration(auctionDuration)
				.auctionLoanParams(auctionLoanParams)
				.borrowerRating(borrower.getRating())
				.build();
		return auctionRepository.save(auction);
	}
	
	@Override
	public boolean allowedToCreateAuction(Borrower auctionOwner){
		return auctionOwner.hasLinkedBankAccount();
	}
	
	@Override
	public List<Auction> getUserAuctions(String userName){
		if(!borrowerRepository.borrowerExists(userName))
			throw new UserNotFoundException(String.format(BORROWER_NOT_FOUND_MSG, userName));
		return auctionRepository.findAllByUsername(userName);
	}
	
	@Override
	public List<Auction> getPlatformAuctions(){
		return auctionRepository.findAll();
	}
	
	@Override
	public Long addOffer(Long auctionId,
						 String lenderName,
						 double amount,
						 double rate,
						 Boolean allowAmountSplit){
		existsInRepositoryCheck(lenderName);
		Auction auction = auctionRepository.findById(auctionId).
				orElseThrow(() -> new AddOfferException(String.format(AUCTION_WITH_ID_NOT_FOUND, auctionId)));
		if(amount<0 || rate<0){
			throw new AddOfferException(String.format(NOT_ALLOWED_OFFER, rate, amount));
		}
		Offer validOffer = Offer.builder()
				.auctionId(auctionId)
				.lenderName(lenderName)
				.borrowerName(auction.getBorrowerUserName())
				.amount(new Money(amount))
				.risk(auction.getAuctionLoanParams().getLoanRisk())
				.rate(Rate.fromPercentValue(rate))
				.duration(auction.getAuctionLoanParams().getLoanDuration())
				.allowAmountSplit(allowAmountSplit == null ? Boolean.FALSE : allowAmountSplit)
				.build();
		auction.addNewOffer(validOffer);
		Long offerId = offerRepository.save(validOffer);
		validOffer.setId(offerId);
		auctionRepository.updateAuction(auctionId, auction);
		return offerId;
	}
	
	@Override
	public List<Offer> getLenderOffers(String lenderName) {
		existsInRepositoryCheck(lenderName);
		return offerRepository.findAllByUserName(lenderName);
	}
	
	private void existsInRepositoryCheck(String lenderName) {
		if(!lenderRepository.lenderExist(lenderName))
			throw new UserNotFoundException(String.format(LENDER_NOT_FOUND, lenderName));
	}
	
	/**
	 * While ending auction <code>OfferSelectionPolicy</code> selects
	 * from all offers list in <code>Auction</Code> object, only these offers
	 * that will be transformed in investment (if Borrower confirm loan creation)
	 * @param auctionId - auction selected to be closed
	 * @param selectionPolicy - offers choosing policy
	 * @return Auction with selected offers
	 */
	@Override
	public EndAuctionEvent endAuction(Long auctionId, OffersSelectionPolicy selectionPolicy){
		Auction auction = auctionRepository.findById(auctionId)
				.orElseThrow(() -> new AuctionNotFoundException(String.format(AUCTION_WITH_ID_NOT_FOUND, auctionId)));
		EndAuctionEvent endEvent = auction.end(selectionPolicy);
		auctionRepository.updateAuction(auctionId, auction);
		auctionRepository.findAuctionOffers(auctionId)
				.forEach(offer -> { offer.archive();
									offerRepository.update(offer.getId(), offer);});
		return endEvent;
	}
	

}
