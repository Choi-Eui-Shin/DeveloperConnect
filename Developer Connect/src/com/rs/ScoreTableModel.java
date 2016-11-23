/**
 *
 * Property of IBM Korea, Copyrightⓒ. IBM Korea 2016 All Rights Reserved.
 *
 * @author: Choi Eui Shin
 *
 */
package com.rs;

import java.util.HashMap;
import java.util.Map;

import com.base.table.MyDefaultTableModel;
import com.rs.DriverService.DriverBehaviorDetail;

public class ScoreTableModel extends MyDefaultTableModel
{
	static class Score
	{
		String behaviorName;
		String title;
		int score;
		int totalTime;
		
		public Score(String s, String t) {
			behaviorName = s;
			title = t;
		}
	}
	
	private Map<String, Score> score;
	
	public ScoreTableModel()
	{
		score = new HashMap<String, Score>();
		
		Score s = new Score("Harsh acceleration", "급가속");
		score.put(s.behaviorName, s);
		super.addRow(s);
		
		s = new Score("Harsh braking", "급정지");
		score.put(s.behaviorName, s);
		super.addRow(s);
		
		s = new Score("Speeding", "과속");
		score.put(s.behaviorName, s);
		super.addRow(s);

		s = new Score("Frequent stops", "빈번한 정차");
		score.put(s.behaviorName, s);
		super.addRow(s);

		s = new Score("Frequent acceleration", "빈번한 가속");
		score.put(s.behaviorName, s);
		super.addRow(s);

		s = new Score("Frequent braking", "빈번한 제동");
		score.put(s.behaviorName, s);
		super.addRow(s);

		s = new Score("Sharp turn", "급회전");
		score.put(s.behaviorName, s);
		super.addRow(s);

		s = new Score("Acceleration before a turn", "회전 전 가속");
		score.put(s.behaviorName, s);
		super.addRow(s);

		s = new Score("Over-braking before exiting a turn", "회전 종료 전 과도한 제동");
		score.put(s.behaviorName, s);
		super.addRow(s);

		s = new Score("Fatigued driving", "피로운전");
		score.put(s.behaviorName, s);
		super.addRow(s);
	}
	
	/* (non-Javadoc)
	 * @see com.base.table.MyDefaultTableModel#clear()
	 */
	public void clear()
	{
		for(int i = 0; i < getRowCount(); i++)
		{
			Score s = (Score)getRow(i);
			s.score = 0;
			s.totalTime = 0;
		}
	}
	
    /**
     * @param deb
     */
    public void addBehavior(DriverBehaviorDetail deb)
    {
    	Score s = score.get(deb.behaviorName);
    	if ( s != null )
    	{
    		s.score++;
    		long t = Long.parseLong(deb.endTime) - Long.parseLong(deb.startTime);
    		s.totalTime += t;
    	}
    }

    /* (비Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object data = null;
        Score ce = null;

        ce = (Score)super.getRow(rowIndex);
        if ( ce == null ) return "";

        switch(columnIndex)
        {
	        case 0:
	        	data = ce.title + " ( " + ce.behaviorName + " )";
	            break;
	
	        case 1:
	        	data = ce.score;
	            break;
	
	        case 2:
	        	data = String.format("%.2f", ce.totalTime/1000.0);
	            break;

            default:
                data = "";
        }

        return data;
    }
}
