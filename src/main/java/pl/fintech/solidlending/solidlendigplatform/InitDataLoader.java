package pl.fintech.solidlending.solidlendigplatform;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

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
	  borrowerRepository.save(Borrower.builder()
			  .userDetails(UserDetails.builder()
					  .userName("Bilbo_Baggins")
					  .name("Bilbo")
					  .surname( "Baggins")
					  .email("borrower@mail")
					  .privateAccountNumber(UUID.randomUUID())
					  .platformAccountNumber(UUID.fromString("d474cb1d-35b6-4d32-b290-0ab36317cdfc"))
					  .build())
			  .rating(new Rating(3))
			  .build());
	  borrowerRepository.save(Borrower.builder()
			  .userDetails(UserDetails.builder()
					  .userName("Frodo_Baggins")
					  .name("Frodo")
					  .surname( "Baggins")
					  .email("Frodo@mail")
					  .privateAccountNumber(UUID.randomUUID())
					  .platformAccountNumber(UUID.randomUUID())
					  .build())
			  .rating(new Rating(3))
			  .build());
	  lenderRepository.save(Lender.builder()
			  .userDetails(UserDetails.builder()
					  .userName("Samwise_Gamgee")
					  .name("Samwise")
					  .surname( "Gamgee")
					  .email("Gamgee@mail")
					  .platformAccountNumber(UUID.fromString("e0c30b15-02e1-423f-9fa3-2a9cf411980d"))
					  .privateAccountNumber(UUID.fromString("6b659158-0e46-460b-b418-13bda2920361"))
					  .build())
			  .build());

    long auction1Id = auctionDomainServiceImpl.createNewAuction(
        "Bilbo_Baggins",
        Period.ofDays(7),
        20,
        Period.of(1, 0, 0),
        10);
	  long auction2Id =auctionDomainServiceImpl.createNewAuction(
        "Frodo_Baggins",
        Period.ofDays(7),
        50,
        Period.of(2, 0, 0),
        15);
	  long auction3Id =auctionDomainServiceImpl.createNewAuction(
			  "Bilbo_Baggins",
			  Period.ofDays(7),
			  120,
			  Period.of(3, 0, 0),
			  22);
	  long auction4Id =auctionDomainServiceImpl.createNewAuction(
			  "Frodo_Baggins",
			  Period.ofDays(7),
			  2000,
			  Period.of(2, 0, 0),
			  7);
	  auctionDomainServiceImpl.addOffer(auction1Id, "Samwise_Gamgee", 20,18,false);
	  auctionDomainServiceImpl.addOffer(auction1Id, "Samwise_Gamgee", 20,12,true);
	  auctionDomainServiceImpl.addOffer(auction2Id, "Samwise_Gamgee", 50,7,false);
  }
}
