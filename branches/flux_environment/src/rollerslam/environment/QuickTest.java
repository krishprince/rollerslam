package rollerslam.environment;
// BEGIN LICENSE BLOCK
// Version: CMPL 1.1
//
// The contents of this file are subject to the Cisco-style Mozilla Public
// License Version 1.1 (the "License"); you may not use this file except
// in compliance with the License.  You may obtain a copy of the License
// at www.eclipse-clp.org/license.
// 
// Software distributed under the License is distributed on an "AS IS"
// basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.  See
// the License for the specific language governing rights and limitations
// under the License. 
// 
// The Original Code is  The ECLiPSe Constraint Logic Programming System. 
// The Initial Developer of the Original Code is  Cisco Systems, Inc. 
// Portions created by the Initial Developer are
// Copyright (C) 2001 - 2006 Cisco Systems, Inc.  All Rights Reserved.
// 
// Contributor(s): Josh Singer, Parc Technologies
// 
// END LICENSE BLOCK

//Title:        Java/ECLiPSe interface
//Version:      $Id: QuickTest.java,v 1.1.1.1 2006/09/23 01:54:13 snovello Exp $
//Author:       Josh Singer
//Company:      Parc Technologies
//Description:  Java/ECLiPSe Interface example Java program
import java.net.Inet4Address;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.RemoteEclipse;

public class QuickTest
{
	//remote_connect(localhost/1023, Control, 1000)
  public static void main(String[] args) throws Exception
  {
    // Object representing the Eclipse process
    EclipseConnection eclipse;

    // Initialise Eclipse
    eclipse = new RemoteEclipse(Inet4Address.getByName("localhost"), 1023); 
    
    // Write a message
    CompoundTerm result = eclipse.rpc("cd(\"D:\\\\software\\\\Flux-3.1\\\\\"), [rollerslam], main0(X)");

    System.out.println("result = " + result);
    
    // Destroy the Eclipse process
    ((RemoteEclipse)eclipse).disconnect();
  }
}
