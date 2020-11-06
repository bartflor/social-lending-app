package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Repository
public class InMemoryAuctionRepo implements AuctionRepository {
	private Map<Long, Auction> repo;
	private static Long lastId;
	
	static {
		lastId = 0l;
	}
	
	public InMemoryAuctionRepo() {
		this.repo = new HashMap<>();
	}
	
	@Override
	public Long save(Auction auction) {
		auction.setId(++lastId);
		repo.put(lastId, auction);
		return lastId;
	}
	
	@Override
	public List<Auction> findAllByUsername(String userName) {
		return repo.values().stream().filter((auction -> auction.getBorrowerUserName().equals(userName)))
				.collect(Collectors.toList());
		
	}
	
	@Override
	public List<Auction> findAll() {
		return List.copyOf(repo.values());
	}
	
	@Override
	public Optional<Auction> findById(Long auctionId) {
		return Optional.ofNullable(repo.get(auctionId));
	}
	
	@Override
	public void updateAuction(Long auctionId, Auction auction) {
		repo.put(auctionId, auction);
	}
	
	@Override
	public List<Offer> findAuctionOffers(Long auctionId) {
    	Auction auction = findById(auctionId).orElse(Auction.builder().build());
    	return List.copyOf(auction.getOffers());
    	
	}
}
