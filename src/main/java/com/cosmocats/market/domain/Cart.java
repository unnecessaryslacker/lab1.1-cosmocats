package com.cosmocats.market.domain;

import java.util.List;

public record Cart(String id, List<String> productIds) {}
