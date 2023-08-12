package com.crisado.pdf.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuoteItem {

    private String description;
    private Integer quantity;
    private Double unitPrice;
    private Double total;
    
}
