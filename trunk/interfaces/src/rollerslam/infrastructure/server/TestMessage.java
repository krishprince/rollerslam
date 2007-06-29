/*
 *  Rollerslam - The Global Fusion Sport of the Third Millennium
 *  Copyright (C) 2007  ORCAS Research Group
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  http://code.google.com/p/rollerslam
 *  
 */
package rollerslam.infrastructure.server;

/**
 * Message used for testing purposes
 * 
 * @author maas
 */
public class TestMessage implements Message {
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 2925240449693073561L;

	private int msg;
	
	/**
	 * Default Constructor
	 * 
	 * @param i
	 */
	public TestMessage(int i) {
		msg = i;	
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "["+msg+"]";
	}
}