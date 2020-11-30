package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOffersRepository extends JpaRepository<OfferEntity, Long> {
	
	List<OfferEntity> findAllByLenderName(String userName);
}
