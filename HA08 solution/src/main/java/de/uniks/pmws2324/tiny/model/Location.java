package de.uniks.pmws2324.tiny.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Location
{
   public static final String PROPERTY_X = "x";
   public static final String PROPERTY_Y = "y";
   public static final String PROPERTY_CARS = "cars";
   private int x;
   private int y;
   private List<Car> cars;
   protected PropertyChangeSupport listeners;

   public int getX()
   {
      return this.x;
   }

   public Location setX(int value)
   {
      if (value == this.x)
      {
         return this;
      }

      final int oldValue = this.x;
      this.x = value;
      this.firePropertyChange(PROPERTY_X, oldValue, value);
      return this;
   }

   public int getY()
   {
      return this.y;
   }

   public Location setY(int value)
   {
      if (value == this.y)
      {
         return this;
      }

      final int oldValue = this.y;
      this.y = value;
      this.firePropertyChange(PROPERTY_Y, oldValue, value);
      return this;
   }

   public List<Car> getCars()
   {
      return this.cars != null ? Collections.unmodifiableList(this.cars) : Collections.emptyList();
   }

   public Location withCars(Car value)
   {
      if (this.cars == null)
      {
         this.cars = new ArrayList<>();
      }
      if (!this.cars.contains(value))
      {
         this.cars.add(value);
         value.setPosition(this);
         this.firePropertyChange(PROPERTY_CARS, null, value);
      }
      return this;
   }

   public Location withCars(Car... value)
   {
      for (final Car item : value)
      {
         this.withCars(item);
      }
      return this;
   }

   public Location withCars(Collection<? extends Car> value)
   {
      for (final Car item : value)
      {
         this.withCars(item);
      }
      return this;
   }

   public Location withoutCars(Car value)
   {
      if (this.cars != null && this.cars.remove(value))
      {
         value.setPosition(null);
         this.firePropertyChange(PROPERTY_CARS, value, null);
      }
      return this;
   }

   public Location withoutCars(Car... value)
   {
      for (final Car item : value)
      {
         this.withoutCars(item);
      }
      return this;
   }

   public Location withoutCars(Collection<? extends Car> value)
   {
      for (final Car item : value)
      {
         this.withoutCars(item);
      }
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
      this.withoutCars(new ArrayList<>(this.getCars()));
   }
}
