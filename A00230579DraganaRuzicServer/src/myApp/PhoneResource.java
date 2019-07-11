package myApp;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/phones")
public class PhoneResource {
	private PhoneDAO dao = new PhoneDAO();
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Phone> getAllPhones() {
		List<Phone> phones = dao.getAllPhones();
		return phones;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_XML)
	public Response addPhone(@FormParam("make") String make, @FormParam("model") String model, @FormParam("battery") String battery) {
		Phone phone = new Phone(make, model, battery);
		
		System.out.println(make + " ... " + model + " ... " + battery);
		
		dao.addPhone(phone);
		
		return Response.status(Response.Status.CREATED).entity(make + " " + model + " " + battery).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_XML)
	public Response updatePhone(@PathParam("id") int id, @FormParam("make") String make, @FormParam("model") String model, @FormParam("battery") String battery) {
		Phone phone = new Phone(id, make, model, battery);
		
		System.out.println(id + "..." + make + "..." + model + "..." + battery);
		
		dao.updatePhone(phone);
		
		return Response
				.status(Response.Status.CREATED)
				.entity(phone)
				.build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deletePhone(@PathParam("id") int id) {
		dao.deletePhone(id);
		
		return Response
			.status(Response.Status.OK)
			.build();
	}
}
