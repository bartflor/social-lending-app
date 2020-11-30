package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface JpaAuctionRepository extends JpaRepository<AuctionEntity, Long> {
	
	List<AuctionEntity> findAllByBorrowerName(String userName);
	
}
