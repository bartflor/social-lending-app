package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/offers")
public class OffersController {
	AuctionDomainService auctionDomainService;
	@GetMapping("/{lenderName}")
	public List<OfferDto> getAllLenderOffers(@PathVariable String lenderName){
		return auctionDomainService.getLenderOffers(lenderName).stream()
				.map(OfferDto::fromOffer)
				.collect(Collectors.toList());
	}
	@PostMapping()
	public Long addNewOffer(@RequestBody OfferDto offerDto){
		//TODO: add lender from auth
		return auctionDomainService.addOffer(offerDto.createDomainOffer());
	
	}
}
