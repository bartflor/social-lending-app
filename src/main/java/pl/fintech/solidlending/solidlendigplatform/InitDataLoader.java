package pl.fintech.solidlending.solidlendigplatform;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainService;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import java.math.BigDecimal;
import java.time.Period;
import java.util.UUID;

@Log
@Component
@AllArgsConstructor
public class InitDataLoader implements CommandLineRunner {
  private AuctionDomainService auctionDomainServiceImpl;
  private BorrowerRepository borrowerRepository;
  private LenderRepository lenderRepository;

  @Override
  public void run(String... args) throws Exception {
    log.info("load init data.");
//	  borrowerRepository.save(Borrower.builder()
//			  .userDetails(new UserDetails("Bilbo_Baggins", "Bilbo Baggins", "borrower@mail", "d474cb1d-35b6-4d32-b290-0ab36317cdfc"))
//			  .rating(new Rating(3))
//			  .balance(new Money(BigDecimal.ZERO))
//			  .build());
//	  borrowerRepository.save(Borrower.builder()
//			  .userDetails(new UserDetails("Frodo_Baggins", "Frodo Baggins", "borrower@mail", UUID.randomUUID().toString()))
//			  .rating(new Rating(3))
//			  .balance(new Money(BigDecimal.ZERO))
//			  .build());
//	  lenderRepository.save(Lender.builder()
//			  .userDetails(new UserDetails("Samwise_Gamgee", "Samwise Gamgee", "lender@mail", "e0c30b15-02e1-423f-9fa3-2a9cf411980d"))
//			  .balance(new Money(BigDecimal.TEN))
//			  .build());
//
//    long auction1Id = auctionDomainServiceImpl.createNewAuction(
//        "Bilbo_Baggins",
//        Period.ofDays(7),
//        20,
//        Period.of(1, 0, 0),
//        10);
//	  long auction2Id =auctionDomainServiceImpl.createNewAuction(
//        "Frodo_Baggins",
//        Period.ofDays(7),
//        50,
//        Period.of(2, 0, 0),
//        15);
//	  long auction3Id =auctionDomainServiceImpl.createNewAuction(
//			  "Bilbo_Baggins",
//			  Period.ofDays(7),
//			  120,
//			  Period.of(3, 0, 0),
//			  22);
//	  long auction4Id =auctionDomainServiceImpl.createNewAuction(
//			  "Frodo_Baggins",
//			  Period.ofDays(7),
//			  2000,
//			  Period.of(2, 0, 0),
//			  7);
//	  Offer offer = Offer.builder()
//			  .auctionId(auction1Id)
//			  .lenderName("Samwise_Gamgee")
//			  .amount(new Money(20))
//			  .rate(Rate.fromPercentValue(5.))
//			  .build();
//	  auctionDomainServiceImpl.addOffer(offer, offer.getLenderName(), offer.getAmount(), offer.getRate(), offer.getAllowAmountSplit());
//	  offer = Offer.builder()
//			  .auctionId(auction1Id)
//			  .lenderName("Samwise_Gamgee")
//			  .amount(new Money(20))
//			  .rate(Rate.fromPercentValue(12.))
//			  .build();
//	  auctionDomainServiceImpl.addOffer(offer, offer.getLenderName(), offer.getAmount(), offer.getRate(), offer.getAllowAmountSplit());
//	  offer = Offer.builder()
//			  .auctionId(auction2Id)
//			  .rate(Rate.fromPercentValue(7.))
//			  .lenderName("Samwise_Gamgee")
//			  .amount(new Money(50))
//			  .build();
//	  auctionDomainServiceImpl.addOffer(offer, offer.getLenderName(), offer.getAmount(), offer.getRate(), offer.getAllowAmountSplit());
  }
}
