package pl.fintech.solidlending.solidlendigplatform.domain.common;

import java.util.Date;

public class PlatformTimeService implements TimeService {
	@Override
	public Date currentDate() {
		return new Date();
	}
}
