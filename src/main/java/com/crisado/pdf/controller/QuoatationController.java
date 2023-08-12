package com.crisado.pdf.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crisado.pdf.model.Quotation;
import com.crisado.pdf.service.QuotationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class QuoatationController {

	private final QuotationService quotationService;
	

	@GetMapping
	public ResponseEntity<byte[]> hello() {
		Quotation quotation = quotationService.getQuotation();
		String filename = String.format("quotation_%s.pdf",
				quotation.getCustomer().getCompanyName().toLowerCase().replaceAll("\\s", "_"));
		byte[] content = quotationService.getPdf(quotation);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition
				.builder("inline")
				.filename(filename)
				.build());
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return ResponseEntity.ok()
				.headers(headers)
				.body(content);
	}

}
