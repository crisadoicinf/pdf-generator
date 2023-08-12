package com.crisado.pdf.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Customer {

    private String companyName;
    private String contactName;
    private String address;
    private String email;
    private String phone;
    
}
