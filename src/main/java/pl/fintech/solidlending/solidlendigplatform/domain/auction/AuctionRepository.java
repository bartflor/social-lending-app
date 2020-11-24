package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository {
	Long save(Auction auction);
	
	List<Auction> findAllByUsername(String userName);
	
	List<Auction> findAll();
	
	Optional<Auction> findById(Long auctionId);
	
	void updateAuction(Long auctionId, Auction auction);
	
	List<Offer> findAuctionOffers(Long auctionId);
	
	void delete(Long auctionId);
	
	void deleteAll();
}
