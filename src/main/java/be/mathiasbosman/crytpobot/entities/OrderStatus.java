package be.mathiasbosman.crytpobot.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {

  NEW("new"),
  AWAITING_TRIGGER("awaitingTrigger"),
  CANCELED("canceled"),
  CANCELED_AUCTION("canceled_auction"),
  CANCELED_SELF_TRADE_PREVENTION("canceledSelfTradePrevention"),
  CANCELED_IOC("canceledIOC"),
  CANCELED_FOK("canceledFOK"),
  CANCELED_MARKET_PROTECTION("canceledMarketProtection"),
  CANCELED_POST_ONLY("canceledPostOnly"),
  FILLED("filled"),
  PARTIALLY_FILLED("partiallyFilled"),
  EXPIRED("expired"),
  REJECTED("rejected");

  private final String label;

  OrderStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  @JsonValue
  public String toString() {
    return getLabel();
  }
}
