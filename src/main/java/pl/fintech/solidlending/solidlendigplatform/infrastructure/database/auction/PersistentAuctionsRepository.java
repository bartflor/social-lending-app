package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Transactional
public class PersistentAuctionsRepository implements AuctionRepository {
	private static final String AUCTION_WITH_ID_NOT_FOUND = "Auction with id:%s not found.";
	
	private JpaAuctionRepository jpaAuctionRepository;
	
	@Override
	public Long save(Auction auction) {
		AuctionEntity entity = jpaAuctionRepository.save(AuctionEntity.from(auction));
		return entity.getId();
	}
	
	@Override
	public List<Auction> findAllByUsername(String userName) {
		return jpaAuctionRepository.findAllByBorrowerName(userName).stream()
				.map(AuctionEntity::toDomain)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Auction> findAll() {
		return jpaAuctionRepository.findAll().stream()
				.map(AuctionEntity::toDomain)
				.collect(Collectors.toList());
	}
	
	@Override
	public Optional<Auction> findById(Long auctionId) {
		return jpaAuctionRepository.findById(auctionId)
				.map(AuctionEntity::toDomain);
	}
	
	@Override
	public void updateAuction(Long auctionId, Auction auction) {
		auction.setId(auctionId);
		jpaAuctionRepository.save(AuctionEntity.from(auction));
	}
	
	@Override
	public List<Offer> findAuctionOffers(Long auctionId) {
		return findById(auctionId)
				.orElseThrow(() -> new AuctionNotFoundException(
						String.format(AUCTION_WITH_ID_NOT_FOUND, auctionId)))
				.getOffers().stream()
				.collect(Collectors.toList());
	}
	
	@Override
	public void delete(Long auctionId) {
		jpaAuctionRepository.deleteById(auctionId);
	}
	
	@Override
	public void deleteAll(){
		jpaAuctionRepository.deleteAll();
	}
}
