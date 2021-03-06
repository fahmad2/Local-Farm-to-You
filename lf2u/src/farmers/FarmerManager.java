package farmers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import products.Product;
import manager.Catalog;
import manager.GeneralCatalog;
import manager.FarmerCatalog;
import order.Order;
import reports.*;

public class FarmerManager implements Finterface {
	private static List<Farmer> farmers = new ArrayList<Farmer>();
	private FarmerReport fr1 = new FarmerReport(701, "Orders to deliver today");
	private FarmerReport fr2 = new FarmerReport(702, "Orders to deliver tomorrow");
	private FarmerReport fr3 = new FarmerReport(703, "Revenue Report");
	private FarmerReport fr4 = new FarmerReport(704, "Orders delivery report");
	private FarmerReport [] fra = {fr1, fr2, fr3, fr4};
	private static List<Farm> farmList = new ArrayList<Farm>();
	
	public void createAccount(Farmer f) {
		Farmer x = f;
		farmers.add(x);
	}//done

	public void updateAccount(int fid, Farmer f) {
		setToID(fid, f);
	}//done

	public Farmer viewAccount(int fid) {
		Farmer x = findByID(fid);
		return (x);
	}//done

	public Farmer [] viewFarmers(String zip) {
		List<Farmer> newFList = farmers;
		List<Farmer> abc = new ArrayList<Farmer>();
		boolean b = false;
		for(Farmer f: newFList){
			String [] x = f.getFarm().getDZip();
			for(String s: x){
				if(s.equals(zip)){
					b = true;
					break;
				}
			}
			abc.add(f);
		}
		Farmer [] rf = abc.toArray(new Farmer[abc.size()]);
		return (rf);
	}
	
	public Farmer [] viewAllFarmers(){
		List<Farmer> newFList = farmers;
		Farmer [] rf = newFList.toArray(new Farmer[newFList.size()]);
		return (rf);
	}//done

	public double viewDeliveryCharge(int fid) {
		Farmer x = findByID(fid);
		double rtd = x.getDeliveryCharge();
		return (rtd);
	}//done

	public void updateDeliveryCharge(int fid, double dc) {
		Farmer x = findByID(fid);
		x.setDeliveryCharge(dc);
		setToID(fid, x);
	}//done

	public Catalog viewFarmStore(int fid) {
		Farmer x = findByID(fid);
		Catalog fc = x.getFarmerCatalog();
		return (fc);
	}//done

	public void addProductToStore(int fid, Product p) {
		findByID(fid).getFarmerCatalog().addToCatalog(p);
	}//done

	public Product viewStoreProductDetail(int fid, int fspid) {
		Farmer x = findByID(fid);
		Product p = x.getFarmerCatalog().findProduct(fspid);
		return (p);
	}//done
	
	public Product getProductByID(int gcpid){
		GeneralCatalog gc = new GeneralCatalog();
		Product p = new Product();
		for(Product x: gc.getCatalogList()){
			if(x.getFSPID() == gcpid){
				p = x;
				break;
			}
		}
		return (p);
	}//done
	
	public void addOrderToList(Order o){
		findByID(o.getFID()).getOrderList().add(o);
	}//done
	
	public List<Order> getOrderList(int fid){
		List<Order> rl = findByID(fid).getOrderList();
		return (rl);
	}//done	
	
	public FarmerReport getReport(int fid, int frid, String sd, String ed) {
		FarmerReport r = new FarmerReport();
		if(frid == 703){
			FarmerReport fr = new FarmerRevenueReport(fid, frid, sd, ed);
			r = fr;
		}
		return (r);
	}//done
	
	public Report getReport(int fid, int frid) {
		Report r = new Report();
		if(frid == 701){
			FarmerReport fr = new DeliverTodayReport(fid, frid);
			r = fr;
		}
		else if(frid == 702){
			FarmerReport fr = new DeliverTomorrowReport(fid,frid);
			r = fr;
		}
		return (r);
	}//done
	
	public Report[] getAllReports(){
		return (fra);
	}//done
	

	public Farmer findByID(int fid){
		Farmer x = new Farmer();
		for(Farmer f: farmers){
			if(f.getID() == fid){
				x = f;
				break;
			}
		}
		return (x);
	}
	
	public void setToID(int fid, Farmer x){
		int i=0;
		for(Farmer f: farmers){
			if(f.getID() == fid){
				x.setID(fid);
				farmers.remove(i);
				farmers.add(i, x);
				break;
			}
			i++;
		}
	}
	
	public void setOrderToID(int oid, Order o){
		int i=0;
		for(Order x: getOrderList(o.getFID())){
			if(x.getID() == oid){
				x.setID(oid);
				getOrderList(o.getFID()).remove(i);
				getOrderList(o.getFID()).add(i, x);
				break;
			}
			
		}
	}
	
	public void setProductToID(int fid, int fspid, Product p) {
		Farmer f = findByID(fid);
		f.getFarmerCatalog().setProdToID(fspid, p);
	}
	
	public Farm [] getFarmList(){
		List<Farm> newList = farmList;
		Farm [] f = (Farm []) newList.toArray();
		return (f);
	}
	
	public void addToFarmList(Farm f){
		this.farmList.add(f);
	}
	
	public Farmer search(String k){
		boolean b = false;
		Farmer f = new Farmer();
		for(Farmer x: farmers){
			String s = ""+x.getID();
			if(k.equals(s)){
				f = x;
				break;
			}
			s = x.getName();
			StringTokenizer strtok = new StringTokenizer(s, " ");
			String s1 = strtok.nextToken();
			String s2 = strtok.nextToken();
			if(k.equals(s1) || k.equals(s2)){
				f= x;
				break;
			}
			s = x.getEmail();
			if(k.equals(s)){
				f = x;
				break;
			}
			s = x.getPhone();
			if(k.equals(s)){
				f = x;
				break;
			}
			Farm fa = x.getFarm();
			s = fa.getAddress();
			if(k.equals(s)){
				f = x;
				break;
			}
			s = fa.getName();
			strtok = new StringTokenizer(s, " ");
			s1 = strtok.nextToken();
			s2 = strtok.nextToken();
			if(k.equals(s1) || k.equals(s2)){
				f= x;
				break;
			}
			s = fa.getPhone();
			if(k.equals(s)){
				f = x;
				break;
			}
			s = fa.getWebsite();
			if(k.equals(s)){
				f = x;
				break;
			}
			String [] dzarr = fa.getDZip();
			for(String dz: dzarr){
				if(k.equals(dz)){
					f = x;
					b = true;
					break;
				}
			}
			if(b){
				break;
			}
		}
		return (f);
	}
	
	public Farm [] searchFWO(){
		return (this.getFarmList());
	}
}