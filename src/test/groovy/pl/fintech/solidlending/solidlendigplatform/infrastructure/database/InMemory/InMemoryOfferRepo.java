package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.OfferRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryOfferRepo implements OfferRepository {
	private Map<Long, Offer> repository;
	private static Long lastId;
	
	static {
		lastId = 0l;
	}
	public InMemoryOfferRepo() {
		this.repository = new HashMap<>();
	}
	
	@Override
	public Long save(Offer offer) {
		repository.put(++lastId, offer);
		return lastId;
	}
	@Override
	public List<Offer> findAllByUserName(String userName) {
		return repository.values().stream().filter((offer -> offer.getLenderName().equals(userName)))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Offer> findAll() {
		return List.copyOf(repository.values());
	}
	
	@Override
	public Optional<Offer> findById(Long offerId) {
		return Optional.of(repository.get(offerId));
	}
	
	@Override
	public void update(Long id, Offer offer) {
		repository.put(id, offer);
	}
}
