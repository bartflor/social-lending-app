package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory


import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer

import java.time.Instant
import java.util.stream.Collectors

class InMemoryAuctionRepo implements AuctionRepository {
	private Map<Long, Auction> repo
	private static Long lastId

	
	InMemoryAuctionRepo() {
		this.repo = new HashMap<>()
		lastId = 0L
	}
	
	@Override
	Long save(Auction auction) {
		auction.setId(++lastId)
		repo.put(lastId, auction)
		return lastId
	}
	
	@Override
	List<Auction> findAllByUsername(String userName) {
		return repo.values().stream().filter({ auction -> auction.getBorrower()
				.getUserDetails().getUserName() == userName})
				.collect(Collectors.toList())
		
	}
	
	@Override
	List<Auction> findAll() {
		return List.copyOf(repo.values())
	}
	
	@Override
	Optional<Auction> findById(Long auctionId) {
		return Optional.ofNullable(repo.get(auctionId))
	}
	
	@Override
	void updateAuction(Long auctionId, Auction auction) {
		repo.put(auctionId, auction)
	}
	
	@Override
	List<Offer> findAuctionOffers(Long auctionId) {
    	Auction auction = findById(auctionId).orElse(Auction.builder().build())
    	return List.copyOf(auction.getOffers())
	}
	
	@Override
	void delete(Long auctionId) {
		repo.remove(auctionId)
	}
	
	@Override
	void deleteAll() {
		repo.clear()
	}

	@Override
	List<Auction> findAllWithEndDateBefore(Instant now) {
		return null
	}
}
