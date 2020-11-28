package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/offers")
public class OffersController {
	AuctionApplicationService auctionApplicationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{lenderName}")
	public List<OfferDto> getAllLenderOffers(@PathVariable String lenderName){
		return auctionApplicationService.getLenderOffers(lenderName).stream()
				.map(OfferDto::fromOffer)
				.collect(Collectors.toList());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping()
	public Long addNewOffer(@RequestBody NewOfferDto offerDto){
    return auctionApplicationService.addOffer(offerDto.getAuctionId(),
			offerDto.lenderUserName,
			offerDto.getAmount(),
			offerDto.getRate());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{offerId}")
	public void deleteOffer(@PathVariable  Long offerId){
		auctionApplicationService.deleteOffer(offerId);
	}
}
