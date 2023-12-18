package de.uniks.pmws2324.tiny.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Car
{
   public static final String PROPERTY_DRIVER = "driver";
   public static final String PROPERTY_POSITION = "position";
   public static final String PROPERTY_ORDER = "order";
   public static final String PROPERTY_OWNER = "owner";
   private String driver;
   private Location position;
   private Order order;
   protected PropertyChangeSupport listeners;
   private HeadQuarter owner;

   public String getDriver()
   {
      return this.driver;
   }

   public Car setDriver(String value)
   {
      if (Objects.equals(value, this.driver))
      {
         return this;
      }

      final String oldValue = this.driver;
      this.driver = value;
      this.firePropertyChange(PROPERTY_DRIVER, oldValue, value);
      return this;
   }

   public Location getPosition()
   {
      return this.position;
   }

   public Car setPosition(Location value)
   {
      if (this.position == value)
      {
         return this;
      }

      final Location oldValue = this.position;
      if (this.position != null)
      {
         this.position = null;
         oldValue.withoutCars(this);
      }
      this.position = value;
      if (value != null)
      {
         value.withCars(this);
      }
      this.firePropertyChange(PROPERTY_POSITION, oldValue, value);
      return this;
   }

   public Order getOrder()
   {
      return this.order;
   }

   public Car setOrder(Order value)
   {
      if (this.order == value)
      {
         return this;
      }

      final Order oldValue = this.order;
      if (this.order != null)
      {
         this.order = null;
         oldValue.setCar(null);
      }
      this.order = value;
      if (value != null)
      {
         value.setCar(this);
      }
      this.firePropertyChange(PROPERTY_ORDER, oldValue, value);
      return this;
   }

   public HeadQuarter getOwner()
   {
      return this.owner;
   }

   public Car setOwner(HeadQuarter value)
   {
      if (this.owner == value)
      {
         return this;
      }

      final HeadQuarter oldValue = this.owner;
      if (this.owner != null)
      {
         this.owner = null;
         oldValue.withoutOwnedCars(this);
      }
      this.owner = value;
      if (value != null)
      {
         value.withOwnedCars(this);
      }
      this.firePropertyChange(PROPERTY_OWNER, oldValue, value);
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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getDriver());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setPosition(null);
      this.setOrder(null);
      this.setOwner(null);
   }
}
