package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainService;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auctions")
@AllArgsConstructor
public class AuctionsController {
	private AuctionDomainService auctionDomainService;
	
	@GetMapping
	public List<AuctionDto> getAllAuctions(){
		return auctionDomainService.getPlatformAuctions().stream()
				.map(AuctionDto::fromAuction)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{borrowerName}")
	public List<AuctionDto> getUserAuctions(@PathVariable String borrowerName){
		return auctionDomainService.getUserAuctions( borrowerName).stream()
				.map(AuctionDto::fromAuction)
				.collect(Collectors.toList());
	}
	
	@PostMapping()
	public Long addNewAuction(@RequestBody AuctionDto auctionDto){
		return auctionDomainService.createNewAuction(auctionDto.getBorrower(), //TODO: from auth
				Period.ofMonths(auctionDto.getAuctionDuration()),
				auctionDto.getAmount(),
				Period.ofDays(auctionDto.getAuctionDuration()),
				auctionDto.getRate(),
				auctionDto.getLoanStartDate());
	}
}
