package lf2u.rest.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//import java.util.Arrays;
//import java.lang.*;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.json.*;

import customers.*;
import farmers.*;
import manager.*;
import products.*;
import order.*;

@Path("")
public class RestApiService {
	
	Cinterface ci = new CustomerManager();
	Finterface fi = new FarmerManager();
	Minterface mi = new ManagerManager();
	Gson gson = new Gson();
	
	//farmer API started
	
    @Path("/farmers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFarmerAccountPOST(String json) throws JSONException{
        JSONObject obj = new JSONObject(json);
        try{
        String farmN = obj.getJSONObject("farm_info").getString("name");
        String farmA = obj.getJSONObject("farm_info").getString("address");
        String farmP = obj.getJSONObject("farm_info").getString("phone");
        JSONArray dzjson = obj.getJSONArray("delivers_to");
        String [] dz = new String[dzjson.length()];
        Farm fa = new Farm();
        for(int i=0; i<dz.length; i++){
        	String o = dzjson.getString(i);
        	dz[i] = o;
        }
        if(obj.getJSONObject("farm_info").has("web")){
        	String farmW = obj.getJSONObject("farm_info").getString("web");
        	fa = new Farm(farmN, farmA, farmP, farmW, dz);
        }
        else{
        	fa = new Farm(farmN, farmA, farmP, dz);
        }
        
        String farmerN = obj.getJSONObject("personal_info").getString("name");
        String farmerE = obj.getJSONObject("personal_info").getString("email");
        String farmerP = obj.getJSONObject("personal_info").getString("phone");
        
        Farmer f = new Farmer(farmerN, farmerP, farmerE, fa);
        Finterface fi = new FarmerManager();
        fi.createAccount(f);
        String s = gson.toJson(f.getID());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFarmerAccountPOST(@Context UriInfo uriInfo, @PathParam("id") int fid, String json) throws JSONException{
        JSONObject obj = new JSONObject(json);
        try{
        String farmN = obj.getJSONObject("farm_info").getString("name");
        String farmA = obj.getJSONObject("farm_info").getString("address");
        String farmP = obj.getJSONObject("farm_info").getString("phone");
        JSONArray dzjson = obj.getJSONArray("delivers_to");
        String [] dz = new String[dzjson.length()];
        Farm fa = new Farm();
        for(int i=0; i<dz.length; i++){
        	String o = dzjson.getString(i);
        	dz[i] = o;
        }
        if(obj.getJSONObject("farm_info").has("web")){
        	String farmW = obj.getJSONObject("farm_info").getString("web");
        	fa = new Farm(farmN, farmA, farmP, farmW, dz);
        }
        else{
        	fa = new Farm(farmN, farmA, farmP, dz);
        }
        
        String farmerN = obj.getJSONObject("personal_info").getString("name");
        String farmerE = obj.getJSONObject("personal_info").getString("email");
        String farmerP = obj.getJSONObject("personal_info").getString("phone");
        
        Farmer f = new Farmer(farmerN, farmerP, farmerE, fa);
        fi.updateAccount(fid, f);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(fid));
        String s = gson.toJson(f.getID());
        return Response.created(builder.build()).entity(s).build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllFarmerAccountsGET() throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.viewAllFarmers());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewFarmerAccountGET(@PathParam("id") int fid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.viewAccount(fid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers?zip={zip}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewFarmersByZipGET(@PathParam("zip") String zip) throws Exception{
        try{
        	System.out.println("faizan is my name "+zip);
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.viewFarmers(zip));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewFarmerProductsGET(@PathParam("id") int fid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.viewFarmStore(fid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/products")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postProductToFarmStore(@PathParam("id") int fid, String json) throws JSONException{
        try{
    	JSONObject obj = new JSONObject(json);
        String gcpid = obj.getString("gcpid");
        Product p = fi.getProductByID(Integer.parseInt(gcpid));
        Product x = p;
        x.setFSPID();
        String note = "";
        String start_date = "";
        String end_date = "";
        String img = "";
        if(obj.has("note")){
        	note = obj.getString("note");
        	x.setNode(note);
        }
        if(obj.has("start_date")){
        	start_date = obj.getString("start_date");
        	x.setSD(start_date);
        }
        if(obj.has("end_date")){
        	end_date = obj.getString("end_date");
        	x.setED(end_date);
        }
        int price = obj.getInt("price");
        String pu = obj.getString("product_unit");
        String produ = pu.toUpperCase();
        if(obj.has("image")){
        	img = obj.getString("image");
        	x.setImg(img);
        }
        fi.addProductToStore(fid, x);
        String r = gson.toJson(x.getFSPID());
        return Response.status(Response.Status.OK).entity(r).build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }//do product unit
    
    @Path("/farmers/{id}/products/{fsid}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyStoreProduct(@PathParam("id") int fid, @PathParam("fsid") int fspid, String json) throws JSONException{
        try{
    	JSONObject obj = new JSONObject(json);
    	Product f = fi.viewStoreProductDetail(fid, fspid);
    	if(obj.has("note")){
    		String note = obj.getString("note");
    		f.setNode(note);
    	}
    	if(obj.has("start_date")){
    		String sd = obj.getString("start_date");
    		f.setSD(sd);
    	}
    	if(obj.has("end_date")){
    		String ed = obj.getString("end_date");
    		f.setED(ed);
    	}
    	if(obj.has("price")){
    		String p = obj.getString("price");
    		f.setPrice(Double.parseDouble(p));
    	}
    	if(obj.has("product_unit")){
    		String pu = obj.getString("product_unit");
    		f.setName(pu);
    	}
    	if(obj.has("image")){
    		String img = obj.getString("image");
    		f.setImg(img);
    	}
    	fi.setProductToID(fid, fspid, f);
    	return Response.ok().build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/products/{fsid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFarmStoreProductDetails(@PathParam("id") int fid, @PathParam("fsid") int fspid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.viewStoreProductDetail(fid, fspid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/reports")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFarmerReports(@PathParam("id") int fid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.getAllReports());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/reports/{frid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFarmerReport(@PathParam("id") int fid, @PathParam("frid") int frid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.getReport(fid, frid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/reports/{frid}?start_date={sd}&end_date={ed}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFarmerReportWithSDED(@PathParam("id") int fid, @PathParam("frid") int frid, @PathParam("sd") String sd, @PathParam("ed") String ed) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.getReport(fid, frid, sd, ed));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/delivery_charge")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeliveryCharge(@PathParam("id") int fid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(fi.viewDeliveryCharge(fid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/farmers/{id}/delivery_charge")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDeliveryCharge(@PathParam("id") int fid, String json) throws JSONException{
        try{
    	JSONObject obj = new JSONObject(json);
    	double dc = obj.getDouble("delivery_charge");
    	fi.updateDeliveryCharge(fid, dc);
    	return Response.ok().build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    //farmer API completed
    
    //customer API started
    
    @Path("/customers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomerAccountPOST(String json) throws JSONException{
        try{
        JSONObject obj = new JSONObject(json);
        String custN = obj.getString("name");
        String custA = obj.getString("street");
        String custZ = obj.getString("zip");
        String custP = obj.getString("phone");
        String custE = obj.getString("email");
        
        Customer c = new Customer(custN, custZ, custA, custP, custE);
        ci.createAccount(c);

        String s = gson.toJson(c.getID());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/customers/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerAccountPOST(@PathParam("id") int cid, String json) throws JSONException{
        try{
        JSONObject obj = new JSONObject(json);
        String custN = obj.getString("name");
        String custA = obj.getString("street");
        String custZ = obj.getString("zip");
        String custP = obj.getString("phone");
        String custE = obj.getString("email");
        
        Customer c = new Customer(custN, custZ, custA, custP, custE);
        ci.updateAccount(cid, c);
        return Response.ok().build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/customers/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewCustomerAccount(@PathParam("id") int cid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(ci.viewAccount(cid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/customers/{id}/orders")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(@PathParam("id") int cid, String json) throws JSONException{
        try{
        JSONObject obj = new JSONObject(json);
        String strF = obj.getString("fid");
        int fid = Integer.parseInt(strF);
        Order o;
        if(obj.has("delivery_note")){
        	String dNote = obj.getString("delivery_note");
        	o = new Order(fid, dNote);
        }
        else {
        	o = new Order(fid);
        }
        JSONArray orderD = obj.getJSONArray("order_detail");
        for(int i=0; i<orderD.length(); i++){
        	JSONObject obj1 = orderD.getJSONObject(i);
        	String strFS = obj1.getString("fspid");
        	int fspid = Integer.parseInt(strFS);
        	double amount = obj1.getDouble("amount");
        	Item item = new Item(fid, fspid, amount);
        	o.addItemToList(item);
        }
        String dNote = obj.getString("delivery_note");
        o.setNote(dNote);
        ci.createOrder(cid, o);

        String s = gson.toJson(o.getID());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/customers/{id}/orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllOrders(@PathParam("id") int cid) throws Exception{
        try{
    	gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(ci.viewOrders(cid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/customers/{id}/orders/{oid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewOrder(@PathParam("id") int cid, @PathParam("oid") int oid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(ci.viewOrderByID(cid, oid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/customers/{id}/orders/{oid}/cancel")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelOrder(@PathParam("id") int cid, @PathParam("oid") int oid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(ci.cancelOrder(cid, oid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    //customer API completed
    
    //manager API started
    
    @Path("/managers/catalog")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewCatalog() throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(mi.viewCatalog());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/managers/catalog")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProductToCatalog(String json) throws JSONException{
        try{
        JSONObject obj = new JSONObject(json);
        String name = obj.getString("name");
        Product p = new Product(name);
        mi.addProductToCatalog(p);
        
        String s = gson.toJson(p.getGCPID());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/managers/catalog/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductGenCatalog(@PathParam("id") int gcpid, String json) throws JSONException{
        try{
        JSONObject obj = new JSONObject(json);
        String name = obj.getString("name");
        Product p = new Product(name);
        p.setGCPID(gcpid);
        mi.updateCatalogProduct(gcpid, p);
        return Response.ok().build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/managers/accounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllAccounts() throws Exception{
	try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(mi.viewAllManagers());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/managers/accounts/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAccount(@PathParam("id") int mid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(mi.viewAccount(mid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/managers/reports")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllReports() throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(mi.getAllReports());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/managers/reports/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewReport(@PathParam("id") int mrid) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(mi.getReport(mrid));
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }//also add method to get reports with SD ED
    
    //Manager API completed
    
    //search API
    
    @Path("/search?topic={word}&key={key}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("word") String w, @PathParam("key") String k) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = "";
        if(w.equals("farm")){
        	s = gson.toJson(fi.search(k));
        }
        else if(w.equals("customer")){
        	s = gson.toJson(ci.searchC(k));
        }
        else if(w.equals("order")){
        	s = gson.toJson(ci.searchO(k));
        }
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    @Path("/search?topic={word}&key=")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchNoKey(@PathParam("word") String w) throws Exception{
        try{
        gson = new GsonBuilder().setPrettyPrinting().create();
        String s = "";
        if(w.equals("farm")){
        	s = gson.toJson(fi.searchFWO());
        }
        else if(w.equals("customer")){
        	s = gson.toJson(ci.searchCWO());
        }
        else if(w.equals("order")){
        	s = gson.toJson(ci.searchOWO());
        }
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(Exception js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    //search API completed
    
    //delivery API
    
    @Path("/delivery/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDeliveryStatus(@PathParam("id") int oid, String json) throws JSONException{
        try{
        JSONObject obj = new JSONObject(json);
        String name = obj.getString("status");
        if(name.equals("delivered")){
        	mi.updateDeliveryOfOrderInList(true, oid);
        }
        else {
        	mi.updateDeliveryOfOrderInList(false, oid);
        }
        String s = gson.toJson(mi.findOrder(oid).getDeliveryStatus());
        return Response.status(Response.Status.OK).entity(s).build();
        }
        catch(JSONException js){
            System.out.println(js.getMessage());
            return null;
        }
    }
    
    //delivery API completed 
}
