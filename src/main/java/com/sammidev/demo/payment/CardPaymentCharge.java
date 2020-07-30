package com.sammidev.demo.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CardPaymentCharge {

    private final boolean isCardDebited;
}