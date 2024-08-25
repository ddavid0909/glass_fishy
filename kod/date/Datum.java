/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package date;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author fafulja
 */
public class Datum {
    public static Date mergeDate(Date datum, Date vrijeme) {
        Calendar k1 = Calendar.getInstance();
        k1.setTime(datum);
        
        Calendar k2 = Calendar.getInstance();
        k2.setTime(vrijeme);
        
        k1.set(Calendar.HOUR_OF_DAY, k2.get(Calendar.HOUR_OF_DAY));
        k1.set(Calendar.MINUTE, k2.get(Calendar.MINUTE));
        k1.set(Calendar.SECOND, k2.get(Calendar.SECOND));
        
        return k1.getTime();
    }
}
