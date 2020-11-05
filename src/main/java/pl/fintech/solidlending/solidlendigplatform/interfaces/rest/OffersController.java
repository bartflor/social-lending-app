package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/offers")
public class OffersController {
	AuctionService auctionService;
	@GetMapping("/{lenderName}")
	public List<OfferResponse> getAllLenderOffers(@PathVariable String lenderName){
		return auctionService.getLenderOffers(lenderName).stream()
				.map(OfferResponse::fromOffer)
				.collect(Collectors.toList());
	}
}
