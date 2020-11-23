package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory


import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer
import pl.fintech.solidlending.solidlendigplatform.domain.auction.OfferRepository

import java.util.stream.Collectors

class InMemoryOfferRepo implements OfferRepository {
	private Map<Long, Offer> repository
	private static Long lastId
	
	static {
		lastId = 0l
	}
	InMemoryOfferRepo() {
		this.repository = new HashMap<>()
	}
	
	@Override
	Long save(Offer offer) {
		repository.put(++lastId, offer)
		return lastId
	}
	@Override
	List<Offer> findAllByUserName(String userName) {
		return repository.values().stream().filter(({ offer -> offer.getLenderName().equals(userName) }))
				.collect(Collectors.toList())
	}
	
	@Override
	List<Offer> findAll() {
		return List.copyOf(repository.values())
	}
	
	@Override
	Optional<Offer> findById(Long offerId) {
		return Optional.of(repository.get(offerId))
	}
	
	@Override
	void update(Long id, Offer offer) {
		repository.put(id, offer)
	}
	
	@Override
	void deleteAll() {
		repository.clear()
	}

	@Override
	void deleteOffer(Long offerId) {
		repository.remove(offerId);
	}
}
