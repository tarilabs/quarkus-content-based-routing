package org.drools.example;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;

@ApplicationScoped
@Path("/hl7v2")
public class HL7v2Resource {

    @Produce("direct:hl7")
    ProducerTemplate startProducer;

    @POST
    @Path("new")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String post(String hl7v2Message) {
        return startProducer.requestBody(hl7v2Message).toString();
    }
}