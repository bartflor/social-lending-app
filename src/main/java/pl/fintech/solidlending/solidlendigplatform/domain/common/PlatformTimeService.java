package pl.fintech.solidlending.solidlendigplatform.domain.common;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
@Component
public class PlatformTimeService implements TimeService {
	@Override
	public Instant now() {
		return Instant.now();
	}
}
