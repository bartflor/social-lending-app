package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/offers")
public class OffersController {
	AuctionApplicationService auctionApplicationService;
	@GetMapping("/{lenderName}")
	public List<OfferDto> getAllLenderOffers(@PathVariable String lenderName){
		return auctionApplicationService.getLenderOffers(lenderName).stream()
				.map(OfferDto::fromOffer)
				.collect(Collectors.toList());
	}
	@PostMapping()
	public Long addNewOffer(@RequestBody OfferDto offerDto){
		//TODO: add lender from auth
		return auctionApplicationService.addOffer(offerDto.createDomainOffer());
	
	}
}
