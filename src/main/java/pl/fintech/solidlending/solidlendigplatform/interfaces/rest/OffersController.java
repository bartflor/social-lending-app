package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/offers")
public class OffersController {
	AuctionService auctionService;
	@GetMapping("/{lenderName}")
	public List<OfferDto> getAllLenderOffers(@PathVariable String lenderName){
		return auctionService.getLenderOffers(lenderName).stream()
				.map(OfferDto::fromOffer)
				.collect(Collectors.toList());
	}
	@PostMapping()
	public Long addNewOffer(@RequestBody OfferDto offerDto){
		//TODO: add lender from auth
		return auctionService.addOffer(offerDto.createDomainOffer());
	
	}
}
