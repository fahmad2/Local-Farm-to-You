package reports;

import java.util.*;
import java.text.*;
import farmers.*;

public class RevenuePreviousMonth extends ManagerReport{
	
	private String start_date;
	private String end_date;
	private int orders_placed = 0;
	private int orders_delivered = 0;
	private int orders_open = 0;
	private int orders_cancelled = 0;
	private double total_revenue = 0.0;
	private double total_products_revenue = 0.0;
	private double total_delivery_revenue = 0.0;
	private final double lf2u_management_fee = 0.03;
	private double total_lftu_fees = 0.0;
	private double total_payable_to_farms = 0.0;
	private List<ManFarmerRevReport> mfrr;
	
	Finterface fi = new FarmerManager();
	
	public RevenuePreviousMonth(int mrid){
		super(mrid, "Revenue for last month");
		this.mfrr = new ArrayList<ManFarmerRevReport>();
		
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 0);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		this.end_date = df2.format(c.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		this.start_date = df2.format(c.getTime());
		
		Farmer[] flist = fi.viewAllFarmers();
		for(Farmer f: flist){
			int fint = f.getID();
			ManFarmerRevReport m = new ManFarmerRevReport(fint, start_date, end_date);
			mfrr.add(m);
		}
		for(ManFarmerRevReport x: mfrr){
			this.orders_placed += x.getOrdersPlaced();
			this.orders_delivered += x.getOrdersDelivered();
			this.orders_open += x.getOrdersOpen();
			this.orders_cancelled += x.getOrdersCancelled();
			this.total_revenue += x.getTotalRev();
			this.total_products_revenue += x.getProductsRev();
			this.total_delivery_revenue += x.getDeliveryRev();
			this.total_lftu_fees += x.getLf2uFees();
			this.total_payable_to_farms += x.getPayableToFarm();
		}
	}
}