package testfield.environment;

import chr.runtime.ChrMachine;
import chr.runtime.language.Constraint;
import chr.runtime.language.FConstraint;

class EnvironmentPerceptInterpretation extends ChrMachine {
    Object A0,A1,A2,A3,A4,A5,A6,A7;
    Object V0,V1,V2,V3,V4,V5,V6,V7;
    FConstraint tR0,tR1,tR2,tR3,tR4,tR5,tR6,tR7;
    FConstraint tK0,tK1,tK2,tK3,tK4,tK5,tK6,tK7;
    
    Constraint perception_dash_3 = new Constraint(this,"perception_dash_3",3) {
      public boolean run(FConstraint term) {
        countConstraint();
        A0 = deref(pop());
        A1 = deref(pop());
        A2 = deref(pop());
        tR0 = state_power_3.firstTerm();
        while (tR0!=null) {
          tR0.lock();
          V0 = tR0.args[0];
          V1 = tR0.args[1];
          V2 = tR0.args[2];
          if (compare(A0,V0)) {
            push(A2);
            push(A1);
            push(V0);
            push(state_power_3);
            
            remove(tR0);
            return true;
          }
          
          tR0.unlock();
          tR0 = tR0.next();
        }
        
        if (term!=null)
          insert(term);
        else
          insert(new FConstraint(perception_dash_3,A0,A1,A2));
        return false;
        
      }
    };
    
    public void perception_dash_3(Object p0, Object p1, Object p2) {
      this.apply(this.perception_dash_3, p0, p1, p2);
    }
    
    Constraint state_power_3 = new Constraint(this,"state_power_3",3) {
      public boolean run(FConstraint term) {
        countConstraint();
        A0 = deref(pop());
        A1 = deref(pop());
        A2 = deref(pop());
        tR1 = perception_dash_3.firstTerm();
        while (tR1!=null) {
          tR1.lock();
          V3 = tR1.args[0];
          V4 = tR1.args[1];
          V5 = tR1.args[2];
          if (compare(V3,A0)) {
            push(V5);
            push(V4);
            push(A0);
            push(state_power_3);
            
            remove(tR1);
            return true;
          }
          
          tR1.unlock();
          tR1 = tR1.next();
        }
        
        if (term!=null)
          insert(term);
        else
          insert(new FConstraint(state_power_3,A0,A1,A2));
        return false;
        
      }
    };
    
    public void state_power_3(Object p0, Object p1, Object p2) {
      this.apply(this.state_power_3, p0, p1, p2);
    }
    
    
}

