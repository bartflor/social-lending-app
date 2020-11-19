package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOffersRepository extends JpaRepository<OfferEntity, Long> {
	
	List<OfferEntity> findAllByLenderName(String userName);
}
