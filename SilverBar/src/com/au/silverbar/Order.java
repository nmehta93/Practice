package com.au.silverbar;


class Order {

    private final Integer customerId;
    
	private final OrderType type;
    private final double price;
    private final double quantity;

    Order(Integer customerId, OrderType type, double price, double quantity) {

        this.customerId = customerId;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    Integer getCustomerId() {
        return customerId;
    }

    double getValue() {
        return price * quantity ;
    }

    boolean typeIs(OrderType type) {
        return this.type.equals(type);
    }

    double getQuantity() {
        return quantity;
    }

    Integer getPriceAsInteger() {
        return new Double(price).intValue();
    }

    OrderType getOrderType() {
        return type;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(quantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (Double.doubleToLongBits(quantity) != Double.doubleToLongBits(other.quantity))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}