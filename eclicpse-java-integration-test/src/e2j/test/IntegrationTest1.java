/*
 * IntegerationTest1.java
 * 
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package e2j.test;

import com.parctechnologies.eclipse.*;
import java.io.*;
import java.util.*;

/**
 * @author: Weslei
 */
public class IntegrationTest1 {
    
  public static void main(String[] args) throws Exception  {
      
    System.setProperty("eclipse.directory", "H:\\Arquivos de Programas\\ECLiPSe 5.10");
  
    EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions();
    EclipseEngine eclipse;
    File eclipseProgram;
    
    eclipseEngineOptions.setUseQueues(false);
    eclipse = EmbeddedEclipse.getInstance(eclipseEngineOptions);

    eclipseProgram = new File("E:\\workspace\\Rollerslam\\eclicpse-java-integration-test\\neededfiles\\flux.pl");
    eclipse.compile(eclipseProgram);
    
    eclipseProgram = new File("E:\\workspace\\Rollerslam\\eclicpse-java-integration-test\\neededfiles\\fluent.chr");
    eclipse.compile(eclipseProgram);
    
    eclipseProgram = new File("E:\\workspace\\Rollerslam\\eclicpse-java-integration-test\\neededfiles\\rollerslam.pl");
    eclipse.compile(eclipseProgram);
  
    CompoundTerm result = eclipse.rpc("main1(X)");
    System.out.println(result);  
    
    CompoundTerm r1 = (CompoundTerm)((List)result.arg(1)).get(0);
    
    CompoundTerm r2 = (CompoundTerm)r1.arg(2);
    
    System.out.println(r2.arg(2));
    ((EmbeddedEclipse) eclipse).destroy();

  }

} 
