package com.crisado.pdf.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Quotation {

	private Customer customer;
	private List<QuoteItem> quoteItems;

}
