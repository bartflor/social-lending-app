package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
	Long save(Offer offer);
	List<Offer> findAllByUserName(String userName);
	List<Offer> findAll();
	Optional<Offer> findById(Long offerId);
	void update(Long id, Offer offer);
}
