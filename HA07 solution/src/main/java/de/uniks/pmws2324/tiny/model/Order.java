package de.uniks.pmws2324.tiny.model;
import java.beans.PropertyChangeSupport;

public class Order
{
   public static final String PROPERTY_EXPIRES = "expires";
   public static final String PROPERTY_REWARD = "reward";
   public static final String PROPERTY_LOCATION = "location";
   public static final String PROPERTY_CAR = "car";
   private long expires;
   private int reward;
   private City location;
   private Car car;
   protected PropertyChangeSupport listeners;

   public long getExpires()
   {
      return this.expires;
   }

   public Order setExpires(long value)
   {
      if (value == this.expires)
      {
         return this;
      }

      final long oldValue = this.expires;
      this.expires = value;
      this.firePropertyChange(PROPERTY_EXPIRES, oldValue, value);
      return this;
   }

   public int getReward()
   {
      return this.reward;
   }

   public Order setReward(int value)
   {
      if (value == this.reward)
      {
         return this;
      }

      final int oldValue = this.reward;
      this.reward = value;
      this.firePropertyChange(PROPERTY_REWARD, oldValue, value);
      return this;
   }

   public City getLocation()
   {
      return this.location;
   }

   public Order setLocation(City value)
   {
      if (this.location == value)
      {
         return this;
      }

      final City oldValue = this.location;
      if (this.location != null)
      {
         this.location = null;
         oldValue.withoutOrders(this);
      }
      this.location = value;
      if (value != null)
      {
         value.withOrders(this);
      }
      this.firePropertyChange(PROPERTY_LOCATION, oldValue, value);
      return this;
   }

   public Car getCar()
   {
      return this.car;
   }

   public Order setCar(Car value)
   {
      if (this.car == value)
      {
         return this;
      }

      final Car oldValue = this.car;
      if (this.car != null)
      {
         this.car = null;
         oldValue.setOrder(null);
      }
      this.car = value;
      if (value != null)
      {
         value.setOrder(this);
      }
      this.firePropertyChange(PROPERTY_CAR, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   public void removeYou()
   {
      this.setLocation(null);
      this.setCar(null);
   }
}
