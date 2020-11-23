package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
public interface JpaAuctionRepository extends JpaRepository<AuctionEntity, Long> {
	
	List<AuctionEntity> findAllByBorrowerName(String userName);
	
}
