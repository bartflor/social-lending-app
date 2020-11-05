package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auctions")
@AllArgsConstructor
public class AuctionsController {
	private AuctionService auctionService;
	
	@GetMapping
	public List<AuctionResponse> getAllAuctions(){
		return auctionService.getPlatformAuctions().stream()
				.map(AuctionResponse::fromAuction)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/${username}")
	public List<AuctionResponse> getUserAuctions(@PathVariable String username){
		return auctionService.getUserAuctions( username).stream()
				.map(AuctionResponse::fromAuction)
				.collect(Collectors.toList());
	}
}
