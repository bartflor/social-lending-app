package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.LoanParams;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.user.UserDetails;

import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
@Repository
public class InMemoryAuctionRepo implements AuctionRepository {
	private Map<Long, Auction> repo;
	private static Long lastId = 0l;
	
	public InMemoryAuctionRepo() {
		this.repo = new HashMap<>();
		save(new Auction("testBorrower",
				new Rating(),
				Period.ofDays(7),
				Collections.emptySet(),
				Auction.AuctionStatus.ACTIVE,
				new LoanParams(new Money(20),
						new Risk(2),
						Period.of(1,0,0),
						new Date(),
						new Rate(2))));
	}
	
	@Override
	public Long save(Auction auction) {
		repo.put(++lastId, auction);
		return lastId;
	}
	
	@Override
	public List<Auction> findAllByUsername(String userName) {
		return repo.values().stream().filter((auction -> auction.getBorrowerUserName().equals(userName)))
				.collect(Collectors.toList());
		
	}
	
	@Override
	public List<Auction> findAll() {
		return List.copyOf(repo.values());
	}
}
