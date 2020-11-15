package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.BestOfferRatePolicy;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auctions")
@AllArgsConstructor
public class AuctionsController {
	private AuctionApplicationService auctionApplicationService;
	private LoanApplicationService loanApplicationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public List<AuctionDto> getAllAuctions(){
		return auctionApplicationService.getPlatformAuctions().stream()
				.map(AuctionDto::fromAuction)
				.collect(Collectors.toList());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/borrower/{borrowerName}")
	public List<AuctionDto> getUserAuctions(@PathVariable String borrowerName){
		return auctionApplicationService.getUserAuctions( borrowerName).stream()
				.map(AuctionDto::fromAuction)
				.collect(Collectors.toList());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping()
	public Long addNewAuction(@RequestBody AuctionDto auctionDto){
		return auctionApplicationService.createNewAuction(auctionDto.getBorrower(), //TODO: from auth
				Period.ofMonths(auctionDto.getAuctionDuration()),
				auctionDto.getAmount(),
				Period.ofDays(auctionDto.getAuctionDuration()),
				auctionDto.getRate());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping("/{auctionId}/create-loan")
	public LoanDto endAuctionCreateLoan(@PathVariable Long auctionId){
		Long newLoanId = auctionApplicationService.createLoanFromEndingAuction(auctionId,
				new BestOfferRatePolicy());
		return LoanDto.from(loanApplicationService.findLoanById(newLoanId));
	}
}
