package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import java.util.List;

public interface AuctionRepository {
	Long save(Auction auction);
	
	List<Auction> findAllByUsername(String userName);
	
	List<Auction> findAll();
}
