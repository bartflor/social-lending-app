package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user.PersistentUserRepo;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Transactional
public class PersistentAuctionsRepository implements AuctionRepository {
	private static final String AUCTION_WITH_ID_NOT_FOUND = "Auction with id:%s not found.";
	private static final String BORROWER_NOT_FOUND = "Borrower with user name: %s not found";
	private PersistentUserRepo userRepo;
	private JpaAuctionRepository jpaAuctionRepository;
	
	@Override
	public Long save(Auction auction) {
		AuctionEntity entity = jpaAuctionRepository.save(AuctionEntity.from(auction));
		return entity.getId();
	}
	
	@Override
	public List<Auction> findAllByUsername(String userName) {
		return mapToDomainList(jpaAuctionRepository.findAllByBorrowerName(userName));
	}
	
	@Override
	public List<Auction> findAll() {
		List<AuctionEntity> auctionEntityList = jpaAuctionRepository.findAll();
		return mapToDomainList(auctionEntityList);
	}
	private List<Auction> mapToDomainList(List<AuctionEntity> auctionEntityList) {
		List<Auction> resultList = new ArrayList<>();
		for(AuctionEntity entity : auctionEntityList){
			Borrower borrower = userRepo.findBorrowerByUserName(entity.getBorrowerName())
					.orElseThrow(() ->
							new UserNotFoundException(String.format(BORROWER_NOT_FOUND, entity.getBorrowerName())));
			Auction auction = entity.toDomain();
			auction.setBorrower(borrower);
			resultList.add(auction);
		}
		return resultList;
	}
	
	@Override
	public Optional<Auction> findById(Long auctionId) {
		AuctionEntity auctionEntity = jpaAuctionRepository.findById(auctionId)
				.orElseThrow(() ->
						new AuctionNotFoundException(String.format(AUCTION_WITH_ID_NOT_FOUND, auctionId)));
		Borrower borrower = userRepo.findBorrowerByUserName(auctionEntity.getBorrowerName())
				.orElseThrow(() ->
						new UserNotFoundException(String.format(BORROWER_NOT_FOUND, auctionEntity.getBorrowerName())));
		Auction auction = auctionEntity.toDomain();
		auction.setBorrower(borrower);
		return Optional.of(auction);
	}
	
	@Override
	public void updateAuction(Long auctionId, Auction auction) {
		auction.setId(auctionId);
		jpaAuctionRepository.save(AuctionEntity.from(auction));
	}
	
	@Override
	public List<Offer> findAuctionOffers(Long auctionId) {
		return new ArrayList<>(findById(auctionId)
				.orElseThrow(() -> new AuctionNotFoundException(
						String.format(AUCTION_WITH_ID_NOT_FOUND, auctionId)))
				.getOffers());
	}
	
	@Override
	public void delete(Long auctionId) {
		jpaAuctionRepository.deleteById(auctionId);
	}
	
	@Override
	public void deleteAll(){
		jpaAuctionRepository.deleteAll();
	}
	
	@Override
	public List<Auction> findAllWithEndDateBefore(Instant date) {
		return mapToDomainList(jpaAuctionRepository.findAllByAuctionEndDateAfter(date));
	}

}
