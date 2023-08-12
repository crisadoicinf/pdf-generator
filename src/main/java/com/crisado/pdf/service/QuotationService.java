package com.crisado.pdf.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateEngineException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.crisado.pdf.exception.QuotationException;
import com.crisado.pdf.model.Customer;
import com.crisado.pdf.model.Quotation;
import com.crisado.pdf.model.QuoteItem;
import com.lowagie.text.DocumentException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuotationService {

	private static final String TEMPLATE_NAME = "quotation";
	private final TemplateEngine templateEngine;

	public Quotation getQuotation() {
		return Quotation.builder().customer(Customer.builder().companyName("My Company").contactName("The Boss")
				.address("Some Street 123, Some City").email("the.boss@mycompany.com").phone("12345678").build())
				.quoteItems(List.of(
						QuoteItem.builder().description("Quote for item 1").quantity(1).unitPrice(100.0).total(100.0)
								.build(),
						QuoteItem.builder().description("Quote for item 2").quantity(4).unitPrice(500.0).total(2000.0)
								.build(),
						QuoteItem.builder().description("Quote for item 3").quantity(2).unitPrice(200.0).total(400.0)
								.build()))
				.build();
	}

	public byte[] getPdf(Quotation quotation) throws QuotationException {
		Context context = new Context();
		context.setVariable("customer", quotation.getCustomer());
		context.setVariable("quoteItems", quotation.getQuoteItems());
		context.setVariable("chart", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(getChart()));

		try {
			String htmlContent = templateEngine.process(TEMPLATE_NAME, context);

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlContent);
			renderer.layout();
			renderer.createPDF(output);
			renderer.finishPDF();
			return output.toByteArray();
		} catch (TemplateEngineException e) {
			throw new QuotationException("Error processing quotation html template", e);
		} catch (DocumentException e) {
			throw new QuotationException("Error creating quotation pdf from html", e);
		}
	}

	public byte[] getChart() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		dataset.setValue("IPhone 5s", 20);
		dataset.setValue("SamSung Grand", 30);
		dataset.setValue("MotoG", 40);
		dataset.setValue("Nokia Lumia", 10);
		JFreeChart chart = ChartFactory.createPieChart("Mobile Sales", dataset, true, true, false);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			ChartUtils.writeChartAsJPEG(output, chart, 640, 480);
			return output.toByteArray();
		} catch (IOException e) {
			throw new QuotationException("Error creating chart", e);
		}
	}
}
