package testfield.environment;

import chr.runtime.*;
import chr.runtime.builtin.*;
import chr.runtime.language.*;
import chr.runtime.util.*;
import java.util.*;

class EnvironmentRamification extends ChrMachine {
    Object A0,A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12,A13,A14,A15,A16,A17,A18,A19;
    Object V0,V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,V11,V12,V13,V14,V15,V16,V17,V18,V19;
    FConstraint tR0,tR1,tR2,tR3,tR4,tR5,tR6,tR7,tR8,tR9,tR10,tR11,tR12,tR13,tR14,tR15,tR16,tR17,tR18,tR19;
    FConstraint tK0,tK1,tK2,tK3,tK4,tK5,tK6,tK7,tK8,tK9,tK10,tK11,tK12,tK13,tK14,tK15,tK16,tK17,tK18,tK19;
    
    Constraint state_position_3 = new Constraint(this,"state_position_3",3) {
      public boolean run(FConstraint term) {
        countConstraint();
        A0 = deref(pop());
        A1 = deref(pop());
        A2 = deref(pop());
        tK0 = state_power_3.firstTerm();
        while (tK0!=null) {
          tK0.lock();
          V0 = tK0.args[0];
          V1 = tK0.args[1];
          V2 = tK0.args[2];
          tR1 = state_speed_3.firstTerm();
          while (tR1!=null) {
            tR1.lock();
            V6 = tR1.args[0];
            V7 = tR1.args[1];
            V8 = tR1.args[2];
            if (compare(A0,V0) && compare(V6,V0)) {
              V11 = new Variable();
              V9 = new Variable();
              V12 = new Variable();
              V10 = new Variable();
              push(V10);
              push(V9);
              push(V0);
              push(state_speed_3);
              
              push(sum_2.evaluate(V8,V2));
              push(V10);
              push(eq);
              
              push(sum_2.evaluate(V7,V1));
              push(V9);
              push(eq);
              
              push(V12);
              push(V11);
              push(V0);
              push(state_position_3);
              
              push(V12);
              push(sum_2.evaluate(A2,V8));
              push(eq);
              
              push(V11);
              push(sum_2.evaluate(A1,V7));
              push(eq);
              
              remove(tR1);
              return true;
            }
            
            tR1.unlock();
            tR1 = tR1.next();
          }
          
          tK0.unlock();
          tK0 = tK0.next();
        }
        
        if (term!=null)
          insert(term);
        else
          insert(new FConstraint(state_position_3,A0,A1,A2));
        return false;
        
      }
    };
    
    public void state_position_3(Object p0, Object p1, Object p2) {
      this.apply(this.state_position_3, p0, p1, p2);
    }
    
    Constraint state_speed_3 = new Constraint(this,"state_speed_3",3) {
      public boolean run(FConstraint term) {
        countConstraint();
        A0 = deref(pop());
        A1 = deref(pop());
        A2 = deref(pop());
        tK0 = state_power_3.firstTerm();
        while (tK0!=null) {
          tK0.lock();
          V0 = tK0.args[0];
          V1 = tK0.args[1];
          V2 = tK0.args[2];
          tR0 = state_position_3.firstTerm();
          while (tR0!=null) {
            tR0.lock();
            V3 = tR0.args[0];
            V4 = tR0.args[1];
            V5 = tR0.args[2];
            if (compare(V3,V0) && compare(A0,V0)) {
              V11 = new Variable();
              V9 = new Variable();
              V12 = new Variable();
              V10 = new Variable();
              push(V10);
              push(V9);
              push(V0);
              push(state_speed_3);
              
              push(sum_2.evaluate(A2,V2));
              push(V10);
              push(eq);
              
              push(sum_2.evaluate(A1,V1));
              push(V9);
              push(eq);
              
              push(V12);
              push(V11);
              push(V0);
              push(state_position_3);
              
              push(V12);
              push(sum_2.evaluate(V5,A2));
              push(eq);
              
              push(V11);
              push(sum_2.evaluate(V4,A1));
              push(eq);
              
              remove(tR0);
              return true;
            }
            
            tR0.unlock();
            tR0 = tR0.next();
          }
          
          tK0.unlock();
          tK0 = tK0.next();
        }
        
        if (term!=null)
          insert(term);
        else
          insert(new FConstraint(state_speed_3,A0,A1,A2));
        return false;
        
      }
    };
    
    public void state_speed_3(Object p0, Object p1, Object p2) {
      this.apply(this.state_speed_3, p0, p1, p2);
    }
    
    Constraint state_power_3 = new Constraint(this,"state_power_3",3) {
      public boolean run(FConstraint term) {
        countConstraint();
        A0 = deref(pop());
        A1 = deref(pop());
        A2 = deref(pop());
        tR1 = state_position_3.firstTerm();
        while (tR1!=null) {
          tR1.lock();
          V3 = tR1.args[0];
          V4 = tR1.args[1];
          V5 = tR1.args[2];
          removePoints0: {
            tR2 = state_speed_3.firstTerm();
            while (tR2!=null) {
              tR2.lock();
              V6 = tR2.args[0];
              V7 = tR2.args[1];
              V8 = tR2.args[2];
              if (compare(V3,A0) && compare(V6,A0)) {
                V11 = new Variable();
                V9 = new Variable();
                V12 = new Variable();
                V10 = new Variable();
                push(V10);
                push(V9);
                push(A0);
                push(state_speed_3);
                
                push(sum_2.evaluate(V8,A2));
                push(V10);
                push(eq);
                
                push(sum_2.evaluate(V7,A1));
                push(V9);
                push(eq);
                
                push(V12);
                push(V11);
                push(A0);
                push(state_position_3);
                
                push(V12);
                push(sum_2.evaluate(V5,V8));
                push(eq);
                
                push(V11);
                push(sum_2.evaluate(V4,V7));
                push(eq);
                
                remove(tR1);
                remove(tR2);
                break removePoints0;
              }
              
              tR2.unlock();
              tR2 = tR2.next();
            }
            
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

