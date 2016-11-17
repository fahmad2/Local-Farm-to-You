package reports;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import farmers.*;
import order.*;

public class DeliverTodayReport extends FarmerReport{
	
	static DateFormat df = new SimpleDateFormat("yyyyMMdd");
	static Date dobj = new Date();
	static String sd = df.format(dobj);
	private List<OrderReport> orlist;
	private Finterface fi = new FarmerManager();
	
	public DeliverTodayReport(int fid, int frid){
		super(frid, "Orders to deliver today");
		orlist = new ArrayList<OrderReport>();
		List<Order> ol = fi.getOrderList(fid);
		for(Order o: ol){
			if(o.getPlannedDate() == sd){
				OrderReport or = new OrderReport(o);
				orlist.add(or);
			}
		}
	}
}