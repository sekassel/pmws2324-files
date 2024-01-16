package de.uniks.pmws2324.tiny.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class Street extends Location
{
   public static final String PROPERTY_SPEED_LIMIT = "speedLimit";
   public static final String PROPERTY_BLOCKED = "blocked";
   public static final String PROPERTY_CONNECTS = "connects";
   private int speedLimit;
   private boolean blocked;
   private List<City> connects;

   public int getSpeedLimit()
   {
      return this.speedLimit;
   }

   public Street setSpeedLimit(int value)
   {
      if (value == this.speedLimit)
      {
         return this;
      }

      final int oldValue = this.speedLimit;
      this.speedLimit = value;
      this.firePropertyChange(PROPERTY_SPEED_LIMIT, oldValue, value);
      return this;
   }

   public boolean isBlocked()
   {
      return this.blocked;
   }

   public Street setBlocked(boolean value)
   {
      if (value == this.blocked)
      {
         return this;
      }

      final boolean oldValue = this.blocked;
      this.blocked = value;
      this.firePropertyChange(PROPERTY_BLOCKED, oldValue, value);
      return this;
   }

   public List<City> getConnects()
   {
      return this.connects != null ? Collections.unmodifiableList(this.connects) : Collections.emptyList();
   }

   public Street withConnects(City value)
   {
      if (this.connects == null)
      {
         this.connects = new ArrayList<>();
      }
      if (!this.connects.contains(value))
      {
         this.connects.add(value);
         value.withStreets(this);
         this.firePropertyChange(PROPERTY_CONNECTS, null, value);
      }
      return this;
   }

   public Street withConnects(City... value)
   {
      for (final City item : value)
      {
         this.withConnects(item);
      }
      return this;
   }

   public Street withConnects(Collection<? extends City> value)
   {
      for (final City item : value)
      {
         this.withConnects(item);
      }
      return this;
   }

   public Street withoutConnects(City value)
   {
      if (this.connects != null && this.connects.remove(value))
      {
         value.withoutStreets(this);
         this.firePropertyChange(PROPERTY_CONNECTS, value, null);
      }
      return this;
   }

   public Street withoutConnects(City... value)
   {
      for (final City item : value)
      {
         this.withoutConnects(item);
      }
      return this;
   }

   public Street withoutConnects(Collection<? extends City> value)
   {
      for (final City item : value)
      {
         this.withoutConnects(item);
      }
      return this;
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutConnects(new ArrayList<>(this.getConnects()));
   }
}
