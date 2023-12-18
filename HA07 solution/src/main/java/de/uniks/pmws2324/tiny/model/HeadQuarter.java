package de.uniks.pmws2324.tiny.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class HeadQuarter
extends City {
   public static final String PROPERTY_MONEY = "money";
   public static final String PROPERTY_OWNED_CARS = "ownedCars";
   private int money;
   private List<Car> ownedCars;

   public int getMoney()
   {
      return this.money;
   }

   public HeadQuarter setMoney(int value)
   {
      if (value == this.money)
      {
         return this;
      }

      final int oldValue = this.money;
      this.money = value;
      this.firePropertyChange(PROPERTY_MONEY, oldValue, value);
      return this;
   }

   public List<Car> getOwnedCars()
   {
      return this.ownedCars != null ? Collections.unmodifiableList(this.ownedCars) : Collections.emptyList();
   }

   public HeadQuarter withOwnedCars(Car value)
   {
      if (this.ownedCars == null)
      {
         this.ownedCars = new ArrayList<>();
      }
      if (!this.ownedCars.contains(value))
      {
         this.ownedCars.add(value);
         value.setOwner(this);
         this.firePropertyChange(PROPERTY_OWNED_CARS, null, value);
      }
      return this;
   }

   public HeadQuarter withOwnedCars(Car... value)
   {
      for (final Car item : value)
      {
         this.withOwnedCars(item);
      }
      return this;
   }

   public HeadQuarter withOwnedCars(Collection<? extends Car> value)
   {
      for (final Car item : value)
      {
         this.withOwnedCars(item);
      }
      return this;
   }

   public HeadQuarter withoutOwnedCars(Car value)
   {
      if (this.ownedCars != null && this.ownedCars.remove(value))
      {
         value.setOwner(null);
         this.firePropertyChange(PROPERTY_OWNED_CARS, value, null);
      }
      return this;
   }

   public HeadQuarter withoutOwnedCars(Car... value)
   {
      for (final Car item : value)
      {
         this.withoutOwnedCars(item);
      }
      return this;
   }

   public HeadQuarter withoutOwnedCars(Collection<? extends Car> value)
   {
      for (final Car item : value)
      {
         this.withoutOwnedCars(item);
      }
      return this;
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutOwnedCars(new ArrayList<>(this.getOwnedCars()));
   }
}
