package de.uniks.pmws2324.tiny.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;

public class City extends Location
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_STREETS = "streets";
   public static final String PROPERTY_ORDERS = "orders";
   private String name;
   private List<Street> streets;
   private List<Order> orders;

   public String getName()
   {
      return this.name;
   }

   public City setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public List<Street> getStreets()
   {
      return this.streets != null ? Collections.unmodifiableList(this.streets) : Collections.emptyList();
   }

   public City withStreets(Street value)
   {
      if (this.streets == null)
      {
         this.streets = new ArrayList<>();
      }
      if (!this.streets.contains(value))
      {
         this.streets.add(value);
         value.withConnects(this);
         this.firePropertyChange(PROPERTY_STREETS, null, value);
      }
      return this;
   }

   public City withStreets(Street... value)
   {
      for (final Street item : value)
      {
         this.withStreets(item);
      }
      return this;
   }

   public City withStreets(Collection<? extends Street> value)
   {
      for (final Street item : value)
      {
         this.withStreets(item);
      }
      return this;
   }

   public City withoutStreets(Street value)
   {
      if (this.streets != null && this.streets.remove(value))
      {
         value.withoutConnects(this);
         this.firePropertyChange(PROPERTY_STREETS, value, null);
      }
      return this;
   }

   public City withoutStreets(Street... value)
   {
      for (final Street item : value)
      {
         this.withoutStreets(item);
      }
      return this;
   }

   public City withoutStreets(Collection<? extends Street> value)
   {
      for (final Street item : value)
      {
         this.withoutStreets(item);
      }
      return this;
   }

   public List<Order> getOrders()
   {
      return this.orders != null ? Collections.unmodifiableList(this.orders) : Collections.emptyList();
   }

   public City withOrders(Order value)
   {
      if (this.orders == null)
      {
         this.orders = new ArrayList<>();
      }
      if (!this.orders.contains(value))
      {
         this.orders.add(value);
         value.setLocation(this);
         this.firePropertyChange(PROPERTY_ORDERS, null, value);
      }
      return this;
   }

   public City withOrders(Order... value)
   {
      for (final Order item : value)
      {
         this.withOrders(item);
      }
      return this;
   }

   public City withOrders(Collection<? extends Order> value)
   {
      for (final Order item : value)
      {
         this.withOrders(item);
      }
      return this;
   }

   public City withoutOrders(Order value)
   {
      if (this.orders != null && this.orders.remove(value))
      {
         value.setLocation(null);
         this.firePropertyChange(PROPERTY_ORDERS, value, null);
      }
      return this;
   }

   public City withoutOrders(Order... value)
   {
      for (final Order item : value)
      {
         this.withoutOrders(item);
      }
      return this;
   }

   public City withoutOrders(Collection<? extends Order> value)
   {
      for (final Order item : value)
      {
         this.withoutOrders(item);
      }
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getName());
      return result.toString();
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutStreets(new ArrayList<>(this.getStreets()));
      this.withoutOrders(new ArrayList<>(this.getOrders()));
   }
}
