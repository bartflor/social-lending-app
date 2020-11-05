package pl.fintech.solidlending.solidlendigplatform;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionService;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
import pl.fintech.solidlending.solidlendigplatform.domain.user.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Log
@Component
@AllArgsConstructor
public class InitDataLoader implements CommandLineRunner {
  private AuctionService auctionService;
  private BorrowerRepository borrowerRepository;
  private LenderRepository lenderRepository;

  @Override
  public void run(String... args) throws Exception {
    log.info("load init data.");
	  borrowerRepository.save(Borrower.builder()
			  .userDetails(new UserDetails("testBorrower", "borrower@mail", UUID.randomUUID().toString()))
			  .rating(new Rating(3))
			  .balance(new Money(BigDecimal.ZERO))
			  .build());
	  lenderRepository.save(Lender.builder()
			  .userDetails(new UserDetails("testLender", "lender@mail", UUID.randomUUID().toString()))
			  .balance(new Money(BigDecimal.TEN))
			  .build());
    
    auctionService.createNewAuction(
        "testBorrower",
        Period.ofDays(7),
        20,
        Period.of(1, 0, 0),
        10,
        LocalDate.now().plus(Period.ofDays(7)));
    auctionService.createNewAuction(
        "testBorrower",
        Period.ofDays(7),
        50,
        Period.of(2, 0, 0),
        15,
        LocalDate.now().plus(Period.ofDays(30)));
	  auctionService.createNewAuction(
			  "testBorrower",
			  Period.ofDays(7),
			  120,
			  Period.of(3, 0, 0),
			  22,
			  LocalDate.now().plus(Period.ofDays(15)));
	  auctionService.createNewAuction(
			  "testBorrower",
			  Period.ofDays(7),
			  2000,
			  Period.of(2, 0, 0),
			  7,
			  LocalDate.now().plus(Period.ofDays(10)));
	  Offer offer = Offer.builder()
			  .auctionId(1l)
			  .lenderName("testLender")
			  .amount(new Money(20))
			  .rate(new Rate(2))
			  .duration(Period.of(1,0,0))
			  .build();
	  auctionService.addOffer(offer);
	  offer = Offer.builder()
			  .auctionId(1l)
			  .lenderName("testLender")
			  .amount(new Money(10))
			  .rate(new Rate(12))
			  .duration(Period.of(2,0,0))
			  .build();
	  auctionService.addOffer(offer);
	  offer = Offer.builder()
			  .auctionId(2l)
			  .lenderName("testLender")
			  .amount(new Money(13))
			  .rate(new Rate(7))
			  .duration(Period.of(1,0,0))
			  .build();
	  auctionService.addOffer(offer);
  }
}
