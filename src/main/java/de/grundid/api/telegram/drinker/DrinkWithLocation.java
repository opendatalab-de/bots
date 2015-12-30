package de.grundid.api.telegram.drinker;

public class DrinkWithLocation {

    private Drink drink;
    private Location location;

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
