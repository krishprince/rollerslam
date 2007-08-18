/*
 * SerializationHelper.java
 *
 * Created on 14/08/2007, 16:01:59
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orcas.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Weslei
 */
public final class SerializationHelper {

    private SerializationHelper() {
    }

    public static String object2String(Object o) {
        String res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.flush();
            
            res = new sun.misc.BASE64Encoder().encode(baos.toByteArray());
            
            baos.close();
        } catch (Exception err) {
            throw new RuntimeException("Error on object2string: " + err, err);
        }
        return res;
    }

    public static Object string2Object(String st) {
        Object o = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(new sun.misc.BASE64Decoder().decodeBuffer(st));
            ObjectInputStream ois = new ObjectInputStream(bais);
            o = ois.readObject();
            bais.close();
        } catch (Exception err) {
            throw new RuntimeException("Error on string2object: " + err, err);
        }
        return o;
    }
}
