/**
 *
 * Property of IBM Korea, Copyrightⓒ. IBM Korea 2016 All Rights Reserved.
 *
 * @author: Choi Eui Shin
 *
 */
package com.rs;

import java.util.Calendar;

import com.base.table.MyDefaultTableModel;
import com.rs.DriverService.DriverBehaviorDetail;

public class BehaviorTableModel extends MyDefaultTableModel
{
    public void addBehavior(DriverBehaviorDetail deb)
    {
        super.addRow(deb);
    }

    /* (비Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object data = null;
        DriverBehaviorDetail ce = null;

        ce = (DriverBehaviorDetail)super.getRow(rowIndex);
        if ( ce == null ) return "";

        switch(columnIndex)
        {
	        case 0:
	        	data = ce.behaviorName;
	            break;
	
	        case 1:
	        	data = String.format("%.7f", ce.startLatitude);
	            break;
	
	        case 2:
	        	data = String.format("%.7f", ce.startLongitude);
	            break;
	
	        case 3:
	        	data = String.format("%.7f", ce.endLatitude);
	            break;
	
	        case 4:
	        	data = String.format("%.7f", ce.endLongitude);
	            break;
	
	        case 5:
	        	data = timeFormat(Long.parseLong(ce.startTime));
	            break;
	
	        case 6:
	        	data = timeFormat(Long.parseLong(ce.endTime));
	            break;

            default:
                data = "";
        }

        return data;
    }
    
	/**
	 * @param time
	 * @return
	 */
	private String timeFormat(long time)
    {
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(time);
    	StringBuffer str = new StringBuffer();
    	
    	int v = c.get(Calendar.YEAR);
    	str.append(v).append("/");
    	
    	v = c.get(Calendar.MONTH) + 1;
    	if ( v < 10 ) str.append("0");
		str.append(v).append("/");
		
		v = c.get(Calendar.DAY_OF_MONTH);
		if ( v < 10 ) str.append("0");
		str.append(v).append(" ");
		
		v = c.get(Calendar.HOUR_OF_DAY);
		if ( v < 10 ) str.append("0");
		str.append(v).append(":");
		
		v = c.get(Calendar.MINUTE);
		if ( v < 10 ) str.append("0");
		str.append(v).append(":");
    	
		v = c.get(Calendar.SECOND);
		if ( v < 10 ) str.append("0");
		str.append(v);

    	return str.toString();
    }
}
