package com.bookstore.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class ExpireDateCheck {

	public boolean expireDateCheck(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
		LocalDateTime tempDateTime = LocalDateTime.from( fromDateTime );
		long years = tempDateTime.until( toDateTime, ChronoUnit.YEARS );
		tempDateTime = tempDateTime.plusYears( years );
		if(years==0) {
			long months = tempDateTime.until( toDateTime, ChronoUnit.MONTHS );
			tempDateTime = tempDateTime.plusMonths( months );
			if(months==0) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
}
