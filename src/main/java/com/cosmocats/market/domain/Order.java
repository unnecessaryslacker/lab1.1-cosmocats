package com.cosmocats.market.domain;

import java.util.List;

public record Order(String id, String customerEmail, List<String> productIds) {}
